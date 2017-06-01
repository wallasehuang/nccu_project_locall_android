package com.example.wallase.locall.fragment;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wallase on 2017/5/29.
 */
@EFragment(R.layout.fragment_message)
public class MessageViewFragment extends Fragment {

    private static final String TAG = MessageViewFragment.class.getSimpleName();

    @ViewById(R.id.map)
    MapView mapView;
    @ViewById(R.id.message)
    EditText edit_message;
    @ViewById(R.id.layout_message)
    MessageItemView messageItem;
    @ViewById(R.id.send)
    FloatingActionButton btn_send;

    private GoogleMap googleMap;

    @AfterViews
    void init(){
        setupMessage();

        mapView.onCreate(null);
        mapView.onResume();
    }

    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
        Log.d(TAG, "lat:" + String.valueOf(getArguments().getDouble("lat")));
        Log.d(TAG, "lng:" + String.valueOf(getArguments().getDouble("lng")));
        setUpMap(new LatLng(getArguments().getDouble("lat"), getArguments().getDouble("lng")));
    }

    @Click(R.id.close)
    void close(){
        getActivity().finish();
    }

    @UiThread
    void setupMessage(){
        btn_send.setVisibility(View.GONE);
        edit_message.setVisibility(View.GONE);
        messageItem.bind(
                getArguments().getString("sender"),
                getArguments().getString("send_time"),
                getArguments().getString("message"));

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


}
