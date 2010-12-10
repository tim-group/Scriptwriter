package com.youdevise.test.scriptwriter;

import com.youdevise.test.narrative.Action;
import com.youdevise.test.narrative.Actor;
import com.youdevise.test.narrative.Extractor;
import com.youdevise.test.narrative.Given;
import com.youdevise.test.narrative.Then;
import com.youdevise.test.narrative.When;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import static org.junit.Assert.fail;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasXPath;

public class UserGeneratesReadableNarrative {
    private static final String A_SIMPLE_NARRATIVE_TEST_CLASS =
        "public class LibraryUsers { }";
    private static final String the_test_class = "LibraryUsers";

    private static File codeDir;
    private static File outputDir;
    private static DocumentBuilder builder;

    @BeforeClass public static void setup() throws Exception {
        codeDir = makeTempDir("Scriptwriter-test-code-");
        outputDir = makeTempDir("Scriptwriter-test-output-");
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        builder.setEntityResolver(new TrivialEntityResolver());
    }

    public static File makeTempDir(String name) throws Exception {
        File dir = File.createTempFile(name, Long.toString(System.nanoTime()));
        dir.delete();
        dir.mkdir();
        return dir;
    }

    @Test public void
    generatesHtmlFromNarrativeStyleTest() throws Exception {
        
        AuthorActor author = new AuthorActor();
        Given.the(author).was_able_to(write(A_SIMPLE_NARRATIVE_TEST_CLASS));
         
        When.the(author).attempts_to(produce_a_human_readable_version_of_the_test());
         
        Then.the(author).expects_that(the_output_for(the_test_class), has_the_same_title_as(the_test_class));
    }
    
    private Action<Scriptwriter, AuthorActor> write(final String code) {
        return new Action<Scriptwriter, AuthorActor>() {
            @Override
            public void performFor(AuthorActor actor) {
                actor.write(code);
            }
        };
    }

    private Action<Scriptwriter, AuthorActor> produce_a_human_readable_version_of_the_test() {
        return new Action<Scriptwriter, AuthorActor>() {
            @Override
            public void performFor(AuthorActor author) {
                Scriptwriter.main(new String[] { "-o", outputDir.getAbsolutePath(), author.getCodePath() });
            }
        };
    }

    private Extractor<Document, AuthorActor> the_output_for(final String className) {
        return new Extractor<Document, AuthorActor>() {
            @Override
            public Document grabFor(AuthorActor author) {
                try {
                    File htmlFile = new File(outputDir, className + ".html"); 
                    return builder.parse(htmlFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Could not read or parse output file: " + e.getMessage());
                } 
                return null;
            }
        }; 
    }

    private Matcher<Node> has_the_same_title_as(String className) {
        return hasXPath("/html/head/title", equalTo(className));
    }

    public static class AuthorActor implements Actor<Scriptwriter, AuthorActor> {
        private File codeFile;

        @Override
        public Scriptwriter tool() {
            return null;
        }

        public String getCodePath() {
            if (null == codeFile) { 
                throw new IllegalStateException("should have created code file before trying to read it");
            }
            return codeFile.getAbsolutePath();
        }

        public void write(String code) {
            try {
                codeFile = File.createTempFile("Scriptwriter-test-", Long.toString(System.nanoTime()), codeDir);
                FileUtils.writeStringToFile(codeFile, code);
            } catch (Exception e) {
                e.printStackTrace();
                fail("Could not write to code file: " + e.getMessage());
            }
        }

        @Override
        public void perform(Action<Scriptwriter, AuthorActor> action) {
            action.performFor(this);
        }

        @Override
        public <DATA> DATA grabUsing(Extractor<DATA, AuthorActor> extractor) {
            return extractor.grabFor(this);
        }
    }
}
