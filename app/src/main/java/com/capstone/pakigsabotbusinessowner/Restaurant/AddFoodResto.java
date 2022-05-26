package com.capstone.pakigsabotbusinessowner.Restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.DentalClinic.AddServiceDentalClinic;
import com.capstone.pakigsabotbusinessowner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddFoodResto extends AppCompatActivity {
    ImageView backBtn, foodItemPic;
    ImageButton saveBtn;
    TextInputEditText foodName, foodDesc, foodPrice;
    TextView foodCategory, foodAvail;
    TextInputLayout foodNameLayout, foodCategoryLayout, foodDescLayout, foodAvailLayout, foodPriceLayout;
    FirebaseAuth fAuth;
    FirebaseFirestore fStoreRef;
    String userId, autoId, txtFoodName, txtFoodCategory, txtFoodDesc, txtFoodAvail, txtFoodPrice;
    Uri foodImageUri;
    StorageReference imageStorageRef;
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_resto);

        //References
        refs();

        fAuth = FirebaseAuth.getInstance();
        fStoreRef = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        imageStorageRef = FirebaseStorage.getInstance().getReference("establishments/restaurantImages/" + userId);
        autoId = UUID.randomUUID().toString();

        //creating string array to get the string resource for the food/menu item categories
        String[] menuItemCategories = getResources().getStringArray(R.array.menuItemCategories);

        //AutoCompleteTextView Reference
        AutoCompleteTextView itemCategoryET = findViewById(R.id.fItemCategoryTxt);

        //An adapter needed to fill the autocompletetv with suggestions
        ArrayAdapter<String> itemCategoryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menuItemCategories);
        itemCategoryET.setAdapter(itemCategoryAdapter);

        //creating string array to get the string resource for the food/menu item categories
        String[] menuItemAvailability = getResources().getStringArray(R.array.menuItemAvailability);

        //AutoCompleteTextView Reference
        AutoCompleteTextView itemAvailabilityET = findViewById(R.id.fItemAvailTxt);

        //An adapter needed to fill the autocompletetv with suggestions
        ArrayAdapter<String> itemAvailabilityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, menuItemAvailability);
        itemAvailabilityET.setAdapter(itemAvailabilityAdapter);

        foodItemPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFoodItemImage();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoMenuItems();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(AddFoodResto.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    saveFoodItemDetailsToFirestore();


                }
            }
        });
    }

    private void refs() {
        foodItemPic = findViewById(R.id.addFItemPic);
        foodName = findViewById(R.id.fItemNameTxt);
        foodCategory = findViewById(R.id.fItemCategoryTxt);
        foodDesc = findViewById(R.id.fItemDescTxt);
        foodAvail = findViewById(R.id.fItemAvailTxt);
        foodPrice = findViewById(R.id.fItemPriceTxt);

        saveBtn = findViewById(R.id.saveFoodResto);
        backBtn = findViewById(R.id.backBtnAddFoodResto);
        foodNameLayout = findViewById(R.id.foodNameLayout);
        foodCategoryLayout = findViewById(R.id.foodAvailLayout);
        foodDescLayout = findViewById(R.id.foodDescLayout);
        foodAvailLayout = findViewById(R.id.foodAvailLayout);
        foodPriceLayout = findViewById(R.id.foodPriceLayout);
    }

    private void restoMenuItems() {
        Intent intent = new Intent(getApplicationContext(), MenuRestaurant.class);
        startActivity(intent);
    }

    private void addFoodItemImage() {
        //uploading image for food item
        foodItemPic.setOnClickListener(new View.OnClickListener() {
            @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pickImg = true;

                if (pickImg) {
                    //check for camera permission
                    if (!checkCameraPermission()) {
                        //request for camera permission
                        reqCameraPermission();
                    } else {
                        pickImage();
                    }
                } else {
                    //check for camera permission
                    if (!checkStoragePermission()) {
                        //request for storage permission
                        reqStoragePermission();
                    } else {
                        pickImage();
                    }
                }
            }
        });

    }

    private void pickImage() {
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
    private void reqCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }


    private boolean checkStoragePermission() {
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return result2;
    }

    private boolean checkCameraPermission() {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return result1 && result2;
    }


    //Picking image from camera or gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                foodImageUri = result.getUri();
                Picasso.get().load(foodImageUri).into(foodItemPic);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    /*//To get file extension from the image
    public String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();

        // Return file Extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }*/


    private void saveFoodItemDetailsToFirestore() {
        txtFoodName = foodName.getText().toString().trim();
        txtFoodCategory = foodCategory.getText().toString().trim();
        txtFoodDesc = foodDesc.getText().toString();
        txtFoodAvail = foodAvail.getText().toString();
        txtFoodPrice = foodPrice.getText().toString();

        if (foodImageUri != null) {
            //Validations::
            if (txtFoodName.isEmpty() || txtFoodCategory.isEmpty() || txtFoodDesc.isEmpty() || txtFoodAvail.isEmpty() || txtFoodPrice.isEmpty()) {
                Toast.makeText(AddFoodResto.this, "Some fields are EMPTY.", Toast.LENGTH_SHORT).show();
            } else {
                if (txtFoodName.isEmpty()) {
                    foodNameLayout.setError("Enter Name of Food Product");
                } else {
                    Boolean validName = txtFoodName.matches("[A-Za-z][A-Za-z ]*+");
                    if (!validName) {
                        foodNameLayout.setError("Invalid Food Product Name");
                    } else {
                        foodNameLayout.setErrorEnabled(false);
                        foodNameLayout.setError("");
                    }
                }
            }
            if (txtFoodCategory.isEmpty()) {
                foodCategoryLayout.setError("Enter Food Category");
            }else {
                foodCategoryLayout.setErrorEnabled(false);
                foodCategoryLayout.setError("");
            }
            if (txtFoodDesc.isEmpty()) {
                foodDescLayout.setError("Enter Description");
            } else {
                foodDescLayout.setErrorEnabled(false);
                foodDescLayout.setError("");
            }
            if (txtFoodAvail.isEmpty()) {
                foodAvailLayout.setError("Enter Food Availability");
            } else {
                foodAvailLayout.setErrorEnabled(false);
                foodAvailLayout.setError("");
            }
            if (txtFoodPrice.isEmpty()) {
                foodPriceLayout.setError("Enter Price");
            } else {
                foodPriceLayout.setErrorEnabled(false);
                foodPriceLayout.setError("");
            }

            StorageReference fileRef = imageStorageRef.child(System.currentTimeMillis() + ".jpg");

            uploadTask = fileRef.putFile(foodImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(AddFoodResto.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                            //Store food item image and details::
                            String imgUrl = uri.toString();//get the firebasestorage image url

                            Map<String, Object> restoMenuItems = new HashMap<>();
                            restoMenuItems.put("restoFIId", autoId);
                            restoMenuItems.put("restoFIName", txtFoodName);
                            restoMenuItems.put("restoFICategory", txtFoodCategory);
                            restoMenuItems.put("restoFIDesc", txtFoodDesc);
                            restoMenuItems.put("restoFIAvail", txtFoodAvail);
                            restoMenuItems.put("restoFIPrice", txtFoodPrice);
                            restoMenuItems.put("restoFIImgUrl", imgUrl);

                            //To save inside the document of the userID, under the resto-menu-items collection
                            fStoreRef.collection("establishments").document(userId).collection("resto-menu-items").document(autoId).set(restoMenuItems);

                            restoMenuItems();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddFoodResto.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }
}