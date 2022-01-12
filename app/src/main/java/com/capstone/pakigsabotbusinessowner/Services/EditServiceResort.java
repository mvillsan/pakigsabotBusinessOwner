package com.capstone.pakigsabotbusinessowner.Services;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;

public class EditServiceResort extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service_resort);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("editRoom") != null){
                Toast.makeText(getApplicationContext(), "data: " + bundle.getString("editRoom"),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}