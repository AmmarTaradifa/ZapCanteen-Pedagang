package com.example.pedagang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.pedagang.menu.AddFood;
import com.example.pedagang.menu.Home;
import com.example.pedagang.menu.ListMakanan;
import com.example.pedagang.menu.MoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
//    public static final String USER_ID = "user_id";
//    public static final String USER_EMAIL = "user_email";
//    public static final String USER_NAME = "user_name";
//    public static final String USER_PASSWORD = "user_password";


    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        Bundle bundle1 = getIntent().getExtras();

//        final String user_id = bundle1.getString(Login.USER_ID);


        loadfragment(new Home());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new Home();
                break;
            case R.id.navigation_food:
                fragment = new AddFood();
                break;
            case R.id.navigation_list:
                fragment = new ListMakanan();
                break;
            case R.id.navigation_more:
                fragment = new MoreFragment();
                break;
        }
        return loadfragment(fragment);
    }

    private boolean loadfragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.isi_content, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void logout(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });

    }


}

