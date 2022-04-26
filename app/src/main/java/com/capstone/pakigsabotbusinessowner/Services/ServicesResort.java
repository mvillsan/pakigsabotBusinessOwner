package com.capstone.pakigsabotbusinessowner.Services;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

//import com.capstone.pakigsabotbusinessowner.Adapter.ImageAdapter;
import com.capstone.pakigsabotbusinessowner.R;
//import com.capstone.pakigsabotbusinessowner.Resort.ImgsActivity;
import com.capstone.pakigsabotbusinessowner.Upload.UploadResortImgDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ServicesResort extends AppCompatActivity {

    ImageView backBtnServicesResort, addRoomBtnServices, imageView6;

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

        backBtnServicesResort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addRoomBtnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoom();
            }
        });

       /* imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayImg();
            }
        });*/

//        editRoomAzul.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editRoom();
//            }
//        });
    }

    private void refs(){
        addRoomBtnServices = findViewById(R.id.addRoomBtnServices);
//        editRoomAzul = findViewById(R.id.editRoomAzul);
        backBtnServicesResort = findViewById(R.id.backBtnServicesResort);
        imageView6 = findViewById(R.id.imageView6);
    }

    public void onBackPressed(){
        super.onBackPressed();
    }

    private void addRoom(){
        Intent intent = new Intent(getApplicationContext(), AddServiceResort.class);
        startActivity(intent);
    }

//    private void editRoom(){
//        Intent intent = new Intent(getApplicationContext(), EditServiceResort.class);
//        startActivity(intent);
//    }

  /*  private void displayImg(){
        Intent intent = new Intent(getApplicationContext(), ImgsActivity.class);
        startActivity(intent);
    }*/
}