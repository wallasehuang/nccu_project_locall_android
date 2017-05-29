package com.example.wallase.locall.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wallase.locall.R;
import com.example.wallase.locall.adapter.FriendListAdapter;
import com.example.wallase.locall.api.FriendApi;
import com.example.wallase.locall.app.MyApp;
import com.example.wallase.locall.green_dao.User;
import com.example.wallase.locall.green_dao.UserDao;
import com.example.wallase.locall.model.Friend;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.Arrays;
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

    @ViewById(R.id.message)
    TextView txt_message;

    private UserDao userDao;
    private User user;

    @AfterViews
    void init(){
        user = app.getUser();
        friendList();
    }

    @Click
    void find_friend(){
        showFragment(new FriendFindFragment_());
    }


    @Background
    void friendList(){
        try{
            friendApi.setHeader("Authorization", user.getApi_token());
            List<Friend> list_friend = new ArrayList<Friend>();


            Friend[] friendByInvitee = friendApi.listByInvitee();
            Friend[] friendByInviter = friendApi.listByInviter();
            Friend[] friend = friendApi.list();

            list_friend.addAll(Arrays.asList(friendByInvitee));
            list_friend.addAll(Arrays.asList(friendByInviter));
            list_friend.addAll(Arrays.asList(friend));
            showFriend(list_friend);
        }catch (HttpServerErrorException e){
            Log.d("TAG", "ResponseEntity:" + e);
        }
    }

    @UiThread
    void showFriend(List<Friend> friend){
        if(friend.isEmpty()){
            txt_message.setText("目前沒有朋友");
        }
        friendListAdapter.setData(friend);
        listView.setAdapter(friendListAdapter);
    }

    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @ItemClick
    void listView(Friend friend){
        Log.d("TAG",friend.getAccount());
    }

}
