package com.transferwise.digitalsignatures.cli;

import com.transferwise.digitalsignatures.DigitalSignatures;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.nio.file.Path;
import java.nio.file.Paths;

class Main {

    private static final String CLI_UTILITY_NAME = "java -jar digital-signatures-cli-<version>-all.jar";
    private static final String CLI_HELP_HEADER = "Calculates SHA1 with RSA signature in Base64 encoding (RFC 4648) for provided data";

    public static void main(String[] args) {
        Option privateKeyFilePathOption = Option.builder("k")
            .longOpt("private-key-file")
            .desc("Path to file containing RSA private key")
            .hasArg(true)
            .argName("PATH")
            .required(true)
            .build();

        Option dataToSignOption = Option.builder("d")
            .longOpt("data-to-sign")
            .desc("String containing data to sign")
            .hasArg(true)
            .argName("DATA")
            .required(true)
            .build();

        Options options = new Options();
        options.addOption(privateKeyFilePathOption);
        options.addOption(dataToSignOption);

        CommandLineParser commandLineParser = new DefaultParser();

        CommandLine commandLine = null;

        try {
            commandLine = commandLineParser.parse(options, args, true);
        } catch (ParseException e) {
            logError(e.getMessage());
            new HelpFormatter().printHelp(CLI_UTILITY_NAME, CLI_HELP_HEADER, options, null, true);

            System.exit(1);
        }

        String privateKeyFilePathString = commandLine.getOptionValue(privateKeyFilePathOption.getOpt());

        byte[] dataToSign = commandLine.getOptionValue(dataToSignOption.getOpt()).getBytes();

        byte[] signature = null;

        try {
            Path privateKeyFilePath = Paths.get(privateKeyFilePathString);
            signature = DigitalSignatures.sign(privateKeyFilePath, dataToSign);
        } catch (Exception e) {
            logError("Failed to sign data. Cause: " + e.getMessage());

            System.exit(2);
        }

        String signatureBase64 = DigitalSignatures.encodeToBase64(signature);

        System.out.print(signatureBase64);
    }

    private static void logError(String errorText) {
        if (errorText != null && !errorText.isEmpty()) {
            System.out.println(errorText);
        }
    }
}
