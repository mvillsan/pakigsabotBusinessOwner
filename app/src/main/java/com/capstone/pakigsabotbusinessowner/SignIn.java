package com.capstone.pakigsabotbusinessowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignIn extends AppCompatActivity {

    ImageView prev;
    TextView signup;
    TextInputEditText emailAddEditTxt,passEditTxt;
    TextInputLayout emailTxtInputL, passTxtInputL;
    Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        refs();

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                welcomeScreen();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpScreen();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInCustomer();
            }
        });

        passEditTxt.addTextChangedListener(new ValidationTextWatcher(passEditTxt));
        emailAddEditTxt.addTextChangedListener(new ValidationTextWatcher(emailAddEditTxt));
    }
    public void refs(){
        prev = findViewById(R.id.backBtnSignIn);
        signup = findViewById(R.id.signUpTxtView);
        signInBtn = findViewById(R.id.signInBtnn);
        emailAddEditTxt = findViewById(R.id.emailAddEditTxtSI);
        passEditTxt = findViewById(R.id.passwordEditTxtSI);
        emailTxtInputL = findViewById(R.id.emailTxtInputLayout);
        passTxtInputL = findViewById(R.id.passwordTextInputLayout);
    }

    public void welcomeScreen(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void signUpScreen(){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    //Validations for Signing In on the Application
    public boolean signInCustomer(){
        boolean isValid = true;

        if(emailAddEditTxt.getText().toString().isEmpty()){
            emailTxtInputL.setError(getString(R.string.email_req));
            isValid = false;
        } else{
            String emailId = emailAddEditTxt.getText().toString();
            boolean validEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
            if (!validEmail) {
                emailTxtInputL.setError("Invalid Email Address, ex: abc@example.com");
                requestFocus(emailAddEditTxt);
                return false;
            } else {
                emailTxtInputL.setErrorEnabled(false);
                emailTxtInputL.setError("");
            }
        }

        if(passEditTxt.getText().toString().isEmpty()){
            passTxtInputL.setError(getString(R.string.pass_req));
            isValid = false;
        } else{
            if(passEditTxt.getText().toString().length() < 8) {
                passTxtInputL.setError(getString(R.string.pass_min));
                requestFocus(passEditTxt);
                return false;
            }else{
                passTxtInputL.setEnabled(false);
                passTxtInputL.setError("");
            }
        }

        if(isValid){
            Toast.makeText(SignIn.this, R.string.signIn_success, Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent(getApplicationContext(), BottomNavigation.class);
            startActivity(intent);*/
        }

        return true;
    }

    //Setting FOCUS
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //Email address Validations
    private boolean validateEmail() {
        if (emailAddEditTxt.getText().toString().trim().isEmpty()) {
            emailTxtInputL.setError(getString(R.string.email_req));
        } else {
            String emailId = emailAddEditTxt.getText().toString();
            Boolean  isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
            if (!isValid) {
                emailTxtInputL.setError("Invalid Email Address, ex: abc@example.com");
                requestFocus(emailAddEditTxt);
                return false;
            } else {
                emailTxtInputL.setErrorEnabled(false);
                emailTxtInputL.setError("");
            }
        }
        return true;
    }

    //Password Validations
    private boolean validatePassword() {
        if (passEditTxt.getText().toString().trim().isEmpty()) {
            passTxtInputL.setError(getString(R.string.pass_req));
            requestFocus(passEditTxt);
            return false;
        }else if(passEditTxt.getText().toString().length() < 8){
            passTxtInputL.setError(getString(R.string.pass_min));
            requestFocus(passEditTxt);
            return false;
        }
        else {
            passTxtInputL.setErrorEnabled(false);
            passTxtInputL.setError("");
        }
        return true;
    }

    //ValidationTextWatcher
    private class ValidationTextWatcher implements TextWatcher {
        private View view;
        private ValidationTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.emailAddEditTxtSI:
                    validateEmail();
                    break;
                case R.id.passwordEditTxtSI:
                    validatePassword();
                    break;
            }
        }
    }
}