#!/bin/bash

export DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cd $DIR/../
./gradlew clean shadowJar
docker build -t bookkeeper/pipeline-trigger .

echo "Pipeline trigger image built."
