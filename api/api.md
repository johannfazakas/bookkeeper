# bookkeeper-web-app
Web App for Bookkeeping.

## Prerequisites
- Docker
- AWS CLI
- [Start local infrastructure services](../infra/local/docker-compose.yml)
- Make scripts executable:
```shell 
chmod +x ./scripts/*.sh
```

## Local development
```shell
./gradlew clean bootRun --args='--spring.profiles.active=local'
```

## Build docker image
- [ ] have Dockerfile handle webpack distribution creation
```shell
./scripts/build-api-image.sh
```

## Push docker image to ECR
```shell
./scripts/push-api-image.sh
```
