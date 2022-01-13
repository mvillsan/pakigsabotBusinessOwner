package com.capstone.pakigsabotbusinessowner.Services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.capstone.pakigsabotbusinessowner.R;

public class ServicesDentalClinic extends AppCompatActivity {

    ImageView addProcBtnServices, editCavityFilling, spaSalonServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_dental_clinic);

        //References:
        refs();

        addProcBtnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProcedure();
            }
        });

        editCavityFilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCavityProc();
            }
        });

        spaSalonServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spaSalonServicesScreen();
            }
        });
    }

    public void refs(){
        addProcBtnServices = findViewById(R.id.addProcBtnServices);
        editCavityFilling = findViewById(R.id.editCavityFilling);
        spaSalonServices = findViewById(R.id.imageView6);
    }

    private void addProcedure(){
        Intent intent = new Intent(getApplicationContext(), AddServiceDentalClinic.class);
        startActivity(intent);
    }

    private void editCavityProc(){
        Intent intent = new Intent(getApplicationContext(), EditProcedureDC.class);
        startActivity(intent);
    }

    private void spaSalonServicesScreen(){
        Intent intent = new Intent(getApplicationContext(), ServicesSpaSalon.class);
        startActivity(intent);
    }
}