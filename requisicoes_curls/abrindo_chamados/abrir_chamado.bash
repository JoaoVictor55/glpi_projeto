#!/usr/bin/env bash
set -euo pipefail
set -x

SMTP_HOST="mailserver"
SMTP_PORT="25"

FROM="usuario@empresa.local"
TO="suporte@empresa.local"

PREFIX="PC-FIN"
PC_START=1
PC_END=30

PROBLEMS=(
  "não liga"
  "tela azul ao iniciar"
  "sem internet"
  "muito lento"
  "não abre o sistema"
  "erro de autenticação"
  "impressora não imprime"
  "sem acesso ao e-mail"
  "travando após login"
)

pick_random_pc() {
  local i=$(( RANDOM % (PC_END - PC_START + 1) + PC_START ))
  printf "%s-%03d" "$PREFIX" "$i"
}

send_mail() {
  local subject="$1"
  local body="$2"

  local msgid="<$(date +%s).$RANDOM@mail.empresa.local>"

  {
    printf "EHLO empresa.local\r\n"
    printf "MAIL FROM:<%s>\r\n" "$FROM"
    printf "RCPT TO:<%s>\r\n" "$TO"
    printf "DATA\r\n"

    printf "From: %s\r\n" "$FROM"
    printf "To: %s\r\n" "$TO"
    printf "Subject: %s\r\n" "$subject"
    printf "Date: %s\r\n" "$(date -R)"
    printf "Message-ID: %s\r\n" "$msgid"
    printf "MIME-Version: 1.0\r\n"
    printf "Content-Type: text/plain; charset=UTF-8\r\n"
    printf "Content-Transfer-Encoding: 8bit\r\n"
    printf "\r\n"

    printf "%s\r\n" "$body"
    printf "\r\n.\r\n"
    printf "QUIT\r\n"
  }  | nc -v -C -w 3 "$SMTP_HOST" "$SMTP_PORT"
}

# =========================
# MAIN
# =========================

# Se vier argumento, usa ele. Se não vier, sorteia.
PC="${1:-$(pick_random_pc)}"

# Problema aleatório
p=$(( RANDOM % ${#PROBLEMS[@]} ))
PROBLEM="${PROBLEMS[$p]}"

SUBJECT="$PC $PROBLEM"

BODY="Olá suporte,

Estou com problema no computador $PC.

Problema: $PROBLEM
Horário: $(date)

Obrigado."

echo "Enviando chamado: $SUBJECT"
send_mail "$SUBJECT" "$BODY"
echo "OK!"
