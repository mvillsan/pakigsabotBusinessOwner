package com.capstone.pakigsabotbusinessowner.Resort;

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

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Resort.Adapters.ResortFacilityAdapter;
import com.capstone.pakigsabotbusinessowner.Resort.Models.ResortFacilityModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ResortFacilities extends AppCompatActivity {

    //Initialize Variables
    ImageView backBtnFaciResort, addFaciBtnServices;

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore,
    // and our progress bar.
    // initializing our variable for firebase
    // firestore and getting its instance.
    RecyclerView resortFaciRecyclerView;
    ArrayList<ResortFacilityModel> resortFArrayList;
    ResortFacilityAdapter resortFAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID;
    ProgressDialog progressDialog;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resort_facilities);

        //References:
        refs();

        //recyclerview initialization
        resortFaciRecyclerView = findViewById(R.id.resortFaciRecyclerView);
        resortFaciRecyclerView.setHasFixedSize(true);
        resortFaciRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        fStore=FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        resortFArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        resortFArrayList = new ArrayList<ResortFacilityModel>();

        //initializing the adapter
        resortFAdapter = new ResortFacilityAdapter(ResortFacilities.this, resortFArrayList);

        resortFaciRecyclerView.setAdapter(resortFAdapter);

        // below line is use to get the data from Firebase Firestore.
        getResortFacilityList();

        //Refresh the recyclerview::
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getResortFacilityList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        backBtnFaciResort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingUp();
            }
        });

        addFaciBtnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoom();
            }
        });
    }

    private void refs(){
        addFaciBtnServices = findViewById(R.id.addFaciBtnServices);
        backBtnFaciResort = findViewById(R.id.backBtnFaciResort);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
    }

    private void addRoom(){
        Intent intent = new Intent(getApplicationContext(), AddFacilityResort.class);
        startActivity(intent);
    }

    private void settingUp(){
        Intent intent = new Intent(getApplicationContext(), SettingUpEstablishmentResort.class);
        startActivity(intent);
    }

    //Display Resort Rooms List
    private void getResortFacilityList(){
        //Progress::
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        fStore.collection("establishments").document(userID).collection("resort-facilities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        resortFArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            ResortFacilityModel list = new ResortFacilityModel(doc.getString("resortFID"),
                                    doc.getString("resortFName"),
                                    doc.getString("resortFCapac"),
                                    doc.getString("resortFDesc"),
                                    doc.getString("resortFRate"),
                                    doc.getString("resortFImgUrl"));
                            resortFArrayList.add(list);
                        }
                        resortFAdapter = new ResortFacilityAdapter(ResortFacilities.this,resortFArrayList);
                        // setting adapter to our recycler view.
                        resortFaciRecyclerView.setAdapter(resortFAdapter);
                        resortFAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ResortFacilities.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}