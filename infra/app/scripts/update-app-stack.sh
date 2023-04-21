#!/bin/bash

export DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$DIR/load-variables.sh"

aws s3 sync $TEMPLATES_DIRECTORY $TEMPLATES_S3_LOCATION

aws cloudformation create-change-set \
--stack-name $STACK_NAME \
--template-url $ROOT_TEMPLATE_S3_LOCATION \
--change-set-name $CHANGE_SET_NAME \

echo "Waiting for change set to be created..."
aws cloudformation wait change-set-create-complete \
--stack-name $STACK_NAME \
--change-set-name $CHANGE_SET_NAME

aws cloudformation execute-change-set \
--stack-name $STACK_NAME \
--change-set-name $CHANGE_SET_NAME
echo "Change set executed, app stack update triggered."
