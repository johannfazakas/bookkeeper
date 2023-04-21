#!/bin/bash

STACK_NAME="stuff-app"
CHANGE_SET_NAME="$STACK_NAME-$(date +%s)"
SCRIPT_DIRECTORY="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TEMPLATE_FILE="$SCRIPT_DIRECTORY/../templates/stuff-app.yml"

aws cloudformation create-change-set \
--stack-name $STACK_NAME \
--template-body file://$TEMPLATE_FILE \
--change-set-name $CHANGE_SET_NAME \

aws cloudformation wait change-set-create-complete \
--stack-name $STACK_NAME \
--change-set-name $CHANGE_SET_NAME

aws cloudformation execute-change-set \
--stack-name $STACK_NAME \
--change-set-name $CHANGE_SET_NAME
