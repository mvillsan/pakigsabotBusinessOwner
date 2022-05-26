package com.capstone.pakigsabotbusinessowner.Restaurant;

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

import com.capstone.pakigsabotbusinessowner.NavigationFragments.HelpCenterFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HistoryFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HomeFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ReservationsFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ServicesFragment;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Restaurant.Adapter.RestoAdapter;
import com.capstone.pakigsabotbusinessowner.Restaurant.Model.RestoMenuItems;
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

public class MenuRestaurant extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    ImageView addFIBtn, backBtn;
    RecyclerView restoRecyclerView;
    ArrayList<RestoMenuItems> restoMenuItemsArrayList;
    RestoAdapter restoAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_restaurant);

        //References:
        refs();

        //recyclerview initialization
        restoRecyclerView = findViewById(R.id.restoRecyclerView);
        restoRecyclerView.setHasFixedSize(true);
        restoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adding array list to recyclerview adapter
        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        restoMenuItemsArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        restoMenuItemsArrayList = new ArrayList<RestoMenuItems>();

        //initializing the adapter
        restoAdapter = new RestoAdapter(MenuRestaurant.this, restoMenuItemsArrayList);
        restoRecyclerView.setAdapter(restoAdapter);

        //to get the data from Firebase Firestore
        getMenuItems();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragment();
            }
        });

        addFIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFoodItem();
            }
        });
    }

    public void refs(){

        addFIBtn = findViewById(R.id.addMenuItemBtn);
        backBtn = findViewById(R.id.backBtnRestoMenu);
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

    private void addFoodItem(){
        Intent intent = new Intent(getApplicationContext(), AddFoodResto.class);
        startActivity(intent);
    }

    private void getMenuItems() {
        //Showing progressdialog while fetching the data
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        fStore.collection("establishments").document(userId).collection("resto-menu-items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        restoMenuItemsArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                           RestoMenuItems rmiList = new RestoMenuItems(doc.getString("restoFIId"),
                                    doc.getString("restoFIName"),
                                    doc.getString("restoFICategory"),
                                    doc.getString("restoFIDesc"),
                                    doc.getString("restoFIAvail"),
                                    doc.getString("restoFIPrice"),
                                    doc.getString("restoFIImgUrl"));
                            restoMenuItemsArrayList.add(rmiList);
                        }
                        restoAdapter = new RestoAdapter(MenuRestaurant.this, restoMenuItemsArrayList);
                        // setting adapter to our recycler view.
                        restoRecyclerView.setAdapter(restoAdapter);
                        restoAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MenuRestaurant.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}