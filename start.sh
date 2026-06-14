#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROXY_DIR="$ROOT/proxy"
CONFIG_FILE="$PROXY_DIR/config.yml"
JAR_FILE="$PROXY_DIR/WaterdogPE.jar"

if [[ ! -f "$JAR_FILE" ]]; then
  echo "WaterdogPE jar not found. Run ./setup.sh first."
  exit 1
fi

echo "Pick a server to proxy:"
options=("Hive" "CubeCraft" "NetherGames" "Mineville" "Custom")
select opt in "${options[@]}"; do
  case "$opt" in
    Hive)
      SERVER_NAME="Hive"
      SERVER_ADDRESS="play.hivemc.com:19132"
      break
      ;;
    CubeCraft)
      SERVER_NAME="CubeCraft"
      SERVER_ADDRESS="play.cubecraft.net:19132"
      break
      ;;
    NetherGames)
      SERVER_NAME="NetherGames"
      SERVER_ADDRESS="play.nethergames.org:19132"
      break
      ;;
    Mineville)
      SERVER_NAME="Mineville"
      SERVER_ADDRESS="play.mineville.org:19132"
      break
      ;;
    Custom)
      read -rp "Enter a server name (no spaces): " SERVER_NAME
      read -rp "Enter the server address or IP: " SERVER_HOST
      read -rp "Enter the server port: " SERVER_PORT
      SERVER_ADDRESS="${SERVER_HOST}:${SERVER_PORT}"
      break
      ;;
    *)
      echo "Invalid choice. Please choose a number from 1 to ${#options[@]}."
      ;;
  esac
done

cat > "$CONFIG_FILE" <<EOF
listener:
  host: 0.0.0.0:19132
  priorities:
  - $SERVER_NAME
servers:
  $SERVER_NAME:
    address: $SERVER_ADDRESS
    server_type: bedrock
EOF

echo "Wrote $CONFIG_FILE"
echo "Starting WaterdogPE proxy..."
cd "$PROXY_DIR"
java -jar "$JAR_FILE"
