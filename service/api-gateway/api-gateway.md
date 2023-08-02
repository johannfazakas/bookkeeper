# bookkeeper-api-gateway

Bookkeeper api gateway.

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
./gradlew clean runFatJar
```

## Build docker image

```shell
./scripts/build-api-gateway-image.sh
```

## Push docker image to ECR

```shell
./scripts/push-api-gateway-image.sh
```
