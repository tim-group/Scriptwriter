package com.youdevise.test.scriptwriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class DocumentConstructor implements TokenListener {
    private DocumentReceiver receiver;
    private StringCleaner cleaner;
    private String className;
    private Document doc;
    private Element html; 
    private Element head;
    private Element body;

    public DocumentConstructor(DocumentReceiver rec) {
        this(rec, new StringCleaner() {
                       public String clean(String text) {
                           return text;
                       }
                  });
    }

    public DocumentConstructor(DocumentReceiver receiver, StringCleaner cleaner) { 
        this.receiver = receiver;
        this.cleaner = cleaner;

        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new DocumentConstructionException("Failed to create empty document", e);
        }

        doc = builder.newDocument();
        DocumentType doctype = doc.getImplementation().createDocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        doc.appendChild(doctype);
        html = DOMUtils.addChildElement(doc, "html");
        html.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");

        head = DOMUtils.addChildElement(html, "head");
        body = DOMUtils.addChildElement(html, "body");
    }

    @Override public void className(String className) { 
        this.className = className; 
        String outputClassName = cleaner.clean(className);

        Element title = DOMUtils.addChildElement(head, "title");
        DOMUtils.addTextChild(title, outputClassName);
        Element h1 = DOMUtils.addChildElement(body, "h1");
        DOMUtils.addTextChild(h1, outputClassName);
    }

    public void output() {
        receiver.receive(className, "html", doc);
    }
}
