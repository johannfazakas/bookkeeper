#!/bin/bash

STACK_NAME="stuff-app"

aws cloudformation delete-stack --stack-name $STACK_NAME
