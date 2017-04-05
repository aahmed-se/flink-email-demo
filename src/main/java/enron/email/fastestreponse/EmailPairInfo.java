package enron.email.fastestreponse;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EmailPairInfo implements Serializable, Comparable<EmailPairInfo> {

    public List<String> sortedEmails;
    public String subject;
    public String normalizedSubject;
    public Date sent;
    public String receiver;
    public String sender;

    public EmailPairInfo(List<String> sortedEmails, String subject, String normalizedSubject, Date sent, String receiver, String sender) {
        this.sortedEmails = sortedEmails;
        this.subject = subject;
        this.normalizedSubject = normalizedSubject;
        this.sent = sent;
        this.receiver = receiver;
        this.sender = sender;
    }

    public EmailPairInfo() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailPairInfo that = (EmailPairInfo) o;

        if (sortedEmails != null ? !sortedEmails.equals(that.sortedEmails) : that.sortedEmails != null)
            return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (normalizedSubject != null ? !normalizedSubject.equals(that.normalizedSubject) : that.normalizedSubject != null)
            return false;
        if (sent != null ? !sent.equals(that.sent) : that.sent != null) return false;
        if (receiver != null ? !receiver.equals(that.receiver) : that.receiver != null)
            return false;
        return sender != null ? sender.equals(that.sender) : that.sender == null;
    }

    @Override
    public int hashCode() {
        int result = sortedEmails != null ? sortedEmails.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (normalizedSubject != null ? normalizedSubject.hashCode() : 0);
        result = 31 * result + (sent != null ? sent.hashCode() : 0);
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EmailPairInfo{" +
                "sortedEmails=" + sortedEmails +
                ", subject='" + subject + '\'' +
                ", normalizedSubject='" + normalizedSubject + '\'' +
                ", sent=" + sent +
                ", receiver='" + receiver + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }

    @Override
    public int compareTo(EmailPairInfo o) {
        return this.toString().compareTo(o.toString());
    }
}
