#!/bin/bash

aws cloudformation create-stack \
  --stack-name stuff-artifacts-stack \
  --template-body file://cloudformation/stuff-artifacts-template.yml
