package com.capstone.pakigsabotbusinessowner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class ReservationDetails extends AppCompatActivity {

    ImageView cancelReservation;
    
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

        cancelReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelRsrvScreen();
            }
        });
    }

    public void refs(){
        cancelReservation = findViewById(R.id.cancelReservationImageView);
    }

    public void cancelRsrvScreen(){
        Intent intent = new Intent(getApplicationContext(), CancelReservation.class);
        startActivity(intent);
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