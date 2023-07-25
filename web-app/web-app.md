# bookkeeper-web-app
Web App for Bookkeeping.

## Prerequisites
- Docker
- AWS CLI
- Make scripts executable:
```shell 
chmod +x ./scripts/*.sh
```

## Local development
```shell
./gradlew clean run --continuous
```

## Build docker image
```shell
./scripts/build-web-app-image.sh
```

## Push docker image to ECR
```shell
./scripts/push-web-app-image.sh
```
