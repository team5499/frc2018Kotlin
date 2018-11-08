#!/bin/bash
set -euxo pipefail

function cleanup {
    :
}

trap cleanup EXIT

command "./gradlew" -p "." build test
