package com.capstone.pakigsabotbusinessowner.Services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.capstone.pakigsabotbusinessowner.R;

public class ServicesSpaSalon extends AppCompatActivity {

    ImageView addBtnSSServices,editBodyMassage,internetCafeServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_spa_salon);

        //References:
        refs();

        addBtnSSServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addServiceSpaSalon();
            }
        });

        editBodyMassage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editServiceSpaSalon();
            }
        });

        internetCafeServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetCafeServicesScreen();
            }
        });
    }

    public void refs(){
        addBtnSSServices = findViewById(R.id.addBtnSSServices);
        editBodyMassage = findViewById(R.id.editBodyMassage);
        internetCafeServices = findViewById(R.id.imageView6);
    }

    private void addServiceSpaSalon(){
        Intent intent = new Intent(getApplicationContext(), AddServiceSpaSalon.class);
        startActivity(intent);
    }

    private void editServiceSpaSalon(){
        Intent intent = new Intent(getApplicationContext(), EditServiceSpaSalon.class);
        startActivity(intent);
    }

    private void internetCafeServicesScreen(){
        Intent intent = new Intent(getApplicationContext(), ServicesInternetCafe.class);
        startActivity(intent);
    }
}