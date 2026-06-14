#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROXY_DIR="$ROOT/proxy"
PLUGIN_DIR="$PROXY_DIR/plugins"
LIB_DIR="$ROOT/plugin/lib"

echo "Updating apt and installing Java 17..."
sudo apt-get update
sudo apt-get install -y openjdk-17-jre-headless curl wget

echo "Creating WaterdogPE folder structure..."
mkdir -p "$PROXY_DIR"
mkdir -p "$PLUGIN_DIR"
mkdir -p "$LIB_DIR"

echo "Downloading WaterdogPE latest jar..."
if ! curl -L -o "$PROXY_DIR/WaterdogPE.jar" "https://github.com/WaterdogPE/WaterdogPE/releases/latest/download/WaterdogPE.jar"; then
  echo "Failed to download WaterdogPE.jar. Check your network or URL." >&2
  exit 1
fi

echo "Downloading Geyser-Waterdog plugin..."
if ! curl -L -o "$PLUGIN_DIR/Geyser-Waterdog.jar" "https://github.com/GeyserMC/Geyser/releases/latest/download/Geyser-Waterdog.jar"; then
  echo "Failed to download Geyser-Waterdog.jar. Check your network or URL." >&2
  exit 1
fi

echo "Copying WaterdogPE jar for plugin compilation..."
cp -f "$PROXY_DIR/WaterdogPE.jar" "$LIB_DIR/WaterdogPE.jar"

echo "Setup complete."
echo "Run ./build.sh to compile the plugin and ./start.sh to launch the proxy."
