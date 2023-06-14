#!/bin/bash

export DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

./$DIR/../gradlew clean shadowJar
docker build -t bookkeeper/pipeline-trigger $DIR/../

echo "Pipeline trigger image built."
