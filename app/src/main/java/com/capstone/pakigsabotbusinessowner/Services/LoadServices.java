package com.capstone.pakigsabotbusinessowner.Services;

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

public class LoadServices extends AppCompatActivity {

    ProgressBar progressBar;
    String userID,est;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String[] estList = new String[]{
            "Restaurant", "Cafe", "Resort", "Dental Clinic", "Eye Clinic", "Spa", "Salon", "Internet Cafe", "Coworking Space"
    };
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_services);

        //References::
        progressBar = findViewById(R.id.progressBar);

        //Specific establishment's service set-up to be displayed::
        loadEst();
    }

    private void loadEst(){
        //Fetching Data from FireStore DB
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        docRef = fStore.collection("establishments").document(userID);
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                est = value.getString("est_Type");
                if(est.equals(estList[0])){
//                    restoEst();
                    Intent in = new Intent(getApplicationContext(), ServicesRestaurant.class);
                    startActivity(in);
                }else if(est.equals(estList[1])){
                    cafeEst();
                }else if(est.equals("Resort")){
//                    resortEst();
                    Intent in = new Intent(getApplicationContext(), ServicesResort.class);
                    startActivity(in);
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
   /* private void restoEst(){
        Intent in = new Intent(getApplicationContext(), ServicesRestaurant.class);
        startActivity(in);
    }*/

    private void cafeEst(){
        Intent in = new Intent(getApplicationContext(), ServicesCafe.class);
        startActivity(in);
    }

  /*  private void resortEst(){
        Intent in = new Intent(getApplicationContext(), ServicesResort.class);
        startActivity(in);
    }*/

    private void dentalEst(){
        Intent in = new Intent(getApplicationContext(), ServicesDentalClinic.class);
        startActivity(in);
    }

    private void eyeEst(){
        Intent in = new Intent(getApplicationContext(), ServicesEyeClinic.class);
        startActivity(in);
    }

    private void spaSalonEst(){
        Intent in = new Intent(getApplicationContext(), ServicesSpaSalon.class);
        startActivity(in);
    }

    private void internetCoEst(){
        Intent in = new Intent(getApplicationContext(), ServicesInternetCCoworking.class);
        startActivity(in);
    }
}