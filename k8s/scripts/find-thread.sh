#!/usr/bin/env bash
# Znajdz MESSENGER_RECIPIENT_ID (thread id grupy) dla powiadomien crash-watchera.
#
# Jak to dziala:
#   Strona (Page) musi byc dodana jako uczestnik grupy na Messengerze.
#   Kazda konwersacja strony ma id typu "t_id_...". To id wpisujesz jako
#   MESSENGER_RECIPIENT_ID w /cosmo/.env-prod.
#
# Uzycie:
#   FB_PAGE_TOKEN=<page token> ./find-thread.sh                          # lista konwersacji
#   FB_PAGE_TOKEN=<page token> ./find-thread.sh --test t_id_... "test"   # test wysylki
#
# Wymaga: curl + jq (ma je obraz alpine/k8s uzywany przez crash-watcher).
set -euo pipefail

: "${FB_PAGE_TOKEN:?FB_PAGE_TOKEN env missing}"
FB_API_VERSION="${FB_API_VERSION:-v21.0}"
GRAPH="https://graph.facebook.com/${FB_API_VERSION}"

send_test() {
  local id="${1:?usage: --test <t_id...> <message>}"
  local text="${2:-Test wiadomosc z crash-watchera}"
  local payload resp
  payload=$(jq -n --arg id "$id" --arg text "$text" \
    '{recipient: {id: $id}, message: {text: $text}}')
  resp=$(curl -sS -X POST "$GRAPH/me/messages?access_token=$FB_PAGE_TOKEN" \
    -H "Content-Type: application/json" -d "$payload")
  if echo "$resp" | grep -q '"error"'; then
    echo "BLAD Graph API: $resp" >&2
    exit 1
  fi
  echo "OK: wyslano test do $id"
}

if [ "${1:-}" = "--test" ]; then
  send_test "${2:-}" "${3:-}"
  exit 0
fi

RESP=$(curl -sS "$GRAPH/me/conversations?fields=id,updated_time,message_count,participants{id,name}&limit=100&access_token=$FB_PAGE_TOKEN")
if echo "$RESP" | grep -q '"error"'; then
  echo "BLAD Graph API: $RESP" >&2
  exit 1
fi

echo "Konwersacje strony:"
echo "$RESP" | jq -r '.data[] | "\(if ((.participants.data // []) | length) >= 3 then "[GRUPA]" else "[1:1]" end) id: \(.id)\n  participants: \([.participants.data[]?.name] | join(", "))\n  messages: \(.message_count) | updated: \(.updated_time)\n"'

echo "WSKAZOWKA:"
echo "  [GRUPA] = czat grupowy (strona + co najmniej 2 osoby) - TO wybierasz,"
echo "           zeby alerty trafialy do grupy, w ktorej siedza uzytkownicy."
echo "  [1:1]   = prywatna rozmowa strony z jednym uzytkownikiem."
echo "  1. Strona musi byc uczestnikiem grupy na Messengerze (dodaj ja do grupy)."
echo "  2. Wybierz id konwersacji oznaczonej [GRUPA] (t_id_...)."
echo "  3. Wpisz je jako MESSENGER_RECIPIENT_ID w /cosmo/.env-prod i odtworz Secret cosmo-env (k8s/apply.sh krok 1)."
echo "  4. Test: FB_PAGE_TOKEN=<token> ./find-thread.sh --test t_id_... \"test alert\""
echo
echo "UWAGA (24h okno wysylki): strona moze pisac tylko w oknie 24h od ostatniej"
echo "interakcji, CHYBA ZE uczestnik konwersacji ma role na stronie (admin/tester)."
echo "Zeby alerty zawsze docieraly, nadaj swojemu uzytkownikowi role tester/admin strony."