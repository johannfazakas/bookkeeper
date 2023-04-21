#!/bin/bash

export DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$DIR/load-variables.sh"

aws cloudformation delete-stack --stack-name $STACK_NAME
echo "Artifacts stack deletion triggered."
