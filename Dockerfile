# Global build arguments
ARG ALPINE_VER=3.19
ARG BUSYBOX_VER=1.36
ARG ETCD_VER=v3.7.3
ARG ETCDCTL_API=3
ARG DOWNLOAD_URL=https://storage.googleapis.com/etcd/${ETCD_VER}/etcd-${ETCD_VER}-linux-amd64.tar.gz

# Stage 1: Download and extract etcd binaries
FROM alpine:${ALPINE_VER} AS builder

ARG DOWNLOAD_URL

RUN apk add --no-cache tar

RUN mkdir -p /tmp/etcd-download-test && \
    wget -O /tmp/etcd.tar.gz "$DOWNLOAD_URL" && \
    tar xzvf /tmp/etcd.tar.gz -C /tmp/etcd-download-test --strip-components=1 --no-same-owner && \
    rm -f /tmp/etcd.tar.gz

# Stage 2: Final lightweight image using Busybox
FROM busybox:${BUSYBOX_VER}

ARG ETCDCTL_API
ENV ETCDCTL_API=${ETCDCTL_API}

COPY --from=builder /tmp/etcd-download-test/etcd* /usr/local/bin/

CMD ["sh", "-c", "etcdctl version && exec sh"]
