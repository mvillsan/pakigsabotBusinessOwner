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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    TextInputLayout staffUNameL,jobTitleL,estNameL, phoneNumL, addressL, emailAddL, passwordL;
    TextInputEditText staffUNameEditTxt,jobTEditTxt,estNameEditTxt, phoneNumEditTxt, addressEditTxt, editTxtEmailAdd, editTxtPass;
    Button createAccBtnn;
    ProgressBar progressSU;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String staff_id;

    private static final String[] EST_TYPE = new String[]{
            "Restaurant", "Resort", "Dental Clinic", "Internet Cafe", "Spa and Salon"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        refs();

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                welcomeScreen();
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
        staffUNameEditTxt.addTextChangedListener(new ValidationTextWatcher(staffUNameEditTxt));
        jobTEditTxt.addTextChangedListener(new ValidationTextWatcher(jobTEditTxt));
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
        staffUNameL = findViewById(R.id.staffUserNameInputLayout);
        jobTitleL = findViewById(R.id.staffPosInputLayout);
        staffUNameEditTxt = findViewById(R.id.staffUNameEditTxt);
        jobTEditTxt = findViewById(R.id.staffPosEditTxt);
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

    public void welcomeScreen(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
        String staffUnameDB = staffUNameEditTxt.getText().toString().trim();
        String jobTitleDB = jobTEditTxt.getText().toString().trim();
        String estNameDB = estNameEditTxt.getText().toString().trim();
        String eType = estType.getText().toString().trim();
        String phoneNum = phoneNumEditTxt.getText().toString().trim();
        String addressDB = addressEditTxt.getText().toString().trim();

        //Staff Username Validation
        if (staffUNameEditTxt.getText().toString().trim().isEmpty()) {
            staffUNameL.setError(getString(R.string.staff_uname_req));
        } else {
            String staffName = staffUNameEditTxt.getText().toString();
            Boolean  validStaffUsername = staffName.matches("[A-Za-z][A-Za-z ]*+");
            if (!validStaffUsername) {
                staffUNameEditTxt.setError("Invalid Staff Username, ex: mvillsan_pgr");
                requestFocus(staffUNameEditTxt);
                return false;
            } else {
                staffUNameL.setErrorEnabled(false);
                staffUNameL.setError("");
            }
        }

        //Staff Job Title Validation
        if (jobTEditTxt.getText().toString().trim().isEmpty()) {
            jobTitleL.setError(getString(R.string.staff_job_req));
        } else {
            String job = jobTEditTxt.getText().toString();
            Boolean  validStaffJob = job.matches("[A-Za-z][A-Za-z ]*+");
            if (!validStaffJob) {
                jobTEditTxt.setError("Invalid Staff Job Title, ex: Front desk clerk");
                requestFocus(jobTEditTxt);
                return false;
            } else {
                jobTitleL.setErrorEnabled(false);
                jobTitleL.setError("");
            }
        }

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
            progressSU.setVisibility(View.VISIBLE);

            //Register the user in Firebase::
            fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUp.this, "Account has been Created Successfully", Toast.LENGTH_SHORT).show();
                        staff_id = fAuth.getCurrentUser().getUid();
                        DocumentReference docRef = fStore.collection("staff").document(staff_id);
                        Map<String,Object> staf = new HashMap<>();
                        staf.put("staff_uname", staffUnameDB);
                        staf.put("staff_jobTitle",jobTitleDB);
                        staf.put("staff_estName", estNameDB);
                        staf.put("staff_estType", eType);
                        staf.put("staff_phoneNum", phoneNum);
                        staf.put("staff_address", addressDB);
                        staf.put("staff_email", email);
                        staf.put("staff_password", pass);
                        docRef.set(staf).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("SignUp", "onSignUpSuccess: Data is Stored for " + staff_id);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("SignUp", "onSignUpFailure: " + e.toString());
                            }
                        });
                        startActivity(new Intent(getApplicationContext(),AgreementScreen.class));
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

    //Staff Username Validations
    private boolean validateStaffUsernameSU() {
        if (staffUNameEditTxt.getText().toString().trim().isEmpty()) {
            staffUNameL.setError(getString(R.string.staff_uname_req));
        } else {
            String staffUName = staffUNameEditTxt.getText().toString();
            Boolean  validUName = staffUName.matches("[A-Za-z][A-Za-z ]*+");
            if (!validUName) {
                staffUNameL.setError("Invalid Staff Username, ex: mvillsan_pgr");
                requestFocus(staffUNameEditTxt);
                return false;
            } else {
                staffUNameL.setErrorEnabled(false);
                staffUNameL.setError("");
            }
        }
        return true;
    }

    //Staff Job Title Validations
    private boolean validateStaffJobSU() {
        if (jobTEditTxt.getText().toString().trim().isEmpty()) {
            jobTitleL.setError(getString(R.string.staff_job_req));
        } else {
            String staffJob = jobTEditTxt.getText().toString();
            Boolean  validJob = staffJob.matches("[A-Za-z][A-Za-z ]*+");
            if (!validJob) {
                jobTitleL.setError("Invalid Staff Job Title, ex: Front desk clerk");
                requestFocus(jobTEditTxt);
                return false;
            } else {
                jobTitleL.setErrorEnabled(false);
                jobTitleL.setError("");
            }
        }
        return true;
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
                case R.id.staffUNameEditTxt:
                    validateStaffUsernameSU();
                    break;
                case R.id.staffPosEditTxt:
                    validateStaffJobSU();
                    break;
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