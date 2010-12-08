package com.youdevise.test.scriptwriter;

import org.w3c.dom.Node;

public interface DocumentReceiver {
    public void receive(String title, String type, Node doc);
}
