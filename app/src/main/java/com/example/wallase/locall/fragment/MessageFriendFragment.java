package com.example.wallase.locall.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.wallase.locall.R;
import com.example.wallase.locall.adapter.FriendListAdapter;
import com.example.wallase.locall.api.FriendApi;
import com.example.wallase.locall.api.MessageApi;
import com.example.wallase.locall.app.MyApp;
import com.example.wallase.locall.model.Friend;
import com.example.wallase.locall.model.Message;
import com.example.wallase.locall.model.Response;
import com.example.wallase.locall.view_group.FriendItemView;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wallase on 2017/5/28.
 */
@EFragment(R.layout.fragment_friend_send_list)
public class MessageFriendFragment extends Fragment{
    private static final String TAG = MessageFriendFragment.class.getSimpleName();

    @App
    MyApp app;

    @RestService
    FriendApi friendApi;
    @RestService
    MessageApi messageApi;

    @Bean
    FriendListAdapter friendListAdapter;

    @ViewById(R.id.listView)
    ListView listView;
    @ViewById(R.id.cancel)
    Button cancel;
    @ViewById(R.id.send)
    Button send;

    private List<Friend> list_friend;

    @AfterViews
    void init(){
        send.setVisibility(View.GONE);
        friendList();
    }

    @Click(R.id.cancel)
    void back(){
        getFragmentManager().popBackStack();
    }

    @Click(R.id.send)
    void send(){
        Message message = new Message();
        message.setLatitude(String.valueOf(getArguments().getDouble("lat")));
        message.setLongitude(String.valueOf(getArguments().getDouble("lng")));
        message.setMessage(getArguments().getString("message"));
        List list_reciver = new ArrayList();
        for(Friend friend : list_friend){
            if(friend.isCheck())
                list_reciver.add(friend.getId());
        }
        int[] reciver = new int[list_reciver.size()];
        for(int i = 0;i<list_reciver.size();i++){
            reciver[i] = (int)list_reciver.get(i);
        }
        message.setReciver(reciver);
        sendMessage(message);
    }

    @Background
    void friendList(){
        try{
            friendApi.setHeader("Authorization",app.getUser().getApi_token());
            list_friend = new ArrayList<Friend>();
            Friend self = new Friend();
            self.setId(app.getUser().getId().intValue());
            self.setAccount(app.getUser().getAccount());
            self.setDevice_token(app.getUser().getDevice_token());
            self.setEmail(app.getUser().getEmail());
            self.setType(4);
            list_friend.add(self);
            Friend[] friends = friendApi.list();
            for(Friend friend:friends){
                friend.setType(4);
            }
            list_friend.addAll(Arrays.asList(friends));
            showFriend(list_friend);
        }catch (HttpServerErrorException e){
            Log.d(TAG,""+e);
        }
    }

    @UiThread
    void showFriend(List<Friend> friend){
        friendListAdapter.setData(friend);
        listView.setAdapter(friendListAdapter);
    }

    @Background
    void sendMessage(Message message){
        try{
            messageApi.setHeader("Authorization",app.getUser().getApi_token());
            ResponseEntity<Response> entity = messageApi.send(message);

            if(entity == null){
                Log.d(TAG,"Something wrong!");
                return;
            }
            Response response = entity.getBody();
            if(response.getError() != null){
                Log.d(TAG,response.getError());
            }
            Log.d(TAG,response.getMessage());
            getActivity().finish();

        }catch (HttpServerErrorException e){
            Log.d(TAG,""+e);
        }
    }

    @ItemClick
    void listView(int postion){
        Friend friend = list_friend.get(postion);
        View itemView = listView.getChildAt(postion);
        CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.check);
        friend.setCheck(!friend.isCheck());
        checkBox.setChecked(friend.isCheck());

        for(Friend f:list_friend){
            if(f.isCheck()){
                send.setVisibility(View.VISIBLE);
                break;
            }else{
                send.setVisibility(View.GONE);
            }

        }
    }

}
