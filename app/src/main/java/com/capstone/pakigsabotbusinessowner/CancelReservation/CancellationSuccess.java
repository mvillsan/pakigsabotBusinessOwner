package com.capstone.pakigsabotbusinessowner.CancelReservation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.pakigsabotbusinessowner.R;

public class CancellationSuccess extends AppCompatActivity {

    ImageView close, detailRect, check;
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
    }

    public void refs(){
        close = findViewById(R.id.closeImageViewCancelSuccess);
        detailRect = findViewById(R.id.detail_rectCancelSuccess);
        check = findViewById(R.id.checkImageView);
        successTxt = findViewById(R.id.cancelSuccessTxt);
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