package com.youdevise.test.scriptwriter;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.Matchers.hasXPath;

public class HTMLBuilderTest {
    private static final String CLASS_NAME = "Order";

    private Mockery context = new Mockery();   

    @Test public void 
    usesClassNameAsTitle() throws Exception {
        final Recorder recorder = context.mock(Recorder.class);

        context.checking(new Expectations() {{
            oneOf (recorder).write(with(equal(CLASS_NAME)), with(equal("html")), with(hasXPath("/html/head/title", equal(CLASS_NAME)))); 
        }});

        HTMLBuilder builder = new HTMLBuilder(recorder);
        builder.className(CLASS_NAME);
        builder.output();

        context.assertIsSatisfied();
    }

    @Test
    public void
    providesXMLNamespace() throws Exception {
        final Recorder recorder = context.mock(Recorder.class);

        context.checking(new Expectations() {{
            oneOf (recorder).write(with(equal(CLASS_NAME)), with(equal("html")), 
                             with(hasXPath("/*/namespace::*[name()='']", equal("http://www.w3.org/1999/xhtml")))); 
        }});

        HTMLBuilder builder = new HTMLBuilder(recorder);
        builder.className(CLASS_NAME);
        builder.output();

        context.assertIsSatisfied();
    }
}

