package enron.email.fastestreponse;

import com.sun.org.apache.regexp.internal.RESyntaxException;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.GroupReduceOperator;
import org.apache.flink.api.java.operators.SortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.MimeMessage;

import enron.email.Utility;


public class FastestResponseTimes {

    public class Reducer implements GroupReduceFunction<Tuple3<String, EmailPairInfo, Date>, Tuple2<Long, EmailPairInfo>>{

        List<Tuple3<String, EmailPairInfo, Date>> responseList = new ArrayList<>();

        @Override
        public void reduce(Iterable<Tuple3<String, EmailPairInfo, Date>> values, Collector<Tuple2<Long, EmailPairInfo>> out) throws Exception {
            for(Tuple3<String, EmailPairInfo, Date> entry : values){
                responseList.add(entry);
            }

            if(responseList.size() > 1){
                long responseTime = Utility.dateDiffinSeconds(responseList.get(1).f2, responseList.get(0).f2);
                out.collect(new Tuple2<>(responseTime, responseList.get(0).f1));
            }

        }
    }


    public class SenderRecipientPairSplitter implements FlatMapFunction<String, Tuple3<String, EmailPairInfo, Date>> {

        @Override
        public void flatMap(String value, Collector<Tuple3<String, EmailPairInfo, Date>> out) throws Exception {

            MimeMessage message = Utility.stringToMimeMessage(value);
            if(message != null && message.getAllRecipients() != null && message.getSubject() != null && message.getSentDate() != null) {
                for (Address addressFrom : message.getFrom()) {
                    for (Address addressTo : message.getAllRecipients()) {
                        String[] temp = new String[2];
                        temp[0] = addressTo.toString();
                        temp[1] = addressFrom.toString();
                        Arrays.sort(temp);
                        List<String> namesList = Arrays.asList(temp);
                        String normalizedSubject = Utility.normalizeSubject(message.getSubject());
                        out.collect(new Tuple3<>(
                                new Conversation(namesList.toString(), normalizedSubject).toString(),
                                new EmailPairInfo(namesList, message.getSubject(), normalizedSubject, message.getSentDate(), addressTo.toString(), addressFrom.toString()),
                                message.getSentDate()
                        ));
                    }
                }
            }
        }

    }

    public void fastestResponseTime(List<String> messages) throws Exception {

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> messageDataSet = env.fromCollection(messages);
//
        DataSet<Tuple2<Long, EmailPairInfo>> fastestReponses = messageDataSet.flatMap(new SenderRecipientPairSplitter())
                .groupBy(0)
                .sortGroup(2, Order.ASCENDING)
                .reduceGroup(new Reducer())
                .sortPartition(0, Order.ASCENDING)
                .first(5);

//        DataSet<Tuple2<Long, EmailPairInfo>> fastestReponses = messageDataSet.flatMap(new SenderRecipientPairSplitter())
//                .groupBy(0)
//                .sortGroup(2, Order.ASCENDING)
//                .reduceGroup(new Reducer());
//                .sortPartition(0, Order.ASCENDING).first(5);


//        DataSet fastestReponses = messageDataSet.flatMap(new SenderRecipientPairSplitter());         .groupBy(0)
//                .sortGroup(2, Order.ASCENDING)
//                .reduceGroup(new Reducer());
        fastestReponses.print();

    }
}
