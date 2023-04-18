#!/bin/bash

aws cloudformation create-stack \
  --stack-name stuff-source-filter-stack \
  --template-body file://cloudformation/stuff-source-filter-template.yml \
  --capabilities CAPABILITY_NAMED_IAM
