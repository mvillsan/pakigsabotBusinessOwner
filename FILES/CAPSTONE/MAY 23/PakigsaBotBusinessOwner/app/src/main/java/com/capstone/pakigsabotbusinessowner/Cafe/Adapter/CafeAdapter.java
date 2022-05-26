package com.capstone.pakigsabotbusinessowner.Cafe.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.capstone.pakigsabotbusinessowner.Cafe.Model.CafeMenuItems;
import com.capstone.pakigsabotbusinessowner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CafeAdapter extends RecyclerView.Adapter<CafeAdapter.MyViewHolder> {
    //creating variables for Context and ArrayList
    Context context;
    ArrayList<CafeMenuItems> cafeMenuItemsArrayList;

    //initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseUser fUser;

    public CafeAdapter() {
        //empty constructor needed
    }

    //creating constructor for adapter class
    public CafeAdapter(Context context, ArrayList<CafeMenuItems> cafeMenuItemsArrayList) {
        this.context = context;
        this.cafeMenuItemsArrayList = cafeMenuItemsArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore =FirebaseFirestore.getInstance();
        this.fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public CafeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.cafe_menu_items,parent, false);
        return  new MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull CafeAdapter.MyViewHolder holder, int position) {
        //creating  object of CafeMenuItems class and setting data to the textviews from the CafeMenuItems class
       CafeMenuItems cmiList = cafeMenuItemsArrayList.get(position);

        holder.name.setText(cmiList.getCafeFIName());
        holder.category.setText(cmiList.getCafeFICategory());
        holder.avail.setText(cmiList.getCafeFIAvailability());
        holder.price.setText(cmiList.getCafeFIPrice());
        Picasso.get()
                .load(cmiList.getCafeFIImgUrl())
                .fit()
                .centerCrop()
                .into(holder.img);

    //Update an item from the resto-menu-items
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_cafe_menu_items_popup))
                        .setExpanded(true,900)
                        .create();

                dialogPlus.show();

                //References
                View viewFI = dialogPlus.getHolderView();
                TextInputLayout foodItemNameLayout = viewFI.findViewById(R.id.updateFINameLayoutCafe);
                TextInputLayout foodItemCategoryLayout = viewFI.findViewById(R.id.updateFICategoryLayoutCafe);
                TextInputLayout foodItemAvailLayout = viewFI.findViewById(R.id.updateFIAvailLayoutCafe);
                TextInputLayout foodItemPriceLayout = viewFI.findViewById(R.id.updateFIPriceLayoutCafe);
                TextInputLayout imgLayout = viewFI.findViewById(R.id.imgLayoutCafe);
                TextInputEditText nameTxt = viewFI.findViewById(R.id.nameTxtCafe);
                TextInputEditText categTxt = viewFI.findViewById(R.id.categoryTxtCafe);
                TextInputEditText availTxt = viewFI.findViewById(R.id.availTxtCafe);
                TextInputEditText priceTxt = viewFI.findViewById(R.id.priceTxtCafe);
                TextInputEditText imgTxt = viewFI.findViewById(R.id.imgTxtCafe);
                Button updateBtnPopUp = viewFI.findViewById(R.id.updateBtnCafePopUp);

                //Retrieving Data
                nameTxt.setText(cmiList.getCafeFIName());
                categTxt.setText(cmiList.getCafeFICategory());
                availTxt.setText(cmiList.getCafeFIAvailability());
                priceTxt.setText(cmiList.getCafeFIPrice());
                imgTxt.setText(cmiList.getCafeFIImgUrl());

                updateBtnPopUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Initialization of variables
                        String txtFIName = nameTxt.getText().toString();
                        String txtFICategory = categTxt.getText().toString();
                        String txtFIAvail = availTxt.getText().toString();
                        String txtFIPrice = priceTxt.getText().toString();
                        String txtImg = imgTxt.getText().toString();
                        Glide.with(context).load(txtImg).into(holder.img);

                        //Validations
                        if(txtFIName.isEmpty() || txtFICategory.isEmpty() || txtFIAvail.isEmpty() || txtFIPrice.isEmpty() || txtImg.isEmpty()){
                            Toast.makeText(context, "Some fields are EMPTY.", Toast.LENGTH_SHORT).show();
                        }else{
                            if(txtFIName.isEmpty()) {
                                foodItemNameLayout.setError("Enter Name of Food Item");
                            } else {
                                Boolean  validName = txtFIName.matches("[A-Za-z][A-Za-z ]*+");
                                if (!validName) {
                                    foodItemNameLayout.setError("Invalid Procedure Name");
                                } else {
                                    foodItemNameLayout.setErrorEnabled(false);
                                    foodItemNameLayout.setError("");
                                }
                            }
                            if (txtFICategory.isEmpty()) {
                                foodItemCategoryLayout.setError("Enter Category");
                            }if (txtFIAvail.isEmpty()) {
                                foodItemAvailLayout.setError("Enter Availability");
                            }if (txtFIPrice.isEmpty()) {
                                foodItemPriceLayout.setError("Enter Price");
                            } else {
                                foodItemPriceLayout.setErrorEnabled(false);
                                foodItemPriceLayout.setError("");
                            } if (txtImg.isEmpty()) {
                                imgLayout.setError("No image selected");
                            }
                            else {
                                imgLayout.setErrorEnabled(false);
                                imgLayout.setError("");
                            }

                            //To save updates to the database
                            Map<String,Object> cafeMenuItems = new HashMap<>();
                            cafeMenuItems.put("restoFIName", txtFIName);
                            cafeMenuItems.put("restoFICategory", txtFICategory);
                            cafeMenuItems.put("restoFIAvail", txtFIAvail);
                            cafeMenuItems.put("restoFIPrice", txtFIPrice);
                            cafeMenuItems.put("restoFIImgUrl", imgTxt.getText().toString().trim());
                            cafeMenuItems.put("restoFIId", cmiList.getCafeFIId());


                            String userId = fUser.getUid();
                            fStore.collection("establishments").document(userId).collection("cafe-menu-items").document(cmiList.getCafeFIId()).set(cafeMenuItems)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, " Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, " Error While Updating!", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    });
                        }

                    }
                });
            }
        });

        //Delete from cafe-menu-items
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setTitle("DELETE MENU ITEM")
                        .setMessage("Do you want to Delete " + cmiList.getCafeFIName()+ "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog = new ProgressDialog(context);
                                progressDialog.setCancelable(false);
                                progressDialog.setTitle("Deleting...");
                                progressDialog.show();

                                String userId = fUser.getUid();
                                fStore.collection("establishments").document(userId).collection("cafe-menu-items").document(cmiList.getCafeFIId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, cmiList.getCafeFIName() + " Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                cafeMenuItemsArrayList.remove(position);
                                                notifyItemRemoved(position);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Failed to Delete!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alert.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cafeMenuItemsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //refer all elements where data will be populated
        TextView name, category, avail, price;
        ImageView img, update, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cafeRVName);
            category = itemView.findViewById(R.id.cafeRVCategory);
            avail = itemView.findViewById(R.id.cafeRVAvailability);
            price = itemView.findViewById(R.id.cafeRVPrice);
            img = itemView.findViewById(R.id.cafeFItemImg);
            update = itemView.findViewById(R.id.cafeUpdateBtn);
            delete = itemView.findViewById(R.id.cafeDeleteBtn);
        }
    }
}
