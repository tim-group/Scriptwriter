package com.youdevise.test.scriptwriter;

import java.io.File;

import org.apache.commons.io.output.ByteArrayOutputStream;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

public class Configuration {
    @Argument(required = true, index = 0, usage = "The Java file to be converted")
    public File codeFile;

    @Option(name = "-o", usage = "Output directory")
    public File outputDir = new File("output");

    public String usage;

    private CmdLineParser cmdParser;

    public Configuration() {
        cmdParser = new CmdLineParser(this);
        // ADD USAGE
    }

    public void parse(String[] args) throws ConfigurationException {
        try {
            cmdParser.parseArgument(args);
        } catch (CmdLineException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }
}
