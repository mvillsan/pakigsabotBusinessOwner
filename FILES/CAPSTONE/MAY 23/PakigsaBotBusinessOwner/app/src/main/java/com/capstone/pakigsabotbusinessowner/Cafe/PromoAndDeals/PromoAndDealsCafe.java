package com.capstone.pakigsabotbusinessowner.Cafe.PromoAndDeals;

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

import com.capstone.pakigsabotbusinessowner.Cafe.AddPromoAndDealsCafe;
import com.capstone.pakigsabotbusinessowner.Cafe.SettingUpEstablishmentCafe;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HelpCenterFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HistoryFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HomeFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ReservationsFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ServicesFragment;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Restaurant.AddPromoAndDealsRestaurant;
import com.capstone.pakigsabotbusinessowner.Restaurant.PromoAndDeals.PromoAndDealsRestaurant;
import com.capstone.pakigsabotbusinessowner.Restaurant.PromoAndDeals.RestaurantPromoAndDealsAdapter;
import com.capstone.pakigsabotbusinessowner.Restaurant.PromoAndDeals.RestaurantPromoAndDealsModel;
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

public class PromoAndDealsCafe extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    ImageView addPADBtn, backPADBtn;
    RecyclerView promoAndDealsRecyclerView;
    ArrayList<CafePromoAndDealsModel> cafePADArrayList;
    CafePromoAndDealsAdapter cafePADAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_and_deals_cafe);
        //References:
        refs();

        //recyclerview initialization
        promoAndDealsRecyclerView = findViewById(R.id.promoAndDealsRecyclerView);
        promoAndDealsRecyclerView.setHasFixedSize(true);
        promoAndDealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adding array list to recyclerview adapter
        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        cafePADArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        cafePADArrayList = new ArrayList<CafePromoAndDealsModel>();

        //initializing the adapter
        cafePADAdapter = new CafePromoAndDealsAdapter(PromoAndDealsCafe.this, cafePADArrayList);
        promoAndDealsRecyclerView.setAdapter(cafePADAdapter);

        //to get the data from Firebase Firestore
        getRestoPromoAndDeals();

        backPADBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               setUpEst();
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
        backPADBtn = findViewById(R.id.backBtnPAD);
    }

    private void setUpEst() {
        Intent in = new Intent(getApplicationContext(), SettingUpEstablishmentCafe.class);
        startActivity(in);
    }

    private void addPromoAndDeals(){
        Intent intent = new Intent(getApplicationContext(), AddPromoAndDealsCafe.class);
        startActivity(intent);
    }

    private void getRestoPromoAndDeals() {
        //Showing progressdialog while fetching the data
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        fStore.collection("establishments").document(userId).collection("cafe-promo-and-deals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        cafePADArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            CafePromoAndDealsModel cafePADList = new CafePromoAndDealsModel(doc.getString("cafePADId"),
                                    doc.getString("cafePADName"),
                                    doc.getString("cafePADDesc"),
                                    doc.getString("cafePADStartDate"),
                                    doc.getString("cafePADEndDate"));
                            cafePADArrayList.add(cafePADList);
                        }
                        cafePADAdapter = new CafePromoAndDealsAdapter(PromoAndDealsCafe.this, cafePADArrayList);
                        // setting adapter to our recycler view.
                        promoAndDealsRecyclerView.setAdapter(cafePADAdapter);
                        cafePADAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PromoAndDealsCafe.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}