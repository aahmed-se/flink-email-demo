package enron.email;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import enron.email.fastestreponse.FastestResponseTimes;

public class Driver {

    public static void main(String[] args) throws Exception {

        List<String> messages = new ArrayList<>();
        for(File entry : Utility.listFilesForFolder(new File(args[0]))){
            String content = new String(Files.readAllBytes(entry.toPath()));
            //MimeMessage message = Utility.stringToMimeMessage(content);
            //System.out.println(content);
            messages.add(content);
        }


        System.out.println("=== Email Count By Day");
        new EmailsCountByDay().getMailCountByDay(messages);
        System.out.println("Email Count By Day ===");
        System.out.println("=== Top Direct Message Receiver");
        new DirectMessage().topDirectMessageReceiver(messages);
        System.out.println("Top Direct Message Receiver === ");
        System.out.println("=== Top Broadcast Sender");
        new BroadCastMessage().topBroadcastMessageSender(messages);
        System.out.println("Top Broadcast Sender === ");
        System.out.println("=== Top 5 fastest Reponse");
        new FastestResponseTimes().fastestResponseTime(messages);
        System.out.println("Top 5 fastest Reponse === ");

    }

}