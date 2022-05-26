package com.capstone.pakigsabotbusinessowner.Reservations;

import static com.airbnb.lottie.L.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.NavigationFragments.HelpCenterFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HistoryFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HomeFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ReservationsFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ServicesFragment;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Reservations.Adapter.ReservationsAdapter;
import com.capstone.pakigsabotbusinessowner.Reservations.Model.ReservationsModel;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewReservationsDentalClinic extends AppCompatActivity {
    //Initialization of Variables::
    MeowBottomNavigation bottomNavigation;
    ImageView imgBackBtn;

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore,
    // and our progress bar.
    // initializing our variable for firebase
    // firestore and getting its instance.
    RecyclerView reservationsRecyclerView;
    ArrayList<ReservationsModel> reservationsArrayList;
    ReservationsAdapter reservationsAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID, dateToday;
    ProgressDialog progressDialog;
    SearchView searchView;
    List<String> listDates;
    SwipeRefreshLayout reservationsSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reservations_dental_clinic);
        //References
        refs();

        //recyclerview initialization
        reservationsRecyclerView = findViewById(R.id.reservationsRecyclerView);
        reservationsRecyclerView.setHasFixedSize(true);
        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        fStore=FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reservationsArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        reservationsArrayList = new ArrayList<ReservationsModel>();

        //initializing the adapter
        reservationsAdapter = new ReservationsAdapter(ViewReservationsDentalClinic.this, reservationsArrayList);
        reservationsRecyclerView.setAdapter(reservationsAdapter);

        // below line is used to get the data from Firebase Firestore.
        getReservationsList();

        getReservationsDates();

        // Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user performs a swipe-to-refresh gesture.
        reservationsSwipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        getReservationsList();
                        reservationsSwipeRefresh.setRefreshing(false);
                    }
                });

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


        //Back to Reservations Fragment::
        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reservationsFragment();
            }
        });
    }

    private void refs(){
        imgBackBtn = findViewById(R.id.backBtnLoadRDC);
        searchView = findViewById(R.id.searchReservSV);
        reservationsSwipeRefresh = findViewById(R.id.reservationSwipeRefresh);
    }

    private void reservationsFragment(){
        setContentView(R.layout.activity_bottom_navigation);
        //Bottom Nav
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
                    case 1: //When id is 1, initialize nearby fragment
                        fragment = new ReservationsFragment();
                        break;

                    case 2: //When id is 2, initialize reservations fragment
                        fragment = new ServicesFragment();
                        break;

                    case 3: //When id is 3, initialize home fragment
                        fragment = new HomeFragment();
                        break;

                    case 4: //When id is 4, initialize favorites fragment
                        fragment = new HistoryFragment();
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
        //Set reservations fragment initially selected
        bottomNavigation.show(1,true);

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

    //Display Reservations List
    public void getReservationsList() {
        //Progress::
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        fStore.collection("reservations")
                .whereEqualTo("reserv_est_ID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        reservationsArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            ReservationsModel list = new ReservationsModel(doc.getString("reserv_id"),
                                    doc.getString("reserv_status"),
                                    doc.getString("reserv_pax"),
                                    doc.getString("reserv_name"),
                                    doc.getString("reserv_dateIn"),
                                    doc.getString("reserv_dateCheckOut"),
                                    doc.getString("reserv_timeCheckIn"),
                                    doc.getString("reserv_cust_ID"),
                                    doc.getString("reserv_cust_FName"),
                                    doc.getString("reserv_cust_LName"),
                                    doc.getString("reserv_cust_phoneNum"),
                                    doc.getString("reserv_cust_emailAdd"),
                                    doc.getString("reserv_est_ID"),
                                    doc.getString("reserv_est_Name"),
                                    doc.getString("reserv_est_emailAdd"),
                                    doc.getString("reserv_fee"),
                                    doc.getString("reserv_notes"),
                                    doc.getString("reserv_adultPax"),
                                    doc.getString("reserv_childPax"),
                                    doc.getString("reserv_infantPax"),
                                    doc.getString("reserv_petPax"),
                                    doc.getString("reserv_transactionDate"),
                                    doc.getString("reserv_confirmedDate"),
                                    doc.getString("reserv_preOrder_items"),
                                    doc.getString("reserv_totalAmt"));
                            reservationsArrayList.add(list);
                        }
                        reservationsAdapter = new ReservationsAdapter(ViewReservationsDentalClinic.this,reservationsArrayList);
                        // setting adapter to our recycler view.
                        reservationsRecyclerView.setAdapter(reservationsAdapter);
                        reservationsAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewReservationsDentalClinic.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String newText){
        List<ReservationsModel> filteredList = new ArrayList<>();
        for(ReservationsModel item : reservationsArrayList){
            if(item.getCustFName().startsWith(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        reservationsAdapter.filterList(filteredList);
    }

    /*private void getReservationsDates(){
        //List of Check-in Dates::
        fStore.collection("reservations").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            listDates = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listDates.add(document.getString("reserv_dateIn"));
                            }
                            Log.d("TAG", listDates.toString());
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        //Getting the date today.
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        dateToday = df.format(date);

        //Getting date tomorrow
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        Date tomorrowDate = c.getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
        String tomorrowDateStr = df2.format(tomorrowDate);

        Log.d("TAG", tomorrowDateStr);
        try{
            if(listDates.contains(tomorrowDateStr)){
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewReservations.this)
                        .setTitle("Notification Reminder Alert!")
                        .setMessage("You have a Reservation Tomorrrow - " + tomorrowDateStr + ". \n\nGo to Reservation List to check its details." +
                                "\n\nTo Cancel the Reservation/s - Go to the Reservation Details")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("TAG", tomorrowDateStr + "true");
                            }
                        });
                alert.show();
            }else{
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewReservations.this)
                        .setTitle("Notification Reminder Alert!")
                        .setMessage("No Reservation/s Tomorrow - " + tomorrowDateStr + "." +
                                "\n\nTo Cancel the Reservation/s - Go to the Reservation Details")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("TAG", tomorrowDateStr + " false");
                            }
                        });
                alert.show();
            }
        }catch(Exception e){
            Toast.makeText(ViewReservations.this, "Refresh Screen", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void getReservationsDates() {
        //List of Check-in Dates::
        fStore.collection("reservations").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            listDates = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listDates.add(document.getString("reserv_dateIn"));
                            }
                            Log.d("TAG", listDates.toString());
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}