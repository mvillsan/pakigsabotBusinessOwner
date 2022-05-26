package com.capstone.pakigsabotbusinessowner.Reservations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;

public class EmailToCustomer extends AppCompatActivity {
    //Initialization of variables::
    String custID, custEmailAddress, custName;
    ImageView backBtnEmail;
    EditText emailAddEditTextEM, subjectETEM, bodyEDEM;
    Button sendEmailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_to_customer);

        //References::
        refs();

        //Getting data from recyclerview::
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            custID = extra.getString("CustomerID");
            custName = extra.getString("CustomerName");
            custEmailAddress = extra.getString("CustomerEmail");
        }

        //Setting fetched data to edittext
        emailAddEditTextEM.setText(custEmailAddress);
        subjectETEM.setText(custName + " Reservation Concern");

        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!emailAddEditTextEM.getText().toString().isEmpty() && !subjectETEM.getText().toString().isEmpty()
                        && !bodyEDEM.getText().toString().isEmpty()){
                    //Connecting to GMAIL application
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddEditTextEM.getText().toString()});
                    intent.putExtra(Intent.EXTRA_SUBJECT, subjectETEM.getText().toString());
                    intent.putExtra(Intent.EXTRA_TEXT, bodyEDEM.getText().toString());
                    intent.setData(Uri.parse("mailto:")); //ensure that only email application will open
                    if(intent.resolveActivity(getPackageManager()) != null){
                        startActivity(Intent.createChooser(intent, "Send Email"));
                    }else{
                        Toast.makeText(EmailToCustomer.this,"There's no email application that supports this action.", Toast.LENGTH_SHORT).show();
                    }
                    //Clear text fields
                    subjectETEM.setText(null);
                    bodyEDEM.setText(null);
                }else{
                    Toast.makeText(EmailToCustomer.this,"Please input all the fields! ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reservationsList();
            }
        });
    }


    private void refs() {
        emailAddEditTextEM = findViewById(R.id.emailAddEditTextEM);
        subjectETEM = findViewById(R.id.subjectETEM);
        bodyEDEM = findViewById(R.id.bodyEDEM);
        sendEmailBtn = findViewById(R.id.sendEmailBtn);
        backBtnEmail = findViewById(R.id.backBtnEmail);
    }
    private void reservationsList() {
        Intent intent = new Intent(getApplicationContext(), ViewReservationsDentalClinic.class);
        startActivity(intent);
    }



}