package enron.email.fastestreponse;

import java.io.Serializable;

public class Conversation implements Serializable {

    public String sortedEmails;
    public String subjectNormalized;

    public Conversation() {


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conversation that = (Conversation) o;

        if (sortedEmails != null ? !sortedEmails.equals(that.sortedEmails) : that.sortedEmails != null)
            return false;
        return subjectNormalized != null ? subjectNormalized.equals(that.subjectNormalized) : that.subjectNormalized == null;
    }

    @Override
    public int hashCode() {
        int result = sortedEmails != null ? sortedEmails.hashCode() : 0;
        result = 31 * result + (subjectNormalized != null ? subjectNormalized.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "sortedEmails=" + sortedEmails +
                ", subjectNormalized='" + subjectNormalized + '\'' +
                '}';
    }

    public Conversation(String sortedEmails, String subjectNormalized) {
        this.sortedEmails = sortedEmails;
        this.subjectNormalized = subjectNormalized;
    }



}
