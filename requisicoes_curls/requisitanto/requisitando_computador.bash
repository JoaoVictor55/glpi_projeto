#!/usr/bin/env bash
set -euo pipefail
#-e se algo der erro encerra o programa
#-u unset ou unbound variáveis são tratadas como erro 
#-o pipefail como o pipeline o erro é igual ao erro do último comando; essa flag faz com que
#caso qualquer comando mais a direta falhe, o script falhe também

#O <<'PY' é um heredoc (here document) um tipo de string multilinha que preserva a identação, espaços...
#com o "-" instrui o interpretador de Python a ler o script até achar o PY
#executamos tudo como um código python e guardamos em uma variável PAYLOAD
