package com.example.wallase.locall.view_group;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wallase.locall.R;
import com.example.wallase.locall.api.FriendApi;
import com.example.wallase.locall.app.MyApp;
import com.example.wallase.locall.model.Friend;
import com.example.wallase.locall.model.Member;
import com.example.wallase.locall.model.Response;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

/**
 * Created by wallase on 2017/5/21.
 */
@EViewGroup(R.layout.friend_list_item)
public class FriendItemView extends LinearLayout{

    @App
    MyApp app;

    @RestService
    FriendApi friendApi;

    @ViewById(R.id.account)
    TextView txt_account;

    @ViewById(R.id.message)
    TextView txt_message;

    @ViewById(R.id.btn_action)
    Button btn_action;

    @ViewById(R.id.check)
    CheckBox checkbox;

    private Friend friend;


    public FriendItemView(Context context) {
        super(context);

    }

    public void bind(Friend friend){
        this.friend = friend;
        this.txt_account.setText(friend.getAccount());
        switch(friend.getType()){
            case 1:
                this.txt_message.setVisibility(View.GONE);
                this.btn_action.setVisibility(View.GONE);
                this.checkbox.setVisibility(View.GONE);
                break;
            case 2:
                this.txt_message.setText("等待對方接受...");
                this.txt_message.setVisibility(View.VISIBLE);
                this.btn_action.setVisibility(View.GONE);
                this.checkbox.setVisibility(View.GONE);
                break;
            case 3:
                this.txt_message.setVisibility(View.GONE);
                this.btn_action.setVisibility(View.VISIBLE);
                this.checkbox.setVisibility(View.GONE);
                break;
            case 4:
                this.btn_action.setVisibility(View.GONE);
                this.checkbox.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Click
    void btn_action(){
        accept();
    }


    @Background
    void accept(){
        try{
            friendApi.setHeader("Authorization", app.getUser().getApi_token());
            ResponseEntity<Response> entity = null;
            Member member_friend = new Member();
            member_friend.setAccount(friend.getAccount());
            entity = friendApi.accept(member_friend);

            if(entity == null){
                Log.d("TAG", "Somethig wrong!");
                return;
            }
            Response response = entity.getBody();

            if(response.getError() != null){
                Log.d("TAG",response.getError());
                return;
            }

            Log.d("TAG",response.getMessage());

        }catch (HttpServerErrorException e){
            Log.d("TAG","RespnseEntity:"+e);
        }
    }
}
