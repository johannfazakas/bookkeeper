#!/bin/bash

STACK_NAME="stuff-artifacts"

aws cloudformation delete-stack --stack-name $STACK_NAME
