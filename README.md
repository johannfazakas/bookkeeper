# stuff-infra
Infra stuff for Stuff!

## Stuff App Infrastructure

### Create new App CloudFormation Stack
```shell
aws cloudformation create-stack \
  --stack-name stuff-app-stack \
  --template-body file://cloudformation/stuff-app-template.yml
```

### Update App Stack
```shell
aws cloudformation update-stack \
  --stack-name stuff-app-stack \
  --template-body file://cloudformation/stuff-app-template.yml
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
  --capabilities CAPABILITY_IAM
```

### Update CI/CD Stack
```shell
aws cloudformation update-stack \
  --stack-name stuff-ci-cd-stack \
  --template-body file://cloudformation/stuff-ci-cd-template.yml \
  --capabilities CAPABILITY_IAM
```

### Delete CI/CD Stack
```shell
aws cloudformation delete-stack --stack-name stuff-ci-cd-stack
```
