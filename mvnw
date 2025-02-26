#!/bin/sh
set -e

# Maven wrapper script

MAVEN_VERSION=3.8.6
MVN_CMD="./mvnw"

if [ -z "$MAVEN_OPTS" ]; then
  MAVEN_OPTS="-Xmx1024m"
fi

if [ -z "$M2_HOME" ]; then
  M2_HOME="$HOME/.m2"
fi

if [ -z "$MAVEN_HOME" ]; then
  MAVEN_HOME="$M2_HOME"
fi

if [ -z "$JAVA_HOME" ]; then
  echo "JAVA_HOME environment variable is not set."
  exit 1
fi

if [ ! -f "$MVN_CMD" ]; then
  echo "Maven wrapper script not found."
  exit 1
fi

exec "$MVN_CMD" "$@"