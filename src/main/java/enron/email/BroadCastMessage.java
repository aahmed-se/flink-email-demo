package enron.email;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.List;

import javax.mail.Address;
import javax.mail.internet.MimeMessage;

public class BroadCastMessage {

    public class BroadCastMailSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {

            MimeMessage message = Utility.stringToMimeMessage(value);
            if(message != null && message.getAllRecipients() != null && message.getFrom() != null) {
                if(message.getAllRecipients().length > 1){
                    for (Address address : message.getFrom()) {
                        out.collect(new Tuple2<>(
                                address.toString(),
                                1));
                    }
                }
            }
        }
    }

    public void topBroadcastMessageSender(List<String> messages) throws Exception {

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> messageDataSet = env.fromCollection(messages);

        DataSet<Tuple2<String, Integer>> counts =
                messageDataSet.flatMap(new BroadCastMailSplitter())
                        .groupBy(0)
                        .aggregate(Aggregations.SUM, 1)
                        .max(1);

        counts.print();

    }
}
