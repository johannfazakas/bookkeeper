#!/bin/bash

export GITHUB_TOKEN='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'

export STACK_NAME="bookkeeper-app"
export BOOKKEEPER_ARTIFACTS_BUCKET="bookkeeper-app-artifacts"
export CHANGE_SET_NAME="$STACK_NAME-$(date +%s)"
export SCRIPTS_DIRECTORY="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export TEMPLATES_DIRECTORY="$SCRIPTS_DIRECTORY/../templates/"
export TEMPLATES_S3_LOCATION="s3://$BOOKKEEPER_ARTIFACTS_BUCKET/templates/"
export ROOT_TEMPLATE_S3_LOCATION="https://$BOOKKEEPER_ARTIFACTS_BUCKET.s3.amazonaws.com/templates/app.yml"
