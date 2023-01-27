# stuff-infra
Infra stuff for Stuff!

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
  --instance-types=t3.medium \
  --capacity-type SPOT
```

### EKS Setup in EC2 Management Console (why not CLI?)
Create cluster in EKS

Create Node Group with fixed number of EC2 instances (yes, could be better)

### Deploy Stuff
```shell
kubectl apply -f kubernetes/stuff-web-app
```
