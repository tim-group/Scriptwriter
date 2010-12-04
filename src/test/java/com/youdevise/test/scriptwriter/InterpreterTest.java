package com.youdevise.test.scriptwriter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

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

    @Test
    public void
    givesClassNameToListener() throws Exception { 
        final TokenListener listener = context.mock(TokenListener.class);
        final Sequence listenerCalls = context.sequence("listenerCalls");

        context.checking(new Expectations() {{
            oneOf (listener).className(CLASS_NAME_FROM_TRIVIAL_CLASS);
        }});

        runAndVerifyInterpreter(listener, TRIVIAL_CLASS_CODE);
    }

    @Test(expected=InterpreterException.class)
    public void
    throwsExceptionIfGivenInterface() throws Exception { 
        final TokenListener listener = context.mock(TokenListener.class);
        runAndVerifyInterpreter(listener, INTERFACE_CODE);
    }

    @Test(expected=InterpreterException.class)
    public void
    throwsExceptionIfGivenInvalidCode() throws Exception { 
        final TokenListener listener = context.mock(TokenListener.class);
        runAndVerifyInterpreter(listener, INVALID_CODE);
    }

    @Test(expected=InterpreterException.class)
    public void
    throwsExceptionIfGivenUnreadableStream() throws Exception {
        final TokenListener listener = context.mock(TokenListener.class);
        InputStream errorStream = new InputStream() {
            public int read() throws IOException {
                throw new IOException("test error");
            }
        };

        Interpreter interpreter = new Interpreter(listener);
        interpreter.interpret(errorStream);
    }

    private void runAndVerifyInterpreter(TokenListener listener, String code) throws Exception {
        InputStream codeStream = new ByteArrayInputStream(code.getBytes());
        Interpreter interpreter = new Interpreter(listener);
        interpreter.interpret(codeStream);

        context.assertIsSatisfied();
    }
}

