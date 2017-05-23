package com.example.wallase.locall.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wallase.locall.R;
import com.example.wallase.locall.api.FriendApi;
import com.example.wallase.locall.app.MyApp;
import com.example.wallase.locall.green_dao.User;
import com.example.wallase.locall.model.Member;
import com.example.wallase.locall.model.Response;
import com.example.wallase.locall.model.ShipStatus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

/**
 * Created by wallase on 2017/5/19.
 */
@EFragment(R.layout.fragment_friend)
public class FriendFragment extends Fragment {

    @App
    MyApp app;

    @RestService
    FriendApi friendApi;

    @ViewById(R.id.edit_search)
    EditText edit_search;
    @ViewById(R.id.img_avatar)
    ImageView img_avatar;
    @ViewById(R.id.account)
    TextView txt_account;
    @ViewById(R.id.message)
    TextView txt_message;
    @ViewById(R.id.btn_action)
    Button btn_action;

    private User user;

    private int action_status;
    private Member member_friend;


    @AfterViews
    void init(){

        user = app.getUser();

        clearView();
    }

    @Click
    void btn_search(){
        clearView();
        String account = edit_search.getText().toString();
        findFriend(account);
    }

    @Click
    void btn_action(){
        clearView();
        actionFriend(action_status);
    }

    @Click
    void btn_back(){
        showFragment(new FriendListFragment_());
    }

    @Background
    void actionFriend(int status){
        try{
            friendApi.setHeader("Authorization", user.getApi_token());
            ResponseEntity<Response> entity = null;
            switch (status){
                case 1:
                    entity = friendApi.invite(member_friend);
                    break;
                case 2:
                    entity = friendApi.accept(member_friend);
                    break;
            }


            if(entity == null){
                Log.d("TAG","Somethig wrong!");
                return;
            }
            Response response = entity.getBody();

            if(response.getError() != null){
                Log.d("TAG",response.getError());
                showMessage(response.getError());
                return;
            }

            Log.d("TAG",response.getMessage());
            showMessage(response.getMessage());


        }catch (HttpServerErrorException e){
            Log.d("TAG","RespnseEntity:"+e);
        }
    }


    @Background
    void findFriend(String account){
        member_friend = new Member();
        Member req_member = new Member();
        req_member.setAccount(account);

        try{
            friendApi.setHeader("Authorization",user.getApi_token());
            ResponseEntity<Member> entity = friendApi.find(req_member);

            if(entity == null){
                Log.d("TAG","Somethig wrong!");
                return;
            }
            Member res_member = entity.getBody();
            if(res_member.getErrors() != null){
                String show_error = "";
                for(String error: res_member.getErrors()){
                    show_error += error + "\n";
                }
                showMessage(show_error);
                return;
            }
            ShipStatus shipStatus  = friendApi.checkStatus(res_member).getBody();
            member_friend.setAccount(res_member.getAccount());
            showFriend(res_member.getAccount(), shipStatus.getStatus());
        }catch (HttpServerErrorException e){
            Log.d("TAG","RespnseEntity:"+e);
        }

    }



    @UiThread
    void showMessage(String message){
        txt_message.setText(message);
    }

    @UiThread
    void showFriend(String account, int status){
        switch(status){
            case 1:
                btn_action.setText("邀請");
                btn_action.setVisibility(View.VISIBLE);
                action_status = 1;
                break;
            case 2:
                txt_message.setText("已發出邀請，等待對方接受中...");
                btn_action.setVisibility(View.GONE);
                break;
            case 3:
                btn_action.setText("接受");
                txt_message.setText(account+"已對您做出邀請，是否接受？");
                btn_action.setVisibility(View.VISIBLE);
                action_status = 2;
                break;
            case 4:
                txt_message.setText("您與" + account + "已是好友！");
                btn_action.setVisibility(View.GONE);
                break;
        }
        txt_account.setText(account);
        img_avatar.setVisibility(View.VISIBLE);
    }

    @UiThread
    void clearView(){
        txt_message.setText("");
        img_avatar.setVisibility(View.GONE);
        txt_account.setText("");
        btn_action.setVisibility(View.GONE);
        btn_action.setText("");
    }


    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



}
