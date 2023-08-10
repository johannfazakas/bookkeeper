# bookkeeper-web-app

Bookkeeper web application.

## Prerequisites

- npm
- Docker
- AWS CLI
- [Start local infrastructure & backend services](../infra/local/docker-compose.yml)
- Make scripts executable:

```shell 
chmod +x ./scripts/*.sh
```

## Local development
Install dependencies:
```shell
npm install
```
Start application
```shell
npm start
```

## Build docker image

[//]: # (TODO Johann)
```shell
./scripts/build-user-service-image.sh
```

## Push docker image to ECR

[//]: # (TODO Johann)
```shell
./scripts/push-user-service-image.sh
```
