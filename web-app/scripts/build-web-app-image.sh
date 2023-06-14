#!/bin/bash

export DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cd $DIR/../
./gradlew clean browserProductionWebpack
docker image build -t bookkeeper/web-app:latest .

echo "Web app image built."
