package com.youdevise.test.scriptwriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class HTMLBuilder implements TokenListener {
    private Printer printer;
    private String className;
    private Document doc;
    private Element html; 
    private Element head;
    private Element body;

    public HTMLBuilder(Printer printer) { 
        this.printer = printer;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
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
        printer.print(className, "html", doc);
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
