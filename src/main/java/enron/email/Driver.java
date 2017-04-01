package enron.email;

import com.google.common.collect.Iterators;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import javax.mail.internet.MimeMessage;
import java.time.Instant;

public class Driver {
    static class LabeledSummary {
        Instant time;
        Double sentiment;
        Integer sentimentCount;

        public Instant getTime() {
            return time;
        }

        public Double getSentiment() {
            return sentiment;
        }

        public Integer getSentimentCount() {
            return sentimentCount;
        }

        public LabeledSummary(Instant time, Double sentiment, Integer sentimentCount) {
            this.time = time;
            this.sentiment = sentiment;
            this.sentimentCount = sentimentCount;
        }

        public LabeledSummary() {
        }

        void parseFromString(String str) {
            String[] lines = str.split("\n");
            for (String line : lines) {
                String[] details = str.split(",");

                // xxx
            }
        }
    }

    public static void main(String[] args) {
        //String logPath = testpath + "*/*.txt"; //+ "1/3111.txt"; // Should be some file on your system
        String logPath = Utils.dir + "*/*.txt";
        String sentimentPath = Utils.dir + "*/*.cats";
        SparkConf conf = new SparkConf().setAppName("Simple Application");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaPairRDD<String, String> logData = sc.wholeTextFiles(logPath)
                .mapToPair(p -> new Tuple2<>(StringUtils.removeEnd(p._1, ".txt"), p._2));
        JavaPairRDD<String, String> sentimentData = sc.wholeTextFiles(sentimentPath)
                .mapToPair(p -> new Tuple2<>(StringUtils.removeEnd(p._1, ".cats"), p._2));

        JavaRDD<Tuple2<String, String>> labeledData = logData.join(sentimentData).values();
        System.out.println("labeledData isEmpty? " + labeledData.isEmpty());

        JavaPairRDD<String, Iterable<LabeledSummary>> labeledEmailsByUser = labeledData.mapToPair(p -> {
            final MimeMessage msg = Utils.stringToMimeMessage(p._1);
            return new Tuple2<>(Utils.getFirstOfFrom(msg), new LabeledSummary());
        }).groupByKey();
        System.out.println("labeledEmailsByUser isEmpty? " + labeledEmailsByUser.isEmpty());

        // I want to get a histogram showing how much labeled data each user tends to have.
        JavaPairRDD<String, Integer> countsByUser = labeledEmailsByUser.mapToPair(p -> new Tuple2<>(p._1, Iterators.size(p._2.iterator())));
        JavaDoubleRDD counts = countsByUser.values().mapToDouble(Double::new);
        //counts.toLocalIterator().forEachRemaining(d -> System.out.print(d + ", "));


        Tuple2<double[], long[]> histogram = counts.histogram(10);

        double[] bounds = histogram._1;
        long[] totals = histogram._2;
        int populationCount = 0;
        for (int i = 0; i < bounds.length - 1; i++) {
            System.out.println(String.format("[%s-%s): %s", bounds[i], bounds[i + 1], totals[i]));
            populationCount += totals[i];
        }
        System.out.println("Number of users: " + populationCount);
    }

}