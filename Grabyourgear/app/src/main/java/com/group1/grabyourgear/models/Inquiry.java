package com.group1.grabyourgear.models;

public class Inquiry {
    private String id;
    private String contact;
    private String details;

    public Inquiry() {}

    public Inquiry(String id, String contact, String details) {
        this.id = id;
        this.contact = contact;
        this.details = details;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getContact() {return contact;}
    public void setContact(String contact) {this.contact = contact;}

    public String getDetails() {return details;}
    public void setDetails(String details) {this.details = details;}
}
