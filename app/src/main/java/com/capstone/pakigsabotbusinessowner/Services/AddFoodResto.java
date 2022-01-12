package com.capstone.pakigsabotbusinessowner.Services;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;

public class AddFoodResto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_resto);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("addRestoFoodBtnServices") != null){
                Toast.makeText(getApplicationContext(), "data: " + bundle.getString("addRestoFoodBtnServices"),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}