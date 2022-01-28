package com.capstone.pakigsabotbusinessowner.Reservations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.CancelReservation.CancelReservation;
import com.capstone.pakigsabotbusinessowner.ConfirmReservation.ConfirmReservationSuccess;
import com.capstone.pakigsabotbusinessowner.R;

public class ReservationDetails extends AppCompatActivity {

    ImageView cancelReservation, confirmReservation, close;
    
    private static final String tag = "ReservationDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_details);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("rd") != null){
                Toast.makeText(getApplicationContext(), "data: " + bundle.getString("rd"),
                        Toast.LENGTH_SHORT).show();
            }
        }

        refs();

        confirmReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmedReservation();
            }
        });

        cancelReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelRsrvScreen();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void refs(){
        cancelReservation = findViewById(R.id.cancelReservationImageView);
        confirmReservation = findViewById(R.id.confirmReservationImageView);
        close = findViewById(R.id.closeImageViewRDetails);
    }

    private void confirmedReservation(){
        Intent intent = new Intent(getApplicationContext(), ConfirmReservationSuccess.class);
        startActivity(intent);
    }

    public void cancelRsrvScreen(){
        Intent intent = new Intent(getApplicationContext(), CancelReservation.class);
        startActivity(intent);
    }

    public void onBackPressed(){
        super.onBackPressed();
    }


    /*//References:
    ImageView cancelRsrv, close;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.reservation_details, container, false);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getDialog().dismiss();
            }
        });

        cancelRsrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }*/



     /*   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.reservation_details);
    }*/
}