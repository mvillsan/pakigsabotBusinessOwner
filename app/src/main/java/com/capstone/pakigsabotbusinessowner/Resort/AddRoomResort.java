package com.capstone.pakigsabotbusinessowner.Resort;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Services.ServicesResort;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddRoomResort extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1000;
    ImageView backBtnAddRoom, addRFPicture, uploadPicBtn, saveBtn;
    EditText nameTxt, numOfPersonTxt, descTxt, rateTxt;
    Uri resortImageUri;
    StorageReference imageFolderRef;
    FirebaseFirestore firestoreRef;
    FirebaseAuth fAuth;
    CollectionReference docRef;
    String userID, autoID, nameTxtStr, numOfPersonTxtStr, descTxtStr, rateTxtStr;
    StorageTask uploadTask;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    DocumentReference documentReference, documentRef, docuRef;
    List<String> listEstablishments, listRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room_resort);

        //References::
        refs();

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        imageFolderRef = FirebaseStorage.getInstance().getReference("establishments/resortEstImages/"+ userID + "/rooms");
        firestoreRef = FirebaseFirestore.getInstance();
        autoID = UUID.randomUUID().toString();

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
                    Toast.makeText(AddRoomResort.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                }else{
                    uploadImgDetails();
                }
            }
        });
    }

    private void refs(){
        backBtnAddRoom = findViewById(R.id.backBtnAddRoom);
        addRFPicture = findViewById(R.id.addRFPicture);
        uploadPicBtn = findViewById(R.id.uploadPicBtn);
        saveBtn = findViewById(R.id.saveBtn);
        nameTxt = findViewById(R.id.nameTxt);
        numOfPersonTxt = findViewById(R.id.numOfPersonTxt);
        descTxt = findViewById(R.id.descTxt);
        rateTxt = findViewById(R.id.rateTxt);
    }

    private void servicesResort(){
        Intent intent = new Intent(getApplicationContext(), ServicesResort.class);
        startActivity(intent);
    }

    private void openFileChoose() {
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

    private void uploadImgDetails() {
        nameTxtStr = nameTxt.getText().toString().trim();
        numOfPersonTxtStr = numOfPersonTxt.getText().toString().trim();
        descTxtStr = descTxt.getText().toString().trim();
        rateTxtStr = rateTxt.getText().toString().trim();

        //Validations::
        if (nameTxtStr.isEmpty()) {
            Toast.makeText(this, "Enter Name of the Room/Facility", Toast.LENGTH_SHORT).show();
        } else if (numOfPersonTxtStr.isEmpty()) {
            Toast.makeText(this, "Enter Capacity", Toast.LENGTH_SHORT).show();
        } else if (descTxtStr.isEmpty()) {
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
        } else if (rateTxtStr.isEmpty()) {
            Toast.makeText(this, "Enter Rate", Toast.LENGTH_SHORT).show();
        } else {
            if (resortImageUri != null) {
                StorageReference fileRef = imageFolderRef.child(System.currentTimeMillis()
                        + "." + getFileExtension(resortImageUri));

                uploadTask = fileRef.putFile(resortImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(AddRoomResort.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                                //Store Room/Facilities image and details::
                                String imgUrl = uri.toString();//get the firebasestorage image url

                                Map<String, Object> resortServices = new HashMap<>();
                                resortServices.put("resortRFID", autoID);
                                resortServices.put("resortRFName", nameTxtStr);
                                resortServices.put("resortCapac", numOfPersonTxtStr);
                                resortServices.put("resortDesc", descTxtStr);
                                resortServices.put("resortRate", rateTxtStr);
                                resortServices.put("resortImgUrl", imgUrl);

                                //To save inside the document of the userID, under the resort-rooms-facilities collection
                                firestoreRef.collection("establishments").document(userID).collection("resort-rooms").document(autoID).set(resortServices);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddRoomResort.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                //Back to Resorts' Services
                servicesResort();
            } else {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
