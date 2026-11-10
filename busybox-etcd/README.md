# Busybox ETCD Docker

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
  -t rezamda/busybox-etcd:1.36-v3.7.3 .
```

Push the image to Docker Hub:

```bash
docker push rezamda/busybox-etcd:1.36-v3.7.3
```

---

## More Info

| Technology | Version / Tag | Purpose in Project |
| :--- | :--- | :--- |
| **Alpine Linux** | `3.19` | Lightweight builder stage used for downloading and extracting `etcd` release binaries securely. |
| **Busybox** | `1.36` | Minimal runtime base image providing essential UNIX utilities for the final lightweight container footprint. |
| **etcd / etcdctl / etcdutl** | `v3.7.3` | Distributed key-value store binaries packaged into the client image for administrative, health, and cluster operations. |
| **Docker** | Latest | Containerization platform used to perform multi-stage builds, tag images, and manage local registries. |
