package com.capstone.pakigsabotbusinessowner.NotificationAlerts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;

public class CustomerResched extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_resched);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("alertCR") != null){
                Toast.makeText(getApplicationContext(), "data: " + bundle.getString("alertCR"),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}