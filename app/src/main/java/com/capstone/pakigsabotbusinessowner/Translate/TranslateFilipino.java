package com.capstone.pakigsabotbusinessowner.Translate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;

public class TranslateFilipino extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_filipino);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("transFil") != null){
                Toast.makeText(getApplicationContext(), "data: " + bundle.getString("transFil"),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}