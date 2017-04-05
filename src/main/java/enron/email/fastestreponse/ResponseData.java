package enron.email.fastestreponse;

public class ResponseData {

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

}
