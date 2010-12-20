package com.youdevise.test.scriptwriter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpaceAddingEditor implements Editor {
    private static Pattern wordPattern = Pattern.compile("[A-Z][a-z]*");
    
    @Override
    public String edit(String text) {
        String editedText = "";
        Matcher wordMatcher = wordPattern.matcher(text);
        
        while (wordMatcher.find()) {
            if (editedText.length() > 0) { editedText += " "; }
            editedText += wordMatcher.group();
        }

        return editedText;        
    }
}
