package com.capstone.pakigsabotbusinessowner.MonthlySubscription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.BuildConfig;
import com.capstone.pakigsabotbusinessowner.MainActivity;
import com.capstone.pakigsabotbusinessowner.NavBar.BottomNavigation;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.SignIn;
import com.capstone.pakigsabotbusinessowner.SignUp;
import com.capstone.pakigsabotbusinessowner.SignUpRequirement.PaySubscription;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.cancel.OnCancel;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.error.ErrorInfo;
import com.paypal.checkout.error.OnError;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.Order;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PayPalButton;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PayMonthlySub extends AppCompatActivity {

    Button subscribeBtn,notnowBtn;
    ImageView backBtnSU;
    CheckBox checkBox;
    TextView termsTxt;
    PayPalButton payPalButton;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String est_id;
    String amount = "100";

    //Paypal Client ID::
    private static final String YOUR_CLIENT_ID ="AdClW46sSl6CwjW-d6zp2FSxDwmzsZUDuR7uEwmKCPUupjrl7AsY8DDaeaoUqftAVhP8Qtid9sAN-50o";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_monthly_sub);

        //References::
        refs();

        termsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewTerms();
            }
        });

        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    payPalButton.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(PayMonthlySub.this, "Agree to the Terms and Conditions First!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        notnowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        backBtnSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //Paypal Integration::
        CheckoutConfig config = new CheckoutConfig(
                getApplication(),
                YOUR_CLIENT_ID,
                Environment.SANDBOX,
                String.format("%s://paypalpay", BuildConfig.APPLICATION_ID),
                CurrencyCode.PHP,
                UserAction.PAY_NOW,
                new SettingsConfig(
                        true,
                        false
                )
        );
        PayPalCheckout.setConfig(config);

        payPalButton.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.PHP)
                                                        .value(amount)
                                                        .build()
                                        )
                                        .build()
                        );
                        Order order = new Order(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", result));
                                //Update est-subscription expiry date::
                                updateExpDate();
                                //Update est account status::
                                updateAccountStatusDB();
                                //Redirected to Home Screen
                                homeScreen();
                            }
                        });
                    }
                },
                new OnCancel() {
                    @Override
                    public void onCancel() {
                        Log.d("OnCancel", "Buyer cancelled the PayPal experience.");
                    }
                },
                new OnError() {
                    @Override
                    public void onError(@NotNull ErrorInfo errorInfo) {
                        Log.d("OnError", String.format("Error: %s", errorInfo));
                    }
                }
        );
    }

    private void refs(){
        subscribeBtn = findViewById(R.id.subscribeBtn);
        notnowBtn = findViewById(R.id.notnowBtn);
        backBtnSU = findViewById(R.id.backBtnSU);
        checkBox = findViewById(R.id.agreementCheckBox);
        termsTxt = findViewById(R.id.termsTxt);
        payPalButton = findViewById(R.id.payPalButton);
        payPalButton.setVisibility(View.GONE);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    private void updateExpDate(){
        //Getting the date today for transaction date.
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String dateToday = df.format(date);

        //Getting date after 30 days
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 30);
        Date expDate = c.getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
        String expDateStr = df2.format(expDate);

        //Save Details to Est Subscription Collection::
        est_id = fAuth.getCurrentUser().getUid();

        DocumentReference docuRef = fStore.collection("est-subscriptions").document(est_id);
        Map<String,Object> sub = new HashMap<>();
        sub.put("subs_date", dateToday);
        sub.put("subs_expDate", expDateStr);

        docuRef.update(sub).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Subscription", "onSignUpSuccess: Data is Stored for " + est_id);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Subscription", "onSignUpFailure: " + e.toString());
            }
        });
    }

    private void updateAccountStatusDB(){
        DocumentReference docuRef = fStore.collection("establishments").document(est_id);
        Map<String,Object> edited = new HashMap<>();
        edited.put("est_status", "Classic");
        docuRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PayMonthlySub.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void homeScreen(){
        Intent intent = new Intent(getApplicationContext(), BottomNavigation.class);
        startActivity(intent);
    }

    //Go back to Sign In Page::
    private void signIn(){
        startActivity(new Intent(getApplicationContext(), SignIn.class));
    }

    //View terms, conditions and renewal terms::
    private void viewTerms(){
        Intent intent = new Intent(getApplicationContext(), MonthlySubsTerms.class);
        startActivity(intent);
    }
}