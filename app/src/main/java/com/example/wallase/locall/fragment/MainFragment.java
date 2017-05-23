package com.example.wallase.locall.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.wallase.locall.R;
import com.example.wallase.locall.activity.LoginActivity_;
import com.example.wallase.locall.api.AuthApi;
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
    AuthApi authApi;

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
        user = app.getUser();

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
            authApi.setHeader("Authorization",user.getApi_token());
            ResponseEntity<Response> entity = authApi.logout();
//
            Log.d("TAG", "api_token: " + user.getApi_token());

            if(entity == null){
                Log.d("TAG","Something wrong!");
                return;
            }

            Response res = entity.getBody();

            if(res.getError() != null){
                Log.d("TAG","error: "+ res.getError());
                return;
            }

            Log.d("TAG","message: " + res.getMessage());

        }catch(HttpServerErrorException e){
            Log.d("TAG","ResponseEntitiy:"+ e);
        }

        userDao.delete(user);

        Intent intent = new Intent(getActivity(), LoginActivity_.class);
        startActivity(intent);

    }
}
