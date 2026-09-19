#!/usr/bin/env bash
# Find MESSENGER_RECIPIENT_ID (group thread id) for crash-watcher alerts.
#
# How it works:
#   The Page must be added as a participant of a Messenger group chat.
#   Every Page conversation has an id like "t_id_...". That id goes into
#   MESSENGER_RECIPIENT_ID in /cosmo/.env-prod.
#
# Usage:
#   FB_PAGE_TOKEN=<page token> ./find-thread.sh                          # list conversations
#   FB_PAGE_TOKEN=<page token> ./find-thread.sh --test t_id_... "test"   # test send
#
# Requires: curl + jq (both available in the alpine/k8s image used by crash-watcher).
set -euo pipefail

: "${FB_PAGE_TOKEN:?FB_PAGE_TOKEN env missing}"
FB_API_VERSION="${FB_API_VERSION:-v21.0}"
GRAPH="https://graph.facebook.com/${FB_API_VERSION}"

send_test() {
  local id="${1:?usage: --test <t_id...> <message>}"
  local text="${2:-Test message from crash-watcher}"
  local payload resp
  payload=$(jq -n --arg id "$id" --arg text "$text" \
    '{recipient: {id: $id}, message: {text: $text}}')
  resp=$(curl -sS -X POST "$GRAPH/me/messages?access_token=$FB_PAGE_TOKEN" \
    -H "Content-Type: application/json" -d "$payload")
  if echo "$resp" | grep -q '"error"'; then
    echo "Graph API error: $resp" >&2
    exit 1
  fi
  echo "OK: test sent to $id"
}

if [ "${1:-}" = "--test" ]; then
  send_test "${2:-}" "${3:-}"
  exit 0
fi

RESP=$(curl -sS "$GRAPH/me/conversations?fields=id,updated_time,message_count,participants{id,name}&limit=100&access_token=$FB_PAGE_TOKEN")
if echo "$RESP" | grep -q '"error"'; then
  echo "Graph API error: $RESP" >&2
  exit 1
fi

echo "Page conversations:"
echo "$RESP" | jq -r '.data[] | "\(if ((.participants.data // []) | length) >= 3 then "[GROUP]" else "[1:1]" end) id: \(.id)\n  participants: \([.participants.data[]?.name] | join(", "))\n  messages: \(.message_count) | updated: \(.updated_time)\n"'

echo "HINT:"
echo "  [GROUP] = group chat (Page + at least 2 people) - pick this one so"
echo "            alerts land in the chat where your users are."
echo "  [1:1]   = private conversation between the Page and one user."
echo "  1. The Page must be a participant of the Messenger group (add it to the group)."
echo "  2. Pick the id of a conversation marked [GROUP] (t_id_...)."
echo "  3. Put it as MESSENGER_RECIPIENT_ID in /cosmo/.env-prod and recreate the cosmo-env Secret (k8s/apply.sh step 1)."
echo "  4. Test: FB_PAGE_TOKEN=<token> ./find-thread.sh --test t_id_... \"test alert\""
echo
echo "NOTE (24h messaging window): a Page can only message within 24h of the last"
echo "interaction, UNLESS a participant has a role on the Page (admin/tester)."
echo "To always get alerts, give your FB user a tester/admin role on the Page."