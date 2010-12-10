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

import static org.hamcrest.Matchers.equalTo;
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
        runBasicTestAndValidateDocumentSatisfies( hasXPath("/html/head/title", equalTo(CLASS_NAME)) ); 
    }

    @Test public void
    providesXHTMLPublicId() throws Exception {
        runBasicTestAndValidateDocumentSatisfies( publicIdOf("-//W3C//DTD XHTML 1.0 Strict//EN") ); 
    }

    @Test public void
    providesXHTMLSystemId() throws Exception {
        runBasicTestAndValidateDocumentSatisfies( systemIdOf("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd") );
    }

    @Test public void
    providesXMLNamespace() throws Exception {
        runBasicTestAndValidateDocumentSatisfies( hasXPath("/*/namespace::*[name()='']", equalTo("http://www.w3.org/1999/xhtml")) ); 
    }

    private void runBasicTestAndValidateDocumentSatisfies(final Matcher<Node> documentMatcher) {
        final DocumentReceiver receiver = context.mock(DocumentReceiver.class);

        context.checking(new Expectations() {{
            oneOf (receiver).receive(with(equal(CLASS_NAME)), with(equal("html")), with(documentMatcher)); 
        }});

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
                description.appendText("a Document with public ID '" + publicId + "'");
            }
        };
    }

    private Matcher<Node> systemIdOf(final String systemId) {
        return new TypeSafeMatcher<Node>() {
            @Override public boolean matchesSafely(Node doc) {
                DocumentType doctype = ((Document)doc).getDoctype();
                if (null == doctype) { return false; }
                return doctype.getSystemId().equals(systemId);
            }

            public void describeTo(Description description) {
                description.appendText("a Document with system ID '" + systemId + "'");
            }
        };
    }
}

