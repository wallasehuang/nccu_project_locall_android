package com.example.wallase.locall.view_group;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wallase.locall.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wallase on 2017/6/1.
 */
@EViewGroup(R.layout.message_item)
public class MessageItemView extends CardView{

    @ViewById(R.id.message_show)
    TextView txt_message;
    @ViewById(R.id.account)
    TextView txt_account;
    @ViewById(R.id.time)
    TextView txt_time;

    public MessageItemView(Context context) {
        super(context);
    }

    public MessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context);
    }

    public void bind(String account,String time,String message){
        this.txt_account.setText(account);
        this.txt_time.setText(time);
        this.txt_message.setText(message);
    }

}
