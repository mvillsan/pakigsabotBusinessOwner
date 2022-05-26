package com.example.pakigsabot.DentalAndEyeClinics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.pakigsabot.EmailMessage.EmailMessageLayout;
import com.example.pakigsabot.EmailMessage.EstReservationSuccess.EmailEst;
import com.example.pakigsabot.NavigationFragments.EstFavoritesFragment;
import com.example.pakigsabot.NavigationFragments.HelpCenterFragment;
import com.example.pakigsabot.NavigationFragments.HomeFragment;
import com.example.pakigsabot.NavigationFragments.NearbyFragment;
import com.example.pakigsabot.NavigationFragments.ReservationsFragment;
import com.example.pakigsabot.R;

public class EyeClinicReservationSuccess extends AppCompatActivity {

    //Initialize Variables
    MeowBottomNavigation bottomNavigation;
    ImageView backBtn;
    TextView gotoReservationTxt, gotoHelpCenterTxt, emailEyeClinicTxt;
    String estID, estName, estEmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_clinic_reservation_success);

        refs();

        //Getting data from recyclerview::
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            estID = extra.getString("EstablishmentID");
            estName = extra.getString("EstablishmentName");
            estEmailAddress = extra.getString("EstablishmentEmail");
        }

        gotoReservationTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reservationsFragment();
            }
        });

        gotoHelpCenterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpcenterFragment();
            }
        });

        emailEyeClinicTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailMessageScreen();
            }
        });


    }

    public void refs(){
        gotoReservationTxt = findViewById(R.id.gotoReservationTxt);
        gotoHelpCenterTxt = findViewById(R.id.gotoHelpCenterTxt);
        emailEyeClinicTxt = findViewById(R.id.emailEyeClinicTxt);

    }

    private void emailMessageScreen(){
        Intent intent = new Intent(getApplicationContext(), EmailEst.class);
        intent.putExtra("EstablishmentID", estID);
        intent.putExtra("EstablishmentName", estName);
        intent.putExtra("EstablishmentEmail", estEmailAddress);
        startActivity(intent);
    }

    private void helpcenterFragment(){
        setContentView(R.layout.activity_bottom_navigation);
        //Bottom Nav
        //Assign variable
        bottomNavigation = findViewById(R.id.bottom_nav);

        //Add menu item
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_nearby));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_reserve));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_favorites));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.ic_help));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //Initialize fragment
                Fragment fragment = null;

                //Check condition
                switch (item.getId()){
                    case 1: //When id is 1, initialize nearby fragment
                        fragment = new NearbyFragment();
                        break;

                    case 2: //When id is 2, initialize reservations fragment
                        fragment = new ReservationsFragment();
                        break;

                    case 3: //When id is 3, initialize home fragment
                        fragment = new HomeFragment();
                        break;

                    case 4: //When id is 4, initialize favorites fragment
                        fragment = new EstFavoritesFragment();
                        break;

                    case 5: //When id is 5, initialize help center fragment
                        fragment = new HelpCenterFragment();
                        break;
                }
                //Load fragment
                loadFragment(fragment);
            }
        });

        /*//Set notification count
        bottomNavigation.setCount(3,"10");*/
        //Set help center fragment initially selected
        bottomNavigation.show(5,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
            }
        });
    }

    private void reservationsFragment(){
        setContentView(R.layout.activity_bottom_navigation);
        //Bottom Nav
        //Assign variable
        bottomNavigation = findViewById(R.id.bottom_nav);

        //Add menu item
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_nearby));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_reserve));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_favorites));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.ic_help));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //Initialize fragment
                Fragment fragment = null;

                //Check condition
                switch (item.getId()){
                    case 1: //When id is 1, initialize nearby fragment
                        fragment = new NearbyFragment();
                        break;

                    case 2: //When id is 2, initialize reservations fragment
                        fragment = new ReservationsFragment();
                        break;

                    case 3: //When id is 3, initialize home fragment
                        fragment = new HomeFragment();
                        break;

                    case 4: //When id is 4, initialize favorites fragment
                        fragment = new EstFavoritesFragment();
                        break;

                    case 5: //When id is 5, initialize help center fragment
                        fragment = new HelpCenterFragment();
                        break;
                }
                //Load fragment
                loadFragment(fragment);
            }
        });

        /*//Set notification count
        bottomNavigation.setCount(3,"10");*/
        //Set reservations fragment initially selected
        bottomNavigation.show(2,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        //Replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();
    }

}