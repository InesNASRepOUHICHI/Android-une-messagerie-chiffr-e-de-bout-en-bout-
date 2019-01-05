package edu.unice.messenger.messageriembds.Model;

import java.sql.Timestamp;
import java.util.Date;

public class Message implements Comparable<Message>{
    private String message;
    private User sender;
    private User receiver;
    private Timestamp dateCreated;

    public Message() {
    }

    public Message(String message, User sender, User receiver, Timestamp dateCreated) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.dateCreated = dateCreated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @Override
    public int compareTo(Message m) {
        return getDateCreated().compareTo(m.getDateCreated());
    }
}