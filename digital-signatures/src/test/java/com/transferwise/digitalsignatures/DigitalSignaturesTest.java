package com.transferwise.digitalsignatures;

import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.Assert.assertEquals;

public class DigitalSignaturesTest {

    private static final String PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
        "MIIEpAIBAAKCAQEA3I1g8u8307u54gh0PVTudKujVFo90500qeJcnxEycWpaPUvl\n" +
        "l1HbvDJiZhkq0zKq6HN560z5r2L1baJOpiUcdKx/Vg3BR7cyCU52a4ezuSY7VmV6\n" +
        "KL56lNK4ieUBF6ABSW2Ivpl7fhHHi6xSMpPIzyzTMDZupCQrsmePqYev2VVx7Ajs\n" +
        "krHK3hT/+0X3AE1NZpaW2VuxHst3ApNHq0ld6oa0Z6q2Bo697X2OYPwbrm9srN+0\n" +
        "S/xwRzbqBxT851MVd2/x4GqzIRIZ1ThetqhtghdXsaPONoWnCzYSgCAXvuXvfbda\n" +
        "NAPyNvABViTghyU843aR8Uvw2ItCIOlp3mLN8QIDAQABAoIBAQCZGRbkbERPmS+m\n" +
        "hQHTlUJWANNG+cGTRLxK9VQgIzrl2dK8XBQK34rt7/e4Md41byWOaKKIQQ3Nvp7p\n" +
        "tNJtqLNBFoDqBnBVzQhRx4KSkEekzbJA/f43jEnhRwlMx4fjk3FxPDTBQh+kWsku\n" +
        "3rbMXyP1FIOhIxfYnzcqB5OFNihObyTYLFMLXKYszd7BX+CPRzu7HyTZdwa815da\n" +
        "eoXIAnJKIW2TgiD+FyF88GVO6PWbvA263selqN68xfBjmgPVs8ceGLD4J7Wqu9Z/\n" +
        "ZTMLZKKZcpe7aF89Mme9sOyzEfNJCY1fGCSY+yQRTsQBGnfVaRJW9Z2WfLd1LD3u\n" +
        "b98MvlQBAoGBAO9kz4fsbE0r+Fy3q3xSVo2cRdxpmZjZhtYQDo3i5ExlvH41LpqX\n" +
        "ViR6n0E+tGQN8oRDu+f7CJ2Zs4duNFhr2INljre09+qarXU8VRjYDb/x7Zy3OJKy\n" +
        "vKZJgvx1+NeQt+B32qJzS1It2GFCjRvXx4rRXW4Jx3VzTIKy3s3XtXbBAoGBAOvZ\n" +
        "+jh15sPAmJu6UshuJBC2sDBc+aPgo9LX+ykndFg+hi4WtdzBzNWjI7TO9jQtIxVP\n" +
        "lWugY6aeVqALKKnAQ0F+fgHjDkVC8TRX/wSz6YKQn5SlO68t8wK5wutACq9RhRfH\n" +
        "50PUu64yXnhx0u2Fx/7cHckHjTogMefkOdBLRtMxAoGAErS85rEZsVoLOSt88eT5\n" +
        "MG2So+t4fhIZUCbHDF07W6DjfrUnJBtJNuaCBTYiIGNanO0yBKl//dihx6Zb3sDm\n" +
        "lTXdVguFB8b4YN3LBHr1cBc2avWCLSxcQ14hJxsMy8NaKucSpXj+3LgKXWc24YMV\n" +
        "64n6k/udo1bUFq5lbI47dsECgYEAgjazrm5xvMvdtcTWJbChmtSyS9FZRsAk0qjK\n" +
        "EzukQYArptCFEd+xzpWmhhHp3n65Ku/oaCaCPiCXZP8kMSxkNYm32iTY4SaHc0XO\n" +
        "F3OZTau5X2EmpZ4x1+RlmGqgO5E/cRS+OzX9dLx8afU15kuBUtWGYFIaB+h0hTn9\n" +
        "LWISNVECgYB+8eV5moyvrpYOogVIx5ncbrYHDSA7vlIa7A3Q3gzjWBOpFSQwzlF5\n" +
        "hYtTsUm4ZCVtBZVs51dcTKT35/j/fJNweQrSLsb8Xk5Mi1BKKcHsal5Gr0i6DChQ\n" +
        "CWwx3RnrXjutobtUteYawvdsnLlWQlfxKWRY9hFi4AIKDgc1EXnsiw==\n" +
        "-----END RSA PRIVATE KEY-----\n";

    @Test
    public void sign() throws IOException, GeneralSecurityException {
        String dataToSign = "65a31b86-aa2e-47fd-a7a4-3710437ba270";
        String expectedSignatureBase64 = "1JnHvXd24R99jZFl5KzJer1iMFGIdrGRmu09h7QkGzo5kgk3cLHdDesitNjK131lmpgAEwnI99j" +
                "tyfJfiMjFZV4VqSAmr68W12r3Jc4ACE17WNa7hGgLC7Gw+m70x9UX5dgv6ws02VlIe9i44iGJ6fN57Piy5LBitxWkAjEEMNjmqO6" +
                "GdnBlxNuSc9m+eImG91nqXa6BLNFFAPD3FzaEbqW8Ob/l8ayd9xXosTNMz0ywsV/l/zthra/7olAvRLqCrMtzI9ltC7kd40xWNes" +
                "ehLxfQIIoAUiDF9iRCzBavXR6O7jUf56QES6ScjQ43a62V0JIdbUDSdRJPr+zesPQug==";

        byte[] signature = DigitalSignatures.sign(PRIVATE_KEY, dataToSign.getBytes());

        String signatureBase64 = DigitalSignatures.encodeToBase64(signature);

        assertEquals(expectedSignatureBase64, signatureBase64);
    }
}
