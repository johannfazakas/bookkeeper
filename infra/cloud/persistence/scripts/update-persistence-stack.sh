#!/bin/bash

export DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$DIR/load-variables.sh"

aws cloudformation create-change-set \
  --stack-name $STACK_NAME \
  --template-body file://$TEMPLATE_FILE \
  --change-set-name $CHANGE_SET_NAME

echo "Waiting for change set to be created..."
aws cloudformation wait change-set-create-complete \
  --stack-name $STACK_NAME \
  --change-set-name $CHANGE_SET_NAME

aws cloudformation execute-change-set \
  --stack-name $STACK_NAME \
  --change-set-name $CHANGE_SET_NAME
echo "Change set executed, persistence stack update triggered."
