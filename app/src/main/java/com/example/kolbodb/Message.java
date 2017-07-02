package com.example.kolbodb;

import android.text.Html;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by bouda04 on 22/6/2017.
 */

public class Message {
    String UserId;
    long creationTime;
    String request;
    String response;
    String msgID;

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public Message() {

    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(creationTime);
        SimpleDateFormat format = new SimpleDateFormat("d/mm/yyyy 'at' HH:mm");

       return format.format(cal.getTime()) + ":" + request;
    }

    @Override
    public boolean equals(Object obj) {
        Message other = (Message) obj;
        return this.request.equals(other.request);
    }
}
