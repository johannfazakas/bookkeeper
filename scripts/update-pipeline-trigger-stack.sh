#!/bin/bash

aws cloudformation update-stack \
  --stack-name stuff-source-filter-stack \
  --template-body file://cloudformation/stuff-pipeline-trigger-template.yml \
  --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM
