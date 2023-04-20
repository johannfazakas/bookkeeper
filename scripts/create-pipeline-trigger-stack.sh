#!/bin/bash

aws cloudformation create-stack \
  --stack-name stuff-source-filter-stack \
  --template-body file://cloudformation/stuff-pipeline-trigger-template.yml \
  --capabilities CAPABILITY_NAMED_IAM
