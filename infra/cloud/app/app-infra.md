# Bookkeeper Application Infrastructure

Cloud resources required for Bookkeeper app to run.

## Prerequisites

- artifacts stack deployed
- images present in ECR repositories

## Components

### ECS Cluster

Elastic Container Service cluster with Fargate launch type to host the app containers.

## Deployment

### Prerequisites

- AWS CLI
- [Deploy app stack](/app-infra.md#deployment)
- [Push pipeline-trigger image to ECR](lambdas/pipeline-trigger/pipeline-trigger.md#build-and-push-image-to-ecr)
- [Push web-app image to ECR](../../web-app/README.md#build-and-push-image-to-ecr)
- [Review script variables](scripts/load-variables.sh)
- Make scripts executable:

```shell 
chmod +x ./scripts/*.sh
```

### Create app stack

```shell
./scripts/create-app-stack.sh
```

### Update app stack

```shell
./scripts/update-app-stack.sh
```

### Delete app stack

```shell
./scripts/delete-app-stack.sh
```
