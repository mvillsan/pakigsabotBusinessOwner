package com.capstone.pakigsabotbusinessowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CancelReservation extends AppCompatActivity {

    ImageView confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_reservation);

        refs();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelReason();
            }
        });
    }

    public void refs(){
        confirm = findViewById(R.id.confirmImageView);
    }

    public void cancelReason(){
        Intent intent = new Intent(getApplicationContext(), CancellationReason.class);
        startActivity(intent);
    }
}