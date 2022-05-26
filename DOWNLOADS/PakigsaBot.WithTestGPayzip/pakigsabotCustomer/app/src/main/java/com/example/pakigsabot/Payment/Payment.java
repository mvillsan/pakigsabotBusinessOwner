package com.example.pakigsabot.Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.pakigsabot.R;
import com.example.pakigsabot.databinding.ActivityPaymentBinding;

public class Payment extends AppCompatActivity {

    private ActivityPaymentBinding binding;
    public static final String GOOGLE_PAY_PACKAGE_NAME = "";
    String amount;
    String name = "ANGEL CASTLE";
    String upiId = "asdfghjkl@123";
    String transactionNote = "Paid transaction fee";
    String status;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        binding= ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //used view binding to reference the object instead of findByViewId
        binding.googlePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = binding.amountEditText.getText().toString();
                //if amount  is not empty
                if(!amount.isEmpty()){
                    uri = getUpiPaymentUri(name, upiId,transactionNote, amount);

                    //call the method to pay with GPay
                    payWithGPay();
                }
            }
        });
    }

    private void payWithGPay() {
        if(isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME))
    }

    private boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);

            return true;
        }

        catch(PackageManager.NameNotFoundException e){
            return false;
        }
    }

    //unified payments interface
    private static Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount)
    {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pId", upiId)
                .appendQueryParameter("pName", name)
                .appendQueryParameter("transNote", transactionNote)
                .appendQueryParameter("amt", amount)
                .appendQueryParameter("currency", "PHP")
                .build();
    }
}