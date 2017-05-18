package com.example.wallase.locall.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.wallase.locall.R;
import com.example.wallase.locall.activity.LoginActivity_;
import com.example.wallase.locall.api.Auth;
import com.example.wallase.locall.app.MyApp;
import com.example.wallase.locall.green_dao.User;
import com.example.wallase.locall.green_dao.UserDao;
import com.example.wallase.locall.model.Response;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

/**
 * Created by wallase on 2017/5/15.
 */

@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {

    @App
    MyApp app;

    @RestService
    Auth auth;

    @ViewById(R.id.name)
    TextView txtName;
    @ViewById(R.id.email)
    TextView txtEmail;
    @ViewById(R.id.btnLogout)
    Button btnLogout;

    private UserDao userDao;
    private User user;


    @AfterViews
    void init(){

        userDao = app.getUserDao();
        user = userDao.queryBuilder().limit(1).unique();

        txtName.setText(user.getAccount());
        txtEmail.setText(user.getEmail());

    }

    @Click
    void btnLogout(){
        logout(user);
    }

    @Background
    void logout(User user){
        try{
            auth.setHeader("Authorization",user.getApi_token());
            ResponseEntity<Response> entitiy = auth.logout();
//
            Log.d("TAG", "api_token: " + user.getApi_token());

            if(entitiy == null){
                Log.d("TAG","Something wrong!");
                return;
            }

            Response res = entitiy.getBody();

            if(res.getErrors() != null){
                for(String error: res.getErrors()){
                    Log.d("TAG","error: "+ error);
                }
                return;
            }

            for(String message: res.getMessages()){
                Log.d("TAG","message: " + message);
            }

        }catch(HttpServerErrorException e){
            Log.d("TAG","ResponseEntitiy:"+ e);
        }

        userDao.delete(user);

        Intent intent = new Intent(getActivity(), LoginActivity_.class);
        startActivity(intent);

    }
}
