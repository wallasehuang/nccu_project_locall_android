package com.example.wallase.locall.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.wallase.locall.R;
import com.example.wallase.locall.activity.MessageActivity_;
import com.example.wallase.locall.api.MessageApi;
import com.example.wallase.locall.app.MyApp;
import com.example.wallase.locall.model.Message;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;

/**
 * Created by wallase on 2017/5/15.
 */

@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = MainFragment.class.getSimpleName();


    @App
    MyApp app;

    @RestService
    MessageApi messageApi;

    @ViewById(R.id.map)
    MapView mapView;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private GoogleMap googleMap;
    private Location myLocation;
    private HashMap<Marker, Integer> markers = new HashMap<Marker, Integer>();


//    private ClusterManager<MessageItem> clusterManager;

    @AfterViews
    void init() {
        mapView.onCreate(null);
        mapView.onResume();

        buildGoogleAPIClient();
        createLocationRequest();

//        getMessageMrak();
        Log.d(TAG, "afterView");
    }

    @Override
    public void onResume() {
        super.onResume();
        googleApiClient.connect();
        mapView.onResume();
        setUpMap();
        getMessageMrak();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient.isConnected())
            googleApiClient.disconnect();
        mapView.onPause();

        Log.d(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();

        Log.d(TAG, "onLowMemory");
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION))
            return;

        if (!hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION))
            return;


        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if(location == null){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        }else{
            handleNewLocation(location);
        }

        Log.d(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location){
        handleNewLocation(location);
    }

    @Background
    void getMessageMrak(){
        try{
            messageApi.setHeader("Authorization",app.getUser().getApi_token());
            Message[] message = messageApi.list();

            showMessageMark(message);
        }catch(HttpServerErrorException e){
            Log.d(TAG,"ResponseEntity:"+e);
        }
    }

    @Background
    void watchMessage(int message_id) {
        try {
            messageApi.setHeader("Authorization",app.getUser().getApi_token());
            Message req_message = new Message();
            req_message.setLatitude(String.valueOf(myLocation.getLatitude()));
            req_message.setLongitude(String.valueOf(myLocation.getLongitude()));
            req_message.setMessage_id(message_id);

            Message res_message = messageApi.watch(req_message);
            if(res_message.getStatus() == 1){
                Log.d(TAG,"你離訊息太遠了");
                showDialog("你離訊息太遠了");
                return;
            }
            Log.d(TAG,res_message.getMessage());

            Intent intent = new Intent(getActivity(), MessageActivity_.class);
            intent.putExtra("status","view");
            intent.putExtra("lat", res_message.getDoubleLatitude());
            intent.putExtra("lng", res_message.getDobleLongitude());
            intent.putExtra("message",res_message.getMessage());
            startActivity(intent);

        }catch (HttpServerErrorException e){
            Log.d(TAG,""+e);
        }
    }

    @UiThread
    void showMessageMark(Message[] message){
//        clusterManager = new ClusterManager<MessageItem>(getActivity(),googleMap);
//        googleMap.setOnCameraIdleListener(clusterManager);
////        googleMap.setOnMarkerClickListener(clusterManager);
//        for(Message mes : message){
//            Log.d(TAG,"add marker");
//            MessageItem messageItem = new MessageItem(mes.getDoubleLatitude(),mes.getDobleLongitude());
//            clusterManager.addItem(messageItem);
//        }
        googleMap.clear();
        for(Message mes :message){
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(mes.getDoubleLatitude(), mes.getDobleLongitude()))
                    .anchor((float)Math.random(),(float)Math.random());
            Marker marker = googleMap.addMarker(options);
            markers.put(marker,mes.getId());
        }
    }

    private void buildGoogleAPIClient(){
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    private void createLocationRequest(){
        if(locationRequest == null){
            locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)
                    .setFastestInterval(1 * 1000);
        }
    }


    private void setUpMap(){
        if(googleMap == null){
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap gMap) {
                    googleMap = gMap;
                    handleMapClick();
                    handleMarkerClick();
                }
            });
        }
    }

    private void handleNewLocation(Location location){
        myLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

//        MarkerOptions options = new MarkerOptions()
//                .position(latLng)
//                .title("I'm here");

        googleMap.setMyLocationEnabled(true);
//        googleMap.addMarker(options);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }

    private void handleMapClick(){
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG, latLng.toString());
                Intent intent = new Intent(getActivity(), MessageActivity_.class);
                intent.putExtra("status", "send");
                intent.putExtra("lat", latLng.latitude);
                intent.putExtra("lng", latLng.longitude);
                startActivity(intent);
//                getActivity().finish();
            }
        });
    }

    private void handleMarkerClick(){
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG, "marker click");
                Log.d(TAG, markers.get(marker).toString());
                watchMessage(markers.get(marker));
                return true;
            }

        });
    }

    @UiThread
    void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(message)
                .setTitle("通知！");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean hasPermission(String permission){
        return ContextCompat.checkSelfPermission(getActivity(),permission) == PackageManager.PERMISSION_GRANTED;
    }











}
