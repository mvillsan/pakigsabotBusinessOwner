package com.capstone.pakigsabotbusinessowner.Services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Upload.UploadResortImgDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddServiceResort extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1000;
    ImageView backBtnAddRoom, addRFPicture, uploadPicBtn, saveBtn;
    TextInputLayout nameRoomLayout, numOfPersonLayout, descLayout, rateLayout;
    TextInputEditText nameTxt, numOfPersonTxt, descTxt, rateTxt;
    ProgressBar progressUploadImg;
    Uri resortImageUri;
    StorageReference storageRef;
    FirebaseFirestore firestoreRef;
    FirebaseAuth fAuth;
    CollectionReference docRef;
    String userID, nameTxtStr, numOfPersonTxtStr, descTxtStr, rateTxtStr;
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_resort);

        //References::
        refs();

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance().getReference("business owner/resortImgs/"+ userID);
        firestoreRef = FirebaseFirestore.getInstance();
        docRef= firestoreRef.collection("establishments").document(userID).collection("resortEst");

        backBtnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicesResort();
            }
        });

        uploadPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChoose();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(AddServiceResort.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                }else{
                    uploadImgDetails();

                    //Back to Resorts' Services
                    servicesResort();
                }
            }
        });
    }

    private void refs(){
        backBtnAddRoom = findViewById(R.id.backBtnAddRoom);
        addRFPicture = findViewById(R.id.addRFPicture);
        uploadPicBtn = findViewById(R.id.uploadPicBtn);
        progressUploadImg = findViewById(R.id.progressUploadImg);
        saveBtn = findViewById(R.id.saveBtn);
        nameRoomLayout = findViewById(R.id.nameRoomLayout);
        numOfPersonLayout = findViewById(R.id.numOfPersonLayout);
        descLayout = findViewById(R.id.descLayout);
        rateLayout = findViewById(R.id.rateLayout);
        nameTxt = findViewById(R.id.nameTxt);
        numOfPersonTxt = findViewById(R.id.numOfPersonTxt);
        descTxt = findViewById(R.id.descTxt);
        rateTxt = findViewById(R.id.rateTxt);
    }

    private void servicesResort(){
        Intent intent = new Intent(getApplicationContext(), ServicesResort.class);
        startActivity(intent);
    }


    private void openFileChoose() { ;
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            resortImageUri = data.getData();

            Picasso.get().load(resortImageUri).into(addRFPicture);

        }
    }

    //To get file extension from the image
    private String getFileExtension(Uri uri){
        ContentResolver contentR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentR.getType(uri));
    }

    private void uploadImgDetails(){
        nameTxtStr = nameTxt.getText().toString().trim();
        numOfPersonTxtStr = numOfPersonTxt.getText().toString().trim();
        descTxtStr = descTxt.getText().toString().trim();
        rateTxtStr = rateTxt.getText().toString().trim();

        if(resortImageUri != null){
            //Validations::
            if (nameTxtStr.isEmpty()) {
                nameRoomLayout.setError("Enter Name of the Room/Facility");
            } else {
                Boolean  validName = nameTxtStr.matches("[A-Za-z][A-Za-z ]*+");
                if (!validName) {
                    nameRoomLayout.setError("Invalid Room/Facility Name");
                } else {
                    nameRoomLayout.setErrorEnabled(false);
                    nameRoomLayout.setError("");
                }
            }

            if (numOfPersonTxtStr.isEmpty()) {
                numOfPersonLayout.setError("Enter Capacity");
            } else {
                numOfPersonLayout.setErrorEnabled(false);
                numOfPersonLayout.setError("");
            }

            if (descTxtStr.isEmpty()) {
                descLayout.setError("Enter Description");
            } else {
                descLayout.setErrorEnabled(false);
                descLayout.setError("");
            }

            if (rateTxtStr.isEmpty()) {
                rateLayout.setError("Enter Rate");
            } else {
                rateLayout.setErrorEnabled(false);
                rateLayout.setError("");
            }

            StorageReference fileRef = storageRef.child(System.currentTimeMillis()
            + "." + getFileExtension(resortImageUri));

            uploadTask = fileRef.putFile(resortImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Delays the progress bar value so that user can see the 100% progress
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressUploadImg.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(AddServiceResort.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                            Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                            UploadResortImgDetails upload = new UploadResortImgDetails(downloadUri.toString(),nameTxtStr, Integer.parseInt(numOfPersonTxtStr), descTxtStr, Integer.parseInt(rateTxtStr));

                            //To save inside the document of the userID
                            docRef.document(userID)
                                    .collection("rooms-facilities").add(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddServiceResort.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() /  snapshot.getTotalByteCount());
                            progressUploadImg.setProgress((int) progress);
                        }
                    });
        }else{
            Toast.makeText(this, "No Image Selected",Toast.LENGTH_SHORT).show();
        }
    }
}