#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PLUGIN_SRC="$ROOT/plugin/src"
PLUGIN_BUILD="$ROOT/plugin/build"
PLUGIN_TARGET="$ROOT/plugin/target"
PLUGIN_YML="$ROOT/plugin/plugin.yml"
PROXY_LIB="$ROOT/plugin/lib/WaterdogPE.jar"
OUTPUT_JAR="$PLUGIN_TARGET/MyProxyPlugin.jar"
DEST_PLUGIN="$ROOT/proxy/plugins"

if [[ ! -f "$PROXY_LIB" ]]; then
  echo "Missing WaterdogPE jar for compilation. Run ./setup.sh first."
  exit 1
fi

rm -rf "$PLUGIN_BUILD"
mkdir -p "$PLUGIN_BUILD" "$PLUGIN_TARGET" "$DEST_PLUGIN"

echo "Compiling MyProxyPlugin..."
javac -classpath "$PROXY_LIB" -d "$PLUGIN_BUILD" "$PLUGIN_SRC"/*.java

cp "$PLUGIN_YML" "$PLUGIN_BUILD/"

cd "$PLUGIN_BUILD"
jar cf "$OUTPUT_JAR" .

mv -f "$OUTPUT_JAR" "$DEST_PLUGIN/"

echo "Build complete: $DEST_PLUGIN/$(basename "$OUTPUT_JAR")"
