package com.example.wallase.locall.view_group;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wallase.locall.R;
import com.example.wallase.locall.model.Member;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wallase on 2017/5/21.
 */
@EViewGroup(R.layout.friend_list_item)
public class FriendItemView extends LinearLayout{

    @ViewById(R.id.account)
    TextView account;


    public FriendItemView(Context context) {
        super(context);
    }

    public void bind(Member member){
        this.account.setText(member.getAccount());
    }
}
