package com.youdevise.test.scriptwriter;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.Matchers.hasXPath;

public class HTMLPrinterTest {
    private static final String CLASS_NAME = "Order";

    private Mockery context = new Mockery();   
    
    @Test public void 
    usesClassNameAsTitle() throws Exception {
        final Recorder recorder = context.mock(Recorder.class);

        context.checking(new Expectations() {{
            oneOf (recorder).write(with(equal(CLASS_NAME)), with(equal("html")), with(hasXPath("/html/head/title", equal(CLASS_NAME)))); 
        }});

        HTMLPrinter printer = new HTMLPrinter(recorder);
        printer.start();
        printer.giveClassName(CLASS_NAME);
        printer.finish();
        printer.print();

        context.assertIsSatisfied();
    }
}

