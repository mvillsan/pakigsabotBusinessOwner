package com.capstone.pakigsabotbusinessowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CancellationReason extends AppCompatActivity {

    ImageView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation_reason);

        refs();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelSuccess();
            }
        });
    }

    public void refs(){
        submit = findViewById(R.id.submitImageView);
    }

    public void cancelSuccess(){
        Intent intent = new Intent(getApplicationContext(), CancellationSuccess.class);
        startActivity(intent);
    }
}