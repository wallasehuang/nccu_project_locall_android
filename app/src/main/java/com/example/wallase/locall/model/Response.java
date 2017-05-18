package com.example.wallase.locall.model;

import java.util.List;

/**
 * Created by wallase on 2017/5/9.
 */
public class Response {

    private List<String> messages;
    private List<String> errors;

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
