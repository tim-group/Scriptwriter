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
        JavaParser parser = new JavaParser();
        JavaClass clazz = parser.parse(codeFile);
        String className = clazz.name;
            
        File output = new File(outputDir, className + ".html");
        try {
            FileUtils.writeStringToFile(output, "<html><head><title>" + className + "</title></head></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
