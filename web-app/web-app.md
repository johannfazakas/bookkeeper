# stuff-web-app
Web App for Stuff. To manage your stuff! You name it.

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
- [ ] have Dockerfile handle webpack distribution creation
```shell
./scripts/build-web-app-image.sh
```

## Push docker image to ECR
```shell
./scripts/push-web-app-image.sh
```
