package com.capstone.pakigsabotbusinessowner.ConfirmReservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.capstone.pakigsabotbusinessowner.CancelReservation.CancellationReason;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Reservations.ReservationWtConfirmed;

public class ConfirmReservationSuccess extends AppCompatActivity {

    ImageView confirmImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reservation_success);

        //References:
        refs();

        confirmImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reservationWtConfirm();
            }
        });
    }

    public void refs(){
        confirmImageView = findViewById(R.id.confirmImageView);
    }

    private void reservationWtConfirm(){
        Intent intent = new Intent(getApplicationContext(), ReservationWtConfirmed.class);
        startActivity(intent);
    }


}