package com.youdevise.test.scriptwriter;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

public class InterpreterTest {
    private static final String TRIVIAL_CLASS_CODE = "public class Translator { }";

    private Mockery context = new Mockery();    
    
    @Test public void 
    callsStartThenFinishOnListener() {
        final TokenListener listener = context.mock(TokenListener.class);
        final Sequence listenerCalls = context.sequence("listenerCalls");

        context.checking(new Expectations() {{
            oneOf (listener).start(); inSequence(listenerCalls);
            allowing (listener).giveClassName(with(any(String.class)));
            oneOf (listener).finish(); inSequence(listenerCalls);
        }});

        Interpreter interpreter = new Interpreter(listener);
        interpreter.interpret(TRIVIAL_CLASS_CODE);

        context.assertIsSatisfied();
    }

    public void
    givesClassNameToListener() { }

    public void
    throwsExceptionForInvalidCode() { }
}
