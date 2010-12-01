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

    public static class Interpreter {
        private static Pattern classDeclarationPattern = Pattern.compile("class\\s*(\\w*)");

        private TokenListener listener;
    
        public Interpreter(TokenListener listener) {
            this.listener = listener;
        }
        
        public void interpret(String code) {
            listener.start();
            listener.giveClassName(extractClassName(code));
            listener.finish();
        }

        public String extractClassName(String code) {
            java.util.regex.Matcher classDeclarationMatcher = classDeclarationPattern.matcher(code);
            if (false == classDeclarationMatcher.find()) { 
                throw new IllegalStateException("cannot find class declaration"); 
            }
            return classDeclarationMatcher.group(1); 
        }
    }

    public static interface TokenListener {
        public void start();
        public void giveClassName(String className);
        public void finish();
    }

    public static class HTMLPrinter implements TokenListener {
        private Recorder recorder;
        private String className;
        private String html;

        public HTMLPrinter(Recorder recorder) { 
            this.recorder = recorder;
            html = "";
        }

        @Override public void start() { html = "<html>"; }
        @Override public void giveClassName(String className) { 
            this.className = className; 
            html += "<head><title>" + className + "</title></head>";
        }
        @Override public void finish() { html += "</html>"; }

        public void print() {
            recorder.write(className, "html", html);
        }
    }

    public static interface Recorder {
        public void write(String title, String type, String contents);
    }

    public static class FileRecorder implements Recorder {
        private File outputDir;

        public FileRecorder(File outputDir) { this.outputDir = outputDir; }
        
        @Override public void write(String title, String type, String contents) {
            outputDir.mkdir();
            File outputFile = new File(outputDir, title + "." + type);

            try {
                FileUtils.writeStringToFile(outputFile, contents);
            } catch (IOException e) {
                throw new IllegalStateException("cannot write to file " + outputFile.getAbsolutePath());
            }
        }
    }
}
