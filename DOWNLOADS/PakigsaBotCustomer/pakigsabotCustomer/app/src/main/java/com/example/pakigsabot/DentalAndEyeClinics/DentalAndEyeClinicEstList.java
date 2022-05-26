package com.example.pakigsabot.DentalAndEyeClinics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.pakigsabot.DentalAndEyeClinics.Adapters.DentalAndEyeClinicsAdapter;
import com.example.pakigsabot.DentalAndEyeClinics.Models.DentalAndEyeClinicsModel;
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
import java.util.Arrays;
import java.util.List;

public class DentalAndEyeClinicEstList extends AppCompatActivity {
    //Initialization of variables::
    MeowBottomNavigation bottomNavigation;
    ImageView imgBackBtn;

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore,
    // and our progress bar.
    // initializing our variable for firebase
    // firestore and getting its instance.
    RecyclerView dentEyeRecyclerView;
    ArrayList<DentalAndEyeClinicsModel> dentalAndEyeClinicsArrayList;
    DentalAndEyeClinicsAdapter dentalAndEyeClinicsAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID;
    ProgressDialog progressDialog;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dental_and_eye_clinic_reservation);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("clinicsReserve") != null) {
                Toast.makeText(getApplicationContext(), "data: " + bundle.getString("clinicsReserve"),
                        Toast.LENGTH_SHORT).show();
            }
        }

        //References::
        refs();

        //recyclerview initialization
        dentEyeRecyclerView = findViewById(R.id.dentEyeRecyclerView);
        dentEyeRecyclerView.setHasFixedSize(true);
        dentEyeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        fStore=FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        dentalAndEyeClinicsArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        dentalAndEyeClinicsArrayList = new ArrayList<DentalAndEyeClinicsModel>();

        //initializing the adapter
        dentalAndEyeClinicsAdapter = new DentalAndEyeClinicsAdapter(DentalAndEyeClinicEstList.this, dentalAndEyeClinicsArrayList);

        dentEyeRecyclerView.setAdapter(dentalAndEyeClinicsAdapter);

        // below line is use to get the data from Firebase Firestore.
        getDentalAndEyeClinicsList();

        //SearchView initialization
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment();
            }
        });
    }

    private void refs(){
        imgBackBtn = findViewById(R.id.imgBackBtn);
        searchView = findViewById(R.id.searchByDentEyeSV);
    }

    private void HomeFragment(){
        setContentView(R.layout.activity_bottom_navigation);

        //Assign variable
        bottomNavigation = findViewById(R.id.bottom_nav);

        //Add menu item
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_nearby));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_reserve));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_favorites));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.ic_help));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //Initialize fragment
                Fragment fragment = null;

                //Check condition
                switch (item.getId()){
                    case 1: //When id is 1, initialize nearby fragment
                        fragment = new NearbyFragment();
                        break;

                    case 2: //When id is 2, initialize reservations fragment
                        fragment = new ReservationsFragment();
                        break;

                    case 3: //When id is 3, initialize home fragment
                        fragment = new HomeFragment();
                        break;

                    case 4: //When id is 4, initialize favorites fragment
                        fragment = new EstFavoritesFragment();
                        break;

                    case 5: //When id is 5, initialize chatbot fragment
                        fragment = new HelpCenterFragment();
                        break;
                }
                //Load fragment
                loadFragment(fragment);
            }
        });

        /*Set notification count
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

    //Display Resort List
    private void getDentalAndEyeClinicsList(){
        //Progress::
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        fStore.collection("establishments")
                .whereIn("est_Type", Arrays.asList("Dental Clinic", "Eye Clinic"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       dentalAndEyeClinicsArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            DentalAndEyeClinicsModel daeList = new DentalAndEyeClinicsModel(doc.getString("est_id"),
                                    doc.getString("est_Name"),
                                    doc.getString("est_address"),
                                    doc.getString("est_image"),
                                    doc.getString("est_phoneNum"));
                            dentalAndEyeClinicsArrayList.add(daeList);
                        }
                        dentalAndEyeClinicsAdapter = new DentalAndEyeClinicsAdapter(DentalAndEyeClinicEstList.this, dentalAndEyeClinicsArrayList);
                        // setting adapter to our recycler view.
                        dentEyeRecyclerView.setAdapter(dentalAndEyeClinicsAdapter);
                        dentalAndEyeClinicsAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DentalAndEyeClinicEstList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String newText){
        List<DentalAndEyeClinicsModel> filteredList = new ArrayList<>();
        for(DentalAndEyeClinicsModel item : dentalAndEyeClinicsArrayList){
            if(item.getEst_Name().toLowerCase().startsWith(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        dentalAndEyeClinicsAdapter.filterList(filteredList);
    }
}
