package com.capstone.pakigsabotbusinessowner.Services;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;

public class EditFoodProdResto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food_prod_resto);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("editFoodProd") != null){
                Toast.makeText(getApplicationContext(), "data: " + bundle.getString("editFoodProd"),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}