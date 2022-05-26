package com.capstone.pakigsabotbusinessowner.Cafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
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

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Restaurant.AddFoodResto;
import com.capstone.pakigsabotbusinessowner.Restaurant.MenuRestaurant;
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

public class AddMenuItemCafe extends AppCompatActivity {
    ImageView backBtn, foodItemPic;
    ImageButton saveBtn;
    TextInputEditText foodName, foodPrice;
    TextView foodCategory, foodAvail;
    TextInputLayout foodNameLayout, foodCategoryLayout, foodAvailLayout, foodPriceLayout;
    FirebaseAuth fAuth;
    FirebaseFirestore fStoreRef;
    String userId, autoId, txtFoodName, txtFoodCategory, txtFoodAvail, txtFoodPrice;
    Uri foodImageUri;
    StorageReference imageStorageRef;
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item_cafe);

        //References
        refs();

        fAuth = FirebaseAuth.getInstance();
        fStoreRef = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        imageStorageRef = FirebaseStorage.getInstance().getReference("establishments/cafeImages/" + userId);
        autoId = UUID.randomUUID().toString();

        //creating string array to get the string resource for the food/menu item categories
        String[] menuItemCategories = getResources().getStringArray(R.array.menuItemCategories);

        //AutoCompleteTextView Reference
        AutoCompleteTextView itemCategoryET = findViewById(R.id.fItemCategoryTxtCafe);

        //An adapter needed to fill the autocompletetv with suggestions
        ArrayAdapter<String> itemCategoryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menuItemCategories);
        itemCategoryET.setAdapter(itemCategoryAdapter);

        //creating string array to get the string resource for the food/menu item categories
        String[] menuItemAvailability = getResources().getStringArray(R.array.menuItemAvailability);

        //AutoCompleteTextView Reference
        AutoCompleteTextView itemAvailabilityET = findViewById(R.id.fItemAvailTxtCafe);

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
                cafeMenuItems();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(AddMenuItemCafe.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    saveFoodItemDetailsToFirestore();


                }
            }
        });
    }

    private void refs() {
        foodItemPic = findViewById(R.id.addFItemPicCafe);
        foodName = findViewById(R.id.fItemNameTxtCafe);
        foodCategory = findViewById(R.id.fItemCategoryTxtCafe);
        foodAvail = findViewById(R.id.fItemAvailTxtCafe);
        foodPrice = findViewById(R.id.fItemPriceTxtCafe);

        saveBtn = findViewById(R.id.saveFoodCafe);
        backBtn = findViewById(R.id.backBtnAddFoodCafe);
        foodNameLayout = findViewById(R.id.foodNameLayoutCafe);
        foodCategoryLayout = findViewById(R.id.foodAvailLayoutCafe);
        foodAvailLayout = findViewById(R.id.foodAvailLayoutCafe);
        foodPriceLayout = findViewById(R.id.foodPriceLayoutCafe);
    }

    private void cafeMenuItems() {
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

    private void saveFoodItemDetailsToFirestore() {
        txtFoodName = foodName.getText().toString().trim();
        txtFoodCategory = foodCategory.getText().toString().trim();
        txtFoodAvail = foodAvail.getText().toString();
        txtFoodPrice = foodPrice.getText().toString();

        if (foodImageUri != null) {
            //Validations::
            if (txtFoodName.isEmpty() || txtFoodCategory.isEmpty() || txtFoodAvail.isEmpty() || txtFoodPrice.isEmpty()) {
                Toast.makeText(AddMenuItemCafe.this, "Some fields are EMPTY.", Toast.LENGTH_SHORT).show();
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
                }if (txtFoodCategory.isEmpty()) {
                    foodCategoryLayout.setError("Enter Food Category");
                }else {
                    foodCategoryLayout.setErrorEnabled(false);
                    foodCategoryLayout.setError("");
                }if (txtFoodAvail.isEmpty()) {
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
                                Toast.makeText(AddMenuItemCafe.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                                //Store food item image and details::
                                String imgUrl = uri.toString();//get the firebasestorage image url

                                Map<String, Object> cafeMenuItems = new HashMap<>();
                                cafeMenuItems.put("cafeFIId", autoId);
                                cafeMenuItems.put("cafeFIName", txtFoodName);
                                cafeMenuItems.put("cafeFICategory", txtFoodCategory);
                                cafeMenuItems.put("cafeFIAvail", txtFoodAvail);
                                cafeMenuItems.put("cafeFIPrice", txtFoodPrice);
                                cafeMenuItems.put("cafeFIImgUrl", imgUrl);
                                cafeMenuItems.put("cafeFIQuantity", "0");

                                //To save inside the document of the userID, under the cafe-menu-items collection
                                fStoreRef.collection("establishments").document(userId).collection("cafe-menu-items").document(autoId).set(cafeMenuItems);

                                cafeMenuItems();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddMenuItemCafe.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

}