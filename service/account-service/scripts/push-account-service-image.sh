#!/bin/bash

export AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query 'Account' --output text)

aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com
docker tag bookkeeper/account-service:latest $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com/bookkeeper/account-service:latest
docker push $AWS_ACCOUNT_ID.dkr.ecr.eu-central-1.amazonaws.com/bookkeeper/account-service:latest

echo "Account service image pushed."
