package com.capstone.pakigsabotbusinessowner.Services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;

public class ServicesResort extends AppCompatActivity {

    ImageView addRoomBtnServices, editRoomAzul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_resort);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("resortServices") != null){
                Toast.makeText(getApplicationContext(), "data: " + bundle.getString("resortServices"),
                        Toast.LENGTH_SHORT).show();
            }
        }

        //References:
        refs();

        addRoomBtnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoom();
            }
        });

        editRoomAzul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editRoom();
            }
        });
    }

    private void refs(){
        addRoomBtnServices = findViewById(R.id.addRoomBtnServices);
        editRoomAzul = findViewById(R.id.editRoomAzul);
    }

    private void addRoom(){
        Intent intent = new Intent(getApplicationContext(), AddServiceResort.class);
        startActivity(intent);
    }

    private void editRoom(){
        Intent intent = new Intent(getApplicationContext(), EditServiceResort.class);
        startActivity(intent);
    }
}