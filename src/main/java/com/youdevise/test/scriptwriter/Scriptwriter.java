package com.youdevise.test.scriptwriter;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

        Interpreter interpreter = new Interpreter(config.outputDir);

        String code;
        try {
            code = FileUtils.readFileToString(config.codeFile);
        } catch (IOException e) {
            throw new IllegalStateException("cannot read from file " + config.codeFile.getAbsolutePath());
        }
        interpreter.interpret(code);
    }

    public static class Interpreter {
        private static Pattern classDeclarationPattern = Pattern.compile("class\\s*(\\w*)");
    
        private File outputDir;

        public Interpreter(File outputDir) { this.outputDir = outputDir; }
        
        public void interpret(String code) {
            java.util.regex.Matcher classDeclarationMatcher = classDeclarationPattern.matcher(code);
            if (false == classDeclarationMatcher.find()) { 
                throw new IllegalStateException("cannot find class declaration"); 
            }
            String className = classDeclarationMatcher.group(1); 

            outputDir.mkdir();
            File outputFile = new File(outputDir, className + ".html");

            try {
                FileUtils.writeStringToFile(outputFile, "<html><head><title>" + className + "</title></head></html>");
            } catch (IOException e) {
                throw new IllegalStateException("cannot write to file " + outputFile.getAbsolutePath());
            }
        }
    }
}
