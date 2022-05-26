package com.example.pakigsabot.DentalAndEyeClinics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakigsabot.Clinics.DentalClinic.Models.DentalProceduresModel;
import com.example.pakigsabot.DentalAndEyeClinics.Adapters.DentalClinic.DentalClinicProceduresAdapter;
import com.example.pakigsabot.DentalAndEyeClinics.Adapters.DentalClinic.DentalClinicPromoAndDealsAdapter;
import com.example.pakigsabot.DentalAndEyeClinics.Models.DentalClinic.DentalClinicProceduresModel;
import com.example.pakigsabot.DentalAndEyeClinics.Models.DentalClinic.DentalClinicPromoAndDealsModel;
import com.example.pakigsabot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DentalClinicReservation extends AppCompatActivity {
    //Initialization of variables
    ImageView dentalEstIcon, imgBackBtn, gmapBtn;
    Button reserveBtn;
    ImageButton favBtn;
    TextView lblProcedures, lblPolicy, lblDentalClinic, lblAddress, lblContactNum, dentalDatePicker;
    String estID, autoId, estName, estAdd, estImageUrl, estPhoneNum;
    DatePickerDialog.OnDateSetListener dentalDateSetListener;

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore,
    // and our progress bar.
    // initializing our variable for firebase
    // firestore and getting its instance.
    RecyclerView proceduresRecyclerView, promoAndDealsRecyclerView, selectedProcRecyclerView;
    ArrayList<DentalClinicProceduresModel> dentalProceduresArrayList;
    DentalClinicProceduresAdapter dentalProceduresAdapter;
    ArrayList<DentalClinicPromoAndDealsModel> dentalPADArrayList;
    DentalClinicPromoAndDealsAdapter dentalPADAdapter;
    FirebaseFirestore fStoreRef = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dental_clinic_reservation);

        //references
        refs();

        fAuth = FirebaseAuth.getInstance();
        fStoreRef = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        autoId = UUID.randomUUID().toString();


        //Set details
        setDetails();


        //Get Promo and Deals List
        getPromoAndDealsList();

        //Get Procedures List
        getProceduresList();

        //Procedures recyclerview initialization
        proceduresRecyclerView = findViewById(R.id.proceduresRecyclerView);
        proceduresRecyclerView.setHasFixedSize(true);
        proceduresRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        dentalProceduresArrayList = new ArrayList<>();
        //initializing the arraylist where all data is stored
        dentalProceduresArrayList = new ArrayList<DentalClinicProceduresModel>();
        //initializing the adapter
        dentalProceduresAdapter = new DentalClinicProceduresAdapter(DentalClinicReservation.this, dentalProceduresArrayList);
        proceduresRecyclerView.setAdapter(dentalProceduresAdapter);

        //Promo and Deals recyclerview initialization
        promoAndDealsRecyclerView = findViewById(R.id.promoDealsRecyclerView);
        promoAndDealsRecyclerView.setHasFixedSize(true);
        promoAndDealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        dentalPADArrayList = new ArrayList<>();
        //initializing the arraylist where all data is stored
        dentalPADArrayList = new ArrayList<DentalClinicPromoAndDealsModel>();
        //initializing the adapter
        dentalPADAdapter = new DentalClinicPromoAndDealsAdapter(DentalClinicReservation.this, dentalPADArrayList);
        promoAndDealsRecyclerView.setAdapter(dentalPADAdapter);

        //Selected Procedures recyclerview initialization
       /* selectedProcRecyclerView = findViewById(R.id.selectedProceduresRecyclerView);
        selectedProcRecyclerView.setHasFixedSize(true);
        selectedProcRecyclerView.setLayoutManager(new LinearLayoutManager(this));*/

        // adding our array list to our recycler view adapter class.
       /* dentalSelProcArrayList = new ArrayList<>();
        //initializing the arraylist where all data is stored
        dentalSelProcArrayList = new ArrayList<DentalClinicSelectedProceduresModel>();
        //initializing the adapter
        dentalSelProcAdapter = new DentalClinicSelectedProceduresAdapter(DentalClinicReservation.this, dentalSelProcArrayList);*/
        /*selectedProcRecyclerView.setAdapter(dentalSelProcAdapter);*/


        //Get directions of the resort::
        gmapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavorites();
            }
        });
       /* reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ;
            }
        });*/

        //Back to Dental and Eye Clinic Establishments List
        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dentalAndEyeClinicList();
            }
        });

        lblPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(DentalClinicReservation.this);
                builder.setTitle("My title");
                builder.setMessage("This is my message.");

                // add a button
                builder.setPositiveButton("OK", null);

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });




    }



    public void refs() {
        dentalEstIcon = findViewById(R.id.dentalEstIcon);
        lblProcedures = findViewById(R.id.proceduresTxt);
        lblDentalClinic = findViewById(R.id.lblDentalClinic);
        lblAddress = findViewById(R.id.lblAddress);
        lblPolicy = findViewById(R.id.policyTxt);
        favBtn = findViewById(R.id.favoriteBtn);
        reserveBtn = findViewById(R.id.reserveBtnDC);
        imgBackBtn = findViewById(R.id.imgBackBtn);
        gmapBtn = findViewById(R.id.gmapBtn);
        lblContactNum = findViewById(R.id.lblContactNum);
        dentalDatePicker = findViewById(R.id.selectReservationDateTV);


    }

    private void setDetails() {
        //Getting data from recyclerview::
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            estID = extra.getString("EstID");
            estName = extra.getString("EstName");
            estAdd = extra.getString("EstAddress");
            estImageUrl = extra.getString("EstImageUrl");
            estPhoneNum = extra.getString("EstPhoneNum");
        }

        //Display Dental Clinic Details::
        lblDentalClinic.setText(estName);
        lblAddress.setText(estAdd);
        lblContactNum.setText("Contact Number > " + estPhoneNum);
        Picasso.get().load(estImageUrl).into(dentalEstIcon);
    }

    private void getProceduresList() {
        fStoreRef.collection("establishments").document(estID).collection("dental-procedures")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        dentalProceduresArrayList.clear();

                        for (DocumentSnapshot doc : task.getResult()) {
                            DentalClinicProceduresModel list = new DentalClinicProceduresModel(doc.getString("dentalPRRId"),
                                    doc.getString("dentalPRName"),
                                    doc.getString("dentalPRDesc"),
                                    doc.getString("dentalPRRate"),
                                    doc.getString("dentalPRImgUrl"),
                                    doc.getString("estId"));
                            dentalProceduresArrayList.add(list);
                        }
                        dentalProceduresAdapter = new DentalClinicProceduresAdapter(DentalClinicReservation.this, dentalProceduresArrayList);
                        // setting adapter to our recycler view.
                        proceduresRecyclerView.setAdapter(dentalProceduresAdapter);
                        dentalProceduresAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DentalClinicReservation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPromoAndDealsList() {
        fStoreRef.collection("establishments").document(estID).collection("dental-clinic-promo-and-deals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        dentalProceduresArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            DentalClinicPromoAndDealsModel dcPADList = new DentalClinicPromoAndDealsModel(doc.getString("dentalPADId"),
                                    doc.getString("dentalPADName"),
                                    doc.getString("dentalPADDesc"),
                                    doc.getString("dentalPADStartDate"),
                                    doc.getString("dentalPADEndDate"));
                            dentalPADArrayList.add(dcPADList);
                        }
                        dentalPADAdapter = new DentalClinicPromoAndDealsAdapter(DentalClinicReservation.this, dentalPADArrayList);
                        // setting adapter to our recycler view.
                        promoAndDealsRecyclerView.setAdapter(dentalPADAdapter);
                        dentalPADAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DentalClinicReservation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getLocation() {
        //When google map is installed
        //Initialize uri
        Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + lblAddress.getText().toString() + "/" + estAdd);
        //Intent with action view
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            intent.setPackage("com.google.android.app.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    private void addToFavorites(){

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            estID = extra.getString("EstID");
            estName = extra.getString("EstName");
            estAdd = extra.getString("EstAddress");
            estImageUrl = extra.getString("EstImageUrl");
        }

        //Store to favorite list
        Map<String,Object> favoriteEst = new HashMap<>();
        favoriteEst.put("favEstId", autoId);
        favoriteEst.put("estId", estID);
        favoriteEst.put("custId", userId);
        favoriteEst.put("estName", estName);
        favoriteEst.put("estAddress", estAdd);
        favoriteEst.put("estImageUrl", estImageUrl);

        Toast.makeText(DentalClinicReservation.this, estName +"added to Favorites!", Toast.LENGTH_SHORT).show();

        fStoreRef.collection("customers").document(userId).collection("favorites").document(autoId).set(favoriteEst);
    }



    private void dentalAndEyeClinicList() {
        Intent intent = new Intent(getApplicationContext(), DentalAndEyeClinicEstList.class);
        startActivity(intent);
    }
}