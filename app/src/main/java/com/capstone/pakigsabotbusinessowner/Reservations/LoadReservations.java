package com.capstone.pakigsabotbusinessowner.Reservations;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.capstone.pakigsabotbusinessowner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class LoadReservations extends AppCompatActivity {

    //Declaration of variables
    ProgressBar progressBar;
    String userID,est;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String[] estList = new String[]{
            "Restaurant", "Cafe", "Resort", "Dental Clinic", "Eye Clinic", "Spa", "Salon", "Internet Cafe", "Coworking Space"
    };
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_reservations);

        //References::
        progressBar = findViewById(R.id.progressBar);

        //Specific establishment's reservation set-up to be displayed::
        loadEst();
    }

    private void loadEst(){
        //Fetching Data from FireStore DB
        userID = fAuth.getCurrentUser().getUid();
        docRef = fStore.collection("establishments").document(userID);
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                est = value.getString("est_Type");
                if(est.equals(estList[0])){
                    restoEst();
                }else if(est.equals(estList[1])){
                    cafeEst();
                }else if(est.equals(estList[2])){
                    resortEst();
                }else if(est.equals(estList[3])){
                    dentalEst();
                }else if(est.equals(estList[4])){
                    eyeEst();
                }else if(est.equals(estList[5])){
                    spaSalonEst();
                }else if(est.equals(estList[6])){
                    spaSalonEst();
                }else if(est.equals(estList[7])){
                    internetCoEst();
                }else if(est.equals(estList[8])){
                    internetCoEst();
                }
            }
        });
    }
    private void restoEst(){
        Intent in = new Intent(getApplicationContext(), ReservationsRestaurant.class);
        startActivity(in);
    }

    private void cafeEst(){
        Intent in = new Intent(getApplicationContext(), ReservationsCafe.class);
        startActivity(in);
    }

    private void resortEst(){
        Intent in = new Intent(getApplicationContext(), ReservationsResort.class);
        startActivity(in);
    }

    private void dentalEst(){
        Intent in = new Intent(getApplicationContext(), ReservationsDentalClinic.class);
        startActivity(in);
    }

    private void eyeEst(){
        Intent in = new Intent(getApplicationContext(), ReservationsEyeClinic.class);
        startActivity(in);
    }

    private void spaSalonEst(){
        Intent in = new Intent(getApplicationContext(), ReservationsSpaSalon.class);
        startActivity(in);
    }

    private void internetCoEst(){
        Intent in = new Intent(getApplicationContext(), ReservationsICCWS.class);
        startActivity(in);
    }
}