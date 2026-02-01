#!/usr/bin/env bash
set -euo pipefail
set -x

PC="${1:-}"

sudo docker run --rm -i \
  --network glpi-mail \
  -v "$(pwd):/work" \
  -w /work \
  alpine:latest sh -lc "
    apk add --no-cache netcat-openbsd >/dev/null
    apk add --no-cache bash netcat-openbsd >/dev/null
    chmod +x ./abrir_chamado.bash
    ./abrir_chamado.bash ${PC}
  "
