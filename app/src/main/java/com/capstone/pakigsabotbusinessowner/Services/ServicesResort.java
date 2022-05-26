package com.capstone.pakigsabotbusinessowner.Services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

//import com.capstone.pakigsabotbusinessowner.Adapter.ImageAdapter;
import com.capstone.pakigsabotbusinessowner.R;
//import com.capstone.pakigsabotbusinessowner.Resort.ImgsActivity;
import com.capstone.pakigsabotbusinessowner.Resort.AddRoomResort;
import com.capstone.pakigsabotbusinessowner.Resort.Adapters.ResortRoomAdapter;
import com.capstone.pakigsabotbusinessowner.Resort.Models.ResortRoomModel;
import com.capstone.pakigsabotbusinessowner.Resort.SettingUpEstablishmentResort;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ServicesResort extends AppCompatActivity {
    //Initialize Variables
    ImageView backBtnServicesResort, addRoomBtnServices, imageView6;

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore,
    // and our progress bar.
    // initializing our variable for firebase
    // firestore and getting its instance.
    RecyclerView roomRecyclerView;
    ArrayList<ResortRoomModel> roomArrayList;
    ResortRoomAdapter roomAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID;
    ProgressDialog progressDialog;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_resort);

        //References:
        refs();

        //recyclerview initialization
        roomRecyclerView = findViewById(R.id.resortsServicesRV);
        roomRecyclerView.setHasFixedSize(true);
        roomRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        fStore=FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        roomArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        roomArrayList = new ArrayList<ResortRoomModel>();

        //initializing the adapter
        roomAdapter = new ResortRoomAdapter(ServicesResort.this, roomArrayList);

        roomRecyclerView.setAdapter(roomAdapter);

        // below line is use to get the data from Firebase Firestore.
        getResortRoomList();

        //Refresh the recyclerview::
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getResortRoomList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        backBtnServicesResort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingUp();
            }
        });

        addRoomBtnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoom();
            }
        });
    }

    private void refs(){
        addRoomBtnServices = findViewById(R.id.addRoomBtnServices);
        backBtnServicesResort = findViewById(R.id.backBtnServicesResort);
        imageView6 = findViewById(R.id.imageView6);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
    }

    private void settingUp(){
        Intent intent = new Intent(getApplicationContext(), SettingUpEstablishmentResort.class);
        startActivity(intent);
    }

    private void addRoom(){
        Intent intent = new Intent(getApplicationContext(), AddRoomResort.class);
        startActivity(intent);
    }

    //Display Resort Rooms List
    private void getResortRoomList(){
        //Progress::
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        fStore.collection("establishments").document(userID).collection("resort-rooms")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        roomArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            ResortRoomModel list = new ResortRoomModel(doc.getString("resortRFID"),
                                    doc.getString("resortRFName"),
                                    doc.getString("resortCapac"),
                                    doc.getString("resortDesc"),
                                    doc.getString("resortRate"),
                                    doc.getString("resortImgUrl"),
                                    doc.getString("estID"));
                            roomArrayList.add(list);
                        }
                        roomAdapter = new ResortRoomAdapter(ServicesResort.this,roomArrayList);
                        // setting adapter to our recycler view.
                        roomRecyclerView.setAdapter(roomAdapter);
                        roomAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ServicesResort.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}