package com.youdevise.test.scriptwriter;

import java.io.File;

import org.apache.commons.io.output.ByteArrayOutputStream;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

public class Configuration {
    @Argument(required = true, index = 0, usage = "The Java file to be converted - required")
    public File codeFile;

    @Option(name = "-o", usage = "The output directory - optional, defaults to 'output'")
    public File outputDir = new File("output");

    public String usage;

    private CmdLineParser cmdParser;

    public Configuration() {
        cmdParser = new CmdLineParser(this);
        ByteArrayOutputStream usageStream = new ByteArrayOutputStream();
        cmdParser.printUsage(usageStream);
        usage = usageStream.toString();
    }

    public void parse(String[] args) throws ConfigurationException {
        try {
            cmdParser.parseArgument(args);
        } catch (CmdLineException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }
}
