#!/usr/bin/env bash
# crash-watcher: detects backend pod restarts, dumps evidence (logs, events,
# describe, yaml) to a PVC and sends a Messenger notification.
# Runs as a CronJob every minute.
#
# The message text is a template at /scripts/alert-template.txt (ConfigMap
# crash-watcher-scripts). Available placeholders:
#   {POD} {RESTARTS_PREV} {RESTARTS_NOW} {REASON} {EXIT_CODE} {STATUS}
#   {IMAGE} {DUMP} {LOG_SNIPPET}
# A failed send is saved under $DATA_DIR/pending/ (one file per incident) and
# retried on the next run.
set -u

NS="${WATCH_NAMESPACE:-cosmo}"
LABEL="${WATCH_LABEL:-app=cosmo-backend}"
DEPLOY_NAME="${WATCH_DEPLOY:-backend}"
DATA_DIR=/data
STATE_DIR="$DATA_DIR/state"
DUMPS_DIR="$DATA_DIR/dumps"
STATE_FILE="$STATE_DIR/restart-counts.txt"
SNAPSHOT="$STATE_DIR/snapshot.txt"
PENDING_DIR="$DATA_DIR/pending"
TEMPLATE_FILE="/scripts/alert-template.txt"

mkdir -p "$STATE_DIR" "$DUMPS_DIR" "$PENDING_DIR"

# ===== retry of pending notifications =====
for f in "$PENDING_DIR"/*.txt; do
  [ -e "$f" ] || break
  if /scripts/notify-messenger.sh "$(cat "$f")"; then
    rm -f "$f"
    echo "retry: pending message sent ($(basename "$f"))"
  else
    echo "retry: still not sent - keeping $(basename "$f")" >&2
  fi
done

# ===== message template =====
template() {
  local tpl
  if [ -f "$TEMPLATE_FILE" ]; then
    tpl=$(cat "$TEMPLATE_FILE")
  else
    tpl='🚨 Heads up! Something went down on the server 🚨

😱 Hey, check the server — the website is not running!

📦 Pod: {POD}
🔄 Restarts: {RESTARTS_PREV} → {RESTARTS_NOW}
🩺 Reason: {REASON} (exit {EXIT_CODE})
🏷️ Version: {IMAGE}
📄 Dump: {DUMP}'
  fi
  tpl="${tpl//\{POD\}/$POD}"
  tpl="${tpl//\{RESTARTS_PREV\}/$PREV}"
  tpl="${tpl//\{RESTARTS_NOW\}/$COUNT}"
  tpl="${tpl//\{REASON\}/$REASON}"
  tpl="${tpl//\{EXIT_CODE\}/$EXIT_CODE}"
  tpl="${tpl//\{STATUS\}/$STATUS}"
  tpl="${tpl//\{IMAGE\}/$IMAGE_TAG}"
  tpl="${tpl//\{DUMP\}/$DUMP_LABEL}"
  tpl="${tpl//\{LOG_SNIPPET\}/$SNIPPET}"
  printf '%s\n' "$tpl"
}

# Current state: pod | restartCount | last termination reason | exit code | waiting.reason
# On failure keep the previous baseline so the CronJob can retry (no false incidents).
kubectl -n "$NS" get pods -l "$LABEL" \
  -o jsonpath='{range .items[*]}{.metadata.name}{"\t"}{.status.containerStatuses[0].restartCount}{"\t"}{.status.containerStatuses[0].lastState.terminated.reason}{"\t"}{.status.containerStatuses[0].lastState.terminated.exitCode}{"\t"}{.status.containerStatuses[0].waiting.reason}{"\n"}{end}' \
  > "$SNAPSHOT" 2>/dev/null || {
  echo "watch: kubectl get pods failed - keeping previous state" >&2
  exit 1
}

while IFS=$'\t' read -r POD COUNT LAST_REASON EXIT_CODE WAITING_REASON; do
  [ -z "${POD:-}" ] && continue
  COUNT="${COUNT:-0}"
  PREV=0
  if [ -f "$STATE_FILE" ]; then
    LINE=$(awk -F'\t' -v p="$POD" '$1==p{print $2}' "$STATE_FILE" | head -n1)
    [ -n "$LINE" ] && PREV="$LINE"
  fi
  [ "$COUNT" -le "$PREV" ] && continue

  # ===== pod restarted - build the dump =====
  INCIDENT_DIR="$DUMPS_DIR/dump-$(date +%Y%m%d-%H%M%S)-$POD"
  mkdir -p "$INCIDENT_DIR"

  kubectl -n "$NS" logs "$POD" --previous --tail=500 > "$INCIDENT_DIR/logs.txt" 2>/dev/null \
    || echo "no previous logs (container did not exist before)" > "$INCIDENT_DIR/logs.txt"
  kubectl -n "$NS" describe pod "$POD" > "$INCIDENT_DIR/describe.txt" 2>/dev/null || true
  kubectl -n "$NS" get events --field-selector "involvedObject.name=$POD" --sort-by=.lastTimestamp 2>/dev/null | tail -n 60 > "$INCIDENT_DIR/events.txt" || true
  kubectl -n "$NS" get pod "$POD" -o yaml > "$INCIDENT_DIR/pod.yaml" 2>/dev/null || true

  REASON="${LAST_REASON:-unknown}"
  STATUS="${WAITING_REASON:-}"
  IMAGE_TAG=$(kubectl -n "$NS" get deploy "$DEPLOY_NAME" -o jsonpath='{.spec.template.spec.containers[0].image}' 2>/dev/null || true)
  IMAGE_TAG="${IMAGE_TAG##*:}"
  [ -z "$IMAGE_TAG" ] && IMAGE_TAG="unknown"
  SNIPPET=$(tail -n 15 "$INCIDENT_DIR/logs.txt" 2>/dev/null | head -c 800)
  DUMP_LABEL="cosmo-incidents/$(basename "$INCIDENT_DIR")"

  MSG=$(template)

  echo "INCIDENT: $POD restarts $PREV -> $COUNT ($REASON)"
  if /scripts/notify-messenger.sh "$MSG"; then
    echo "INCIDENT: notification sent for $POD"
  else
    PENDING_ALERT="$PENDING_DIR/alert-$(date +%Y%m%d-%H%M%S)-$POD.txt"
    echo "$MSG" > "$PENDING_ALERT"
    echo "INCIDENT: notification NOT sent for $POD - queued for retry ($(basename "$PENDING_ALERT"))" >&2
  fi
done < "$SNAPSHOT"

mv "$SNAPSHOT" "$STATE_FILE"
exit 0