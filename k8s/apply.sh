#!/usr/bin/env bash
# One-time Kubernetes install (branch feature/k8s-self-healing-and-alerts).
# Run ON the production server (where /cosmo/.env-prod and cluster access live).
# Requires: kubectl with access to the cluster.
set -euo pipefail

NS="cosmo"

echo "==> Step 0/4: regcred Secret (pull private Docker Hub image)"
echo "    cosmopk/cosmo-page-backend lives in a private repo."
echo "    Uses DOCKERHUB_USERNAME/DOCKERHUB_TOKEN from the environment if set;"
echo "    otherwise it prompts. The token is an access token, NOT the account password."
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

echo "==> Step 1/4: cosmo-env Secret from /cosmo/.env-prod"
echo "    BEFORE this, add to /cosmo/.env-prod:"
echo "      MESSENGER_RECIPIENT_ID=<Messenger group thread id, e.g. t_id_...>"
echo "    (FB_PAGE_TOKEN is already there)"
kubectl -n "$NS" create secret generic cosmo-env \
  --from-env-file=/cosmo/.env-prod \
  --dry-run=client -o yaml | kubectl apply -f -

echo "==> Step 2/4: backend.yaml (Namespace, PVC dumps, Deployment, Service)"
kubectl apply -f backend.yaml

echo "==> Step 3/4: ConfigMap with scripts + template + crash-watcher.yaml (RBAC, PVC, CronJob)"
kubectl create configmap crash-watcher-scripts \
  --from-file=scripts/watch.sh \
  --from-file=scripts/notify-messenger.sh \
  --from-file=scripts/alert-template.txt \
  -n "$NS" --dry-run=client -o yaml | kubectl apply -f -
kubectl apply -f crash-watcher.yaml

echo "==> Done. Check with:"
echo "    kubectl -n $NS get pods"
echo "    kubectl -n $NS get cronjob crash-watcher"
echo "    kubectl -n $NS logs -l job-name --tail=50   # logs of the last watcher run"