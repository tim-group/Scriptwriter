package com.youdevise.test.scriptwriter;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

public class Scriptwriter {
    public static void main(String[] args) {
        config = new Configuration(args);

        JavaParser parser = new JavaParser();
        JavaClass clazz = parser.parse(config.codeFile);
        String className = clazz.name;
            
        config.outputDir.mkdir();
        File output = new File(config.outputDir, className + ".html");
        try {
            FileUtils.writeStringToFile(output, "<html><head><title>" + className + "</title></head></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Configuration {
        @Argument(required = true, index = 0, usage = "The Java file to be converted")
        public File codeFile;

        @Option(name = "-o", usage = "Output directory")
        public File outputDir = new File("output");

        public Configuration(String[] args) {
            CmdLineParser cmdParser = new CmdLineParser(this);
            try {
                cmdParser.parseArgument(args);
            } catch (CmdLineException e) { // FIXXXXXXXX
                System.err.println(e.getMessage());
                cmdParser.printUsage(System.err);
                return;
            }
        }
    }

    public static class Options {
    }

    public static class JavaParser {
        private static Pattern classDeclarationPattern = Pattern.compile("class\\s*(\\w*)");
        
        public JavaClass parse(File file) {
            String text;
            try {
                text = FileUtils.readFileToString(file);
            } catch (IOException e) {
                throw new IllegalStateException("cannot find file " + file.getAbsolutePath());
            }

            java.util.regex.Matcher classDeclarationMatcher = classDeclarationPattern.matcher(text);
            if (classDeclarationMatcher.find()) { 
                return new JavaClass(classDeclarationMatcher.group(1)); 
            } else { 
                throw new IllegalStateException("cannot parse"); 
            }
        }
    }

    public static class JavaClass {
        public String name;

        public JavaClass(String name) { this.name = name; }
    }
}
