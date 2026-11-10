# TLS Certificate Generation

This repository contains step-by-step instructions and references for generating, verifying, and managing Transport Layer Security (TLS) certificates for any given technology.

---

## Overview of Certificates

To implement Mutual TLS (mTLS), three primary sets of files are required:

| Certificate File | Type | Description | Intended Recipient |
| :--- | :--- | :--- | :--- |
| `ca.crt` | Certificate Authority | Public root certificate used to verify server and client authenticity. | etcd Server & Clients |
| `ca.key` | CA Private Key | Secret key used strictly to sign certificate requests. | Certificate Administrator |
| `server.crt` / `server.key` | Server Keypair | Public cert and private key pair for the etcd server nodes. | etcd Server |
| `client.crt` / `client.key` | Client Keypair | Public cert and private key pair for clients accessing etcd (`etcdctl`, apps). | etcd Clients / Tools |

---

## Certificate Generation

### Prerequisites
Ensure OpenSSL is installed on your local machine:
```bash
openssl version
```

---

### Generate the Certificate Authority (CA)

1. **Generate the CA private key:**
   ```bash
   openssl genrsa -out ca.key 4096
   ```

2. **Generate the self-signed CA certificate (valid for 365 days):**
   ```bash
   openssl req -x509 -new -nodes -key ca.key -sha256 -days 365 -out ca.crt \
       -subj "/C=US/ST=State/L=City/O=DevOps/CN=reza.mousavi@somewhere.com"
   ```

---

### Generate Server Certificates (with Subject Alternative Names)

Normally it is required to have **Subject Alternative Names (SANs)** to match the IP addresses or domain names of the nodes.

1. **Generate the server private key:**
   ```bash
   openssl genrsa -out server.key 2048
   ```

2. **Create the SAN configuration file (`server.ext`):**
   ```ini
   authorityKeyIdentifier=keyid,issuer
   basicConstraints=CA:FALSE
   keyUsage = digitalSignature, keyEncipherment
   extendedKeyUsage = serverAuth, clientAuth
   subjectAltName = @alt_names

   [alt_names]
   DNS.1 = localhost
   IP.1 = 127.0.0.1
   ```

3. **Generate the Certificate Signing Request (CSR):**
   ```bash
   openssl req -new -key server.key -out server.csr \
       -subj "/C=US/ST=State/L=City/O=DevOps/CN=some-server"
   ```

4. **Sign the server certificate with your CA:**
   ```bash
   openssl x509 -req -in server.csr -CA ca.crt -CAkey ca.key -CAcreateserial \
       -out server.crt -days 365 -sha256 -extfile server.ext
   ```

---

### Generate Client Certificates

1. **Generate the client private key:**
   ```bash
   openssl genrsa -out client.key 2048
   ```

2. **Generate the client CSR:**
   ```bash
   openssl req -new -key client.key -out client.csr \
       -subj "/C=US/ST=State/L=City/O=DevOps/CN=some-client"
   ```

3. **Sign the client certificate with your CA:**
   ```bash
   openssl x509 -req -in client.csr -CA ca.crt -CAkey ca.key -CAcreateserial \
       -out client.crt -days 365 -sha256
   ```

---

## Verification & Inspection Commands

### Verify Certificate Trust Chain
Ensure your server certificate is valid against your root CA:
```bash
openssl verify -CAfile ca.crt server.crt
```
*Expected Output:* `server.crt: OK`

### Inspect Certificate Details & Expiration
Check dates, issuers, and SAN extensions:
```bash
openssl x509 -in server.crt -text -noout
```

### Check Expiration Date Directly
```bash
openssl x509 -enddate -noout -in server.crt
```

---

