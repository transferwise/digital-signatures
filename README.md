# Library for signing data with private key

Provides functionality for creating RSA digital signatures.

## Requirements

* Java &ge; 8

## Generating a RSA private/public key pair

To generate a RSA key pair and store it in PEM format you can use the OpenSSL cryptography and SSL/TLS toolkit:

1. Install OpenSSL following the instructions from [its official website](https://www.openssl.org/).
2. Generate private RSA key (key length &ge; 2048 is required for sufficient cryptographic complexity):
    ```bash
    $ openssl genrsa -out private.pem 2048
    ```
3. Generate public RSA key from private key:
    ```bash
    $ openssl rsa -pubout -in private.pem -out public.pem
    ```

## [Library](./digital-signatures)

Contains a single utility class 
[DigitalSignatures](./digital-signatures/src/main/java/com/transferwise/digitalsignatures/DigitalSignatures.java) 
with straightforward usage:
```java
byte[] signature = DigitalSignatures.sign(Path privateKeyFilePath, byte[] dataToSign);
```
There are also options to provide the private key as `String` or `Reader`.
The resulting signature byte array can be encoded to [Base64](https://en.wikipedia.org/wiki/Base64) in case it is 
going to be transferred over HTTP. For such cases there is a convenience method:
```java
String signatureBase64 = DigitalSignatires.encodeToBase64(byte[] bytes);
```

## [CLI tool](./digital-signatures-cli)

To allow users to sign their data via CLI there is an executable JAR:
```bash
usage: java -jar digital-signatures-cli-<version>-all.jar -d <DATA> -k <PATH>
Calculates SHA1 with RSA signature in Base64 encoding for provided data
 -d,--data-to-sign <DATA>       String containing data to sign
 -k,--private-key-file <PATH>   Path to file containing RSA private key
```

## Building

Run `./gradlew clean build`. 

The CLI tool executable JAR is assembled to an extra `*-all.jar` artifact of `digital-signatures-cli` module.
