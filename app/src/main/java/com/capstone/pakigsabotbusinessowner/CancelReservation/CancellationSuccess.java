package com.capstone.pakigsabotbusinessowner.CancelReservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Reservations.ReservationWtCancel;

public class CancellationSuccess extends AppCompatActivity {

    ImageView close, detailRect, check, closeImageViewCancelSuccess;
    TextView successTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation_success);

        refs();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        closeImageViewCancelSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedReservations();
            }
        });
    }

    public void refs(){
        close = findViewById(R.id.closeImageViewCancelSuccess);
        detailRect = findViewById(R.id.detail_rectCancelSuccess);
        check = findViewById(R.id.checkImageView);
        successTxt = findViewById(R.id.cancelSuccessTxt);
        closeImageViewCancelSuccess = findViewById(R.id.closeImageViewCancelSuccess);
    }

    private void updatedReservations(){
        Intent intent = new Intent(getApplicationContext(), ReservationWtCancel.class);
        startActivity(intent);
    }
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void close(){
        detailRect.setVisibility(View.INVISIBLE);
        close.setVisibility(View.INVISIBLE);
        check.setVisibility(View.INVISIBLE);
        successTxt.setVisibility(View.INVISIBLE);
    }
}