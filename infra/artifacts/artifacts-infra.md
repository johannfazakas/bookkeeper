# Bookkeeper Artifacts Infrastructure

Resources keeping artifacts with a separate lifecycle than the app itself.
Even if the main app is deleted due to cost concerns, the artifacts should be kept for future use.

## Components

### ECR Repositories

Docker Images kept using AWS ECR Repositories:

- bookkeeper-pipeline-trigger
- bookkeeper-web-app

### S3 Bucket

Bucket used by app infra to store ci/cd artifacts and cache files.

## Deployment

### Prerequisites

- AWS CLI
- Make scripts executable:

```shell 
chmod +x ./scripts/*.sh
```

### Create artifacts stack

```shell
./scripts/create-artifacts-stack.sh
```

### Update artifacts stack

```shell
./scripts/update-artifacts-stack.sh
```

### Delete artifacts stack

Stack deletion will work only if the bucket and the image repositories are empty.  
Otherwise, the resources should be manually deleted or emptied first.

```shell
./scripts/delete-artifacts-stack.sh
```
