# stuff-infra
Infra stuff for Stuff!

## Stuff App Infrastructure

### Create new App CloudFormation Stack
```shell
aws cloudformation create-stack \
  --stack-name stuff-app-stack \
  --template-body file://cloudformation/stuff-app-template.yml \
  --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM
```

### Update App Stack
```shell
aws cloudformation update-stack \
  --stack-name stuff-app-stack \
  --template-body file://cloudformation/stuff-app-template.yml \
  --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM
```

### Delete App Stack
```shell
aws cloudformation delete-stack --stack-name stuff-app-stack
```

## Stuff App CI/CD

### Create new CI/CD CloudFormation Stack
```shell
aws cloudformation create-stack \
  --stack-name stuff-ci-cd-stack \
  --template-body file://cloudformation/stuff-ci-cd-template.yml \
  --parameters ParameterKey=GitHubOAuthToken,ParameterValue={REPLACE_ME_WITH_YOUR_TOKEN} \
  --capabilities CAPABILITY_IAM
```

### Update CI/CD Stack
```shell
export GITHUB_TOKEN={REPLACE_ME_WITH_YOUR_TOKEN}
aws cloudformation update-stack \
  --stack-name stuff-ci-cd-stack \
  --template-body file://cloudformation/stuff-ci-cd-template.yml \
  --parameters ParameterKey=GitHubOAuthToken,ParameterValue={REPLACE_ME_WITH_YOUR_TOKEN} \
  --capabilities CAPABILITY_IAM
```

### Delete CI/CD Stack
```shell
aws cloudformation delete-stack --stack-name stuff-ci-cd-stack
```
