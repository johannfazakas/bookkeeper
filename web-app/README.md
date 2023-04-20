# stuff-web-app
Web App for Stuff. To manage your stuff! You name it.

## Build & Run

### Local development
```shell
./gradlew clean run --continuous
```

### Build docker image
- [ ] have Dockerfile handle webpack distribution creation
```shell
./gradlew clean browserProductionWebpack
docker image build -t stuff-web-app:latest .
```

### Push docker image to ECR
```shell
docker tag stuff-web-app:latest 668485322428.dkr.ecr.eu-central-1.amazonaws.com/stuff-web-app:latest
docker image push 668485322428.dkr.ecr.eu-central-1.amazonaws.com/stuff-web-app:latest
```

### Run docker container on EC2 instance
Start EC2 instance.

EC2 instance should have IAM Role with AmazonEC2ContainerRegistryReadOnly Policy.

EC2 instance Security Groups should allow HTTP access on port 80.

Add User Data to start web-app:
```
#! /bin/sh
yum update -y
amazon-linux-extras install docker
service docker start
usermod -a -G docker ec2-user
chkconfig docker on
$(aws ecr get-login --no-include-email --region eu-central-1)
docker pull 668485322428.dkr.ecr.eu-central-1.amazonaws.com/stuff-web-app:latest
docker run -d -p 80:80 668485322428.dkr.ecr.eu-central-1.amazonaws.com/stuff-web-app:latest
```