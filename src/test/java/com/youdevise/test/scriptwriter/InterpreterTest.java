package com.youdevise.test.scriptwriter;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

public class InterpreterTest {
    private static final String TRIVIAL_CLASS_CODE = "public class Translator { }";
    private static final String CLASS_NAME_FROM_TRIVIAL_CLASS = "Translator";
    private static final String INTERFACE_CODE = "public interface Animal { }";
    private static final String INVALID_CODE = "public klass Invalid { }";

    private Mockery context = new Mockery();   
    
    @Test public void 
    callsStartThenFinishOnListener() throws Exception {
        final TokenListener listener = context.mock(TokenListener.class);
        final Sequence listenerCalls = context.sequence("listenerCalls");

        context.checking(new Expectations() {{
            oneOf (listener).start(); inSequence(listenerCalls);
            allowing (listener).giveClassName(with(any(String.class)));
            oneOf (listener).finish(); inSequence(listenerCalls);
        }});

        runAndVerifyInterpreter(listener, TRIVIAL_CLASS_CODE);
    }

    @Test
    public void
    givesClassNameToListener() throws Exception { 
        final TokenListener listener = context.mock(TokenListener.class);
        final Sequence listenerCalls = context.sequence("listenerCalls");

        context.checking(new Expectations() {{
            oneOf (listener).start(); inSequence(listenerCalls);
            oneOf (listener).giveClassName(CLASS_NAME_FROM_TRIVIAL_CLASS);
            oneOf (listener).finish(); inSequence(listenerCalls);
        }});

        runAndVerifyInterpreter(listener, TRIVIAL_CLASS_CODE);
    }

    private void runAndVerifyInterpreter(TokenListener listener, String code) throws Exception {
        Interpreter interpreter = new Interpreter(listener);
        interpreter.interpret(code);

        context.assertIsSatisfied();
    }

    @Test(expected=InterpreterException.class)
    public void
    throwsExceptionIfGivenInterface() throws Exception { 
        final TokenListener listener = context.mock(TokenListener.class);
        
        context.checking(new Expectations() {{
            oneOf (listener).start(); 
        }});

        runAndVerifyInterpreter(listener, INTERFACE_CODE);
    }

    @Test(expected=InterpreterException.class)
    public void
    throwsExceptionIfGivenInvalidCode() throws Exception { 
        final TokenListener listener = context.mock(TokenListener.class);
        
        context.checking(new Expectations() {{
            oneOf (listener).start(); 
        }});

        runAndVerifyInterpreter(listener, INVALID_CODE);
    }
}
