package com.capstone.pakigsabotbusinessowner.Services;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;

public class EditServiceResto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service_resto);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("editIndoorD") != null){
                Toast.makeText(getApplicationContext(), "data: " + bundle.getString("editIndoorD"),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}