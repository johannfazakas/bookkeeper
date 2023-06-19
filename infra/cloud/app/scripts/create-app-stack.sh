#!/bin/bash

export DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$DIR/load-variables.sh"

aws s3 sync $TEMPLATES_DIRECTORY $TEMPLATES_S3_LOCATION

aws cloudformation create-stack \
--stack-name $STACK_NAME \
--template-url $ROOT_TEMPLATE_S3_LOCATION \
--parameters ParameterKey=GitHubOAuthToken,ParameterValue="$GITHUB_TOKEN" \
--capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM

echo "App stack creation triggered."
