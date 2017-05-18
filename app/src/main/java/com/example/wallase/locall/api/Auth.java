package com.example.wallase.locall.api;


import com.example.wallase.locall.model.Member;
import com.example.wallase.locall.model.Response;

import org.androidannotations.rest.spring.annotations.Accept;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresAuthentication;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


/**
 * Created by wallase on 2017/4/8.
 */

@Rest(rootUrl = "http://140.119.19.25/api",converters = {MappingJackson2HttpMessageConverter.class,FormHttpMessageConverter.class,StringHttpMessageConverter.class})
@Accept(MediaType.APPLICATION_JSON)
public interface Auth {

    @Post("/login")
    ResponseEntity<Member> login(@Body Member member);

    @Post("/register")
    ResponseEntity<Member> register(@Body Member member);

    @Get("/logout")
    @RequiresHeader("Authorization")
    ResponseEntity<Response> logout();

    void setHeader(String name, String value);

//    void setAuthentication(String token);


}
