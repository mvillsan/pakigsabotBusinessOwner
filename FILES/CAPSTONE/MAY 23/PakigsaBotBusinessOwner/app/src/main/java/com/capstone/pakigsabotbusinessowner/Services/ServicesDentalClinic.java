package com.capstone.pakigsabotbusinessowner.Services;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.pakigsabotbusinessowner.DentalClinic.AddServiceDentalClinic;
import com.capstone.pakigsabotbusinessowner.DentalClinic.Adapter.DentalClinicAdapter;
import com.capstone.pakigsabotbusinessowner.DentalClinic.Model.DentalClinicProcedures;
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

public class ServicesDentalClinic extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    ImageView addProcBtn, backBtn;
    RecyclerView dentalPRRecyclerView;
    ArrayList<DentalClinicProcedures> dentalClinicProceduresArrayList;
    DentalClinicAdapter dentalClinicAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_dental_clinic);

        //References:
        refs();

        //recyclerview initialization
        dentalPRRecyclerView = findViewById(R.id.dentalRecyclerView);
        dentalPRRecyclerView.setHasFixedSize(true);
        dentalPRRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adding array list to recyclerview adapter
        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        dentalClinicProceduresArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        dentalClinicProceduresArrayList = new ArrayList<DentalClinicProcedures>();

        //initializing the adapter
        dentalClinicAdapter = new DentalClinicAdapter(ServicesDentalClinic.this, dentalClinicProceduresArrayList);
        dentalPRRecyclerView.setAdapter(dentalClinicAdapter);

        //to get the data from Firebase Firestore
        getDentalProcedures();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragment();
            }
        });

        addProcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProcedure();
            }
        });
    }

    public void refs(){

        addProcBtn = findViewById(R.id.addProcBtn);
        backBtn = findViewById(R.id.backBtnDentalServices);
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

    private void addProcedure(){
        Intent intent = new Intent(getApplicationContext(), AddServiceDentalClinic.class);
        startActivity(intent);
    }

    private void getDentalProcedures() {
        //Showing progressdialog while fetching the data
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        fStore.collection("establishments").document(userId).collection("dental-procedures")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        dentalClinicProceduresArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            DentalClinicProcedures dpList = new DentalClinicProcedures(doc.getString("dentalPRId"),
                                    doc.getString("dentalPRName"),
                                    doc.getString("dentalPRDesc"),
                                    doc.getString("dentalPRRate"),
                                    doc.getString("dentalPRImgUrl"),
                                    doc.getString("estId"));
                            dentalClinicProceduresArrayList.add(dpList);
                        }
                        dentalClinicAdapter = new DentalClinicAdapter(ServicesDentalClinic.this, dentalClinicProceduresArrayList);
                        // setting adapter to our recycler view.
                        dentalPRRecyclerView.setAdapter(dentalClinicAdapter);
                        dentalClinicAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ServicesDentalClinic.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}