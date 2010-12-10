package com.youdevise.test.scriptwriter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;

import static org.hamcrest.Matchers.hasXPath;

// delete me
import java.io.File;

/*
  To fix:
    remove duplication of execution & context check
    mockReceiverWithCheck(Matcher<T>)
    xPathInDocumentOf, and use Document in receive()
*/

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

    @Test public void
    providesXHTMLPublicId() throws Exception {
        final DocumentReceiver receiver = context.mock(DocumentReceiver.class);

        context.checking(new Expectations() {{
            oneOf (receiver).receive(with(equal(CLASS_NAME)), with(equal("html")), 
                                     with(publicIdOf("-//W3C//DTD XHTML 1.0 Strict//EN"))); 
        }});

        DocumentConstructor constructor = new DocumentConstructor(receiver);
        constructor.className(CLASS_NAME);
        constructor.output();

        context.assertIsSatisfied();
    }

    @Test public void
    providesXMLNamespace() throws Exception {
        final DocumentReceiver receiver = mockReceiverCheckingXPath("/*/namespace::*[name()='']", "http://www.w3.org/1999/xhtml"); 

        DocumentConstructor constructor = new DocumentConstructor(receiver);
        constructor.className(CLASS_NAME);
        constructor.output();

        context.assertIsSatisfied();
    }

    private Matcher<Node> publicIdOf(final String publicId) {
        return new TypeSafeMatcher<Node>() {
            @Override public boolean matchesSafely(Node doc) {
                DocumentType doctype = ((Document)doc).getDoctype();
                if (null == doctype) { return false; }
                return doctype.getPublicId().equals(publicId);
            }

            public void describeTo(Description description) {
                description.appendText("a Document with public ID " + publicId);
            }
        };
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

