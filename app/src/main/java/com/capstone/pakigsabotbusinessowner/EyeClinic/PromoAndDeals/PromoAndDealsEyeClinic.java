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
    MeowBottomNavigation bottomNavigation;
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
                homeFragment();
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

    private void homeFragment() {
        setContentView(R.layout.activity_bottom_navigation);

        //Assign variable
        bottomNavigation = findViewById(R.id.bottom_nav);

        //Add menu item
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_reserve));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_services));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_history));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.ic_help));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //Initialize fragment
                Fragment fragment = null;

                //Check condition
                switch (item.getId()){
                    case 1: //When id is 1, initialize reservations fragment
                        fragment = new ReservationsFragment();
                        break;

                    case 2: //When id is 2, initialize services fragment
                        fragment = new ServicesFragment();
                        break;

                    case 3: //When id is 3, initialize home fragment
                        fragment = new HomeFragment();
                        break;

                    case 4: //When id is 4, initialize reservations history fragment
                        fragment = new HistoryFragment();
                        break;

                    case 5: //When id is 5, initialize reservation chatbot fragment
                        fragment = new HelpCenterFragment();
                        break;
                }
                //Load fragment
                loadFragment(fragment);
            }
        });

        /*//Set notification count
        bottomNavigation.setCount(3,"10");*/
        //Set home fragment initially selected
        bottomNavigation.show(3,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        //Replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();
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