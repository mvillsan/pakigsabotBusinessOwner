package com.capstone.pakigsabotbusinessowner.Resort;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Resort.Adapters.ResortPromoAndDealsAdapter;
import com.capstone.pakigsabotbusinessowner.Resort.Models.ResortPromoAndDealsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PromoAndDealsResort extends AppCompatActivity {
    ImageView addPADBtn, backPADBtn;
    RecyclerView promoAndDealsRecyclerView;
    ArrayList<ResortPromoAndDealsModel> resortPADArrayList;
    ResortPromoAndDealsAdapter resortPADAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_and_deals_resort);

        References:
        refs();

        //recyclerview initialization
        promoAndDealsRecyclerView = findViewById(R.id.promoAndDealsRecyclerView);
        promoAndDealsRecyclerView.setHasFixedSize(true);
        promoAndDealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adding array list to recyclerview adapter
        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        resortPADArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        resortPADArrayList = new ArrayList<ResortPromoAndDealsModel>();

        //initializing the adapter
        resortPADAdapter = new ResortPromoAndDealsAdapter(PromoAndDealsResort.this, resortPADArrayList);
        promoAndDealsRecyclerView.setAdapter(resortPADAdapter);

        //to get the data from Firebase Firestore
        getResortPromoAndDeals();

        backPADBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingUpEst();
            }
        });

        addPADBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPromoAndDeals();
            }
        });
    }

    private void refs() {
        addPADBtn = findViewById(R.id.addPADBtn);
        backPADBtn = findViewById(R.id.backBtnDentalPAD);
    }

    private void settingUpEst(){
        Intent intent = new Intent(getApplicationContext(), SettingUpEstablishmentResort.class);
        startActivity(intent);
    }

    private void addPromoAndDeals(){
        Intent intent = new Intent(getApplicationContext(), AddPromoAndDealsResort.class);
        startActivity(intent);
    }

    private void getResortPromoAndDeals() {
        //Showing progressdialog while fetching the data
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        fStore.collection("establishments").document(userId).collection("resort-promo-and-deals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        resortPADArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            ResortPromoAndDealsModel rPADList = new ResortPromoAndDealsModel(doc.getString("resortPADId"),
                                    doc.getString("resortPADName"),
                                    doc.getString("resortPADDesc"),
                                    doc.getString("resortPADStartDate"),
                                    doc.getString("resortPADEndDate"));
                            resortPADArrayList.add(rPADList);
                        }
                        resortPADAdapter = new ResortPromoAndDealsAdapter(PromoAndDealsResort.this, resortPADArrayList);
                        // setting adapter to our recycler view.
                        promoAndDealsRecyclerView.setAdapter(resortPADAdapter);
                        resortPADAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PromoAndDealsResort.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}