package com.example.wallase.locall.model;

import java.util.List;

/**
 * Created by wallase on 2017/5/9.
 */
public class Response {

    private String message;
    private String error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
