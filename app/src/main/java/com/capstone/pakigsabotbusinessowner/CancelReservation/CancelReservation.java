package com.capstone.pakigsabotbusinessowner.CancelReservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.pakigsabotbusinessowner.Profile.Profile;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.SignIn;

public class CancelReservation extends AppCompatActivity {

    ImageView confirm,close, cancelAction, detailRect;
    TextView cancelRsrvTxt, dateTxtCancelRsrv,jan11TxtCancelRsrv,timeTxtCancelRsrv,pmTxtCancelRsrv,
            customerNameTxtCancelRsrv,janeTxtCancelRsrv,reservedTxtCancelRsrv,terraceTxtCancelRsrv,
            rfTxtCancelRsrv,pesosTxtCancelRsrv,rIDTxtCancelRsrv,idTxtCancelRsrv,confirmTxt,cancelTxt;

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

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        cancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void refs(){
        confirm = findViewById(R.id.confirmImageView);
        close = findViewById(R.id.closeImageViewCancelRsrv);
        cancelAction = findViewById(R.id.cancelActionCancelRsrv);
        detailRect = findViewById(R.id.detail_rectCancelRsrv);
        cancelRsrvTxt = findViewById(R.id.cancelRsrvTxt);
        dateTxtCancelRsrv = findViewById(R.id.dateTxtCancelRsrv);
        jan11TxtCancelRsrv = findViewById(R.id.jan11TxtCancelRsrv);
        timeTxtCancelRsrv = findViewById(R.id.timeTxtCancelRsrv);
        pmTxtCancelRsrv = findViewById(R.id.pmTxtCancelRsrv);
        customerNameTxtCancelRsrv = findViewById(R.id.customerNameTxtCancelRsrv);
        janeTxtCancelRsrv = findViewById(R.id.janeTxtCancelRsrv);
        reservedTxtCancelRsrv = findViewById(R.id.reservedTxtCancelRsrv);
        terraceTxtCancelRsrv = findViewById(R.id.terraceTxtCancelRsrv);
        rfTxtCancelRsrv = findViewById(R.id.rfTxtCancelRsrv);
        pesosTxtCancelRsrv = findViewById(R.id.pesosTxtCancelRsrv);
        rIDTxtCancelRsrv = findViewById(R.id.rIDTxtCancelRsrv);
        idTxtCancelRsrv = findViewById(R.id.idTxtCancelRsrv);
        confirmTxt = findViewById(R.id.confirmTxtCancelRsrv);
        cancelTxt = findViewById(R.id.cancelActionTxtCancelRsrv);
    }

    public void cancelReason(){
        Intent intent = new Intent(getApplicationContext(), CancellationReason.class);
        startActivity(intent);
    }

    public void onBackPressed(){
        super.onBackPressed();
    }

    public void close(){
        detailRect.setVisibility(View.INVISIBLE);
        close.setVisibility(View.INVISIBLE);
        confirm.setVisibility(View.INVISIBLE);
        cancelAction.setVisibility(View.INVISIBLE);
        cancelRsrvTxt.setVisibility(View.INVISIBLE);
        dateTxtCancelRsrv.setVisibility(View.INVISIBLE);
        jan11TxtCancelRsrv.setVisibility(View.INVISIBLE);
        timeTxtCancelRsrv.setVisibility(View.INVISIBLE);
        pmTxtCancelRsrv.setVisibility(View.INVISIBLE);
        customerNameTxtCancelRsrv.setVisibility(View.INVISIBLE);
        janeTxtCancelRsrv.setVisibility(View.INVISIBLE);
        reservedTxtCancelRsrv.setVisibility(View.INVISIBLE);
        terraceTxtCancelRsrv.setVisibility(View.INVISIBLE);
        rfTxtCancelRsrv.setVisibility(View.INVISIBLE);
        pesosTxtCancelRsrv.setVisibility(View.INVISIBLE);
        rIDTxtCancelRsrv.setVisibility(View.INVISIBLE);
        idTxtCancelRsrv.setVisibility(View.INVISIBLE);
        confirmTxt.setVisibility(View.INVISIBLE);
        cancelTxt.setVisibility(View.INVISIBLE);
    }
}