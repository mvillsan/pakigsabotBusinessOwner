package com.capstone.pakigsabotbusinessowner.EyeClinic.PromoAndDeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.EyeClinic.AddPromoAndDealsEyeClinic;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HelpCenterFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HistoryFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HomeFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ReservationsFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ServicesFragment;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Restaurant.SettingUpEstablishmentRestaurant;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PromoAndDealsEyeClinic extends AppCompatActivity {
    ImageView addPADBtn, backPADBtn;
    RecyclerView promoAndDealsRecyclerView;
    ArrayList<EyeClinicPromoAndDealsModel> eyeClinicPADArrayList;
    EyeClinicPromoAndDealsAdapter eyeClinicPADAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_and_deals_eye_clinic);
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
        eyeClinicPADArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        eyeClinicPADArrayList = new ArrayList<EyeClinicPromoAndDealsModel>();

        //initializing the adapter
        eyeClinicPADAdapter = new EyeClinicPromoAndDealsAdapter(PromoAndDealsEyeClinic.this, eyeClinicPADArrayList);
        promoAndDealsRecyclerView.setAdapter(eyeClinicPADAdapter);

        //to get the data from Firebase Firestore
        getECPromoAndDeals();

        backPADBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SettingUpEstablishmentRestaurant.class);
                startActivity(intent);
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
        backPADBtn = findViewById(R.id.backBtnOpticalPAD);
    }

    private void addPromoAndDeals(){
        Intent intent = new Intent(getApplicationContext(), AddPromoAndDealsEyeClinic.class);
        startActivity(intent);
    }

    private void getECPromoAndDeals() {
        //Showing progressdialog while fetching the data
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        fStore.collection("establishments").document(userId).collection("eye-clinic-promo-and-deals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        eyeClinicPADArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            EyeClinicPromoAndDealsModel ecPADList = new EyeClinicPromoAndDealsModel(doc.getString("opticalPADId"),
                                    doc.getString("opticalPADName"),
                                    doc.getString("opticalPADDesc"),
                                    doc.getString("opticalPADStartDate"),
                                    doc.getString("opticalPADEndDate"));
                            eyeClinicPADArrayList.add(ecPADList);
                        }
                        eyeClinicPADAdapter = new EyeClinicPromoAndDealsAdapter(PromoAndDealsEyeClinic.this, eyeClinicPADArrayList);
                        // setting adapter to our recycler view.
                        promoAndDealsRecyclerView.setAdapter(eyeClinicPADAdapter);
                        eyeClinicPADAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PromoAndDealsEyeClinic.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}