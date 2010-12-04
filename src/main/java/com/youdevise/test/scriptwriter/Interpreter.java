package com.youdevise.test.scriptwriter;

import java.io.InputStream;
import java.io.IOException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.io.IOUtils;

public class Interpreter {
    private static Pattern classDeclarationPattern = Pattern.compile("class\\s*(\\w*)");

    private TokenListener listener;
    
    public Interpreter(TokenListener listener) {
        this.listener = listener;
    }
        
    public void interpret(InputStream codeStream) throws InterpreterException {
        String className = parse(codeStream);

        listener.start();
        listener.className(className);
        listener.finish();
    }

    public String parse(InputStream codeStream) throws InterpreterException {
        String code;
        try {
            code = IOUtils.toString(codeStream);
        } catch (IOException e) {
            throw new InterpreterException("Error in reading code input stream", e);
        }

        java.util.regex.Matcher classDeclarationMatcher = classDeclarationPattern.matcher(code);
        if (false == classDeclarationMatcher.find()) { 
            throw new InterpreterException("Cannot find class declaration"); 
        }
        return classDeclarationMatcher.group(1); 
    }
}
