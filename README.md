# Busybox etcd Client

A lightweight, containerized `etcd` client and utility image built on Alpine and Busybox. It packages the official `etcd`, `etcdctl`, and `etcdutl` binaries into a minimal footprint container for Kubernetes cluster administration, benchmarking, and troubleshooting.

## Repository Structure

```text
etcd-experiment/
├── Dockerfile
└── k8s/
    ├── ns.yml         # Creating namespace
    ├── cluster/       # an etcd cluster
    └── standalone/    # standalone etcd node

## Step 1: Running ETCD

Create the dedicated ds namespace (ns.yml) for your etcd deployment and client pods:

```bash
kubectl apply -f k8s/ns.yml
```

In the K8S repo there are two folders having yaml files to run ETCD, either standalone or in a cluster.

### Running Standalone ETCD

After creating the namespace, you can run the following command to create a pod and a headless service for ETCD.

```bash
kubectl apply -f k8s/standalone/
kubectl get po,svc -n ds -o wide --show-labels 
```


### Running Clustered ETCD

In case you would rather experimenting with a clustered ETCD, after creating the namespace, you can run the following command to create three pods with their own headless services.

```bash
kubectl apply -f k8s/cluster/ -n ds
kubectl get po,svc -n ds -o wide --show-labels
```


## Step 2: Connecting to ETCD

In order to connect to ETCD you can either connect to it using a program written on any programming languages or trying with some utilities provided by ETCD itself. I use **etcdctl** which is very powerful. There a re multiple way for getting access to the binary, among those using the binaries provided by etcd.io. Running the utility on K8S platform have some challenges though since kubernetes understands only containers.

To overcome this limitaiton a Docker images is created at the root if this project to build a client utility based on **busybox** image to create a busybox container equipped with etcd utilities.


#### Features

* **Multi-stage build:** Uses Alpine for downloading and unpacking release binaries, resulting in a minimal final Busybox image.
* **Configurable ARGs:** Easily customize versions, base images, and download URLs at build time.
* **Pre-configured Environment:** Automatically sets `ETCDCTL_API=3` for v3 client operations.

### Build and Push the Utility Image

The instruction below provided to create the utility for my **dockerhub** account, however, could be replaced with any other account.

### Build Arguments

Docker file provided on the rpeo support build arguments so that it is fairly easy to upgrade the utility without much change on the Dockerfileitself.

| Build Argument | Default Value | Description |
| :--- | :--- | :--- |
| `ALPINE_VER` | `3.19` | Alpine version used for the temporary downloader stage |
| `BUSYBOX_VER` | `1.36` | Busybox version used for the final lightweight image |
| `ETCD_VER` | `v3.7.3` | Target etcd version to download |
| `DOWNLOAD_URL` | `https://storage.googleapis.com/etcd/...` | Direct tarball download URL |
| `ETCDCTL_API` | `3` | Default etcdctl API version environment variable |

### Building the Image

Build the image locally and tag it for your user or registry. The following command builds the image for my DockerHub repo!

```bash
docker build -t rezamda/bysybox-etcd:latest .

```

Passing build arguments(Downloading etcd from Google APIs):

```bash
docker build \
  --build-arg ALPINE_VER=3.19 \
  --build-arg BUSYBOX_VER=1.36 \
  --build-arg ETCD_VER=v3.7.3 \
  --build-arg ETCDCTL_API=3 \
  --build-arg DOWNLOAD_URL=https://storage.googleapis.com/etcd/v3.7.3/etcd-v3.7.3-linux-amd64.tar.gz \
  -t rezamda/busybox-etcd:latest .
```

Don't forget to push the image Dockerhub:

```bash
docker push rezamda/busybox-etcd:1.36-v3.7.3
```

## Step 3: Connecting to ETCD

First, spin up the utility container

```bash
docker run --rm -it rezamda/busybox-etcd:1.36-v3.7.3
```

Connecting to the standalone node, no clustering:

