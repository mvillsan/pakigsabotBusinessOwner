package com.capstone.pakigsabotbusinessowner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.capstone.pakigsabotbusinessowner.NavBar.BottomNavigation;
import com.capstone.pakigsabotbusinessowner.SignUpRequirement.AgreementScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    ImageView prev;
    AutoCompleteTextView estType;
    TextView signin;
    TextInputLayout estNameL, phoneNumL, addressL, emailAddL, passwordL;
    TextInputEditText estNameEditTxt, phoneNumEditTxt, addressEditTxt, editTxtEmailAdd, editTxtPass;
    Button createAccBtnn;
    ProgressBar progressSU;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String est_id;

    private static final String[] EST_TYPE = new String[]{
            "Restaurant", "Cafe", "Resort", "Dental Clinic", "Eye Clinic", "Spa", "Salon", "Internet Cafe", "Coworking Space"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        refs();

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agreementScreen();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInScreen();
            }
        });

        createAccBtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpCustomer();
            }
        });

        //Validations
        estNameEditTxt.addTextChangedListener(new ValidationTextWatcher(estNameEditTxt));
        estType.addTextChangedListener(new ValidationTextWatcher(estType));
        phoneNumEditTxt.addTextChangedListener(new ValidationTextWatcher(phoneNumEditTxt));
        addressEditTxt.addTextChangedListener(new ValidationTextWatcher(addressEditTxt));
        editTxtPass.addTextChangedListener(new ValidationTextWatcher(editTxtPass));
        editTxtEmailAdd.addTextChangedListener(new ValidationTextWatcher(editTxtEmailAdd));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,EST_TYPE);
        estType.setAdapter(adapter);
        estType.setThreshold(1);
    }

    public void refs(){
        prev = findViewById(R.id.backBtnSU);
        signin = findViewById(R.id.signinBtnSUS);
        estNameL = findViewById(R.id.estNameInputLayout);
        phoneNumL = findViewById(R.id.mobileInputLayout);
        addressL = findViewById(R.id.addressInputLayout);
        emailAddL = findViewById(R.id.emailAddInputLayoutSU);
        passwordL = findViewById(R.id.passInputLayoutSU);
        estNameEditTxt = findViewById(R.id.estNameEditTxt);
        estType = findViewById(R.id.estTypeTxtView);
        phoneNumEditTxt = findViewById(R.id.mobileEditTxt);
        addressEditTxt = findViewById(R.id.addressEditTxt);
        editTxtEmailAdd = findViewById(R.id.emailAddEditTextSU);
        editTxtPass = findViewById(R.id.passEditTextSU);
        createAccBtnn = findViewById(R.id.createAccBtn);
        progressSU = findViewById(R.id.progressBarSignUp);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    public void agreementScreen(){
        Intent intent = new Intent(getApplicationContext(), AgreementScreen.class);
        startActivity(intent);
    }

    public void signInScreen(){
        Intent intent = new Intent(getApplicationContext(), SignIn.class);
        startActivity(intent);
    }


    //Validations for Signing Up on the Application
    public boolean signUpCustomer(){

        //Variables::
        boolean isValid = true;
        String email = editTxtEmailAdd.getText().toString().trim();
        String pass = editTxtPass.getText().toString().trim();
        String estNameDB = estNameEditTxt.getText().toString().trim();
        String eType = estType.getText().toString().trim();
        String phoneNum = phoneNumEditTxt.getText().toString().trim();
        String addressDB = addressEditTxt.getText().toString().trim();

        //Establishment Name Validation
        if (estNameEditTxt.getText().toString().trim().isEmpty()) {
            estNameL.setError(getString(R.string.est_name_req));
        } else {
            String estName = estNameEditTxt.getText().toString();
            Boolean  validUsername = estName.matches("[A-Za-z][A-Za-z ]*+");
            if (!validUsername) {
                estNameL.setError("Invalid Establishment Name, ex: Hello Resto");
                requestFocus(estNameEditTxt);
                return false;
            } else {
                estNameL.setErrorEnabled(false);
                estNameL.setError("");
            }
        }
        
        //Establishment Type Validation
        if (estType.getText().toString().trim().isEmpty()) {
            estType.setError(getString(R.string.est_type_req));
        }

        //Phone Number Validation
        if (phoneNumEditTxt.getText().toString().trim().isEmpty()) {
            phoneNumL.setError(getString(R.string.phoneNum_req));
        } else {
            String phone = phoneNumEditTxt.getText().toString();
            Boolean  validPhone = Patterns.PHONE.matcher(phone).matches();
            if (!validPhone) {
                phoneNumL.setError("Invalid Phone Number");
                requestFocus(phoneNumEditTxt);
                return false;
            } else {
                phoneNumL.setErrorEnabled(false);
                phoneNumL.setError("");
            }
        }

        //Address Validation
        if (addressEditTxt.getText().toString().trim().isEmpty()) {
            addressL.setError(getString(R.string.address_req));
        } else {
            String add = addressEditTxt.getText().toString();
            Boolean  validAddress = add.matches("^[#.0-9a-zA-Z\\s,-]+$");
            if (!validAddress) {
                addressL.setError("Invalid Address, ex: Banilad,Cebu City");
                requestFocus(addressEditTxt);
                return false;
            } else {
                addressL.setErrorEnabled(false);
                addressL.setError("");
            }
        }

        //Email address Validation
        if (editTxtEmailAdd.getText().toString().trim().isEmpty()) {
            emailAddL.setError(getString(R.string.email_req));
        } else {
            String emailId = editTxtEmailAdd.getText().toString();
            Boolean  validEmail = Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
            if (!validEmail) {
                emailAddL.setError("Invalid Email Address, ex: abc@example.com");
                requestFocus(editTxtEmailAdd);
                return false;
            } else {
                emailAddL.setErrorEnabled(false);
                emailAddL.setError("");
            }
        }

        //Password Validation
        if (editTxtPass.getText().toString().trim().isEmpty()) {
            passwordL.setError(getString(R.string.pass_req));
            requestFocus(editTxtPass);
            return false;
        }else if(editTxtPass.getText().toString().length() < 8){
            passwordL.setError(getString(R.string.pass_min));
            requestFocus(editTxtPass);
            return false;
        }
        else {
            passwordL.setErrorEnabled(false);
            passwordL.setError("");
        }

        if(isValid){
            String defStat = "Classic";

            progressSU.setVisibility(View.VISIBLE);

            //Register the user in Firebase::
            fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUp.this, "Account has been Created Successfully", Toast.LENGTH_SHORT).show();
                        est_id = fAuth.getCurrentUser().getUid();
                        DocumentReference docRef = fStore.collection("establishments").document(est_id);
                        Map<String,Object> staf = new HashMap<>();
                        staf.put("est_Name", estNameDB);
                        staf.put("est_Type", eType);
                        staf.put("est_phoneNum", phoneNum);
                        staf.put("est_address", addressDB);
                        staf.put("est_email", email);
                        staf.put("est_password", pass);
                        staf.put("est_status", defStat);

                        docRef.set(staf).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("SignUp", "onSignUpSuccess: Data is Stored for " + est_id);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("SignUp", "onSignUpFailure: " + e.toString());
                            }
                        });
                        startActivity(new Intent(getApplicationContext(), BottomNavigation.class));

                        //Clear Text Fields::
                        estNameEditTxt.setText(null);
                        estType.setText(null);
                        phoneNumEditTxt.setText(null);
                        addressEditTxt.setText(null);
                        editTxtEmailAdd.setText(null);
                        editTxtPass.setText(null);
                    }else{
                        Toast.makeText(SignUp.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressSU.setVisibility(View.GONE);
                    }
                }
            });
        }

        //User is Log-in already::
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), SignUp.class));
            finish();
        }
        return true;
    }

    //Setting FOCUS
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //Establishment Name Validations
    private boolean validateEstablishmentSU() {
        if (estNameEditTxt.getText().toString().trim().isEmpty()) {
            estNameL.setError(getString(R.string.est_name_req));
        } else {
            String estName = estNameEditTxt.getText().toString();
            Boolean  isValid = estName.matches("[A-Za-z][A-Za-z ]*+");
            if (!isValid) {
                estNameL.setError("Invalid Establishment Name, ex: Hello Resto");
                requestFocus(estNameEditTxt);
                return false;
            } else {
                estNameL.setErrorEnabled(false);
                estNameL.setError("");
            }
        }
        return true;
    }

    //Establishment Type Validations
    private boolean validateEstablishmentTypeSU() {
        if (estType.getText().toString().trim().isEmpty()) {
            estType.setError(getString(R.string.est_type_req));
        }
        return true;
    }

    //Phone Number Validations
    private boolean validatePhoneNumSU(){
        if (phoneNumEditTxt.getText().toString().trim().isEmpty()) {
            phoneNumL.setError(getString(R.string.phoneNum_req));
        } else {
            String phone = phoneNumEditTxt.getText().toString();
            Boolean  validPhone = Patterns.PHONE.matcher(phone).matches();
            if (!validPhone) {
                phoneNumL.setError("Invalid Phone Number");
                requestFocus(phoneNumEditTxt);
                return false;
            } else {
                phoneNumL.setErrorEnabled(false);
                phoneNumL.setError("");
            }
        }
        return true;
    }

    //Address Validations
    private boolean validateAddressSU(){
        if (addressEditTxt.getText().toString().trim().isEmpty()) {
            addressL.setError(getString(R.string.address_req));
        } else {
            String add = addressEditTxt.getText().toString();
            Boolean  validAddress = add.matches("^[#.0-9a-zA-Z\\s,-]+$");
            if (!validAddress) {
                addressL.setError("Invalid Address, ex: Banilad,Cebu City");
                requestFocus(addressEditTxt);
                return false;
            } else {
                addressL.setErrorEnabled(false);
                addressL.setError("");
            }
        }
        return true;
    }

    //Email address Validations
    private boolean validateEmailSU() {
        if (editTxtEmailAdd.getText().toString().trim().isEmpty()) {
            emailAddL.setError(getString(R.string.email_req));
        } else {
            String emailId = editTxtEmailAdd.getText().toString();
            Boolean  isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
            if (!isValid) {
                emailAddL.setError("Invalid Email Address, ex: abc@example.com");
                requestFocus(editTxtEmailAdd);
                return false;
            } else {
                emailAddL.setErrorEnabled(false);
                emailAddL.setError("");
            }
        }
        return true;
    }

    //Password Validations
    private boolean validatePasswordSU() {
        if (editTxtPass.getText().toString().trim().isEmpty()) {
            passwordL.setError(getString(R.string.pass_req));
            requestFocus(editTxtPass);
            return false;
        }else if(editTxtPass.getText().toString().length() < 8){
            passwordL.setError(getString(R.string.pass_min));
            requestFocus(editTxtPass);
            return false;
        }
        else {
            passwordL.setErrorEnabled(false);
            passwordL.setError("");
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
                case R.id.estNameEditTxt:
                    validateEstablishmentSU();
                    break;
                case R.id.estTypeTxtView:
                    validateEstablishmentTypeSU();
                    break;
                case R.id.mobileEditTxt:
                    validatePhoneNumSU();
                    break;
                case R.id.addressEditTxt:
                    validateAddressSU();
                    break;
                case R.id.emailAddEditTextSU:
                    validateEmailSU();
                    break;
                case R.id.passEditTextSU:
                    validatePasswordSU();
                    break;
            }
        }
    }
}