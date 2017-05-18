package com.example.wallase.locall.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.wallase.locall.R;
import com.example.wallase.locall.api.Auth;
import com.example.wallase.locall.app.MyApp;
import com.example.wallase.locall.fragment.MainFragment_;
import com.example.wallase.locall.fragment.TestFragment_;
import com.example.wallase.locall.green_dao.User;
import com.example.wallase.locall.green_dao.UserDao;
import com.example.wallase.locall.model.Response;

import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener{


    @App
    MyApp app;

    @RestService
    Auth auth;

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawer;
    @ViewById(R.id.nav_view)
    NavigationView navigationView;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    private TextView txtAccount,txtEmail;


    private UserDao userDao;
    private User user;

    @AfterViews
    void init(){
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        userInfo();

        Fragment main_fragment = new MainFragment_();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent,main_fragment).commit();

        navigationView.setNavigationItemSelectedListener(this);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_main) {
            Fragment main_fragment = new TestFragment_();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent,main_fragment).commit();
        } else if (id == R.id.nav_friend) {
            Log.d("TAG","id = friend");
        } else if (id == R.id.nav_logout) {
            logout(user);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Background
    void userInfo(){
        userDao = app.getUserDao();
        user = userDao.queryBuilder().limit(1).unique();

        showUserInfo(user.getAccount(),user.getEmail());
    }

    @UiThread
    void showUserInfo(String account, String email){
        View header = navigationView.getHeaderView(0);
        txtAccount = (TextView)header.findViewById(R.id.account);
        txtEmail = (TextView)header.findViewById(R.id.email);
        txtAccount.setText(account);
        txtEmail.setText(email);
    }


    @Background
    void logout(User user){
        try{
            auth.setHeader("Authorization",user.getApi_token());
            ResponseEntity<Response> entitiy = auth.logout();
            Log.d("TAG","api_token: "+ user.getApi_token());

            if(entitiy == null){
                Log.d("TAG","Something wrong!");
                return;
            }

            Response res = entitiy.getBody();

            if(res.getErrors() != null){
                for(String error: res.getErrors()){
                    Log.d("TAG","error: "+ error);
                }
                return;
            }

            for(String message: res.getMessages()){
                Log.d("TAG","message: " + message);
            }

        }catch(HttpServerErrorException e){
            Log.d("TAG","ResponseEntitiy:"+ e);
        }

        userDao.delete(user);

        Intent intent = new Intent(this, LoginActivity_.class);
        startActivity(intent);
        finish();

    }



}
