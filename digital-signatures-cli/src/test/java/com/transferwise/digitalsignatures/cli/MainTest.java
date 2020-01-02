package com.transferwise.digitalsignatures.cli;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeThat;

public class MainTest {

    private static final String DATA_TO_SIGN = "65a31b86-aa2e-47fd-a7a4-3710437ba270";
    private static final String SIGNATURE = "oMbriRqpykbUnoL2sIX5xCO/yhrpZFd4TDu2lWdbcHkfxoYHQIvjdm/Px9SBgO5Lc58qjPkmeJA4z8B8spOVaxLRienkzvqrT0I11OFH7jJkoMu2g8bxPe7hmnRDdTB8cLZyFYGmlYjsr3vxemTUWSYYXdrys5Dh3LuOzWZmuYQ3bOwsBPm2sl7K39QM2KqXWckyqg9xpguWIGWzO86aKc/OboWqompVYKztLtdzMwAT5WQ5tPH+AA/lpiV3VG8J9TKTYpUzcrsRjUIelY+jznOkrFtqyyQsZ6l/G7yFXYTaA55ARc+k7CJExiw4mFX8wgPUHrGt289170HS+UJZDw==";

    private static final long OPENSSL_TEST_TIMEOUT_MILLISECONDS = 1000;

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Rule
    public final SystemOutRule systemOut = new SystemOutRule();

    @Test
    public void exitsWithErrorCode1OnIncompleteArguments() {
        exit.expectSystemExitWithStatus(1);
        Main.main(new String[0]);
    }

    @Test
    public void exitsWithErrorCode2OnInvalidPrivateKeyFilePath() {
        exit.expectSystemExitWithStatus(2);
        Main.main(new String[]{"-k", "not a path", "-d", "data to sign"});
    }

    @Test
    public void printsCorrectSignatureToSystemOut() {
        String testPrivateKeyFilePath = MainTest.class.getResource("/keys/private.pem").getPath();

        systemOut.enableLog();
        Main.main(new String[]{"-k", testPrivateKeyFilePath, "-d", DATA_TO_SIGN});
        assertEquals(SIGNATURE, systemOut.getLog());
    }

    @Test(timeout = OPENSSL_TEST_TIMEOUT_MILLISECONDS)
    public void signatureIsIdenticalToGeneratedByOpenSSL() throws InterruptedException {
        String testPrivateKeyFilePath = MainTest.class.getResource("/keys/private.pem").getPath();

        Process process;
        try {
            String command = String.format("printf '%s' | openssl sha1 -sign %s | base64 -b 0", DATA_TO_SIGN, testPrivateKeyFilePath);
            process = new ProcessBuilder("/bin/sh", "-c", command).start();
        } catch (Exception e) {
            assumeNoException(e);

            return;
        }

        boolean processExited = process.waitFor(OPENSSL_TEST_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
        assertTrue(processExited);

        int processExitCode = process.exitValue();
        assumeThat(processExitCode, is(0));

        String processOutput = readProcessOutput(process);
        assertEquals(SIGNATURE, processOutput);
    }

    private static String readProcessOutput(Process process) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader.lines().collect(Collectors.joining(System.getProperty("line.separator")));
    }
}
