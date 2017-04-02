package enron.email;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Utility {

    final static String emailsPath = "/Users/a.ahmed/workspace/enron-email/src/main/resources/enron_with_categories/";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    //final static String emailsPath = "/enron_with_categories/*/*.txt";

    public static MimeMessage stringToMimeMessage(String content) {
        Session s = Session.getDefaultInstance(new Properties());
        InputStream is = new ByteArrayInputStream(content.getBytes());
        try {
            return new MimeMessage(s, is);
        } catch (MessagingException e) {
            System.err.println("ERROR parsing: " + content);
            return null;
        }
    }

    public static String getFirstOfFrom(MimeMessage msg) {
        try {
            if (msg != null && msg.getFrom() != null && msg.getFrom().length > 0) {
                return msg.getFrom()[0].toString();
            }
        } catch (MessagingException e) {
            System.err.println("ERROR reading msg" + msg.toString());
            return null;
        }
        return null;
    }

    public static List<File> listFilesForFolder(final File folder) throws IOException {
        return Files.walk(folder.toPath())
                .filter(Files::isRegularFile)
                .filter(f -> (f.toString().endsWith(".txt") && !f.toString().contains("categories.txt")))
                .map(Path::toFile)
                .collect(Collectors.toList());

    }

    // We use this to group all messages between A,B
    static class UnorderedUserPair implements Serializable {
        final String user1;
        final String user2;
        final int hashcode;

        UnorderedUserPair(String in1, String in2) {
            if (in1.compareTo(in2) < 0) {
                user1 = in1;
                user2 = in2;
            } else {
                user1 = in2;
                user2 = in1;
            }

            hashcode = (user1 + user2).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && (obj instanceof UnorderedUserPair)) {
                UnorderedUserPair other = (UnorderedUserPair) obj;
                return other.user1.equals(user1) && other.user2.equals(user2);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return hashcode;
        }
    }
}