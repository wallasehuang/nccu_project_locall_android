package com.example.wallase.locall.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.wallase.locall.R;
import com.example.wallase.locall.app.MyApp;
import com.example.wallase.locall.green_dao.UserDao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;

/**
 * Created by wallase on 2017/5/31.
 */
@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends AppCompatActivity {

    @App
    MyApp app;



    @AfterViews
    void init(){

        if(checkLogin()){
            showActivity(MainActivity_.class);
        }else{
            showActivity(LoginActivity_.class);
        }
    }

     private boolean checkLogin(){
        UserDao userDao = app.getUserDao();

        if(userDao.count() > 0)
            return true;

         return false;
     }

    private void showActivity(final Class activity){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
              Intent intent = new Intent(WelcomeActivity.this,activity);
                startActivity(intent);
                finish();
            }
        },1500);
    }
}
