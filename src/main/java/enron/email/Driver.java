package enron.email;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import javax.mail.Address;
import javax.mail.internet.MimeMessage;

import java.io.File;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Driver {

    public static class MailSplitter implements FlatMapFunction<MimeMessage, Tuple2<String, Integer>> {

        @Override
        public void flatMap(MimeMessage value, Collector<Tuple2<String, Integer>> out) throws Exception {

            for(Address address : value.getAllRecipients()){
                out.collect(new Tuple2<String, Integer>(
                        address.toString() + ":" + Utility.DATE_FORMAT.parse(value.getSentDate().toString()).toString(),
                        1));
            }
        }
    }

//    public class WC {
//        public String word;
//        public int count;
//        // [...]
//    }
//
//    // ReduceFunction that sums Integer attributes of a POJO
//    public class WordCounter implements ReduceFunction<WC> {
//        @Override
//        public WC reduce(WC in1, WC in2) {
//            return new WC(in1.word, in1.count + in2.count);
//        }
//    }

    public static void main(String[] args) throws Exception {
        String logPath = Utility.emailsPath;
        List<MimeMessage> messages = new ArrayList<>();
        for(File entry : Utility.listFilesForFolder(new File(logPath))){
            String content = new String(Files.readAllBytes(entry.toPath()));
            MimeMessage message = Utility.stringToMimeMessage(content);
            messages.add(message);
            message.getSentDate();
            //System.out.println(message.getSubject() + " " + message.getFrom().length);
        }

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<MimeMessage> messageDataSet = env.fromCollection(messages);

        System.out.println(messageDataSet.count());

        DataSet<Tuple2<String, Integer>> counts =
                // split up the lines in pairs (2-tuples) containing: (word,1)
                messageDataSet.flatMap(new MailSplitter())
                        .groupBy(0)
                        .aggregate(Aggregations.SUM, 1);

        // emit result
        counts.print();

        // execute program
        env.execute("WordCount Example");

    }

}