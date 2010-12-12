package com.youdevise.test.scriptwriter;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PrinterTest {
    private static final String PUBLIC_ID = "-//TEST//DTD PUBLIC-ID//EN";
    private static final String SYSTEM_ID = "http://www.example.com/system-id.dtd";
    private DocumentBuilder builder;
    private Document doc;
    private File outputDir;

    @Before public void
    setupDocument() throws Exception {
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        builder.setEntityResolver(new TrivialEntityResolver());

        doc = builder.newDocument();
        DocumentType doctype = doc.getImplementation().createDocumentType("books", PUBLIC_ID, SYSTEM_ID);
        doc.appendChild(doctype);

        Element books = DOMUtils.addChildElement(doc, "books");
        Element bigSleep = DOMUtils.addChildElement(books, "book");
        DOMUtils.addTextChild(bigSleep, "The Big Sleep");
    }

    @Before public void
    createOutputDirectory() throws Exception {
        outputDir = makeTempDir("Printer-test-output");
    }
    
    @Test public void 
    createsAFileInOutputDir() throws Exception {
        runPrinter();
        assertThat(outputDir.list(), is(arrayWithSize(greaterThan(0)))); 
    }

    @Test public void
    createsAFileWithGivenNameAndExtension() throws Exception {
        runPrinter();
        File outputFile = new File(outputDir, "books.xml");
        assertThat(outputFile.exists(), is(true)); 
    }

    @Test public void
    serializesTheGivenXMLFile() throws Exception {
        runPrinter();
        Document outputDoc = builder.parse(new File(outputDir, "books.xml"));
        assertThat(outputDoc, hasXPath("/books/book", equalTo("The Big Sleep")));
    }

    @Test public void
    assignsADoctype() throws Exception {
        runPrinter();
        Document outputDoc = builder.parse(new File(outputDir, "books.xml"));
        DocumentType docType = outputDoc.getDoctype();
        assertThat(docType, is(notNullValue()));
    }

    @Test public void
    usesPublicId() throws Exception {
        runPrinter();
        Document outputDoc = builder.parse(new File(outputDir, "books.xml"));
        DocumentType docType = outputDoc.getDoctype();
        assertThat(docType.getPublicId(), is(equalTo(PUBLIC_ID)));
    }

    @Test public void
    usesSystemId() throws Exception {
        runPrinter();
        Document outputDoc = builder.parse(new File(outputDir, "books.xml"));
        DocumentType docType = outputDoc.getDoctype();
        assertThat(docType.getSystemId(), is(equalTo(SYSTEM_ID)));
    }

    private void runPrinter() {
        Printer printer = new Printer(outputDir);
        printer.receive("books", "xml", doc);
    }

    public static File makeTempDir(String name) throws Exception {
        File dir = File.createTempFile(name, Long.toString(System.nanoTime()));
        dir.delete();
        dir.mkdir();
        return dir;
    }
}

