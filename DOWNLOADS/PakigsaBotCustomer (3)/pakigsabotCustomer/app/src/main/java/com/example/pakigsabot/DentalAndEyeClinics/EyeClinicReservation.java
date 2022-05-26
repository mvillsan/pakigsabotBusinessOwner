package com.example.pakigsabot.DentalAndEyeClinics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakigsabot.DentalAndEyeClinics.Adapters.DentalClinic.DentalClinicProceduresAdapter;
import com.example.pakigsabot.DentalAndEyeClinics.Adapters.DentalClinic.DentalClinicPromoAndDealsAdapter;
import com.example.pakigsabot.DentalAndEyeClinics.Adapters.EyeClinic.EyeClinicProceduresAdapter;
import com.example.pakigsabot.DentalAndEyeClinics.Adapters.EyeClinic.EyeClinicPromoAndDealsAdapter;
import com.example.pakigsabot.DentalAndEyeClinics.Models.DentalClinic.DentalClinicProceduresModel;
import com.example.pakigsabot.DentalAndEyeClinics.Models.DentalClinic.DentalClinicPromoAndDealsModel;
import com.example.pakigsabot.DentalAndEyeClinics.Models.EyeClinic.EyeClinicProceduresModel;
import com.example.pakigsabot.DentalAndEyeClinics.Models.EyeClinic.EyeClinicPromoAndDealsModel;
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

public class EyeClinicReservation extends AppCompatActivity {
    //Initialization of variables
    ImageView eyeEstIcon, imgBackBtn, gmapBtn;
    Button reserveBtn;
    ImageButton favBtn;
    TextView lblProcedures, lblPolicy, lblDentalClinic, lblAddress, lblContactNum, dentalDatePicker;
    String estID, autoId, estName, estAdd, estImageUrl, estPhoneNum;


    // creating variables for our recycler view,
    // array list, adapter, firebase firestore,
    // and our progress bar.
    // initializing our variable for firebase
    // firestore and getting its instance.
    RecyclerView proceduresRecyclerView, promoAndDealsRecyclerView;
    ArrayList<EyeClinicProceduresModel> opticalProceduresArrayList;
    EyeClinicProceduresAdapter opticalProceduresAdapter;
    ArrayList<EyeClinicPromoAndDealsModel> opticalPADArrayList;
    EyeClinicPromoAndDealsAdapter opticalPADAdapter;
    FirebaseFirestore fStoreRef = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_clinic_reservation);

        //References
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
        opticalProceduresArrayList = new ArrayList<>();
        //initializing the arraylist where all data is stored
        opticalProceduresArrayList = new ArrayList<EyeClinicProceduresModel>();
        //initializing the adapter
        opticalProceduresAdapter= new EyeClinicProceduresAdapter(EyeClinicReservation.this, opticalProceduresArrayList);
        proceduresRecyclerView.setAdapter(opticalProceduresAdapter);

        //Promo and Deals recyclerview initialization
        promoAndDealsRecyclerView = findViewById(R.id.promoDealsRecyclerView);
        promoAndDealsRecyclerView.setHasFixedSize(true);
        promoAndDealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        opticalPADArrayList = new ArrayList<>();
        //initializing the arraylist where all data is stored
        opticalPADArrayList = new ArrayList<EyeClinicPromoAndDealsModel>();
        //initializing the adapter
        opticalPADAdapter = new EyeClinicPromoAndDealsAdapter(EyeClinicReservation.this, opticalPADArrayList);
        promoAndDealsRecyclerView.setAdapter(opticalPADAdapter);


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

        //Back to Dental and Eye Clinic Establishments List
        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dentalAndEyeClinicList();
            }
        });

    }

    private void refs() {
        eyeEstIcon = findViewById(R.id.dentalEstIcon);
        lblProcedures = findViewById(R.id.proceduresTxt);
        lblDentalClinic = findViewById(R.id.lblDentalClinic);
        lblAddress = findViewById(R.id.lblAddress);
        lblPolicy = findViewById(R.id.policyTxt);
        favBtn = findViewById(R.id.favoriteBtn);
        reserveBtn = findViewById(R.id.reserveBtnDC);
        imgBackBtn = findViewById(R.id.imgBackBtn);
        gmapBtn = findViewById(R.id.gmapBtn);
        lblContactNum = findViewById(R.id.lblContactNum);

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

        //Display Eye Clinic Details
        lblDentalClinic.setText(estName);
        lblAddress.setText(estAdd);
        lblContactNum.setText("Contact Number > " + estPhoneNum);
        Picasso.get().load(estImageUrl).into(eyeEstIcon);
    }

    private void getProceduresList() {
        fStoreRef.collection("establishments").document(estID).collection("optical-procedures")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        opticalProceduresArrayList.clear();

                        for (DocumentSnapshot doc : task.getResult()) {
                            EyeClinicProceduresModel list = new EyeClinicProceduresModel(doc.getString("opticalPRRId"),
                                    doc.getString("opticalPRName"),
                                    doc.getString("opticalPRDesc"),
                                    doc.getString("opticalPRRate"),
                                    doc.getString("opticalPRImgUrl"),
                                    doc.getString("estId"));
                            opticalProceduresArrayList.add(list);
                        }
                        opticalProceduresAdapter = new EyeClinicProceduresAdapter(EyeClinicReservation.this, opticalProceduresArrayList);
                        // setting adapter to our recycler view.
                        proceduresRecyclerView.setAdapter(opticalProceduresAdapter);
                        opticalProceduresAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EyeClinicReservation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPromoAndDealsList() {
        fStoreRef.collection("establishments").document(estID).collection("eye-clinic-promo-and-deals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        opticalProceduresArrayList.clear();

                        for(DocumentSnapshot doc : task.getResult()){
                            EyeClinicPromoAndDealsModel dcPADList = new EyeClinicPromoAndDealsModel(doc.getString("opticalPADId"),
                                    doc.getString("opticalPADName"),
                                    doc.getString("opticalPADDesc"),
                                    doc.getString("opticalPADStartDate"),
                                    doc.getString("opticalPADEndDate"));
                            opticalPADArrayList.add(dcPADList);
                        }
                        opticalPADAdapter = new EyeClinicPromoAndDealsAdapter(EyeClinicReservation.this, opticalPADArrayList);
                        // setting adapter to our recycler view.
                        promoAndDealsRecyclerView.setAdapter(opticalPADAdapter);
                        opticalPADAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EyeClinicReservation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

        Toast.makeText(EyeClinicReservation.this, estName +"added to Favorites!", Toast.LENGTH_SHORT).show();

        fStoreRef.collection("customers").document(userId).collection("favorites").document(autoId).set(favoriteEst);
    }



    private void dentalAndEyeClinicList() {
        Intent intent = new Intent(getApplicationContext(), DentalAndEyeClinicEstList.class);
        startActivity(intent);
    }
}