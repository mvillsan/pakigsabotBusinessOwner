package com.capstone.pakigsabotbusinessowner.EstablishmentRules.Resto;

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

import com.capstone.pakigsabotbusinessowner.DentalClinic.AddPromoAndDealsDentalClinic;
import com.capstone.pakigsabotbusinessowner.EstablishmentRules.EstRulesAdapter;
import com.capstone.pakigsabotbusinessowner.EstablishmentRules.EstRulesModel;
import com.capstone.pakigsabotbusinessowner.EyeClinic.PromoAndDeals.EyeClinicPromoAndDealsAdapter;
import com.capstone.pakigsabotbusinessowner.EyeClinic.PromoAndDeals.EyeClinicPromoAndDealsModel;
import com.capstone.pakigsabotbusinessowner.EyeClinic.PromoAndDeals.PromoAndDealsEyeClinic;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Restaurant.SettingUpEstablishmentRestaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EstRulesResto extends AppCompatActivity {
    ImageView backBtn, addBtn;
    RecyclerView estRulesRecyclerView;
    ArrayList<EstRulesModel> restoERArrayList;
    EstRulesAdapter restoERAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_est_rules_resto);

        //References
        refs();

        //recyclerview initialization
        estRulesRecyclerView = findViewById(R.id.estRulesRecyclerView);
        estRulesRecyclerView.setHasFixedSize(true);
        estRulesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adding array list to recyclerview adapter
        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        restoERArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        restoERArrayList = new ArrayList<EstRulesModel>();

        //initializing the adapter
        restoERAdapter = new EstRulesAdapter(EstRulesResto.this, restoERArrayList);
        estRulesRecyclerView.setAdapter(restoERAdapter);

        //to get the data from Firebase Firestore
        getEstRules();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddEstRulesResto.class);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingUpEstablishmentRestaurant.class);
                startActivity(intent);
            }
        });
    }

    private void refs() {
        backBtn = findViewById(R.id.backBtnRestoER);
        addBtn = findViewById(R.id.addERBtn);
    }

    private void getEstRules() {
        //Showing progressdialog while fetching the data
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        fStore.collection("establishments").document(userId).collection("resto-est-rules")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        restoERArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            EstRulesModel restoERList = new EstRulesModel(doc.getString("restoERId"),
                                    doc.getString("restoERName"),
                                    doc.getString("restoERDesc"),
                                    doc.getString("restoERDesc"));
                            restoERArrayList.add(restoERList);
                        }
                        restoERAdapter = new EstRulesAdapter(EstRulesResto.this, restoERArrayList);
                        // setting adapter to our recycler view.
                        estRulesRecyclerView.setAdapter(restoERAdapter);
                        restoERAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EstRulesResto.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}