#!/bin/bash

export DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query 'Account' --output text)

cd $DIR/../
./gradlew clean bootBuildImage --imageName=$AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com/bookkeeper/api
docker tag $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com/bookkeeper/api:latest bookkeeper/api:latest

echo "Api image built."
