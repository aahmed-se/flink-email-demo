package enron.email;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Utility {

    public static final SimpleDateFormat DATE_FORMAT_DAY = new SimpleDateFormat("yyyy-MM-dd");

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

    public static List<File> listFilesForFolder(final File folder) throws IOException {
        return Files.walk(folder.toPath())
                .filter(Files::isRegularFile)
                .filter(f -> (f.toString().endsWith(".txt") && !f.toString().contains("categories.txt")))
                .map(Path::toFile)
                .collect(Collectors.toList());

    }

    public static String normalizeSubject(String subject) throws IOException {
        if(subject == null){
            return null;
        }
        subject = subject.toLowerCase();
        subject = subject.replaceAll("re:","");
        subject = subject.replaceAll("fe:","");
        return subject;
    }

    public static long dateDiffInSeconds(Date newDate , Date oldDate){

        return (newDate.toInstant().toEpochMilli() - oldDate.toInstant().toEpochMilli()) / 1000;

    }

}