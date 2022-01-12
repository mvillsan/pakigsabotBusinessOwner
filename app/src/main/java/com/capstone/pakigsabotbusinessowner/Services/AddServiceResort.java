package com.capstone.pakigsabotbusinessowner.Services;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;

public class AddServiceResort extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_resort);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("addRoom") != null){
                Toast.makeText(getApplicationContext(), "data: " + bundle.getString("addRoom"),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}