package edu.uvm.cs275.notslacker;

import java.util.Date;
import java.util.UUID;

public class Slack {
    private UUID mID;
    private String mTitle;
    private String mDescription;
    private Date mDueDate;
    private boolean mCompleted;

    public Slack(){
        this(UUID.randomUUID());
    }

    public Slack(UUID id) {
        mID = id;
        mDueDate = new Date();
    }

    public UUID getID() {
        return mID;
    }

    public void setID(UUID ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getDueDate() {
        return mDueDate;
    }

    public void setDueDate(Date dueDate) {
        mDueDate = dueDate;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }
}
