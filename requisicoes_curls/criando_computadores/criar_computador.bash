#!/usr/bin/env bash
set -euo pipefail
#-e se algo der erro encerra o programa
#-u unset ou unbound variáveis são tratadas como erro 
#-o pipefail como o pipeline o erro é igual ao erro do último comando; essa flag faz com que
#caso qualquer comando mais a direta falhe, o script falhe também

#O <<'PY' é um heredoc (here document) um tipo de string multilinha que preserva a identação, espaços...
#com o "-" instrui o interpretador de Python a ler o script até achar o PY
#executamos tudo como um código python e guardamos em uma variável PAYLOAD
PAYLOAD=$(python3 - <<'PY'
import json
from random import randint

name="PC-FIN-"+str(randint(300, 1000))

payload = {"name" : name}

print(json.dumps(payload))
PY
)

echo "$PAYLOAD" | python3 -m json.tool

set -a
DIRETORIO=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" &> /dev/null && pwd)
source "$DIRETORIO/../auth.env"
set +a

GLPI_URL="http://localhost"

TOKEN_JSON=$(curl -s -X POST "$GLPI_URL/api.php/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data-urlencode "grant_type=password" \
  --data-urlencode "client_id=$CLIENT_ID" \
  --data-urlencode "client_secret=$CLIENT_SECRET" \
  --data-urlencode "username=$USERNAME" \
  --data-urlencode "password=$PASSWORD" \
  --data-urlencode "scope=api")

ACCESS_TOKEN=$(echo "$TOKEN_JSON" | python3 -c "import sys, json; print(json.load(sys.stdin).get('access_token',''))")

if [[ -z "$ACCESS_TOKEN" ]]; then
  echo "ERRO: access_token vazio. TOKEN_JSON foi:"
  echo "$TOKEN_JSON" | python3 -m json.tool
  exit 1
fi


echo "$PAYLOAD" | curl -s -X POST "$GLPI_URL/api.php/Assets/Computer" \
    -H "Authorization: Bearer $ACCESS_TOKEN" \
    -H "Content-Type: application/json" \
    -d @-

