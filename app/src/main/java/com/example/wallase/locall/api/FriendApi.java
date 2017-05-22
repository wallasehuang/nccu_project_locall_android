package com.example.wallase.locall.api;

import com.example.wallase.locall.model.Member;
import com.example.wallase.locall.model.Friend;
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

import java.util.List;

/**
 * Created by wallase on 2017/5/19.
 */
@Rest(rootUrl = "http://140.119.19.25/api",converters = {MappingJackson2HttpMessageConverter.class,FormHttpMessageConverter.class,StringHttpMessageConverter.class})
@Accept(MediaType.APPLICATION_JSON)
public interface FriendApi {

    @Post("/member/account")
    @RequiresHeader("Authorization")
    ResponseEntity<Member> find(@Body Member member);

    @Post("/friend/checkFriendShip")
    @RequiresHeader("Authorization")
    ResponseEntity<ShipStatus> checkStatus(@Body Member member);

    @Post("/friend/list")
    @RequiresHeader("Authorization")
    List<Friend> list();

    @Post("/friend/listByInviter")
    @RequiresHeader("Authorization")
    ResponseEntity<Friend> listByInviter();

    @Post("/friend/invite")
    @RequiresHeader("Authorization")
    ResponseEntity<Friend> listByInvitee();

    @Post("/friend/invite")
    @RequiresHeader("Authorization")
    ResponseEntity<Response> invite(@Body Member member);

    @Post("/friend/accept")
    @RequiresHeader("Authorization")
    ResponseEntity<Response> accept(@Body Member member);

    void setHeader(String name, String value);
}
