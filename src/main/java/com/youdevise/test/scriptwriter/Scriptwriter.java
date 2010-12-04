package com.youdevise.test.scriptwriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Scriptwriter {
    public static void main(String[] args) {
        Configuration config = new Configuration();

        try {
            config.parse(args);

            HTMLPrinter printer = new HTMLPrinter(new FileRecorder(config.outputDir));
            Interpreter interpreter = new Interpreter(printer);

            FileInputStream codeStream = new FileInputStream(config.codeFile);
            interpreter.interpret(codeStream);

            printer.print();
        } catch (IOException e) {
            System.err.print("Error in reading code file");
            System.err.println(e.getMessage());
        } catch (ConfigurationException e) {
            System.err.print(config.usage);
            System.err.println(e.getMessage());
        } catch (InterpreterException e) {
            System.err.println("Error in interpreting code");
            System.err.println(e.getMessage());
        }

    }
}
