package com.capstone.pakigsabotbusinessowner.CancelReservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.pakigsabotbusinessowner.Profile.Profile;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.SignIn;

public class CancellationSuccess extends AppCompatActivity {

    ImageView profile,signout, close, detailRect, check;
    TextView successTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation_success);

        refs();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signout();
            }
        });
    }

    public void refs(){
        profile = findViewById(R.id.profileBtnCancelSuccess);
        signout = findViewById(R.id.signoutCancelSuccess);
        close = findViewById(R.id.closeImageViewCancelSuccess);
        detailRect = findViewById(R.id.detail_rectCancelSuccess);
        check = findViewById(R.id.checkImageView);
        successTxt = findViewById(R.id.cancelSuccessTxt);
    }

    public void onBackPressed(){
        super.onBackPressed();
    }

    public void profile(){
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        startActivity(intent);
    }

    public void signout(){
        Intent intent = new Intent(getApplicationContext(), SignIn.class);
        startActivity(intent);
    }

    public void close(){
        detailRect.setVisibility(View.INVISIBLE);
        close.setVisibility(View.INVISIBLE);
        check.setVisibility(View.INVISIBLE);
        successTxt.setVisibility(View.INVISIBLE);
    }
}