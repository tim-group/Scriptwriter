package com.youdevise.test.scriptwriter;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.Matchers.hasXPath;

public class DocumentConstructorTest {
    private static final String CLASS_NAME = "Order";

    private Mockery context = new Mockery();   

    @Test public void 
    usesClassNameAsTitle() throws Exception {
        final DocumentReceiver receiver = mockReceiverCheckingXPath("/html/head/title", CLASS_NAME); 

        DocumentConstructor constructor = new DocumentConstructor(receiver);
        constructor.className(CLASS_NAME);
        constructor.output();

        context.assertIsSatisfied();
    }

    @Test
    public void
    providesXMLNamespace() throws Exception {
        final DocumentReceiver receiver = mockReceiverCheckingXPath("/*/namespace::*[name()='']", "http://www.w3.org/1999/xhtml"); 

        DocumentConstructor constructor = new DocumentConstructor(receiver);
        constructor.className(CLASS_NAME);
        constructor.output();

        context.assertIsSatisfied();
    }

    private DocumentReceiver mockReceiverCheckingXPath(final String xpath, final String value) {
        final DocumentReceiver receiver = context.mock(DocumentReceiver.class);

        context.checking(new Expectations() {{
            oneOf (receiver).receive(with(equal(CLASS_NAME)), with(equal("html")), 
                                     with(hasXPath(xpath, equal(value)))); 
        }});

        return receiver;
    }
}

