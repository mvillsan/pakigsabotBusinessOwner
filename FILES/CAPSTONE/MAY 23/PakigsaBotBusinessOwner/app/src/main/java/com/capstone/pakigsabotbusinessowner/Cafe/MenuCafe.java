package com.capstone.pakigsabotbusinessowner.Cafe;

import static com.airbnb.lottie.L.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.Cafe.Adapter.CafeAdapter;
import com.capstone.pakigsabotbusinessowner.Cafe.Model.CafeMenuItems;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HelpCenterFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HistoryFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HomeFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ReservationsFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ServicesFragment;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Restaurant.Adapter.RestoAdapter;
import com.capstone.pakigsabotbusinessowner.Restaurant.AddFoodResto;
import com.capstone.pakigsabotbusinessowner.Restaurant.MenuRestaurant;
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

public class MenuCafe extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    ImageView addFIBtn, backBtn;
    RecyclerView cafeRecyclerView;
    ArrayList<CafeMenuItems> cafeMenuItemsArrayList;
    CafeAdapter cafeAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    ProgressDialog progressDialog;
    SwipeRefreshLayout menuCafeSwipeRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cafe);

        //References:
        refs();

        //recyclerview initialization
        cafeRecyclerView = findViewById(R.id.cafeRecyclerView);
        cafeRecyclerView.setHasFixedSize(true);
        cafeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adding array list to recyclerview adapter
        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        cafeMenuItemsArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        cafeMenuItemsArrayList = new ArrayList<CafeMenuItems>();

        //initializing the adapter
        cafeAdapter = new CafeAdapter(MenuCafe.this, cafeMenuItemsArrayList);
        cafeRecyclerView.setAdapter(cafeAdapter);

        //to get the data from Firebase Firestore
        getMenuItems();

        // Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user performs a swipe-to-refresh gesture.
        menuCafeSwipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        getMenuItems();
                        menuCafeSwipeRefresh.setRefreshing(false);
                    }
                });

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



    private void refs() {
        addFIBtn = findViewById(R.id.addMenuItemBtnCafe);
        backBtn = findViewById(R.id.backBtnCafeMenu);
        menuCafeSwipeRefresh = findViewById(R.id.cafeMenuSwipeRefresh);
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
        bottomNavigation.show(2,true);

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
        Intent intent = new Intent(getApplicationContext(), AddMenuItemCafe.class);
        startActivity(intent);
    }


    private void getMenuItems() {
        //Showing progressdialog while fetching the data
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        fStore.collection("establishments").document(userId).collection("cafe-menu-items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        cafeMenuItemsArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            CafeMenuItems cmiList = new CafeMenuItems(doc.getString("cafeFIId"),
                                    doc.getString("cafeFIName"),
                                    doc.getString("cafeFICategory"),
                                    doc.getString("cafeFIAvail"),
                                    doc.getString("cafeFIPrice"),
                                    doc.getString("cafeFIImgUrl"));
                            cafeMenuItemsArrayList.add(cmiList);
                        }
                        cafeAdapter = new CafeAdapter(MenuCafe.this, cafeMenuItemsArrayList);
                        // setting adapter to our recycler view.
                        cafeRecyclerView.setAdapter(cafeAdapter);
                        cafeAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MenuCafe.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}