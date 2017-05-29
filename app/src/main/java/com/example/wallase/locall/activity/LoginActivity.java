package com.example.wallase.locall.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wallase.locall.R;
import com.example.wallase.locall.api.AuthApi;
import com.example.wallase.locall.app.MyApp;
import com.example.wallase.locall.green_dao.UserDao;
import com.example.wallase.locall.green_dao.User;
import com.example.wallase.locall.model.Member;

import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

/**
 * Created by wallase on 2017/5/2.
 */

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @App
    MyApp app;

    @RestService
    AuthApi authApi;

    @ViewById(R.id.account)
    EditText editAccount;
    @ViewById(R.id.password)
    EditText editPassword;
    @ViewById(R.id.btnLogin)
    Button btnLogin;
    @ViewById(R.id.btnLinkToRegisterScreen)
    Button btnRegister;
    @ViewById(R.id.error)
    TextView txtError;

    @AfterViews
    void init(){
        checkLogin();
    }

    @Click
    void btnLogin(){


        String account = editAccount.getText().toString();
        String password = editPassword.getText().toString();
        String device_token = "1234567890";
        apiLogin(account, password, device_token);

    }

    @Click
    void btnLinkToRegisterScreen(){
        Intent intent = new Intent(this,RegisterActivity_.class);
        startActivity(intent);
        finish();
    }

    @Background
    void checkLogin(){
        UserDao userDao = app.getUserDao();
        if(userDao.count() > 0){
            Intent intent = new Intent(this,MainActivity_.class);
            startActivity(intent);
            finish();
        }
    }

    @Background
    void apiLogin(String account, String password, String device_token){

        Member req_member = new Member();
        req_member.setAccount(account);
        req_member.setPassword(password);
        req_member.setDevice_token(device_token);

        try{
            ResponseEntity<Member> entity = authApi.login(req_member);

            if(entity == null){
                Log.d("TAG","Something wrong!");
                return;
            }

            Member res_member = entity.getBody();

            if(res_member.getErrors() != null){
                String show_error ="";
                for(String error : res_member.getErrors()){
                    show_error += error +"\n";
                }
                showError(show_error);
                return;
            }

            Log.d("TAG","id: "+res_member.getId());
            Log.d("TAG","account: "+res_member.getAccount());
            Log.d("TAG","email: "+res_member.getEmail());
            Log.d("TAG","api_token: "+res_member.getApi_token());
            Log.d("TAG","device_token: "+res_member.getDevice_token());

            UserDao userDao = app.getUserDao();
            User user = new User();
            user.setId((long)res_member.getId());
            user.setAccount(res_member.getAccount());
            user.setEmail(res_member.getEmail());
            user.setApi_token(res_member.getApi_token());
            user.setDevice_token(res_member.getDevice_token());
            userDao.insert(user);

            Log.d("TAG", "Login success!");

            finish(); Intent intent = new Intent(this,MainActivity_.class);
            startActivity(intent);
            finish();

        }catch(HttpServerErrorException e){
            Log.d("TAG","ResponseEntitiy:"+ e);
        }




    }

    @UiThread
    void showError(String error){
        txtError.setText(error);
    }



}
