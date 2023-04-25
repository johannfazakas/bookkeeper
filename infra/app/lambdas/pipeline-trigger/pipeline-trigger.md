# Stuff Pipeline Trigger
AWS Lambda function that triggers the appropriate pipelines in monorepo based on the changed files in the commit.

## Prerequisites
- Docker
- AWS CLI

## Build and Push image to ECR
```shell
./gradlew clean shadowJar
docker build -t stuff/pipeline-trigger .
aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin 572338991617.dkr.ecr.eu-central-1.amazonaws.com
docker tag stuff/pipeline-trigger:latest 572338991617.dkr.ecr.eu-central-1.amazonaws.com/stuff/pipeline-trigger:latest
docker push 572338991617.dkr.ecr.eu-central-1.amazonaws.com/stuff/pipeline-trigger:latest
```