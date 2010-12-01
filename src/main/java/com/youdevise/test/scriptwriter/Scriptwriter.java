package com.youdevise.test.scriptwriter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Scriptwriter {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        try {
            config.parse(args);
        } catch (ConfigurationException e) {
            System.err.print(config.usage);
            System.err.println(e.getMessage());
            return;
        }

        HTMLPrinter printer = new HTMLPrinter(new FileRecorder(config.outputDir));
        Interpreter interpreter = new Interpreter(printer);

        String code;
        try {
            code = FileUtils.readFileToString(config.codeFile);
        } catch (IOException e) {
            throw new IllegalStateException("cannot read from file " + config.codeFile.getAbsolutePath());
        }

        interpreter.interpret(code);
        printer.print();
    }
}
