#!/usr/bin/env bash
# crash-watcher: wykrywa restarty podow backendu, robi "dump" (logi, events,
# describe, yaml) do PVC i wysyla powiadomienie na Messengera.
# Uruchamiany jako CronJob co minute.
#
# Tresc wiadomosci to szablon z /scripts/alert-template.txt (ConfigMap
# crash-watcher-scripts). Dostepne placeholdery:
#   {POD} {RESTARTS_PREV} {RESTARTS_NOW} {REASON} {EXIT_CODE} {STATUS}
#   {IMAGE} {DUMP} {LOG_SNIPPET}
# Nieudana wysylka jest zapisywana do $DATA_DIR/pending-alert.txt i ponawiana
# przy nastepnym uruchomieniu.
set -u

NS="${WATCH_NAMESPACE:-cosmo}"
LABEL="${WATCH_LABEL:-app=cosmo-backend}"
DEPLOY_NAME="${WATCH_DEPLOY:-backend}"
DATA_DIR=/data
STATE_DIR="$DATA_DIR/state"
DUMPS_DIR="$DATA_DIR/dumps"
STATE_FILE="$STATE_DIR/restart-counts.txt"
SNAPSHOT="$STATE_DIR/snapshot.txt"
PENDING_FILE="$DATA_DIR/pending-alert.txt"
TEMPLATE_FILE="/scripts/alert-template.txt"

mkdir -p "$STATE_DIR" "$DUMPS_DIR"

# ===== retry zaleglego powiadomienia =====
if [ -f "$PENDING_FILE" ]; then
  if /scripts/notify-messenger.sh "$(cat "$PENDING_FILE")"; then
    rm -f "$PENDING_FILE"
    echo "retry: zalegla wiadomosc wyslana"
  else
    echo "retry: nadal nie wyslano - zostaje w pending" >&2
  fi
fi

# ===== szablon wiadomosci =====
template() {
  local tpl
  if [ -f "$TEMPLATE_FILE" ]; then
    tpl=$(cat "$TEMPLATE_FILE")
  else
    tpl='🚨 Uwaga! Coś padło na serwerze 🚨

😱 Hej, sprawdźcie serwer — strona nie działa!

📦 Pod: {POD}
🔄 Restarty: {RESTARTS_PREV} → {RESTARTS_NOW}
🩺 Powód: {REASON} (exit {EXIT_CODE})
🏷️ Wersja: {IMAGE}
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

# Biezacy stan: pod | restartCount | powod ostatniego termination | exit code | waiting.reason
kubectl -n "$NS" get pods -l "$LABEL" \
  -o jsonpath='{range .items[*]}{.metadata.name}{"\t"}{.status.containerStatuses[0].restartCount}{"\t"}{.status.containerStatuses[0].lastState.terminated.reason}{"\t"}{.status.containerStatuses[0].lastState.terminated.exitCode}{"\t"}{.status.containerStatuses[0].waiting.reason}{"\n"}{end}' \
  > "$SNAPSHOT" 2>/dev/null

while IFS=$'\t' read -r POD COUNT LAST_REASON EXIT_CODE WAITING_REASON; do
  [ -z "${POD:-}" ] && continue
  COUNT="${COUNT:-0}"
  PREV=0
  if [ -f "$STATE_FILE" ]; then
    LINE=$(awk -F'\t' -v p="$POD" '$1==p{print $2}' "$STATE_FILE" | head -n1)
    [ -n "$LINE" ] && PREV="$LINE"
  fi
  [ "$COUNT" -le "$PREV" ] && continue

  # ===== pod zostal zrestartowany - robimy dump =====
  INCIDENT_DIR="$DUMPS_DIR/dump-$(date +%Y%m%d-%H%M%S)-$POD"
  mkdir -p "$INCIDENT_DIR"

  kubectl -n "$NS" logs "$POD" --previous --tail=500 > "$INCIDENT_DIR/logs.txt" 2>/dev/null \
    || echo "brak poprzednich logow (kontener nie istnial wczesniej)" > "$INCIDENT_DIR/logs.txt"
  kubectl -n "$NS" describe pod "$POD" > "$INCIDENT_DIR/describe.txt" 2>/dev/null || true
  kubectl -n "$NS" get events --field-selector "involvedObject.name=$POD" --sort-by=.lastTimestamp 2>/dev/null | tail -n 60 > "$INCIDENT_DIR/events.txt" || true
  kubectl -n "$NS" get pod "$POD" -o yaml > "$INCIDENT_DIR/pod.yaml" 2>/dev/null || true

  REASON="${LAST_REASON:-nieznany}"
  STATUS="${WAITING_REASON:-}"
  IMAGE_TAG=$(kubectl -n "$NS" get deploy "$DEPLOY_NAME" -o jsonpath='{.spec.template.spec.containers[0].image}' 2>/dev/null || true)
  IMAGE_TAG="${IMAGE_TAG##*:}"
  [ -z "$IMAGE_TAG" ] && IMAGE_TAG="nieznana"
  SNIPPET=$(tail -n 15 "$INCIDENT_DIR/logs.txt" 2>/dev/null | head -c 800)
  DUMP_LABEL="cosmo-incidents/$(basename "$INCIDENT_DIR")"

  MSG=$(template)

  echo "INCIDENT: $POD restarts $PREV -> $COUNT ($REASON)"
  if /scripts/notify-messenger.sh "$MSG"; then
    echo "INCIDENT: powiadomienie wyslane dla $POD"
  else
    echo "$MSG" > "$PENDING_FILE"
    echo "INCIDENT: powiadomienie NIE wyslane dla $POD - zapisane do retry" >&2
  fi
done < "$SNAPSHOT"

mv "$SNAPSHOT" "$STATE_FILE"
exit 0