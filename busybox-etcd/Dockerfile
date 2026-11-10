# Global build arguments
ARG ALPINE_VER=3.19
ARG BUSYBOX_VER=1.36
ARG ETCD_VER=v3.7.3
ARG ETCDCTL_API=3

# Stage 1: Download and extract etcd binaries
FROM alpine:${ALPINE_VER} AS builder

ARG ETCD_VER

ENV GOOGLE_URL=https://storage.googleapis.com/etcd
ENV GITHUB_URL=https://github.com/etcd-io/etcd/releases/download
ENV PACKAGE_URL=${GOOGLE_URL}
ENV DOWNLOAD_URL=${PACKAGE_URL}/${ETCD_VER}/etcd-${ETCD_VER}-linux-amd64.tar.gz

RUN apk add --no-cache tar

RUN mkdir -p /tmp/etcd-download-test && \
    wget -O /tmp/etcd.tar.gz "${DOWNLOAD_URL}" && \
    tar xzvf /tmp/etcd.tar.gz -C /tmp/etcd-download-test --strip-components=1 --no-same-owner && \
    rm -f /tmp/etcd.tar.gz

# Stage 2: Final lightweight image using Busybox
FROM busybox:${BUSYBOX_VER}

ARG ETCDCTL_API
ENV ETCDCTL_API=${ETCDCTL_API}

COPY --from=builder /tmp/etcd-download-test/etcd* /usr/local/bin/

CMD ["/bin/sh", "-c", "echo \"etcdctl version: $(etcdctl version)\" && exec sh"]
