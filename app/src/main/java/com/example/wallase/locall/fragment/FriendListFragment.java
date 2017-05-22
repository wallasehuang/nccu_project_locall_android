package com.example.wallase.locall.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

import com.example.wallase.locall.R;
import com.example.wallase.locall.adapter.FriendListAdapter;
import com.example.wallase.locall.api.FriendApi;
import com.example.wallase.locall.app.MyApp;
import com.example.wallase.locall.green_dao.User;
import com.example.wallase.locall.green_dao.UserDao;
import com.example.wallase.locall.model.Member;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

/**
 * Created by wallase on 2017/5/21.
 */
@EFragment(R.layout.fragment_friend_list)
public class FriendListFragment extends Fragment{

    @App
    MyApp app;

    @RestService
    FriendApi friendApi;


    @Bean
    FriendListAdapter friendListAdapter;

    @ViewById(R.id.listView)
    ListView listView;

    private UserDao userDao;
    private User user;

    @AfterViews
    void init(){
        userDao = app.getUserDao();
        user = userDao.queryBuilder().limit(1).unique();
        friendList();
    }


    @Background
    void friendList(){
        try{
            friendApi.setHeader("Authorization", user.getApi_token());
            List<Member> members = friendApi.list();
            showFriend(members);
        }catch (HttpServerErrorException e){
            Log.d("TAG", "RespnseEntity:" + e);
        }
    }

    @UiThread
    void showFriend(List<Member> members){
        friendListAdapter.setData(members);
        listView.setAdapter(friendListAdapter);
    }


}
