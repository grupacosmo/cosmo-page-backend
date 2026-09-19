#!/usr/bin/env bash
# Wdrazanie czesci k8s (branch feature/k8s-self-healing-and-alerts).
# Uruchom NA serwerze produkcyjnym (tam gdzie /cosmo/.env-prod i dostep do klastra).
# Wymagane: kubectl z dostepem do klastra, curl/jq niepotrzebne (skrypty robi CronJob).
set -euo pipefail

NS="cosmo"

echo "==> Krok 0/4: Secret regcred (pull prywatnego obrazu z Docker Hub)"
echo "    Obraz cosmopk/cosmo-page-backend jest na prywatnym repo."
echo "    Uzyje DOCKERHUB_USERNAME/DOCKERHUB_TOKEN ze srodowiska, jesli sa ustawione;"
echo "    w przeciwnym razie zapyta. Token = access token, NIE haslo konta."
if [ -n "${DOCKERHUB_USERNAME:-}" ] && [ -n "${DOCKERHUB_TOKEN:-}" ]; then
  REGCRED_USER="$DOCKERHUB_USERNAME"
  REGCRED_PASS="$DOCKERHUB_TOKEN"
else
  read -rp "Docker Hub username: " REGCRED_USER
  read -rsp "Docker Hub access token: " REGCRED_PASS
  echo
fi
kubectl -n "$NS" create secret docker-registry regcred \
  --docker-server=https://index.docker.io/v1/ \
  --docker-username="$REGCRED_USER" \
  --docker-password="$REGCRED_PASS" \
  --dry-run=client -o yaml | kubectl apply -f -

echo "==> Krok 1/4: Secret cosmo-env z /cosmo/.env-prod"
echo "    PRZED tym dodaj do /cosmo/.env-prod:"
echo "      MESSENGER_RECIPIENT_ID=<thread_id grupy na Messengerze, np. t_id_...>"
echo "    (FB_PAGE_TOKEN juz tam jest)"
kubectl -n "$NS" create secret generic cosmo-env \
  --from-env-file=/cosmo/.env-prod \
  --dry-run=client -o yaml | kubectl apply -f -

echo "==> Krok 2/4: backend.yaml (Namespace, PVC dumps, Deployment, Service)"
kubectl apply -f backend.yaml

echo "==> Krok 3/4: ConfigMap ze skryptami + szablonem + crash-watcher.yaml (RBAC, PVC, CronJob)"
kubectl create configmap crash-watcher-scripts \
  --from-file=scripts/watch.sh \
  --from-file=scripts/notify-messenger.sh \
  --from-file=scripts/alert-template.txt \
  -n "$NS" --dry-run=client -o yaml | kubectl apply -f -
kubectl apply -f crash-watcher.yaml

echo "==> Gotowe. Sprawdzenie:"
echo "    kubectl -n $NS get pods"
echo "    kubectl -n $NS get cronjob crash-watcher"
echo "    kubectl -n $NS logs -l job-name --tail=50   # logi ostatniego uruchomienia watchera"