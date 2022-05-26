package com.capstone.pakigsabotbusinessowner.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    Button saveBtnPassword, cancelBtnPassword;
    ImageView backBtnProfile;
    TextInputLayout passInputLayout,newPassInputLayout,newPassInputLayout2;
    TextInputEditText passEditText,newPassEditText,newPassEditText2;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID, oldPass;
    DocumentReference documentReference;
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //References
        refs();

        backBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileScreen();
            }
        });

        saveBtnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePassword();
            }
        });

        cancelBtnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileScreen();
            }
        });

    }

    public void refs(){
        saveBtnPassword = findViewById(R.id.saveBtnPassword);
        cancelBtnPassword = findViewById(R.id.cancelBtnPassword);
        backBtnProfile = findViewById(R.id.backBtnProfile);
        passInputLayout = findViewById(R.id.passInputLayout);
        newPassInputLayout = findViewById(R.id.newPassInputLayout);
        newPassInputLayout2 = findViewById(R.id.newPassInputLayout2);
        passEditText = findViewById(R.id.passEditText);
        newPassEditText = findViewById(R.id.newPassEditText);
        newPassEditText2 = findViewById(R.id.newPassEditText2);
    }

    private void profileScreen(){
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        startActivity(intent);
    }

    private void savePassword(){
        String currentPass = passEditText.getText().toString();
        String newPass = newPassEditText.getText().toString();
        String confirmPass = newPassEditText2.getText().toString();

        //Fetching Data from FireStore DB
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        userID = fAuth.getCurrentUser().getUid();

        documentReference = fStore.collection("establishments/").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                oldPass = value.getString("est_password");
            }
        });

        if(currentPass.length() < 8){
            passInputLayout.setError(getString(R.string.pass_min));
        }else if(newPass.length() < 8){
            newPassInputLayout.setError(getString(R.string.pass_min));
        }else if(confirmPass.length() < 8){
            newPassInputLayout2.setError(getString(R.string.pass_min));
        }else if(currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()){
            Toast.makeText(ChangePassword.this, "Some fields are EMPTY.", Toast.LENGTH_SHORT).show();
        }else{
            passInputLayout.setError(null);
            newPassInputLayout.setError(null);
            newPassInputLayout2.setError(null);

            //Check Whether Inputted New Password Matches the Inputted Confirm Password
            if(confirmPass.equals(newPass)){
                //Check if Entered Current Password Matches to the Firebase DB
                if(currentPass.equals(oldPass)){
                    //Store New Password to the database
                    user.updatePassword(confirmPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            docRef = fStore.collection("establishments/").document(user.getUid());
                            Map<String, Object> edited = new HashMap<>();
                            edited.put("est_password", confirmPass);
                            docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ChangePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Profile.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChangePassword.this, "No Changes has been made", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChangePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(ChangePassword.this, "Enter OLD Password", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(ChangePassword.this, "Password Mismatched", Toast.LENGTH_SHORT).show();
            }
        }
    }
}