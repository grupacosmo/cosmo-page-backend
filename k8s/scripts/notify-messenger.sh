#!/usr/bin/env bash
# Sends a Messenger notification (Graph API, sent as the Page).
# Required env: FB_PAGE_TOKEN (already in /cosmo/.env-prod) and
# MESSENGER_RECIPIENT_ID = group/chat thread id (usually starts with "t_id...").
set -u

MESSAGE="${1:-}"
: "${FB_PAGE_TOKEN:?FB_PAGE_TOKEN env missing}"
: "${MESSENGER_RECIPIENT_ID:?MESSENGER_RECIPIENT_ID env missing (Messenger group thread id)}"
FB_API_VERSION="${FB_API_VERSION:-v21.0}"

if [ -z "$MESSAGE" ]; then
  echo "notify-messenger: empty message, skipping" >&2
  exit 0
fi

PAYLOAD=$(jq -n --arg id "$MESSENGER_RECIPIENT_ID" --arg text "$MESSAGE" \
  '{recipient: {id: $id}, message: {text: $text}}')

RESP=$(curl -sS -X POST "https://graph.facebook.com/${FB_API_VERSION}/me/messages" \
  -H "Content-Type: application/json" \
  -d "$PAYLOAD")

if echo "$RESP" | grep -q '"error"'; then
  echo "notify-messenger: API error: $RESP" >&2
  exit 1
fi
echo "notify-messenger: sent to $MESSENGER_RECIPIENT_ID"