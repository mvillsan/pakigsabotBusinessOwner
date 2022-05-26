package com.capstone.pakigsabotbusinessowner.EstablishmentRules.Resto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.DentalClinic.AddPromoAndDealsDentalClinic;
import com.capstone.pakigsabotbusinessowner.DentalClinic.PromoAndDeals.PromoAndDealsDentalClinic;
import com.capstone.pakigsabotbusinessowner.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddEstRulesResto extends AppCompatActivity {
    ImageView backBtn, saveBtn;
    TextInputEditText estRulesName, estRulesDesc;
    TextInputLayout estRulesNameLayout, estRulesDescLayout;
    FirebaseAuth fAuth;
    FirebaseFirestore fStoreRef;
    String userId, autoId, txtEstRulesName, txtEstRulesDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_est_rules_resto);
        //References
        refs();

        fAuth = FirebaseAuth.getInstance();
        fStoreRef = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        autoId = UUID.randomUUID().toString();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               restoRules();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRestoEstRules();
            }
        });
    }

    private void refs() {
        estRulesName = findViewById(R.id.estRulesNameTxt);
        estRulesDesc = findViewById(R.id.estRulesDescTxt);

        backBtn = findViewById(R.id.backBtnAddEstRules);
        saveBtn = findViewById(R.id.saveEstRulesBtn);
        estRulesNameLayout = findViewById(R.id.estRulesNameLayout);
        estRulesDescLayout = findViewById(R.id.estRulesDescLayout);
    }
    private void restoRules() {
        Intent intent = new Intent(getApplicationContext(), EstRulesResto.class);
        startActivity(intent);
    }

    private void saveRestoEstRules() {
        txtEstRulesName = estRulesName.getText().toString().trim();
        txtEstRulesDesc = estRulesDesc.getText().toString().trim();


        //Validations::
        if (txtEstRulesName.isEmpty() || txtEstRulesDesc.isEmpty()) {
            Toast.makeText(AddEstRulesResto.this, "Some fields are EMPTY.", Toast.LENGTH_SHORT).show();
        } else {
            if (txtEstRulesName.isEmpty()) {
                estRulesNameLayout.setError("Enter Name of Rule");
            } else {
                Boolean validName = txtEstRulesName.matches("[A-Za-z][A-Za-z ]*+");
                if (!validName) {
                    estRulesNameLayout.setError("Invalid Rule Name");
                } else {
                    estRulesNameLayout.setErrorEnabled(false);
                    estRulesNameLayout.setError("");
                }
            }
            if (txtEstRulesDesc.isEmpty()) {
                estRulesDescLayout.setError("Enter Description");
            } else {
                estRulesDescLayout.setErrorEnabled(false);
                estRulesDescLayout.setError("");
            }

                Toast.makeText(AddEstRulesResto.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                //Store promo and deals details
                Map<String, Object> restoEstRules = new HashMap<>();
                restoEstRules.put("restoERId", autoId);
                restoEstRules.put("restoERName", txtEstRulesName);
                restoEstRules.put("restoERDesc", txtEstRulesDesc);
                restoEstRules.put("estId", userId);

                //To save inside the document of the userID, under the dental-procedures collection
                fStoreRef.collection("establishments").document(userId).collection("resto-est-rules").document(autoId).set(restoEstRules);

                restoRules();
            }
        }
    }
