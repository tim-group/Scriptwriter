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
        final Printer printer = mockPrinterCheckingXPath("/html/head/title", CLASS_NAME); 

        HTMLBuilder builder = new HTMLBuilder(printer);
        builder.className(CLASS_NAME);
        builder.output();

        context.assertIsSatisfied();
    }

    @Test
    public void
    providesXMLNamespace() throws Exception {
        final Printer printer = mockPrinterCheckingXPath("/*/namespace::*[name()='']", "http://www.w3.org/1999/xhtml"); 

        HTMLBuilder builder = new HTMLBuilder(printer);
        builder.className(CLASS_NAME);
        builder.output();

        context.assertIsSatisfied();
    }

    private Printer mockPrinterCheckingXPath(final String xpath, final String value) {
        final Printer printer = context.mock(Printer.class);

        context.checking(new Expectations() {{
            oneOf (printer).print(with(equal(CLASS_NAME)), with(equal("html")), 
                                  with(hasXPath(xpath, equal(value)))); 
        }});

        return printer;
    }
}

