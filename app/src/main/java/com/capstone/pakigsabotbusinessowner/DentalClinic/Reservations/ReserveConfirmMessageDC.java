package com.capstone.pakigsabotbusinessowner.DentalClinic.Reservations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;

public class ReserveConfirmMessageDC extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    EditText custPhoneNum, confirmMessage;
    Button sendSMSBtn;
    String pNumber, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_confirm_message_dc);

        //References
        refs();

        sendSMSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*pNumber = custPhoneNum.getText().toString();
                message = confirmMessage.getText().toString();*/
                sendSMS();
            }
        });
    }

    private void refs() {
        custPhoneNum = findViewById(R.id.custNumRCMET);
        confirmMessage = findViewById(R.id.confirmMessageRCMET);
        sendSMSBtn = findViewById(R.id.sendRCMBtn);
    }

    private void sendSMS() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        message = "This message serve as verification that your RESERVATION has been CONFIRMED. Thank you for trusting Pakigsa-Bot!";
        pNumber = "09201840738";
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(pNumber, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
}