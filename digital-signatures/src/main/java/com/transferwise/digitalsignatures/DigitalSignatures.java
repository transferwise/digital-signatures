package com.transferwise.digitalsignatures;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

/**
 * Utility methods to sign data with private key.
 */
public class DigitalSignatures {

    /**
     * Default signature algorithm.
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Signs data with provided private key reader.
     *
     * @param privateKeyReader private key reader
     * @param dataToSign       data to sign
     * @return signature for provided data
     * @throws IOException         in case of error reading private key
     * @throws InvalidKeyException in case of provided key being invalid
     */
    public static byte[] sign(Reader privateKeyReader, byte[] dataToSign) throws IOException, InvalidKeyException {
        PrivateKey key = privateKeyFromReader(privateKeyReader);

        try {
            Signature signer = Signature.getInstance(SIGNATURE_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            signer.initSign(key);
            signer.update(dataToSign);
            return signer.sign();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Signs data with provided private key.
     *
     * @param privateKeyFileContents private key file contents
     * @param dataToSign             data to sign
     * @return signature for provided data
     * @throws IOException         in case of error reading private key
     * @throws InvalidKeyException in case of provided key being invalid
     */
    public static byte[] sign(String privateKeyFileContents, byte[] dataToSign) throws IOException, InvalidKeyException {
        return sign(new StringReader(privateKeyFileContents), dataToSign);
    }

    /**
     * Signs data with provided private key.
     *
     * @param privateKeyFilePath path to private key file
     * @param dataToSign         data to sign
     * @return signature for provided data
     * @throws IOException         in case of error reading private key
     * @throws InvalidKeyException in case of provided key being invalid
     */
    public static byte[] sign(Path privateKeyFilePath, byte[] dataToSign) throws IOException, InvalidKeyException {
        return sign(new FileReader(privateKeyFilePath.toFile()), dataToSign);
    }

    /**
     * Encodes byte array with Base64 (RFC 4648).
     *
     * @param bytes byte array
     * @return array in Base64 encoding
     */
    public static String encodeToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static PrivateKey privateKeyFromReader(Reader keyReader) throws IOException, InvalidKeyException {
        try (PEMParser pemParser = new PEMParser(keyReader)) {
            Object object = pemParser.readObject();

            if (!(object instanceof PEMKeyPair)) {
                throw new InvalidKeyException("Provided key is not a private key in PEM format");
            }

            PrivateKeyInfo privateKeyInfo = ((PEMKeyPair) object).getPrivateKeyInfo();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);

            try {
                return converter.getPrivateKey(privateKeyInfo);
            } catch (PEMException e) {
                throw new InvalidKeyException(e);
            }
        }
    }
}
