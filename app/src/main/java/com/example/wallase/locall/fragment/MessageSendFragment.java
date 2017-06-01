package com.example.wallase.locall.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wallase.locall.R;
import com.example.wallase.locall.view_group.MessageItemView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wallase on 2017/5/28.
 */
@EFragment(R.layout.fragment_message)
public class MessageSendFragment extends Fragment{


    private static final String TAG = MessageSendFragment.class.getSimpleName();

    @ViewById(R.id.map)
    MapView mapView;
    @ViewById(R.id.message)
    EditText edit_message;
    @ViewById(R.id.layout_message)
    MessageItemView layout_message;

    private GoogleMap googleMap;

    @AfterViews
    void init(){
//        layout_message.gone();
        layout_message.setVisibility(View.GONE);

        mapView.onCreate(null);
        mapView.onResume();


    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        setUpMap(new LatLng(getArguments().getDouble("lat"), getArguments().getDouble("lng")));
        Log.d(TAG, "onResume");
    }

    @Click(R.id.close)
    void close(){
        getActivity().finish();
    }

    @Click(R.id.send)
    void send(){
        Bundle args = new Bundle();
        MessageFriendFragment messageFriendFragment = new MessageFriendFragment_();
        args.putDouble("lat",getArguments().getDouble("lat"));
        args.putDouble("lng",getArguments().getDouble("lng"));
        args.putString("message", edit_message.getText().toString());
        messageFriendFragment.setArguments(args);
        showFragment(messageFriendFragment);
    }

    private void setUpMap(final LatLng location){
        if(googleMap == null){
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap gMap) {
                    googleMap = gMap;
                    googleMap.getUiSettings().setAllGesturesEnabled(false);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                    googleMap.addMarker(new MarkerOptions().position(location));
                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            return true;
                        }

                    });
                }
            });
        }
    }

    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.flContent, fragment);
        fragmentTransaction.hide(this);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
