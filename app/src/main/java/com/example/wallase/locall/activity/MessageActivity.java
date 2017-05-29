package com.example.wallase.locall.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.wallase.locall.R;
import com.example.wallase.locall.fragment.MainFragment;
import com.example.wallase.locall.fragment.MessageSendFragment;
import com.example.wallase.locall.fragment.MessageSendFragment_;
import com.example.wallase.locall.fragment.MessageViewFragment;
import com.example.wallase.locall.fragment.MessageViewFragment_;
import com.example.wallase.locall.model.Message;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by wallase on 2017/5/28.
 */
@EActivity(R.layout.activity_message)
public class MessageActivity extends AppCompatActivity {
    private static final String TAG = MessageActivity.class.getSimpleName();

    @AfterViews
    void init(){
        Bundle passData = getIntent().getExtras();

        switch (passData.getString("status")){
            case "view":
                MessageViewFragment messageViewFragment = new MessageViewFragment_();
                messageViewFragment.setArguments(passData);
                showFragment(messageViewFragment);
                break;
            case "send":
                MessageSendFragment messageSendFragment = new MessageSendFragment_();
                messageSendFragment.setArguments(passData);
                showFragment(messageSendFragment);
                break;
        }
    }

    private void showFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent,fragment)
                .commit();
    }
}
