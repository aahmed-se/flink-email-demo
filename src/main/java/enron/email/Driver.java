package enron.email;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import enron.email.fastestreponse.FastestResponseTimes;

public class Driver {

    public static void main(String[] args) throws Exception {

        String logPath = Utility.emailsPath;

        List<String> messages = new ArrayList<>();
        for(File entry : Utility.listFilesForFolder(new File(logPath))){
            String content = new String(Files.readAllBytes(entry.toPath()));
            //MimeMessage message = Utility.stringToMimeMessage(content);
            //System.out.println(content);
            messages.add(content);
        }


        //new EmailsCountByDay().getMailCountByDay(messages);
        //new DirectMessage().topDirectMessageReceiver(messages);
        //new BroadCastMessage().topBroadcastMessageSender(messages);
        new FastestResponseTimes().fastestResponseTime(messages);
    }

}