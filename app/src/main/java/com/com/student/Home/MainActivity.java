package com.com.student.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import android.os.Bundle;
import android.view.View;

import com.com.student.R;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class MainActivity extends AppCompatActivity {
    //Meow Bottom  filed
    MeowBottomNavigation mMeowNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // hi hamo
        //Hi My Friend...to day is 5/1/20 i will set add meow button nav an navDrawer and fragment home & book & quiz
        iniView(); ///find filed view
        addMeowNav(); // add button nav

    }


    //Start find View filed ....
    private void iniView() {
        mMeowNavigation = findViewById(R.id.meow_bottom);
    }//end find View filed ..


    //Start add meow
    //her ypu will find button nav in cntroal it
    private void addMeowNav() {

        //الكود يستخد للتحكم ف الفريجمنت والانتقال فيما بينهم
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_contr);
        NavController navController = navHostFragment.getNavController();

        //add nav and give id and icon
        mMeowNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.homestudent));
        mMeowNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.openbook));
        mMeowNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.educationquiz));


        //on click in item
        mMeowNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 1 :
                        navController.navigate(R.id.homeFragment);
                        break;
                    case 2 :
                        navController.navigate(R.id.bookFragment);
                        break;
                    case 3 :
                        navController.navigate(R.id.quizFragment);
                        break;
                }
            }
        });

        //show bottom nav
        mMeowNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

            }
        });
    }//end add meow


  // method to finish app.............
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}