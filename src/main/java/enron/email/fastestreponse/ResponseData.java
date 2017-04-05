package enron.email.fastestreponse;

import java.io.Serializable;

public class ResponseData implements Serializable, Comparable<ResponseData> {

    public EmailPairInfo emailPairInfo;
    public long durationSeconds;

    @Override
    public String toString() {
        return "ResponseData{" +
                "emailPairInfo=" + emailPairInfo +
                ", durationSeconds=" + durationSeconds +
                '}';
    }

    public ResponseData(long durationSeconds, EmailPairInfo emailPairInfo) {
        this.durationSeconds = durationSeconds;
        this.emailPairInfo = emailPairInfo;
    }

    public ResponseData(){

    }

    @Override
    public int compareTo(ResponseData o) {
        return this.toString().compareTo(o.toString());
    }

}
