package com.capstone.pakigsabotbusinessowner.Services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.capstone.pakigsabotbusinessowner.R;

public class ServicesInternetCCoworking extends AppCompatActivity {

    ImageView addBtnICServices, editIDPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_internet_cafe);

        //References
        refs();

        addBtnICServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addServicesIC();
            }
        });

        editIDPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editServicesIC();
            }
        });
    }

    public void refs(){
        addBtnICServices = findViewById(R.id.addBtnICServices);
        editIDPic = findViewById(R.id.editIDPic);
    }

    private void addServicesIC(){
        Intent intent = new Intent(getApplicationContext(), AddServiceInternetCafe.class);
        startActivity(intent);
    }

    private void editServicesIC(){
        Intent intent = new Intent(getApplicationContext(), EditServiceInternetCafe.class);
        startActivity(intent);
    }
}