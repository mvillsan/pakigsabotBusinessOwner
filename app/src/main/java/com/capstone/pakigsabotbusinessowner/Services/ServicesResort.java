package com.capstone.pakigsabotbusinessowner.Services;

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
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.NavigationFragments.HelpCenter;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HistoryFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HomeFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ReservationsFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ServicesFragment;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Resort.AddRoomResort;
import com.capstone.pakigsabotbusinessowner.Resort.ResortFacilities;
import com.capstone.pakigsabotbusinessowner.Resort.ResortRoomAdapter;
import com.capstone.pakigsabotbusinessowner.Resort.ResortRoomModel;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ServicesResort extends AppCompatActivity {

    //Initialize Variables
    MeowBottomNavigation bottomNavigation;
    ImageView backBtnServicesResort, addRoomBtnServices;
    TextView facilityTxt;

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore,
    // and our progress bar.
    // initializing our variable for firebase
    // firestore and getting its instance.
    RecyclerView resortsServicesRV;
    ArrayList<ResortRoomModel> resortRFArrayList;
    ResortRoomAdapter resortAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_resort);

        //References:
        refs();

        //recyclerview initialization
        resortsServicesRV = findViewById(R.id.resortRecyclerView);
        resortsServicesRV.setHasFixedSize(true);
        resortsServicesRV.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        fStore=FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        resortRFArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        resortRFArrayList = new ArrayList<ResortRoomModel>();

        //initializing the adapter
        resortAdapter = new ResortRoomAdapter(ServicesResort.this, resortRFArrayList);

        resortsServicesRV.setAdapter(resortAdapter);

        // below line is use to get the data from Firebase Firestore.
        getResortRoomList();

        backBtnServicesResort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragment();
            }
        });

        addRoomBtnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoom();
            }
        });

        facilityTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResortFacilitiesList();
            }
        });
    }

    private void refs(){
        addRoomBtnServices = findViewById(R.id.addRoomBtnServices);
        backBtnServicesResort = findViewById(R.id.backBtnServicesResort);
        facilityTxt = findViewById(R.id.facilityTxt);
    }

    private void homeFragment(){
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
                        fragment = new HelpCenter();
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
                        resortRFArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            ResortRoomModel list = new ResortRoomModel(doc.getString("resortRFID"),
                                    doc.getString("resortRFName"),
                                    doc.getString("resortCapac"),
                                    doc.getString("resortDesc"),
                                    doc.getString("resortRate"),
                                    doc.getString("resortImgUrl"),
                                    doc.getString("estID"));
                            resortRFArrayList.add(list);
                        }
                        resortAdapter = new ResortRoomAdapter(ServicesResort.this,resortRFArrayList);
                        // setting adapter to our recycler view.
                        resortsServicesRV.setAdapter(resortAdapter);
                        resortAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ServicesResort.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Display Resort Facilities List
    private void getResortFacilitiesList(){
        Intent intent = new Intent(getApplicationContext(), ResortFacilities.class);
        startActivity(intent);
    }
}

