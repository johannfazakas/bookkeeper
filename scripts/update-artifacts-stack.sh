#!/bin/bash

aws cloudformation update-stack \
  --stack-name stuff-artifacts-stack \
  --template-body file://cloudformation/stuff-artifacts-template.yml