```bash
echo "=== 1. Obtain service address using nslookup ==="
nslookup etcd-headless.ds.svc.cluster.local

echo "=== 2. Ensure port 2379 is open using nc ==="
nc -zv etcd-headless.ds.svc.cluster.local 2379

echo "=== 3. Ensure etcd is running & check container logs ==="
kubectl get pods -n ds -l run=etcd
kubectl logs -n ds -l run=etcd --tail=50

echo "=== 4. Check endpoint health & member list ==="
ENDPOINTS="http://etcd-headless.ds.svc.cluster.local:2379"
etcdctl --endpoints=$ENDPOINTS endpoint health
etcdctl --endpoints=$ENDPOINTS member list

echo "=== 5. Put a key and read it ==="
etcdctl --endpoints=$ENDPOINTS put /cluster/status "active"
etcdctl --endpoints=$ENDPOINTS get /cluster/status

echo "=== 6. Update the key and read it again ==="
etcdctl --endpoints=$ENDPOINTS put /cluster/status "synced"
etcdctl --endpoints=$ENDPOINTS get /cluster/status

echo "=== 7. Create another key and get list of all keys with their values ==="
etcdctl --endpoints=$ENDPOINTS put /cluster/region "us-east"
etcdctl --endpoints=$ENDPOINTS get /cluster/ --prefix --write-out=fields
```

In case you have created the cluster:

```bash
echo "=== 1. Obtain service address using nslookup ==="
nslookup etcd1-headless.ds.svc.cluster.local
nslookup etcd2-headless.ds.svc.cluster.local
nslookup etcd3-headless.ds.svc.cluster.local

echo "=== 2. Ensure port 2379 is open using nc ==="
nc -zv etcd1-headless.ds.svc.cluster.local 2379
nc -zv etcd2-headless.ds.svc.cluster.local 2379
nc -zv etcd3-headless.ds.svc.cluster.local 2379

echo "=== 3. Ensure cluster is running & check container logs ==="
kubectl get pods -n ds -l cluster=etcd
kubectl logs -n ds -l app=etcd1 --tail=50

echo "=== 4. Check endpoint health & member list across all 3 nodes ==="
ENDPOINTS="http://etcd1-headless.ds.svc.cluster.local:2379,http://etcd2-headless.ds.svc.cluster.local:2379,http://etcd3-headless.ds.svc.cluster.local:2379"
etcdctl --endpoints=$ENDPOINTS endpoint health
etcdctl --endpoints=$ENDPOINTS member list

echo "=== 5. Put a key and read it ==="
etcdctl --endpoints=$ENDPOINTS put /cluster/status "active"
etcdctl --endpoints=$ENDPOINTS get /cluster/status

echo "=== 6. Update the key and read it again ==="
etcdctl --endpoints=$ENDPOINTS put /cluster/status "synced"
etcdctl --endpoints=$ENDPOINTS get /cluster/status

echo "=== 7. Create another key and get list of all keys with their values ==="
etcdctl --endpoints=$ENDPOINTS put /cluster/region "us-east"
etcdctl --endpoints=$ENDPOINTS get /cluster/ --prefix
```

## More info

| Technology | Version / Tag | Purpose in Project |
| :--- | :--- | :--- |
| **Alpine Linux** | `3.19` | Lightweight builder stage used for downloading and extracting `etcd` release binaries securely. |
| **Busybox** | `1.36` | Minimal runtime base image providing essential UNIX utilities for the final lightweight container footprint. |
| **etcd / etcdctl / etcdutl** | `v3.7.3` | Distributed key-value store binaries packaged into the client image for administrative, health, and cluster operations. |
| **Docker** | Latest | Containerization platform used to perform multi-stage builds, tag images, and manage local registries. |
| **Kubernetes (`kubectl`)** | Latest | Orchestration platform used to deploy namespaces, standalone pods, and 3-node stateful cluster architectures. |
