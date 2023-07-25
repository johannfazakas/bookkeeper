# bookkeeper-account-service

Bookkeeper account service.

## Prerequisites

- Docker
- AWS CLI
- [Start local infrastructure services](../../infra/local/docker-compose.yml)
- Make scripts executable:

```shell 
chmod +x ./scripts/*.sh
```

## Local development

```shell
./gradlew clean bootRun
```

## Build docker image

```shell
./scripts/build-api-image.sh
```

## Push docker image to ECR

```shell
./scripts/push-api-image.sh
```
