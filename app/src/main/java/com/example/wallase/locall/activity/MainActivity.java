package com.example.wallase.locall.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import com.example.wallase.locall.api.AuthApi;
import com.example.wallase.locall.app.MyApp;
import com.example.wallase.locall.fragment.FriendListFragment_;
import com.example.wallase.locall.fragment.MainFragment_;
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
    AuthApi authApi;

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

        showFragment(new MainFragment_());

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_main) {

            showFragment(new MainFragment_());
        } else if (id == R.id.nav_friend) {
            showFragment(new FriendListFragment_());
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

        showUserInfo(user.getAccount(), user.getEmail());
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
            authApi.setHeader("Authorization",user.getApi_token());
            ResponseEntity<Response> entitiy = authApi.logout();

            Log.d("TAG", "api_token: " + user.getApi_token());

            if(entitiy == null){
                Log.d("TAG","Something wrong!");
                return;
            }

            Response res = entitiy.getBody();

            if(res.getError() != null){
                Log.d("TAG","error: "+ res.getError());
                return;
            }

            Log.d("TAG","message: " + res.getMessage());

        }catch(HttpServerErrorException e){
            Log.d("TAG","ResponseEntitiy:"+ e);
        }

        userDao.delete(user);

        Intent intent = new Intent(this, LoginActivity_.class);
        startActivity(intent);
        finish();

    }

    private void showFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent,fragment)
                .commit();
    }



}
