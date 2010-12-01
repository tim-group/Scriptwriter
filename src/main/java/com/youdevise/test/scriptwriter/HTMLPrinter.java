package com.youdevise.test.scriptwriter;

public class HTMLPrinter implements TokenListener {
    private Recorder recorder;
    private String className;
    private String html;

    public HTMLPrinter(Recorder recorder) { 
        this.recorder = recorder;
        html = "";
    }

    @Override public void start() { html = "<html>"; }
    @Override public void giveClassName(String className) { 
        this.className = className; 
        html += "<head><title>" + className + "</title></head>";
    }
    @Override public void finish() { html += "</html>"; }

    public void print() {
        recorder.write(className, "html", html);
    }
}
