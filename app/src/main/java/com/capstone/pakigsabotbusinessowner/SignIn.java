package com.capstone.pakigsabotbusinessowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.FacialRecog.EnableFacialRecog;
import com.capstone.pakigsabotbusinessowner.FacialRecog.FacialRecog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    ImageView prev, facialRecogBtn;
    TextView signup;
    TextInputEditText emailAddEditTxt,passEditTxt;
    TextInputLayout emailTxtInputL, passTxtInputL;
    Button signInBtn;
    ProgressBar progressSI;
    FirebaseAuth fAuth2;

    public static String passwordAuth;

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
                signInBusinessStaff();
            }
        });

        facialRecogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facialSignIn();
            }
        });

        //Validations
        passEditTxt.addTextChangedListener(new ValidationTextWatcher(passEditTxt));
        emailAddEditTxt.addTextChangedListener(new ValidationTextWatcher(emailAddEditTxt));

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("signin") != null){
                Toast.makeText(getApplicationContext(), "data: " + bundle.getString("signin"),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void refs(){
        prev = findViewById(R.id.backBtnSignIn);
        signup = findViewById(R.id.signUpTxtView);
        signInBtn = findViewById(R.id.signInBtnn);
        emailAddEditTxt = findViewById(R.id.emailAddEditTxtSI);
        passEditTxt = findViewById(R.id.passwordEditTxtSI);
        emailTxtInputL = findViewById(R.id.emailTxtInputLayout);
        passTxtInputL = findViewById(R.id.passwordTextInputLayout);
        facialRecogBtn = findViewById(R.id.facialRecogBtn);
        progressSI = findViewById(R.id.progressBarSignIn);
        fAuth2 = FirebaseAuth.getInstance();
    }

    public void welcomeScreen(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void signUpScreen(){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    public void facialSignIn(){
        Intent intent = new Intent(getApplicationContext(), FacialRecog.class);
        startActivity(intent);
    }

    //Validations for Signing In on the Application
    public boolean signInBusinessStaff(){

        //Variables::
        boolean isValid = true;
        String email = emailAddEditTxt.getText().toString();
        String pass = passEditTxt.getText().toString();

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

        passEditTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_SEND || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    passwordAuth = passEditTxt.getText().toString();
                    handled = true;
                }else{
                    Toast.makeText(SignIn.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    passEditTxt.requestFocus();
                }return handled;
            }
        });

        if(isValid){
            progressSI.setVisibility(View.VISIBLE);

            //Authenticate User::
            fAuth2.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignIn.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), EnableFacialRecog.class));
                    }else{
                        Toast.makeText(SignIn.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressSI.setVisibility(View.GONE);
                    }
                }
            });
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

//end