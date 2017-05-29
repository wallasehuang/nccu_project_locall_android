package com.example.wallase.locall.model;

import java.util.DoubleSummaryStatistics;

/**
 * Created by wallase on 2017/5/27.
 */
public class Message {

    private int id;
    private String sender;
    private String send_time;
    private String latitude;
    private String longitude;
    private String message;
    private String error;
    private int message_id;
    private int status;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int[] reciver;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getLatitude() {
        return latitude;
    }

    public double getDoubleLatitude(){
        return Double.parseDouble(getLatitude());
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public double getDobleLongitude(){
        return Double.parseDouble(getLongitude());
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int[] getReciver() {
        return reciver;
    }

    public void setReciver(int[] reciver) {
        this.reciver = reciver;
    }
}
