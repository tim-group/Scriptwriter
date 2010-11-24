package test;

import com.youdevise.test.narrative.Action;
import com.youdevise.test.narrative.Actor;
import com.youdevise.test.narrative.Extractor;
import com.youdevise.test.narrative.Given;
import com.youdevise.test.narrative.Then;
import com.youdevise.test.narrative.When;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

public class UserGeneratesReadableNarrative {
    private static final String A_SIMPLE_NARRATIVE_TEST =
        "public class LibraryUsers { }";

    @Test public void
    generatesHtmlFromNarrativeStyleTest() {
        
         AuthorActor author = new AuthorActor();
         Given.the(author).was_able_to(write(A_SIMPLE_NARRATIVE_TEST));
         
         When.the(author).attempts_to(produce_a_human_readable_version_of_the_test());
         
         Then.the(author).expects_that(the_output())
                         .should_be(a_readable_form_of_the_narrative());
    }
    
    private Action<ScriptWriter, AuthorActor> write(final String code) {
        return new Action<ScriptWriter, AuthorActor>() {
            @Override
            public void performFor(AuthorActor actor) {
                actor.write(code);
            }
        };
    }

    private Action<ScriptWriter, AuthorActor> produce_a_human_readable_version_of_the_test() {
        return new Action<ScriptWriter, AuthorActor>() {
            @Override
            public void performFor(AuthorActor author) {
                ScriptWriter.main(new String[] { author.getCodePath() });
            }
        };
    }

    private Extractor<String, AuthorActor> the_output() {
        return new Extractor<String, AuthorActor>() {
            @Override
            public String grabFor(AuthorActor author) {
                if (!author.getOutputPath().exists()) {
                   throw new RuntimeException("Output directory was not created.");
                }
                
                File firstOutput = FileUtils.iterateFiles(author.getOutputPath(), new String[] { "html" }, false).next();
                
                try {
                    return FileUtils.readFileToString(firstOutput);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                return null;
            }
        }; 
    }

    private Matcher<String> a_readable_form_of_the_narrative() {
        return allOf(containsString("<html>"),
                     containsString("LibraryUsers"),
                     containsString("the operator was able to"));
    }

    public static class AuthorActor implements Actor<ScriptWriter, AuthorActor> {
        private File codeFile;

        @Override
        public ScriptWriter tool() {
            return null;
        }

        public File getOutputPath() {
            return new File("output");
        }

        public String getCodePath() {
            return codeFile.getAbsolutePath();
        }

        public void write(String code) {
            try {
                codeFile = File.createTempFile("code.java", Long.toString(System.nanoTime()));
                FileUtils.writeStringToFile(codeFile, code);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void perform(Action<ScriptWriter, AuthorActor> action) {
            action.performFor(this);
        }

        @Override
        public <DATA> DATA grabUsing(Extractor<DATA, AuthorActor> extractor) {
            return extractor.grabFor(this);
        }
    }
    
    public static class ScriptWriter {
        public static void main(String[] args) {
            File outputDir = new File("output");
            outputDir.mkdir();
            
            File output = new File(outputDir, "t.html");
            try {
                FileUtils.writeStringToFile(output, "Hello");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
