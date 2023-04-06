#!/bin/bash

aws cloudformation update-stack \
  --stack-name stuff-app-stack \
  --template-body file://cloudformation/stuff-app-template.yml \
  --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM
