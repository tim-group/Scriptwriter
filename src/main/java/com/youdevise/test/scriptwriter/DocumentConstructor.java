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
    private String className;
    private Document doc;
    private Element html; 
    private Element head;
    private Element body;

    public DocumentConstructor(DocumentReceiver receiver) { 
        this.receiver = receiver;

        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new DocumentConstructionException("Failed to create empty document", e);
        }

        doc = builder.newDocument();
        DOMImplementation domImpl = doc.getImplementation();
        DocumentType doctype = domImpl.createDocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtdqq");
        doc.appendChild(doctype);
        html = addChildElement(doc, "html");
        html.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");

        head = addChildElement(html, "head");
        body = addChildElement(html, "body");
    }

    @Override public void className(String className) { 
        this.className = className; 
        Element title = addChildElement(head, "title");
        addTextChild(title, className);
    }

    public void output() {
        receiver.receive(className, "html", doc);
    }

    private Element addChildElement(Node node, String name) {
        Element element = doc.createElement(name);
        node.appendChild(element);
        return element;
    }

    private void addTextChild(Node node, String text) {
        Text textNode = doc.createTextNode(text);
        node.appendChild(textNode);
    }
}
