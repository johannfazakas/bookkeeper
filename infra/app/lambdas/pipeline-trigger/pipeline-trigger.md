# Stuff Pipeline Trigger
AWS Lambda function that triggers the appropriate pipelines in monorepo based on the changed files in the commit.

## Prerequisites
- Docker
- AWS CLI
- Make scripts executable:
```shell 
chmod +x ./scripts/*.sh
```

## Build docker image
```shell
./scripts/build-pipeline-trigger-image.sh
```

## Push image to ECR
```shell
./scripts/push-pipeline-trigger-image.sh
```
