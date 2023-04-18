#!/bin/bash

aws cloudformation update-stack \
  --stack-name stuff-source-filter-stack \
  --template-body file://cloudformation/stuff-source-filter-template.yml \
  --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM
