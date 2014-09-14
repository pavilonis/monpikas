package lt.pavilonis.monpikas.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.Lob;

@Entity
public class Message {

    public Message() {
    }

    public Message(String sendername, String senderphone, String sendermail, String text) {
        this.sendername = sendername;
        this.senderphone = senderphone;
        this.sendermail = sendermail;
        this.text = text;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sendername;

    private String senderphone;

    private String sendermail;

    @Lob
    @Column( length = 1024 )
    private String text;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSendername() {
        return sendername;
    }
    public void setSendername(String sendername) {
        this.sendername = sendername;
    }
    public String getSenderphone() {
        return senderphone;
    }
    public void setSenderphone(String senderphone) {
        this.senderphone = senderphone;
    }
    public String getSendermail() {
        return sendermail;
    }
    public void setSendermail(String sendermail) {
        this.sendermail = sendermail;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}