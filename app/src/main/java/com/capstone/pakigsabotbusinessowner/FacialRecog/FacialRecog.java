package com.capstone.pakigsabotbusinessowner.FacialRecog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.SignIn;

public class FacialRecog extends AppCompatActivity {

    Button enableNowBtn, maybeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facial_recog);

        //References::
        refs();

        maybeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInScreen();
            }
        });
    }

    public void refs(){
        enableNowBtn = findViewById(R.id.enableNowBtnFRSI);
        maybeBtn = findViewById(R.id.maybeBtnFRSI);
    }

    public void signInScreen(){
        Toast.makeText(FacialRecog.this, "Sign In Using Email Address", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), SignIn.class);
        startActivity(intent);
    }


}