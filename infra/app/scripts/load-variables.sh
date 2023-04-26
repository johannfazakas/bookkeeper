#!/bin/bash

export GITHUB_TOKEN='REPLACE_ME'

export STACK_NAME="stuff-app"
export STUFF_ARTIFACTS_BUCKET="stuff-artifacts"
export CHANGE_SET_NAME="$STACK_NAME-$(date +%s)"
export SCRIPTS_DIRECTORY="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export TEMPLATES_DIRECTORY="$SCRIPTS_DIRECTORY/../templates/"
export TEMPLATES_S3_LOCATION="s3://$STUFF_ARTIFACTS_BUCKET/templates/"
export ROOT_TEMPLATE_S3_LOCATION="https://$STUFF_ARTIFACTS_BUCKET.s3.amazonaws.com/templates/app.yml"
