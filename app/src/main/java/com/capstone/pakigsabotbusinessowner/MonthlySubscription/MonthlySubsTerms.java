package com.capstone.pakigsabotbusinessowner.MonthlySubscription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.capstone.pakigsabotbusinessowner.R;

public class MonthlySubsTerms extends AppCompatActivity {

    //Initialization of variables::
    ImageView backBtnSU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_subs_terms);

        //Referencess::
        refs();

        //Back to Payment Subscription Screen::
        backBtnSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthlyPaymentSub();
            }
        });
    }

    private void refs(){
        backBtnSU = findViewById(R.id.backBtnSU);
    }

    private void monthlyPaymentSub(){
        Intent intent = new Intent(getApplicationContext(), PayMonthlySub.class);
        startActivity(intent);
    }

}