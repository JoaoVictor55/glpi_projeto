#!/usr/bin/env bash
set -euo pipefail
#-e se algo der erro encerra o programa
#-u unset ou unbound variáveis são tratadas como erro 
#-o pipefail como o pipeline o erro é igual ao erro do último comando; essa flag faz com que
#caso qualquer comando mais a direta falhe, o script falhe também

#O <<'PY' é um heredoc (here document) um tipo de string multilinha que preserva a identação, espaços...
#com o "-" instrui o interpretador de Python a ler o script até achar o PY
#executamos tudo como um código python e guardamos em uma variável PAYLOAD

#definindo algumas variáveis
GLPI_URL="http://localhost"

set -a

#aqui estamos pegando o nome do diretório atual:
#o cd muda para o diretório, o -- faz com que tudo após ele seja argumento posicional
#o "$(dirname -- "${BASH_SOURCE[0]}")" pega o caminho do script. Redirecionamos tanto o erro 
#quando o resultado do cd para o null e caso o comando der certo, imprimios onde estamos
DIRETORIO=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" &> /dev/null && pwd)

source "$DIRETORIO/../auth.env"
set +a

echo "[1/2] Pegando access token (OAuth2 password grant)..."

#executamos o comando curl e o resultado é armazenado na variável TOKEN_JSON
TOKEN_JSON=$(curl -s -X POST "$GLPI_URL/api.php/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data-urlencode "grant_type=password" \
  --data-urlencode "client_id=$CLIENT_ID" \
  --data-urlencode "client_secret=$CLIENT_SECRET" \
  --data-urlencode "username=$USERNAME" \
  --data-urlencode "password=$PASSWORD" \
  --data-urlencode "scope=$SCOPE")

#pega o conteúdo da variável TOKEN_JSON e passa (o operador | é pipe, ele pega a saída de um
#comando e passa para outro) o comando json.tool formata o json em um formato legível.
echo "$TOKEN_JSON" | python3 -m json.tool

#pega o conteúdo de TOKEN_JSON e passa para o código em python que
#extrai do mesmo o access_token que é armazenado na variável ACCESS_TOKEN
#em json.load a fonte de dados é estabelecida como o stdin e o print do python solta para o stdout que é #capturada
ACCESS_TOKEN=$(echo "$TOKEN_JSON" | python3 -c "import sys, json; print(json.load(sys.stdin).get('access_token',''))")

#se $ACCESS_TOKEN for vazio (o que o -z significa; use sempre com "" para tais testes) então:
if [ -z "$ACCESS_TOKEN" ]; then
  echo "Não veio access_token. Veja o JSON acima."
  exit 1
fi

#curl -s -X GET "$GLPI_URL/api.php/Assets/Computer/1" \
#-H "Authorization: Bearer $ACCESS_TOKEN" \
curl -s -X GET "http://localhost/api.php/Assets/Computer/1" -H "Authorization: Bearer $ACCESS_TOKEN"