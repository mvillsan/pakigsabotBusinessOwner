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
import com.capstone.pakigsabotbusinessowner.Resort.Adapters.ResortRulesAdapter;
import com.capstone.pakigsabotbusinessowner.Resort.Models.ResortFacilityModel;
import com.capstone.pakigsabotbusinessowner.Resort.Models.ResortRulesModels;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ResortRules extends AppCompatActivity {
    //Initialize Variables
    ImageView backBtn, addRules;

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore,
    // and our progress bar.
    // initializing our variable for firebase
    // firestore and getting its instance.
    RecyclerView rulesRV;
    ArrayList<ResortRulesModels> rulesArrayList;
    ResortRulesAdapter rulesAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID;
    ProgressDialog progressDialog;

    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resort_rules);

        //References:
        refs();

        //recyclerview initialization
        rulesRV = findViewById(R.id.rulesRV);
        rulesRV.setHasFixedSize(true);
        rulesRV.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        fStore=FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        rulesArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        rulesArrayList = new ArrayList<ResortRulesModels>();

        //initializing the adapter
        rulesAdapter = new ResortRulesAdapter(ResortRules.this, rulesArrayList);

        rulesRV.setAdapter(rulesAdapter);

        // below line is use to get the data from Firebase Firestore.
        getResortRulesList();

        //Refresh the recyclerview::
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getResortRulesList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingUp();
            }
        });

        addRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRules();
            }
        });
    }

    private void refs(){
        addRules = findViewById(R.id.addRules);
        backBtn = findViewById(R.id.backBtn);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
    }

    private void addRules(){
        Intent intent = new Intent(getApplicationContext(), AddRuleResort.class);
        startActivity(intent);
    }

    private void settingUp(){
        Intent intent = new Intent(getApplicationContext(), SettingUpEstablishmentResort.class);
        startActivity(intent);
    }

    //Display Resort Rules List
    private void getResortRulesList(){
        //Progress::
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        fStore.collection("establishments").document(userID).collection("resort-rules")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        rulesArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            ResortRulesModels list = new ResortRulesModels(doc.getString("resort_ruleID"),
                                    doc.getString("resort_desc"));
                            rulesArrayList.add(list);
                        }
                        rulesAdapter = new ResortRulesAdapter(ResortRules.this,rulesArrayList);
                        // setting adapter to our recycler view.
                        rulesRV.setAdapter(rulesAdapter);
                        rulesAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ResortRules.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}