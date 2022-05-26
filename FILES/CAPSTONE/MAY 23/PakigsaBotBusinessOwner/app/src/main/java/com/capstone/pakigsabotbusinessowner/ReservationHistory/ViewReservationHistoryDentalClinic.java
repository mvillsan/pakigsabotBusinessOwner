package com.capstone.pakigsabotbusinessowner.ReservationHistory;

import static com.airbnb.lottie.L.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

import com.capstone.pakigsabotbusinessowner.NavigationFragments.HistoryFragment;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.ReservationHistory.Adapter.CancelledRHistoryAdapter;
import com.capstone.pakigsabotbusinessowner.ReservationHistory.Adapter.CompletedRHistoryAdapter;
import com.capstone.pakigsabotbusinessowner.ReservationHistory.Model.CancelledRHistoryModel;
import com.capstone.pakigsabotbusinessowner.ReservationHistory.Model.CompletedRHistoryModel;
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

public class ViewReservationHistoryDentalClinic extends AppCompatActivity {
    //Initialization of Variables::
    ImageView imgBackBtn, refreshIcon;

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore,
    // and our progress bar.
    // initializing our variable for firebase
    // firestore and getting its instance.
    RecyclerView completedReservationsRecyclerView;
    ArrayList<CompletedRHistoryModel> completedRHistoryArrayList;
    CompletedRHistoryAdapter completedRHistoryAdapter;
    RecyclerView cancelledReservationRecyclerView;
    ArrayList<CancelledRHistoryModel> cancelledRHistoryArrayList;
    CancelledRHistoryAdapter cancelledRHistoryAdapter;

    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID, dateToday;
    ProgressDialog progressDialog;
    SearchView searchView;
    List<String> listDates;
    SwipeRefreshLayout completedReservationsSwipeRefresh, cancelledReservationsSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reservation_history_dental_clinic);
        //References::
        refs();

        //--FOR COMPLETED RESERVATIONS--
        //recyclerview initialization
        completedReservationsRecyclerView = findViewById(R.id.rHistoryRecyclerViewCompleteDentalClinic);
        completedReservationsRecyclerView.setHasFixedSize(true);
        completedReservationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        fStore=FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        completedRHistoryArrayList = new ArrayList<>();

        //initializing the arraylist where all data is stored
        completedRHistoryArrayList = new ArrayList<CompletedRHistoryModel>();

        //initializing the adapter
        completedRHistoryAdapter = new CompletedRHistoryAdapter(ViewReservationHistoryDentalClinic.this, completedRHistoryArrayList);
        completedReservationsRecyclerView.setAdapter(completedRHistoryAdapter);

        // Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user performs a swipe-to-refresh gesture.
        completedReservationsSwipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        getCompletedReservationsList();
                        completedReservationsSwipeRefresh.setRefreshing(false);
                    }
                });


        //--FOR CANCELLED RESERVATIONS--
        //recyclerview initialization
        cancelledReservationRecyclerView = findViewById(R.id.rHistoryRecyclerViewCancelledDentalClinic);
        cancelledReservationRecyclerView.setHasFixedSize(true);
        cancelledReservationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the arraylist where all data is stored
        cancelledRHistoryArrayList = new ArrayList<CancelledRHistoryModel>();

        //initializing the adapter
        cancelledRHistoryAdapter = new CancelledRHistoryAdapter(ViewReservationHistoryDentalClinic.this, cancelledRHistoryArrayList);
        cancelledReservationRecyclerView.setAdapter(cancelledRHistoryAdapter);

        // Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user performs a swipe-to-refresh gesture.
        cancelledReservationsSwipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        getCancelledReservationsList();
                        completedReservationsSwipeRefresh.setRefreshing(false);
                    }
                });


        // below line is used to get the data from Firebase Firestore.
        getCompletedReservationsList();
        getCancelledReservationsList();

        //getReservationsDates();

        //Back to reservation history fragment screen
        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryFragment.class);
                startActivity(intent);
            }
        });

    }


    private void refs() {
        completedReservationsSwipeRefresh = findViewById(R.id.rHistorySwipeRefreshCompleteDentalClinic);
        cancelledReservationsSwipeRefresh = findViewById(R.id.rHistorySwipeRefresCancelledDentalClinic);
        imgBackBtn = findViewById(R.id.backBtnRHistoryDentalClinic);
    }

    //Display Reservations List
    public void getCompletedReservationsList() {
        //Progress::
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        //Getting data to Firestore for Completed Reservations
        fStore.collection("completed-reservations")
                .whereEqualTo("completeReserv_est_ID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        completedRHistoryArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            CompletedRHistoryModel list = new CompletedRHistoryModel(doc.getString("completeReserv_id"),
                                    doc.getString("completeReserv_status"),
                                    doc.getString("completeReserv_pax"),
                                    doc.getString("completeReserv_name"),
                                    doc.getString("completeReserv_dateIn"),
                                    doc.getString("completeReserv_dateCheckOut"),
                                    doc.getString("completeReserv_timeCheckIn"),
                                    doc.getString("completeReserv_cust_ID"),
                                    doc.getString("completeReserv_cust_FName"),
                                    doc.getString("completeReserv_cust_LName"),
                                    doc.getString("completeReserv_cust_phoneNum"),
                                    doc.getString("completeReserv_cust_emailAdd"),
                                    doc.getString("completeReserv_est_ID"),
                                    doc.getString("completeReserv_est_Name"),
                                    doc.getString("completeReserv_est_emailAdd"),
                                    doc.getString("completeReserv_fee"),
                                    doc.getString("completeReserv_notes"),
                                    doc.getString("completeReserv_adultPax"),
                                    doc.getString("completeReserv_childPax"),
                                    doc.getString("completeReserv_infantPax"),
                                    doc.getString("completeReserv_petPax"),
                                    doc.getString("completeReserv_transactionDate"),
                                    doc.getString("completeReserv_confirmedDate"));
                            completedRHistoryArrayList.add(list);
                        }
                        completedRHistoryAdapter = new CompletedRHistoryAdapter(ViewReservationHistoryDentalClinic.this,completedRHistoryArrayList);
                        // setting adapter to our recycler view.
                        completedReservationsRecyclerView.setAdapter(completedRHistoryAdapter);
                        completedRHistoryAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewReservationHistoryDentalClinic.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCancelledReservationsList() {
        //Getting data from Firestore for Cancelled Reservations
        fStore.collection("cancelled-reservations")
                .whereEqualTo("cancelReserv_est_ID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        cancelledRHistoryArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            CancelledRHistoryModel list = new CancelledRHistoryModel(doc.getString("cancelReserv_id"),
                                    doc.getString("cancelReserv_status"),
                                    doc.getString("cancelReserv_pax"),
                                    doc.getString("cancelReserv_name"),
                                    doc.getString("cancelReserv_dateIn"),
                                    doc.getString("cancelReserv_dateCheckOut"),
                                    doc.getString("cancelReserv_timeCheckIn"),
                                    doc.getString("cancelReserv_cust_ID"),
                                    doc.getString("cancelReserv_cust_FName"),
                                    doc.getString("cancelReserv_cust_LName"),
                                    doc.getString("cancelReserv_cust_phoneNum"),
                                    doc.getString("cancelReserv_cust_emailAdd"),
                                    doc.getString("cancelReserv_est_ID"),
                                    doc.getString("cancelReserv_est_Name"),
                                    doc.getString("cancelReserv_est_emailAdd"),
                                    doc.getString("cancelReserv_fee"),
                                    doc.getString("cancelReserv_notes"),
                                    doc.getString("cancelReserv_adultPax"),
                                    doc.getString("cancelReserv_childPax"),
                                    doc.getString("cancelReserv_infantPax"),
                                    doc.getString("cancelReserv_petPax"),
                                    doc.getString("cancelReserv_transactionDate"),
                                    doc.getString("cancelReserv_cancelledDate"));
                            cancelledRHistoryArrayList.add(list);
                        }
                        cancelledRHistoryAdapter = new CancelledRHistoryAdapter(ViewReservationHistoryDentalClinic.this,cancelledRHistoryArrayList);
                        // setting adapter to our recycler view.
                        cancelledReservationRecyclerView.setAdapter(cancelledRHistoryAdapter);
                        cancelledRHistoryAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewReservationHistoryDentalClinic.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getReservationsDates() {
        //List of Check-in Dates::
        fStore.collection("completed-reservations").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            listDates = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listDates.add(document.getString("completeReserv_dateIn"));
                            }
                            Log.d("TAG", listDates.toString());
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}