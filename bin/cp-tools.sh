#!/bin/bash

if [ ! -d $JAVA_HOME ]; then
  echo "The JAVA_HOME environment variable must be set!"
  exit -1
fi

if [ ! -d $ANT_HOME ]; then
  echo "The ANT_HOME environment variable must be set!"
  exit -2
fi

ANT_CMD=$ANT_HOME/bin/ant
BUILD_FILE=cp-tools.xml
DEFAULT_RUN_TARGET=help
JAVA_CMD=$JAVA_HOME/bin/java
RUN_TARGET=$1

if [ -z "$RUN_TARGET" ]; then
  RUN_TARGET=$DEFAULT_RUN_TARGET
fi

$ANT_CMD -buildfile $BUILD_FILE $RUN_TARGET
