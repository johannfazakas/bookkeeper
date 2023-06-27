#!/bin/bash

export DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cd $DIR/../
./gradlew clean bootBuildImage

echo "Api image built."
