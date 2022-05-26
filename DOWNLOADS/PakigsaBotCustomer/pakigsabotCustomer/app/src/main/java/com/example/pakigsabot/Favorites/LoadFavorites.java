package com.example.pakigsabot.Favorites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.pakigsabot.NavigationFragments.EstFavoritesFragment;
import com.example.pakigsabot.NavigationFragments.HelpCenterFragment;
import com.example.pakigsabot.NavigationFragments.HomeFragment;
import com.example.pakigsabot.NavigationFragments.NearbyFragment;
import com.example.pakigsabot.NavigationFragments.ReservationsFragment;
import com.example.pakigsabot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoadFavorites extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    ImageView addProcBtn, backBtn;
    RecyclerView favoriteEstRecyclerView;
    ArrayList<FavoritesModel> favoriteEstArrayList;
    FavoritesAdapter favoritesAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_favorites);

        //References:
        refs();

        //recyclerview initialization
        favoriteEstRecyclerView = findViewById(R.id.favoriteEstRecyclerView);
        favoriteEstRecyclerView.setHasFixedSize(true);
        favoriteEstRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adding array list to recyclerview adapter
        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        favoriteEstArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        favoriteEstArrayList = new ArrayList<FavoritesModel>();

        //initializing the adapter
        favoritesAdapter = new FavoritesAdapter(LoadFavorites.this, favoriteEstArrayList);
       favoriteEstRecyclerView.setAdapter(favoritesAdapter);

        //to get the data from Firebase Firestore
        getFavoriteEstablishments();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment();
                getFavoriteEstablishments();
            }
        });

    }


    private void refs() {
        backBtn= findViewById(R.id.backBtnFavorites);

    }

    private void homeFragment() {
        setContentView(R.layout.activity_bottom_navigation);

        //Assign variable
        bottomNavigation = findViewById(R.id.bottom_nav);

        //Add menu item
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_reserve));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_nearby));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_favicon));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_help));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //Initialize fragment
                Fragment fragment = null;

                //Check condition
                switch (item.getId()) {
                    case 1: //When id is 1, initialize reservations fragment
                        fragment = new ReservationsFragment();
                        break;

                    case 2: //When id is 2, initialize nearby fragment
                        fragment = new NearbyFragment();
                        break;

                    case 3: //When id is 3, initialize home fragment
                        fragment = new HomeFragment();
                        break;

                    case 4: //When id is 4, initialize favorite establishment fragment
                        fragment = new EstFavoritesFragment();
                        break;

                    case 5: //When id is 5, initialize help center fragment
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
        bottomNavigation.show(3, true);

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


        private void getFavoriteEstablishments(){
            //Showing progressdialog while fetching the data
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Fetching Data...");
            progressDialog.show();

            fStore.collection("customers").document(userId).collection("favorites")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            favoriteEstArrayList.clear();

                            for(DocumentSnapshot doc : task.getResult()){
                                FavoritesModel favList = new FavoritesModel(doc.getString("favEstId"),
                                        doc.getString("estName"),
                                        doc.getString("estAddress"),
                                        doc.getString("estImageUrl"));
                                favoriteEstArrayList.add(favList);
                            }
                            favoritesAdapter = new FavoritesAdapter(LoadFavorites.this, favoriteEstArrayList);
                            // setting adapter to our recycler view.
                            favoriteEstRecyclerView.setAdapter(favoritesAdapter);
                            favoritesAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoadFavorites.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
}