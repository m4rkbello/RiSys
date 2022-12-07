package com.softwaresolution.water_irrigation.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.softwaresolution.water_irrigation.Pojo.UserAccount;
import com.softwaresolution.water_irrigation.R;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
//        , Home.IHome
{
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        initProfile();

    }

    private void initProfile() {
        View headerView = navigationView.getHeaderView(0);
        TextView txt_email = (TextView) headerView.findViewById(R.id.txt_email);
        TextView txt_name = (TextView ) headerView.findViewById(R.id.txt_name);
        ImageView img_profile = (ImageView) headerView.findViewById(R.id.imageView);

        UserAccount userAccount = LoginForm.userProfile;
        txt_name.setText(userAccount.getName());
        txt_email.setText(userAccount.getEmail());
        Glide.with(MainActivity.this)
                .load(userAccount.getImg_url())
                .centerCrop()
                .into(img_profile);
    }

    private void init() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                new Home()).commit();

        navigationView =(NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.nav_scheduling:
                startActivity(new Intent(MainActivity.this, Schedule.class));
                break;
            case R.id.nav_control:
                startActivity(new Intent(MainActivity.this, Control.class));
                break;
//            case R.id.nav_profile:
//                startActivity(new Intent(MainActivity.this, RegisterForm.class)
//                .putExtra("action","update")
//                .putExtra("user",new Gson().toJson(LoginForm.userProfile)));
//                break;
//            case R.id.nav_monitor:
//                startActivity(new Intent(MainActivity.this, Activities.class));
//                break;
            case R.id.nav_report:
                startActivity(new Intent(MainActivity.this, History.class));
                break;
            case R.id.nav_about:
                startActivity(new Intent(MainActivity.this, About.class));
                break;
            case R.id.nav_logout:
                this.finish();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginForm.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

//    @Override
//    public void onClickMenu() {
//        drawerLayout.openDrawer(GravityCompat.END);
//    }

    @Override
    public void onBackPressed() {
        if (false)
            super.onBackPressed();
    }
}
