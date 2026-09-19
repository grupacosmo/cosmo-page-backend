#!/usr/bin/env bash
# Wysyla powiadomienie do Messengera (Graph API, wiadomosc strony).
# Wymagane zmienne: FB_PAGE_TOKEN (juz jest w /cosmo/.env-prod) oraz
# MESSENGER_RECIPIENT_ID = thread id grupy/czatowki (poczatek zwykle "t_id...").
set -u

MESSAGE="${1:-}"
: "${FB_PAGE_TOKEN:?FB_PAGE_TOKEN env missing}"
: "${MESSENGER_RECIPIENT_ID:?MESSENGER_RECIPIENT_ID env missing (thread id grupy na Messengerze)}"
FB_API_VERSION="${FB_API_VERSION:-v21.0}"

if [ -z "$MESSAGE" ]; then
  echo "notify-messenger: pusty message, pomijam" >&2
  exit 0
fi

PAYLOAD=$(jq -n --arg id "$MESSENGER_RECIPIENT_ID" --arg text "$MESSAGE" \
  '{recipient: {id: $id}, message: {text: $text}}')

RESP=$(curl -sS -X POST "https://graph.facebook.com/${FB_API_VERSION}/me/messages" \
  -H "Content-Type: application/json" \
  -d "$PAYLOAD")

if echo "$RESP" | grep -q '"error"'; then
  echo "notify-messenger: blad API: $RESP" >&2
  exit 1
fi
echo "notify-messenger: wyslano do $MESSENGER_RECIPIENT_ID"