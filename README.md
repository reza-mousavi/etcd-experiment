# Busybox etcd Client

A lightweight, containerized `etcd` client and utility image built on Alpine and Busybox. It packages the official `etcd`, `etcdctl`, and `etcdutl` binaries into a minimal footprint container for Kubernetes cluster administration, benchmarking, and troubleshooting.

## Features

* **Multi-stage build:** Uses Alpine for downloading and unpacking release binaries, resulting in a minimal final Busybox image.
* **Configurable ARGs:** Easily customize versions, base images, and download URLs at build time.
* **Pre-configured Environment:** Automatically sets `ETCDCTL_API=3` for v3 client operations.

## Build Arguments

| Build Argument | Default Value | Description |
| :--- | :--- | :--- |
| `ALPINE_VER` | `3.19` | Alpine version used for the temporary downloader stage |
| `BUSYBOX_VER` | `1.36` | Busybox version used for the final lightweight image |
| `ETCD_VER` | `v3.7.3` | Target etcd version to download |
| `DOWNLOAD_URL` | `https://storage.googleapis.com/etcd/...` | Direct tarball download URL |
| `ETCDCTL_API` | `3` | Default etcdctl API version environment variable |

## Building the Image

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
