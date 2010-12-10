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

public class DocumentConstructorTest {
    private static final String CLASS_NAME = "Order";

    private Mockery context = new Mockery();   

    @Test public void 
    usesClassNameAsTitle() throws Exception {
        runBasicTestAndValidateDocumentSatisfies( xPathInDocumentOf("/html/head/title", CLASS_NAME) ); 
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
        runBasicTestAndValidateDocumentSatisfies( xPathInDocumentOf("/*/namespace::*[name()='']", "http://www.w3.org/1999/xhtml") ); 
    }

    private void runBasicTestAndValidateDocumentSatisfies(final Matcher<Document> documentMatcher) {
        final DocumentReceiver receiver = context.mock(DocumentReceiver.class);

        context.checking(new Expectations() {{
            oneOf (receiver).receive(with(equal(CLASS_NAME)), with(equal("html")), with(documentMatcher)); 
        }});

        DocumentConstructor constructor = new DocumentConstructor(receiver);
        constructor.className(CLASS_NAME);
        constructor.output();

        context.assertIsSatisfied();
    }

    private Matcher<Document> xPathInDocumentOf(final String xpath, final String value) {
        return new TypeSafeMatcher<Document>() {
            @Override public boolean matchesSafely(Document doc) {
                Matcher<Node> hasXPathMatcher = hasXPath(xpath, equalTo(value));
                return hasXPathMatcher.matches(doc);
            }

            public void describeTo(Description description) {
                description.appendText("a Document where the xpath '" + xpath + "' has the value '" + value + "'");
            }
        };
    }

    private Matcher<Document> publicIdOf(final String publicId) {
        return new TypeSafeMatcher<Document>() {
            @Override public boolean matchesSafely(Document doc) {
                DocumentType doctype = doc.getDoctype();
                if (null == doctype) { return false; }
                return doctype.getPublicId().equals(publicId);
            }

            public void describeTo(Description description) {
                description.appendText("a Document with public ID '" + publicId + "'");
            }
        };
    }

    private Matcher<Document> systemIdOf(final String systemId) {
        return new TypeSafeMatcher<Document>() {
            @Override public boolean matchesSafely(Document doc) {
                DocumentType doctype = doc.getDoctype();
                if (null == doctype) { return false; }
                return doctype.getSystemId().equals(systemId);
            }

            public void describeTo(Description description) {
                description.appendText("a Document with system ID '" + systemId + "'");
            }
        };
    }
}

