package com.example.wallase.locall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.wallase.locall.model.Member;
import com.example.wallase.locall.view_group.FriendItemView;
import com.example.wallase.locall.view_group.FriendItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by wallase on 2017/5/21.
 */
@EBean
public class FriendListAdapter extends BaseAdapter{

    List<Member> members;

    @RootContext
    Context context;

    public void setData(List<Member> members){
        this.members = members;
    }


    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Member getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FriendItemView friendItemView;
        if(convertView == null){
            friendItemView = FriendItemView_.build(context);
        } else {
            friendItemView = (FriendItemView) convertView;
        }
        friendItemView.bind(getItem(position));
        return friendItemView;
    }
}
