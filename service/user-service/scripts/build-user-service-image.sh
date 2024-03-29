#!/bin/bash

export DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query 'Account' --output text)

cd $DIR/../
./gradlew clean jibDockerBuild
docker tag bookkeeper/user-service:latest $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com/bookkeeper/user-service:latest

echo "User service image built."
