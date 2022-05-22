package com.capstone.pakigsabotbusinessowner.SignUpRequirement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.capstone.pakigsabotbusinessowner.MonthlySubscription.PayMonthlySub;
import com.capstone.pakigsabotbusinessowner.R;

public class ClassicSubsTerms extends AppCompatActivity {

    //Initialization of variables::
    ImageView backBtnSU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_subs_terms);

        //Referencess::
        refs();

        //Back to Payment Subscription Screen::
        backBtnSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentSub();
            }
        });
    }

    private void refs(){
        backBtnSU = findViewById(R.id.backBtnSU);
    }

    private void paymentSub(){
        Intent intent = new Intent(getApplicationContext(), PaySubscription.class);
        startActivity(intent);
    }
}