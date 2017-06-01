package com.example.wallase.locall.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by wallase on 2017/5/31.
 */
public class NetworkReceiver extends BroadcastReceiver {

    private static final String TAG = "network receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            Log.d(TAG,"connectivity action");

            NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            if(networkInfo != null){
                if(networkInfo.isConnected()){
                    if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                        Log.d(TAG,"使用 wifi 網路連線");
                    }else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                        Log.d(TAG,"使用 mobile 網路連線");
                    }
                }else{
                    Log.d(TAG,"目前沒有網路");
                }
            }else{
                Log.d(TAG,"目前沒有網路");
            }
        }

        Log.d(TAG,"no action");
    }
}
