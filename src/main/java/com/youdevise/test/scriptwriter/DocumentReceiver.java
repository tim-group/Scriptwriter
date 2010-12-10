package com.youdevise.test.scriptwriter;

import org.w3c.dom.Document;

public interface DocumentReceiver {
    public void receive(String title, String type, Document doc);
}
