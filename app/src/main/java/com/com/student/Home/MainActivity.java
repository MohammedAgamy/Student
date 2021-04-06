package com.com.student.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.com.student.Activity.PostsActivity;
import com.com.student.R;
import com.com.student.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final float END_SCALE = 0.85f;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavView;
    private CoordinatorLayout contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initToolbar();
        initNavigation();
    }
        private void initToolbar () {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        }


        private void initNavigation() {

            drawer = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            bottomNavView = findViewById(R.id.bottom_nav_view);
            contentView = findViewById(R.id.content_view);


            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home,
                    R.id.nav_share, R.id.nav_send,
                    R.id.bottom_home, R.id.bottom_book, R.id.bottom_quaiz)
                    .setDrawerLayout(drawer)
                    .build();
            navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

            NavigationUI.setupWithNavController(navigationView, navController);
            NavigationUI.setupWithNavController(bottomNavView, navController);


            animateNavigationDrawer();

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                    new HomeFragment()).commit();

                        case R.id.nav_share:
                            Toast.makeText(MainActivity.this, "share", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.nav_post:
                            Intent intent=new Intent(MainActivity.this, PostsActivity.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "posts", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
        }


        private void animateNavigationDrawer() {

            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                    // Scale the View based on current slide offset
                    final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                    final float offsetScale = 1 - diffScaledOffset;
                    contentView.setScaleX(offsetScale);
                    contentView.setScaleY(offsetScale);

                    // Translate the View, accounting for the scaled width
                    final float xOffset = drawerView.getWidth() * slideOffset;
                    final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                    final float xTranslation = xOffset - xOffsetDiff;
                    contentView.setTranslationX(xTranslation);
                }
            });
        }




        @Override
        public boolean onSupportNavigateUp() {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                    || super.onSupportNavigateUp();
        }


        @Override
        public void onBackPressed() {

            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }

        }

    }


