package com.capstone.pakigsabotbusinessowner.Resort;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddPolicyResort extends AppCompatActivity {

    //Initializations of variable::
    ImageView backBtn, saveBtn;
    EditText policyTxt;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID, autoID, policyTxtStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_policy_resort);

        //References::
        refs();

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();
        autoID = UUID.randomUUID().toString();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingUp();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(policyTxt.getText().toString().isEmpty()){
                    Toast.makeText(AddPolicyResort.this, "Please Input Rule", Toast.LENGTH_SHORT).show();
                }else{
                    saveDetailsDB();
                }
            }
        });
    }

    private void refs(){
        backBtn = findViewById(R.id.backBtn);
        policyTxt = findViewById(R.id.policyTxt);
        saveBtn = findViewById(R.id.saveBtn);
    }

    private void settingUp(){
        Intent intent = new Intent(getApplicationContext(), ResortCancellationPol.class);
        startActivity(intent);
    }


    private void saveDetailsDB(){
        policyTxtStr = policyTxt.getText().toString();

        //Save to database::
        Map<String, Object> rules = new HashMap<>();
        rules.put("resort_polID", autoID);
        rules.put("resort_desc", policyTxtStr);

        //To save inside the document of the userID, under the resort-rooms-facilities collection
        fStore.collection("establishments").document(userID).collection("resort-cancellation-pol").document(autoID).set(rules);

        Toast.makeText(this, "Policy has been Added", Toast.LENGTH_SHORT).show();
        //Back to Resorts' Services
        resortPolicy();
    }

    private void resortPolicy(){
        Intent intent = new Intent(getApplicationContext(), ResortCancellationPol.class);
        startActivity(intent);
    }
}