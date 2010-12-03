package com.youdevise.test.scriptwriter;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Interpreter {
    private static Pattern classDeclarationPattern = Pattern.compile("class\\s*(\\w*)");

    private TokenListener listener;
    
    public Interpreter(TokenListener listener) {
        this.listener = listener;
    }
        
    public void interpret(String code) throws InterpreterException {
        listener.start();
        listener.giveClassName(extractClassName(code));
        listener.finish();
    }

    public String extractClassName(String code) throws InterpreterException {
        java.util.regex.Matcher classDeclarationMatcher = classDeclarationPattern.matcher(code);
        if (false == classDeclarationMatcher.find()) { 
            throw new InterpreterException("Cannot find class declaration"); 
        }
        return classDeclarationMatcher.group(1); 
    }
}
