package com.capstone.pakigsabotbusinessowner.SignUpRequirement;

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
import com.capstone.pakigsabotbusinessowner.MonthlySubscription.MonthlySubsTerms;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.SignUp;
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

import java.util.ArrayList;

public class PaySubscription extends AppCompatActivity {

    Button subscribeBtn,notnowBtn;
    ImageView backBtnSU;
    CheckBox checkBox;
    TextView termsTxt;
    PayPalButton payPalButton;
    String amount = "100";

    //Paypal Client ID::
    private static final String YOUR_CLIENT_ID ="AdClW46sSl6CwjW-d6zp2FSxDwmzsZUDuR7uEwmKCPUupjrl7AsY8DDaeaoUqftAVhP8Qtid9sAN-50o";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_subscription);

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
                    Toast.makeText(PaySubscription.this, "Agree to the Terms and Conditions First!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        notnowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainScreen();
            }
        });

        backBtnSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainScreen();
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
                                //Redirected to Sign Up Page
                                signUp();
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
    }

    private void signUp(){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    //Go back to Main Page::
    private void mainScreen(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    //View terms, conditions and renewal terms::
    private void viewTerms(){
        Intent intent = new Intent(getApplicationContext(), ClassicSubsTerms.class);
        startActivity(intent);
    }
}