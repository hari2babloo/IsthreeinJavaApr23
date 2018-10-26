package com.a3x3conect.mobile.isthreeinjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hari.isthreeinjava.ChangeAddress;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.R;
import com.example.hari.isthreeinjava.Signin;
import com.example.hari.isthreeinjava.VerifyEmail;

public class NavDash extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_dash);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
//        View hView =  navigationView.getHeaderView(0);
//        TextView nav_user = (TextView)hView.findViewById(R.id.textView);
//        nav_user.setText(user);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_dash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navmyorder) {

            Intent intent = new Intent(NavDash.this, OrderHead.class);

            //           tinydb.putString("custid","");

            // tinydb.clear();
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.navwallet) {

        } else if (id == R.id.nav_slideshow) {

        }

//        else if (id == R.id.nav_manage) {
//
//        }


        else if (id == R.id.navchngaddr) {


            Intent intent = new Intent(NavDash.this, ChangeAddress.class);

            //           tinydb.putString("custid","");

            // tinydb.clear();
            startActivity(intent);


        } else if (id == R.id.navchngpass) {

            Intent intent = new Intent(NavDash.this, VerifyEmail.class);

            //           tinydb.putString("custid","");

            // tinydb.clear();
            startActivity(intent);

        } else if (id == R.id.navlogout) {


            Intent intent = new Intent(NavDash.this, Signin.class);

            //           tinydb.putString("custid","");


            TinyDB tinydb = new TinyDB(this);
            tinydb.clear();
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
