#!/bin/bash

export GITHUB_TOKEN='REPLACE_ME'

aws cloudformation create-stack \
  --stack-name stuff-ci-cd-stack \
  --template-body file://cloudformation/stuff-ci-cd-template.yml \
  --parameters ParameterKey=GitHubOAuthToken,ParameterValue="$GITHUB_TOKEN" \
  --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM
