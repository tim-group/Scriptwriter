package com.youdevise.test.scriptwriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class DOMUtils {
    public static Element addChildElement(Node node, String name) {
        Document doc = getDocFrom(node);
        Element element = doc.createElement(name);
        node.appendChild(element);
        return element;
    }

    public static void addTextChild(Node node, String text) {
        Document doc = getDocFrom(node);
        Text textNode = doc.createTextNode(text);
        node.appendChild(textNode);
    }

    private static Document getDocFrom(Node node) {
        Document doc = node.getOwnerDocument();
        if (null == doc) { doc = (Document) node; }
        return doc;
    }
}
