package com.capstone.pakigsabotbusinessowner.Restaurant.PromoAndDeals;

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

import com.capstone.pakigsabotbusinessowner.Cafe.MenuCafe;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HelpCenterFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HistoryFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HomeFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ReservationsFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ServicesFragment;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Restaurant.AddPromoAndDealsRestaurant;
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

public class PromoAndDealsRestaurant extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    ImageView addPADBtn, backPADBtn;
    RecyclerView promoAndDealsRecyclerView;
    ArrayList<RestaurantPromoAndDealsModel> restoPADArrayList;
    RestaurantPromoAndDealsAdapter restoPADAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_and_deals_restaurant);
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
        restoPADArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        restoPADArrayList = new ArrayList<RestaurantPromoAndDealsModel>();

        //initializing the adapter
        restoPADAdapter = new RestaurantPromoAndDealsAdapter(PromoAndDealsRestaurant.this, restoPADArrayList);
        promoAndDealsRecyclerView.setAdapter(restoPADAdapter);

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
        Intent in = new Intent(getApplicationContext(), SettingUpEstablishmentRestaurant.class);
        startActivity(in);
    }

    private void addPromoAndDeals(){
        Intent intent = new Intent(getApplicationContext(), AddPromoAndDealsRestaurant.class);
        startActivity(intent);
    }

    private void getRestoPromoAndDeals() {
        //Showing progressdialog while fetching the data
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        fStore.collection("establishments").document(userId).collection("resto-promo-and-deals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        restoPADArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            RestaurantPromoAndDealsModel restoPADList = new RestaurantPromoAndDealsModel(doc.getString("restoPADId"),
                                    doc.getString("restoPADName"),
                                    doc.getString("restoPADDesc"),
                                    doc.getString("restoPADStartDate"),
                                    doc.getString("restoPADEndDate"));
                            restoPADArrayList.add(restoPADList);
                        }
                        restoPADAdapter = new RestaurantPromoAndDealsAdapter(PromoAndDealsRestaurant.this, restoPADArrayList);
                        // setting adapter to our recycler view.
                        promoAndDealsRecyclerView.setAdapter(restoPADAdapter);
                        restoPADAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PromoAndDealsRestaurant.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}