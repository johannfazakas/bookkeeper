# stuff-infra
Infra stuff for Stuff!

## Stuff App Infrastructure

### Create new App CloudFormation Stack
```shell
aws cloudformation create-stack \
  --stack-name stuff-app-stack \
  --template-body file://cloudformation/stuff-app-template.yml
```

### Update App Stack
```shell
aws cloudformation create-stack \
  --stack-name stuff-app-stack \
  --template-body file://cloudformation/stuff-app-template.yml
```

### Delete App Stack
```shell
aws cloudformation delete-stack --stack-name stuff-app-stack
```

## Stuff App CI/CD

### Create new CI/CD CloudFormation Stack
```shell
aws cloudformation create-stack \
  --stack-name stuff-ci-cd-stack \
  --template-body file://cloudformation/stuff-ci-cd-template.yml \
  --capabilities CAPABILITY_IAM
```

### Update CI/CD Stack
```shell
aws cloudformation update-stack \
  --stack-name stuff-ci-cd-stack \
  --template-body file://cloudformation/stuff-ci-cd-template.yml \
  --capabilities CAPABILITY_IAM
```

### Delete CI/CD Stack
```shell
aws cloudformation delete-stack --stack-name stuff-ci-cd-stack
```

## AWS EKS

### EKS Setup in CLI
#### Create EKS Cluster IAM Role and attach Policy
```shell
aws iam create-role \
  --role-name stuff-eks-role \
  --assume-role-policy-document file://"iam/policies/eks-trust-policy.json"
aws iam attach-role-policy \
  --policy-arn arn:aws:iam::aws:policy/AmazonEKSClusterPolicy \
  --role-name stuff-eks-role
aws iam get-role --role-name stuff-eks-role
```
#### Check default sSubnets in Region to be used
```shell
aws ec2 describe-vpcs --query="Vpcs[*].VpcId"
aws ec2 describe-subnets \
  --filters="Name=vpcId,Values=vpc-00364995c23a351e3" \
  --query="Subnets[*].SubnetId"
```
#### Create cluster
```shell
aws eks create-cluster \
  --name stuff-eks \
  --role-arn arn:aws:iam::668485322428:role/stuff-eks-role \
  --resources-vpc-config subnetIds=subnet-0da41047add6a233c,subnet-0b7ac52749f96f200,subnet-0d098f0409ea8b2df
aws eks describe-cluster --name stuff-eks
```
#### Setup local kubectl config
```shell
aws eks update-kubeconfig --name stuff-eks
```
#### Create EKS Node IAM Role and attach Policy
```shell
aws iam create-role \
  --role-name stuff-eks-node-role \
  --assume-role-policy-document file://"iam/policies/ec2-trust-policy.json"
aws iam attach-role-policy \
  --policy-arn arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy \
  --role-name stuff-eks-node-role
aws iam attach-role-policy \
  --policy-arn arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly \
  --role-name stuff-eks-node-role
aws iam attach-role-policy \
  --policy-arn arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy \
  --role-name stuff-eks-node-role
aws iam get-role --role-name stuff-eks-node-role
```
#### Create EKS Node Group
```shell
aws eks create-nodegroup \
  --cluster-name stuff-eks \
  --nodegroup-name stuff-eks-group \
  --subnets subnet-0da41047add6a233c subnet-0b7ac52749f96f200 subnet-0d098f0409ea8b2df \
  --node-role arn:aws:iam::668485322428:role/stuff-eks-node-role \
  --scaling-config minSize=2,maxSize=2,desiredSize=2 \
  --instance-types=t2.micro \
  --capacity-type SPOT
```

#### Check IAM OIDC identity provider and create it if not present
```shell
aws eks describe-cluster --name stuff-eks --query cluster.identity.oidc.issuer
aws iam list-open-id-connect-providers | grep https://aws eks describe-cluster --name stuff-eks --query cluster.identity.oidc.issuer
eksctl utils associate-iam-oidc-provider --cluster stuff-eks --approve
```
#### Install AWS Load Balancer Controller add-on in EKS cluster
This is currently failing! The purpose is to be able to create ALBs as Ingress Kubernetes services
```shell
eksctl utils associate-iam-oidc-provider --cluster stuff-eks --approve
aws iam create-policy \
  --policy-name stuff-eks-lb-controller-policy \
  --policy-document file://"iam/policies/eks-lb-controller-policy.json"
eksctl create iamserviceaccount \
  --cluster=stuff-eks \
  --namespace=kube-system \
  --name=aws-load-balancer-controller \
  --role-name stuff-eks-lb-controller-role \
  --attach-policy-arn=arn:aws:iam::668485322428:policy/stuff-eks-lb-controller-policy \
  --approve  
helm repo add eks https://aws.github.com/io/eks-charts
helm repo update
helm install aws-load-balancer-controller eks/aws-load-balancer-controller \
  -n kube-system \
  --set clusterName=stuff-eks \
  --set serviceAccount.create=false \
  --set serviceAccount.name=aws-load-balancer-controller \
  --set image.repository=602401143452.dkr.ecr.eu-central-1.amazonaws.com/amazon/aws-load-balancer-controller \
  --set region=eu-central-1 \
  --set vpcId=vpc-00364995c23a351e3
```

### Deploy Stuff
```shell
kubectl apply -f kubernetes/stuff-web-app
```
