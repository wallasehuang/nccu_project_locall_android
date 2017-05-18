package com.example.wallase.locall.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.example.wallase.locall.R;
import com.example.wallase.locall.api.Auth;
import com.example.wallase.locall.model.Member;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

/**
 * Created by wallase on 2017/5/8.
 */

@EActivity(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {
    @RestService
    Auth auth;

    @ViewById(R.id.account)
    EditText editAccount;
    @ViewById(R.id.email)
    EditText editEmail;
    @ViewById(R.id.password)
    EditText editPassword;


    @Click
    void btnLinkToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity_.class);
        startActivity(intent);
        finish();
    }

    @Click
    void btnRegister() {

        String account = editAccount.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        apiRegister(account, password, email);

    }


    @Background
    void apiRegister(String account, String password, String email) {

        Member req_member = new Member();
        req_member.setAccount(account);
        req_member.setPassword(password);
        req_member.setEmail(email);

        try {
            ResponseEntity<Member> entity = auth.register(req_member);

            if(entity == null){
                Log.d("Tag","something wrong!");
                return;
            }

            Member res_member = entity.getBody();

            if(res_member.getErrors() != null){
                for(String error : res_member.getErrors()) {
                    Log.d("TAG","error: "+ error);
                }
                return;
            }

            Log.d("TAG", "id:" + res_member.getId());
            Log.d("TAG", "account:" + res_member.getAccount());
            Log.d("TAG", "email:" + res_member.getEmail());

            Intent intent = new Intent(this, LoginActivity_.class);
            startActivity(intent);
            finish();


        } catch (HttpServerErrorException e) {
            Log.d("TAG", "ResponseEntitiy:" + e);
        }


    }
}
