package com.capstone.pakigsabotbusinessowner.Profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.capstone.pakigsabotbusinessowner.NavigationFragments.HelpCenterFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HistoryFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.HomeFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ReservationsFragment;
import com.capstone.pakigsabotbusinessowner.NavigationFragments.ServicesFragment;
import com.capstone.pakigsabotbusinessowner.PremiumApp.GoPremiumWS;
import com.capstone.pakigsabotbusinessowner.R;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    ImageView profileImg, prevBtn, saveProfileBtn;
    Button goPremiumBtn, changePasswordBtn, premiumAccount;
    FloatingActionButton addPhoto;
    EditText estName, estType, emailAdd, mobileNum, address;
    FirebaseAuth fAuthProfile;
    FirebaseFirestore fStoreProfile;
    FirebaseUser user;
    String userId, estStatus;
    public Uri resultUri;
    StorageReference storageRef;
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fAuthProfile = FirebaseAuth.getInstance();
        fStoreProfile = FirebaseFirestore.getInstance();
        user = fAuthProfile.getCurrentUser();
        userId = fAuthProfile.getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance().getReference();

        //References
        refs();

        //Displaying the details
        displayDetails();


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("profile") != null){
                Toast.makeText(getApplicationContext(), "data: " + bundle.getString("profile"),
                        Toast.LENGTH_SHORT).show();
            }
        }

        //Adding photo in profile
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pickImg = true;

                if(pickImg)
                {
                    //check for camera permission
                    if(!checkCameraPermission())
                    {
                        //request for camera permission
                        reqCameraPermission();
                    }

                    else
                    {
                        PickImage();
                    }
                }

                else
                {
                    //check for camera permission
                    if(!checkStoragePermission())
                    {
                        //request for storage permission
                        reqStoragePermission();
                    }

                    else
                    {
                        PickImage();
                    }
                }
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragments();
            }
        });

        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfileUpdatedDetails();
            }
        });

        goPremiumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goPremiumScreen();
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordScreen();
            }
        });
    }


    public void refs()
    {
        estName = findViewById(R.id.estNameProfileET);
        estType = findViewById(R.id.estTypeProfileET);
        emailAdd = findViewById(R.id.emailAddProfileET);
        mobileNum = findViewById(R.id.mobileNumProfileET);
        address = findViewById(R.id.addressProfileET);
        profileImg = findViewById(R.id.profileImageView);
        addPhoto = findViewById(R.id.addPhotoFAB);
        saveProfileBtn = findViewById(R.id.saveProfileBtn);
        prevBtn = findViewById(R.id.backBtnProfile);
        goPremiumBtn = findViewById(R.id.goPremiumBtn);
        premiumAccount = findViewById(R.id.premiumAccount);
        changePasswordBtn = findViewById(R.id.changePassBtn);
    }

    //Picking image from camera or gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                resultUri = result.getUri();
                try
                {
                    //decode the Uri
                    InputStream streamUri = getContentResolver().openInputStream(resultUri);

                    //convert InputStream to bitmap with bitmap factory
                    Bitmap bitmap = BitmapFactory.decodeStream(streamUri);

                    //setting profileImg image
                    profileImg.setImageBitmap(bitmap);
                    uploadImgToFirebase(resultUri);

                }
                catch(Exception e){
                    //print the error
                    e.printStackTrace();
                }

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }

    }
    private void PickImage()
    {
        // starts the  picker to get image for cropping and then use the image in cropping activity
        CropImage.activity().start(this);
    }

    //checks the permission code to access the device's storage
    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.M)
    private void reqStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    //checks the permission code to access the device's camera
    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.M)
    private void reqCameraPermission()
    {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }


    private boolean checkStoragePermission()
    {
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return result2;
    }

    private boolean checkCameraPermission()
    {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return result1 && result2;
    }



    //Upload Image to Firebase Storage
    private void uploadImgToFirebase(Uri resultUri) {

        // Create a reference to the image
        final StorageReference imgRef = storageRef.child("establishments/profile_pictures/" + userId + "img.jpg");
        imgRef.putFile(resultUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.get().load(uri).into(profileImg);

                                    //Save the image to firestore database
                                    String imgUrl = String.valueOf(uri);
                                    DocumentReference docRef = fStoreProfile.collection("establishments").document(userId);
                                    Map<String, Object> est = new HashMap<>();
                                    est.put("est_image", imgUrl);
                                    docRef.update(est).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("PROFILE-IMG SAVED TO DB", "SUCCESS");
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
                                    }
                            });
                }
        });
    }


    //Save Updated Profile Details to Firebase
    private void saveProfileUpdatedDetails(){
        String estNameP = estName.getText().toString();
        String estTypeP = estType.getText().toString();
        String emailAddP = emailAdd.getText().toString();
        String mobilePhoneP = mobileNum.getText().toString();
        String addressP = address.getText().toString();

        //Check whether there are empty fields ::
        if(estNameP.isEmpty() || estTypeP.isEmpty() || emailAddP.isEmpty() || mobilePhoneP.isEmpty() || addressP.isEmpty()){
            Toast.makeText(Profile.this, "Some fields are EMPTY.", Toast.LENGTH_SHORT).show();
        }else{
            Boolean  isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddP).matches();
            Boolean  isValidFName = estNameP.matches("[A-Za-z][A-Za-z ]*+");
            Boolean  isValidLName = estTypeP.matches("[A-Za-z][A-Za-z ]*+");
            Boolean  validPhone = mobilePhoneP.matches("^(?:\\d{2}-\\d{3}-\\d{3}-\\d{3}|\\d{11})$");


            if(!isValidEmail) {//Validate valid Email Address
                Toast.makeText(Profile.this, "Invalid Email Address, ex: abc@example.com", Toast.LENGTH_SHORT).show();
            }else if (!isValidFName) {//Validate valid First name
                Toast.makeText(Profile.this, "Invalid First Name, ex: John Anthony", Toast.LENGTH_SHORT).show();
            }else if (!isValidLName) {//Validate valid Last name
                Toast.makeText(Profile.this, "Invalid Last Name, ex: Doe", Toast.LENGTH_SHORT).show();
            }else if (!validPhone) {//Validate valid Phone Number
                Toast.makeText(Profile.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            }else{//save the updates on the firebase db
                user.updateEmail(emailAddP).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        docRef = fStoreProfile.collection("establishments/").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("est_email",emailAddP);
                        edited.put("est_Name",estNameP);
                        edited.put("est_Type",estTypeP);
                        edited.put("est_phoneNum",mobilePhoneP);
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Profile.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Profile.this, "No Changes has been made", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void  goPremiumScreen(){
        Intent intent = new Intent(getApplicationContext(), GoPremiumWS.class);
        startActivity(intent);
    }

    //To redirect in change password screen
    private void changePasswordScreen() {
        Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
        startActivity(intent);
    }

    //Display details
    public void displayDetails(){
        //Fetching data from Firestore
        DocumentReference documentReference = fStoreProfile.collection("establishments").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                estName.setText(documentSnapshot.getString("est_Name"));
                estType.setText(documentSnapshot.getString("est_Type"));
                emailAdd.setText(documentSnapshot.getString("est_email"));
                mobileNum.setText(documentSnapshot.getString("est_phoneNum"));
                address.setText(documentSnapshot.getString("est_address"));
                estStatus = documentSnapshot.getString("est_status");

                //Check Account Status::
                if(estStatus.equalsIgnoreCase("Classic")){
                    goPremiumBtn.setVisibility(View.VISIBLE);
                    premiumAccount.setVisibility(View.GONE);
                }else{
                    goPremiumBtn.setVisibility(View.GONE);
                    premiumAccount.setVisibility(View.VISIBLE);
                }
                Log.d("TAG", estType.getText().toString());
            }
        });

        //Display profile image
        StorageReference profileRef = storageRef.child("establishments/profile_pictures/"+ userId +"img.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImg);

                //Save the image to firestore database
                String imgUrl = String.valueOf(uri);
                DocumentReference docRef = fStoreProfile.collection("establishments").document(userId);
                Map<String,Object> est = new HashMap<>();
                est.put("est_image", imgUrl);
                docRef.update(est).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("PROFILE-IMG SAVED TO DB","SUCCESS");
                    }
                });
            }
        });
    }

    //Previous Pages::
    private void fragments(){
        setContentView(R.layout.activity_bottom_navigation);
        //Bottom Nav
        //Assign variable
        bottomNavigation = findViewById(R.id.bottom_nav);

        //Add menu item
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_reserve));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_services));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_history));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.ic_help));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //Initialize fragment
                Fragment fragment = null;

                //Check condition
                switch (item.getId()){
                    case 1: //When id is 1, initialize nearby fragment
                        fragment = new ReservationsFragment();
                        break;

                    case 2: //When id is 2, initialize reservations fragment
                        fragment = new ServicesFragment();
                        break;

                    case 3: //When id is 3, initialize home fragment
                        fragment = new HomeFragment();
                        break;

                    case 4: //When id is 4, initialize favorites fragment
                        fragment = new HistoryFragment();
                        break;

                    case 5: //When id is 5, initialize help center fragment
                        fragment = new HelpCenterFragment();
                        break;
                }
                //Load fragment
                loadFragment(fragment);
            }
        });

        /*//Set notification count
        bottomNavigation.setCount(3,"10");*/
        //Set home fragment initially selected
        bottomNavigation.show(3,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        //Replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();
    }
}