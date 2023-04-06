#!/bin/bash

aws cloudformation create-stack \
  --stack-name stuff-ci-cd-stack \
  --template-body file://cloudformation/stuff-ci-cd-template.yml \
  --parameters ParameterKey=GitHubOAuthToken,ParameterValue={REPLACE_ME_WITH_YOUR_TOKEN} \
  --capabilities CAPABILITY_IAM