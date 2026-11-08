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

---

## Step 2: Connecting to ETCD

To connect to etcd, you can either use a program written in any programming language or try the utilities provided by etcd itself. 

**etcdctl**, which is very powerful, is used in the context of this repo. 

There are multiple ways to get access to the utility, including using binaries provided by etcd.io. 

Running the utility on a Kubernetes platform, however, comes with challenges since Kubernetes only understands containers.

To overcome this limitation, a Docker image is created at the root of this project using a **busybox** base image, creating a container equipped with etcd utilities. 

It packages the official `etcd`, `etcdctl`, and `etcdutl` binaries into a minimal footprint container for Kubernetes cluster administration, benchmarking, and troubleshooting.

### Features

* **Multi-stage build:** Uses Alpine for downloading and unpacking release binaries, resulting in a minimal final Busybox image.
* **Configurable ARGs:** Easily customize versions, base images, and download URLs at build time.
* **Pre-configured Environment:** Automatically sets `ETCDCTL_API=3` for v3 client operations.

### Build and Push the Utility Image

The instructions below are provided to create the utility for a **Docker Hub** account, but they can be adapted for any other container registry.

### Build Arguments

The Dockerfile provided in the repo supports build arguments so that it is fairly easy to upgrade the utility without modifying the Dockerfile itself.

| Build Argument | Default Value | Description |
| :--- | :--- | :--- |
| `ALPINE_VER` | `3.19` | Alpine version used for the temporary downloader stage |
| `BUSYBOX_VER` | `1.36` | Busybox version used for the final lightweight image |
| `ETCD_VER` | `v3.7.3` | Target etcd version to download |
| `DOWNLOAD_URL` | `https://storage.googleapis.com/...` | Direct tarball download URL |
| `ETCDCTL_API` | `3` | Default etcdctl API version environment variable |

### Building the Image

Build the image locally and tag it for your user or registry:

```bash
docker build -t rezamda/busybox-etcd:1.36-v3.7.3 .
```

Or pass explicit build arguments to download etcd directly from Google APIs:

```bash
docker build \
  --build-arg ALPINE_VER=3.19 \
  --build-arg BUSYBOX_VER=1.36 \
  --build-arg ETCD_VER=v3.7.3 \
  --build-arg ETCDCTL_API=3 \
  --build-arg DOWNLOAD_URL=https://storage.googleapis.com/etcd/v3.7.3/etcd-v3.7.3-linux-amd64.tar.gz \
  -t rezamda/busybox-etcd:1.36-v3.7.3 .
```

Push the image to Docker Hub:

```bash
docker push rezamda/busybox-etcd:1.36-v3.7.3
```

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
| **Alpine Linux** | `3.19` | Lightweight builder stage used for downloading and extracting `etcd` release binaries securely. |
| **Busybox** | `1.36` | Minimal runtime base image providing essential UNIX utilities for the final lightweight container footprint. |
| **etcd / etcdctl / etcdutl** | `v3.7.3` | Distributed key-value store binaries packaged into the client image for administrative, health, and cluster operations. |
| **Docker** | Latest | Containerization platform used to perform multi-stage builds, tag images, and manage local registries. |
| **Kubernetes (`kubectl`)** | Latest | Orchestration platform used to deploy namespaces, standalone pods, and 3-node stateful cluster architectures. |