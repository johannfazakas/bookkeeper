#!/bin/bash

export STACK_NAME="stuff-app"
export CHANGE_SET_NAME="$STACK_NAME-$(date +%s)"
export SCRIPTS_DIRECTORY="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export TEMPLATE_FILE="$SCRIPTS_DIRECTORY/../templates/app.yml"
