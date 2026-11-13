# ETCD Experiment

Kubernetes relies heavily on etcd as a synchronization point for all of its operations. 

It is crucial to understand how Kubernetes utilizes etcd for its workloads. 

The aim behind this repo is to experiment with etcd to ensure a clear understanding of what it is and how it works.

---

## Repository Structure

```text
etcd-experiment/
├── Dockerfile
└── k8s/
    ├── ns.yml         # Creating namespace
    ├── cluster/       # An etcd cluster
    └── standalone/    # Standalone etcd node
```

Kubernetes resources are placed under the `k8s` folder within the repo to create and run etcd and a namespace called **ds**. 

There are two sets of resources created to run etcd either standalone or as a cluster.

---

## Step 1: Running ETCD

Create the dedicated `ds` namespace (`ns.yml`) for your etcd deployment and client pods:

```bash
kubectl apply -f k8s/ns.yml
```

In the `k8s` folder, there are two subfolders containing YAML files to run etcd either standalone or in a cluster.

### Running Standalone ETCD

After creating the namespace, you can run the following commands to create a pod and a service for standalone etcd:

```bash
kubectl apply -f k8s/standalone/
kubectl get po,svc -n ds -o wide --show-labels 
kubectl logs -n ds -l run=etcd --tail=50
```

### Running Clustered ETCD

If you would rather experiment with a clustered etcd setup, after creating the namespace, run the following commands to deploy the cluster architecture:

```bash
kubectl apply -f k8s/cluster/ -n ds
kubectl get po,svc -n ds -o wide --show-labels
kubectl logs -n ds etcd1 --tail=50
kubectl logs -n ds etcd2 --tail=50
kubectl logs -n ds etcd3 --tail=50
kubectl logs -n ds -l cluster=etcd --tail=50
```

### TLS Configuration

There are different ways to make cluster secure. However, I have create a guide to create necessary certfiicates required to make ETCD nodes secure. 

# TLS Certificate Generation & Management Guide for etcd

This repository contains step-by-step instructions and references for generating, verifying, and managing Transport Layer Security (TLS) certificates for an **etcd** cluster and its clients.


[TLS README](./tls/README.md)

---

## Step 2: Connecting to ETCD

To connect to etcd, you can either use a program written in any programming language or try the utilities provided by etcd itself. 

**etcdctl**, which is very powerful, is used in the context of this repo. 

There are multiple ways to get access to the utility, including using binaries provided by etcd.io. 

Running the utility on a Kubernetes platform, however, comes with challenges since Kubernetes only understands containers.

To overcome this limitation, a Docker image is created from **busybox** base image, busybox-etcd folder, creating a container equipped with etcd utilities. 

It packages the official `etcd`, `etcdctl`, and `etcdutl` binaries into a minimal footprint container for Kubernetes cluster administration, benchmarking, and troubleshooting.

[Busybox ETCD README](./busybox-etcd/README.md)
---


## Step 3: Connecting to ETCD

First, spin up the utility container directly inside Kubernetes:

```bash
kubectl run etcdctl --rm -it --image=rezamda/busybox-etcd:1.36-v3.7.3 --namespace=ds --restart=Never -- sh
```

### Connecting to the Standalone Node:

```bash
echo "=== 1. Obtain service address using nslookup ==="
nslookup etcd-headless.ds.svc.cluster.local

echo "=== 2. Ensure port 2379 is open using nc ==="
nc -zv etcd-headless.ds.svc.cluster.local 2379

echo "=== 3. Check endpoint health & member list ==="
ENDPOINTS="http://etcd-headless.ds.svc.cluster.local:2379"
etcdctl --endpoints=$ENDPOINTS endpoint health
etcdctl --endpoints=$ENDPOINTS member list

echo "=== 4. Put a key and read it ==="
etcdctl --endpoints=$ENDPOINTS put /cluster/status "active"
etcdctl --endpoints=$ENDPOINTS get /cluster/status

echo "=== 5. Update the key and read it again ==="
etcdctl --endpoints=$ENDPOINTS put /cluster/status "synced"
etcdctl --endpoints=$ENDPOINTS get /cluster/status

echo "=== 6. Create another key and get list of all keys with their values ==="
etcdctl --endpoints=$ENDPOINTS put /cluster/region "us-east"
etcdctl --endpoints=$ENDPOINTS get /cluster/ --prefix --write-out=fields
```

### Connecting to the Cluster:

```bash
echo "=== 1. Obtain service address using nslookup ==="
nslookup etcd1-headless.ds.svc.cluster.local
nslookup etcd2-headless.ds.svc.cluster.local
nslookup etcd3-headless.ds.svc.cluster.local

echo "=== 2. Ensure port 2379 is open using nc ==="
nc -zv etcd1-headless.ds.svc.cluster.local 2379
nc -zv etcd2-headless.ds.svc.cluster.local 2379
nc -zv etcd3-headless.ds.svc.cluster.local 2379

echo "=== 3. Check endpoint health & member list across all 3 nodes ==="
ENDPOINTS="http://etcd1-headless.ds.svc.cluster.local:2379,http://etcd2-headless.ds.svc.cluster.local:2379,http://etcd3-headless.ds.svc.cluster.local:2379"
etcdctl --endpoints=$ENDPOINTS endpoint health
etcdctl --endpoints=$ENDPOINTS member list

echo "=== 4. Put a key and read it ==="
etcdctl --endpoints=$ENDPOINTS put /cluster/status "active"
etcdctl --endpoints=$ENDPOINTS get /cluster/status

echo "=== 5. Update the key and read it again ==="
etcdctl --endpoints=$ENDPOINTS put /cluster/status "synced"
etcdctl --endpoints=$ENDPOINTS get /cluster/status

echo "=== 6. Create another key and get list of all keys with their values ==="
etcdctl --endpoints=$ENDPOINTS put /cluster/region "us-east"
etcdctl --endpoints=$ENDPOINTS get /cluster/--prefix
```
---

## More Info

| Technology | Version / Tag | Purpose in Project |
| :--- | :--- | :--- |
| **etcd / etcdctl / etcdutl** | `v3.7.3` | Distributed key-value store binaries packaged into the client image for administrative, health, and cluster operations. |
| **Docker** | Latest | Containerization platform used to perform multi-stage builds, tag images, and manage local registries. |
| **Kubernetes (`kubectl`)** | Latest | Orchestration platform used to deploy namespaces, standalone pods, and 3-node stateful cluster architectures. |
