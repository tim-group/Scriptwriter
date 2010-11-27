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
        File outputDir = new File(args[1]);
        outputDir.mkdir();

        File codeFile = new File(args[2]);
        JavaClass clazz = new JavaClass(codeFile);
        String className = clazz.getName();
            
        File output = new File(outputDir, className + ".html");
        try {
            FileUtils.writeStringToFile(output, "<html><head><title>" + className + "</title></head></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class JavaClass {
        private static Pattern classDeclarationPattern = Pattern.compile("class\\s*(\\w*)");

        private String name;

        public JavaClass(File file) {
            String text;
            try {
                text = FileUtils.readFileToString(file);
            } catch (IOException e) {
                throw new IllegalStateException("cannot find file " + file.getAbsolutePath());
            }

            java.util.regex.Matcher classDeclarationMatcher = classDeclarationPattern.matcher(text);
            if (classDeclarationMatcher.find()) { 
                name = classDeclarationMatcher.group(1); 
            } else { 
                throw new IllegalStateException("cannot parse"); 
            }
        }

        public String getName() { return name; }
    }
}
