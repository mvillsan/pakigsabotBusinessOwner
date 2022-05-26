package com.capstone.pakigsabotbusinessowner.Resort;

import static com.airbnb.lottie.L.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.DentalClinic.AddPromoAndDealsDentalClinic;
import com.capstone.pakigsabotbusinessowner.DentalClinic.PromoAndDeals.PromoAndDealsDentalClinic;
import com.capstone.pakigsabotbusinessowner.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddPromoAndDealsResort extends AppCompatActivity {
    ImageView backBtn, saveBtn;
    TextInputEditText padName, padDesc;
    TextView padStartDate, padEndDate;
    TextInputLayout padNameLayout, padDescLayout, padEffectiveStartDateLayout, padEffectiveEndDateLayout;
    FirebaseAuth fAuth;
    FirebaseFirestore fStoreRef;
    String userId, autoId, txtPADName, txtPADDesc, txtPADStartDate, txtPADEndDate;
    DatePickerDialog.OnDateSetListener resortPADStartDateSetListener, resortPADEndDateSetListener;
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_promo_and_deals_resort);

        //References
        refs();

        fAuth = FirebaseAuth.getInstance();
        fStoreRef = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        autoId = UUID.randomUUID().toString();

        //StartDate
        padStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar1 = Calendar.getInstance();
                int year = calendar1.get(Calendar.YEAR);
                int month = calendar1.get(Calendar.MONTH);
                int day = calendar1.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog1 = new DatePickerDialog(
                        AddPromoAndDealsResort.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        resortPADStartDateSetListener,
                        year,month,day);

                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();
            }
        });

        resortPADStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day ){
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yy: "  + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                padStartDate.setText(date);
            }
        };

        //EndDate
        padEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar2 = Calendar.getInstance();
                int year = calendar2.get(Calendar.YEAR);
                int month = calendar2.get(Calendar.MONTH);
                int day = calendar2.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog2 = new DatePickerDialog(
                        AddPromoAndDealsResort.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        resortPADEndDateSetListener,
                        year,month,day);

                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();
            }
        });

        resortPADEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day ){
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yy: "  + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                padEndDate.setText(date);
            }
        };

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resortPromoAndDeals();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePADDetailsToFirestore();
            }
        });
    }

    public void refs() {
        padName = findViewById(R.id.padNameTxt);
        padDesc = findViewById(R.id.padDescTxt);
        padStartDate = findViewById(R.id.padESDateTxt);
        padEndDate = findViewById(R.id.padEEDateTxt);

        backBtn = findViewById(R.id.backBtnAddPromoDeals);
        saveBtn = findViewById(R.id.savePADBtn);
        padNameLayout = findViewById(R.id.padNameLayout);
        padDescLayout = findViewById(R.id.padDescLayout);
        padEffectiveStartDateLayout = findViewById(R.id.padEffectiveStartDateLayout);
        padEffectiveEndDateLayout = findViewById(R.id.padEffectiveEndDateLayout);

    }

    private void resortPromoAndDeals() {
        Intent intent = new Intent(getApplicationContext(), PromoAndDealsResort.class);
        startActivity(intent);
    }

    private void savePADDetailsToFirestore() {
        txtPADName = padName.getText().toString().trim();
        txtPADDesc = padDesc.getText().toString().trim();
        txtPADStartDate = padStartDate.getText().toString().trim();
        txtPADEndDate = padEndDate.getText().toString().trim();

        //Validations::
        if (txtPADName.isEmpty() || txtPADDesc.isEmpty() || txtPADStartDate.isEmpty() || txtPADEndDate.isEmpty()) {
            Toast.makeText(AddPromoAndDealsResort.this, "Some fields are EMPTY.", Toast.LENGTH_SHORT).show();
        } else {
            if (txtPADName.isEmpty()) {
                padNameLayout.setError("Enter Name of Promo or Deals");
            } else {
                Boolean validName = txtPADName.matches("[A-Za-z][A-Za-z ]*+");
                if (!validName) {
                    padNameLayout.setError("Invalid Promo or Deals Name");
                } else {
                    padNameLayout.setErrorEnabled(false);
                    padNameLayout.setError("");
                }
            }
            if (txtPADDesc.isEmpty()) {
                padDescLayout.setError("Enter Description");
            } else {
                padDescLayout.setErrorEnabled(false);
                padDescLayout.setError("");
            }
            if (txtPADStartDate.isEmpty()) {
                padEffectiveStartDateLayout.setError("Enter Rate");
            } else {
                padEffectiveStartDateLayout.setErrorEnabled(false);
                padEffectiveStartDateLayout.setError("");
                if (txtPADEndDate.isEmpty()) {
                    padEffectiveEndDateLayout.setError("Enter Rate");
                } else {
                    padEffectiveEndDateLayout.setErrorEnabled(false);
                    padEffectiveEndDateLayout.setError("");
                }

                Toast.makeText(AddPromoAndDealsResort.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                //Store promo and deals details
                Map<String, Object> resortPAD = new HashMap<>();
                resortPAD.put("resortPADId", autoId);
                resortPAD.put("resortPADName", txtPADName);
                resortPAD.put("resortPADDesc", txtPADDesc);
                resortPAD.put("resortPADStartDate", txtPADStartDate);
                resortPAD.put("resortPADEndDate", txtPADEndDate);

                //To save inside the document of the userID, under the dental-procedures collection
                fStoreRef.collection("establishments").document(userId).collection("resort-promo-and-deals").document(autoId).set(resortPAD);

                resortPromoAndDeals();
            }

        }
    }
}