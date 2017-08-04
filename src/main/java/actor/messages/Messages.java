package actor.messages;

import java.io.File;
import java.io.Serializable;

/**
 * Messages that are passed around the actors are usually immutable classes.
 * Think how you go about creating immutable classes:) Make them all static
 * classes inside the Messages class.
 * 
 * This class should have all the immutable messages that you need to pass
 * around actors. You are free to add more classes(Messages) that you think is
 * necessary
 * 
 * @author
 *
 */
public class Messages{

    public static class FinalResultResponse {
        private final long count;

        public FinalResultResponse(long count) {
            this.count = count;
        }

        public long getCount() {
            return count;
        }
    }
    public static class FileResultResponse {
        private final long count;

        public FileResultResponse(long count) {
            this.count = count;
        }

        public long getCount() {
            return count;
        }
    }
    public static class LineResultResponse
    {
        private long count;

        public LineResultResponse(long count) {
            this.count = count;
        }

        public long getCount() {
            return count;
        }
    }


   /* public class ReadFile
    {
        private File file;

        public ReadFile(File file) {
            this.file = file;
        }

        public File getFile() {
            return file;
        }
    }
    public static class CountInAFileResult {
    private long wordCount;

        public CountInAFileResult(long wordCount) {
            this.wordCount = wordCount;
        }

        public long getWordCount() {
            return wordCount;
        }
    }
    public static class ReadLine {
        private String lineStr;

        public ReadLine(String lineStr) {
            this.lineStr = lineStr;
        }

        public String getLineStr() {
            return lineStr;
        }
    }

    public static class CountLineResult
    {
        private long value;

        public long getValue() {
            return value;
        }

        public CountLineResult(long value) {
            this.value = value;
        }
    }
*/
}