# stuff-infra
Infra stuff for Stuff!

## Prerequisites
Make scripts executable:
```shell 
chmod +x scripts/*.sh
```

## Stuff Artifacts
### Create artifacts stack
```shell
./scripts/create-artifacts-stack.sh
```

### Update artifacts stack
```shell
./scripts/update-artifacts-stack.sh
```

### Delete artifacts stack
```shell
./scripts/delete-artifacts-stack.sh
```

## Stuff App Infrastructure

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

## Stuff Pipeline Trigger
After deploying the Pipeline Trigger stack, the Webhook inside the GitHub repository needs to be updated to point to the URL.

### Create new Pipeline Trigger CloudFormation Stack
```shell
./scripts/create-pipeline-trigger-stack.sh
```

### Update Pipeline Trigger Stack
```shell
./scripts/update-pipeline-trigger-stack.sh
```

### Delete Pipeline Trigger Stack
```shell
./scripts/delete-pipeline-trigger-stack.sh
```

## Stuff App CI/CD

### Create new CI/CD CloudFormation Stack
```shell
./scripts/create-ci-cd-stack.sh
```

### Update CI/CD Stack
```shell
./scripts/update-ci-cd-stack.sh
```

### Delete CI/CD Stack
```shell
./scripts/delete-ci-cd-stack.sh
```
