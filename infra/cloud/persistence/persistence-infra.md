# Bookkeeper Persistence Infrastructure

Resources keeping persistence resources with a separate lifecycle than the app itself.
Even if the main app is deleted due to cost concerns, the databases could be kept for future use.

## Components

### RDS Database

PostgreSQL database used by the bookkeeper app

## Deployment

### Prerequisites

- AWS CLI
- Make scripts executable:

```shell
chmod +x ./scripts/*.sh
```

### Create persistence stack

```shell
./scripts/create-persistence-stack.sh
```

### Update persistence stack

```shell
./scripts/update-persistence-stack.sh
```

### Delete persistence stack

```shell
./scripts/delete-persistence-stack.sh
```
