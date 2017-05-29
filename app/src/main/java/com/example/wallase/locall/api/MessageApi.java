package com.example.wallase.locall.api;

import com.example.wallase.locall.model.Friend;
import com.example.wallase.locall.model.Member;
import com.example.wallase.locall.model.Message;
import com.example.wallase.locall.model.Response;
import com.example.wallase.locall.model.ShipStatus;

import org.androidannotations.rest.spring.annotations.Accept;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by wallase on 2017/5/27.
 */
@Rest(rootUrl = "http://140.119.19.25/api",converters = {MappingJackson2HttpMessageConverter.class,FormHttpMessageConverter.class,StringHttpMessageConverter.class})
@Accept(MediaType.APPLICATION_JSON)
public interface MessageApi {

    @Post("/message/list")
    @RequiresHeader("Authorization")
    Message[] list();

    @Post("/message/send")
    @RequiresHeader("Authorization")
    ResponseEntity<Response> send(@Body Message message);

    @Post("/message/watch")
    @RequiresHeader("Authorization")
    Message watch(@Body Message message);

    void setHeader(String name, String value);
}
