#!/bin/bash

STACK_NAME="stuff-app"
SCRIPT_DIRECTORY="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TEMPLATE_FILE="$SCRIPT_DIRECTORY/../templates/stuff-app.yml"

aws cloudformation create-stack \
--stack-name $STACK_NAME \
--template-body file://$TEMPLATE_FILE
