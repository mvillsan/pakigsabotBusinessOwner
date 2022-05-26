package com.capstone.pakigsabotbusinessowner.Resort;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Services.ServicesResort;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddRuleResort extends AppCompatActivity {

    //Initializations of variable::
    ImageView backBtn, saveBtn;
    EditText ruleTxt;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID, autoID, ruleTxtStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule_resort);

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
                if(ruleTxt.getText().toString().isEmpty()){
                    Toast.makeText(AddRuleResort.this, "Please Input Rule", Toast.LENGTH_SHORT).show();
                }else{
                    saveDetailsDB();
                }
            }
        });
    }

    private void refs(){
        backBtn = findViewById(R.id.backBtn);
        ruleTxt = findViewById(R.id.ruleTxt);
        saveBtn = findViewById(R.id.saveBtn);
    }

    private void settingUp(){
        Intent intent = new Intent(getApplicationContext(), ResortRules.class);
        startActivity(intent);
    }


    private void saveDetailsDB(){
        ruleTxtStr = ruleTxt.getText().toString();

        //Save to database::
        Map<String, Object> rules = new HashMap<>();
        rules.put("resort_ruleID", autoID);
        rules.put("resort_desc", ruleTxtStr);

        //To save inside the document of the userID, under the resort-rooms-facilities collection
        fStore.collection("establishments").document(userID).collection("resort-rules").document(autoID).set(rules);

        Toast.makeText(this, "Rule Added", Toast.LENGTH_SHORT).show();
        //Back to Resorts' Services
        resortRules();
    }

    private void resortRules(){
        Intent intent = new Intent(getApplicationContext(), ResortRules.class);
        startActivity(intent);
    }
}