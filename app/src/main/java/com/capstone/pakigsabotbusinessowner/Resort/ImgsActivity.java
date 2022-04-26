/*
package com.capstone.pakigsabotbusinessowner.Resort;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;

import com.capstone.pakigsabotbusinessowner.Adapter.ImageAdapter;
import com.capstone.pakigsabotbusinessowner.Upload.UploadResortImgDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ImgsActivity extends AppCompatActivity {

    RecyclerView resortsServicesRV;
    ImageAdapter resortsAdapter;
    FirebaseFirestore fStore;
    CollectionReference colRef;
    List<UploadResortImgDetails> resortUploads;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgs);

        //References::
        refs();

        //Progress::
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        //Set recycler view defaults::
        resortsServicesRV.setHasFixedSize(true);
        resortsServicesRV.setLayoutManager(new LinearLayoutManager(this));

        //Fetching data from firestore::
        resortUploads = new ArrayList<>();
        fStore = FirebaseFirestore.getInstance();
        resortsAdapter = new ImageAdapter(ImgsActivity.this, resortUploads);
        resortsServicesRV.setAdapter(resortsAdapter);

        EventChangeListener();
    }

    private void refs(){
        resortsServicesRV = findViewById(R.id.resortsServicesRV);
    }

    //show Resort's rooms/facilities::
    private void EventChangeListener() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userID = fAuth.getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();
        colRef = fStore.collection("establishments").document(userID).collection("resortEst");
        colRef.document(userID)
                .collection("rooms-facilities")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Toast.makeText(ImgsActivity.this,"Firestore error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED) {
                                resortUploads.add(dc.getDocument().toObject(UploadResortImgDetails.class));
                            }
                            resortsAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });
    }
}*/
