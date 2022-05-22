package com.capstone.pakigsabotbusinessowner.Restaurant.Adapter;

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
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Restaurant.Model.RestoMenuItems;
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

public class RestoAdapter extends RecyclerView.Adapter<RestoAdapter.MyViewHolder> {
    //creating variables for Context and ArrayList
    Context context;
    ArrayList<RestoMenuItems> restoMenuItemsArrayList;

    //initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseUser fUser;

    public RestoAdapter() {
        //empty constructor needed
    }

    //creating constructor for adapter class
    public RestoAdapter(Context context, ArrayList<RestoMenuItems> restoMenuItemsArrayList) {
        this.context = context;
        this.restoMenuItemsArrayList = restoMenuItemsArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore =FirebaseFirestore.getInstance();
        this.fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public RestoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.resto_menu_items,parent, false);
        return  new MyViewHolder(itemView);
    }


    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RestoAdapter.MyViewHolder holder, int position) {
        //creating  object of RestoMenuItems class and setting data to the textviews from the RestoMenuItems class
        RestoMenuItems rmiList = restoMenuItemsArrayList.get(position);

        holder.name.setText(rmiList.getRestoFIName());
        holder.category.setText(rmiList.getRestoFICategory());
        holder.desc.setText(rmiList.getRestoFIDesc());
        holder.avail.setText(rmiList.getRestoFIAvailability());
        holder.price.setText(rmiList.getRestoFIPrice());
        Picasso.with(context)
                .load(rmiList.getRestoFIImgUrl())
                .fit()
                .centerCrop()
                .into(holder.img);

        //Update an item from the resto-menu-items
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_resto_menu_items_popup))
                        .setExpanded(true,900)
                        .create();

                dialogPlus.show();

                //References
                View viewFI = dialogPlus.getHolderView();
                TextInputLayout foodItemNameLayout = viewFI.findViewById(R.id.updateFINameLayout);
                TextInputLayout foodItemCategoryLayout = viewFI.findViewById(R.id.updateFICategoryLayout);
                TextInputLayout foodItemDescLayout = viewFI.findViewById(R.id.updateFIDescLayout);
                TextInputLayout foodItemAvailLayout = viewFI.findViewById(R.id.updateFIAvailLayout);
                TextInputLayout foodItemPriceLayout = viewFI.findViewById(R.id.updateFIPriceLayout);
                TextInputLayout imgLayout = viewFI.findViewById(R.id.imgLayout);
                TextInputEditText nameTxt = viewFI.findViewById(R.id.nameTxt);
                TextInputEditText categTxt = viewFI.findViewById(R.id.categoryTxt);
                TextInputEditText descTxt = viewFI.findViewById(R.id.descTxt);
                TextInputEditText availTxt = viewFI.findViewById(R.id.availTxt);
                TextInputEditText priceTxt = viewFI.findViewById(R.id.priceTxt);
                TextInputEditText imgTxt = viewFI.findViewById(R.id.imgTxt);
                Button updateBtnPopUp = viewFI.findViewById(R.id.updateBtnRestoCafePopUp);

                //Retrieving Data
                nameTxt.setText(rmiList.getRestoFIName());
                categTxt.setText(rmiList.getRestoFICategory());
                descTxt.setText(rmiList.getRestoFIDesc());
                availTxt.setText(rmiList.getRestoFIAvailability());
                priceTxt.setText(rmiList.getRestoFIPrice());
                imgTxt.setText(rmiList.getRestoFIImgUrl());

                updateBtnPopUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Initialization of variables
                        String txtFIName = nameTxt.getText().toString();
                        String txtFICategory = categTxt.getText().toString();
                        String txtFIDesc = descTxt.getText().toString();
                        String txtFIAvail = availTxt.getText().toString();
                        String txtFIPrice = priceTxt.getText().toString();
                        String txtImg = imgTxt.getText().toString();
                        Glide.with(context).load(txtImg).into(holder.img);

                        //Validations
                        if(txtFIName.isEmpty() || txtFICategory.isEmpty() || txtFIDesc.isEmpty() || txtFIAvail.isEmpty() || txtFIPrice.isEmpty() || txtImg.isEmpty()){
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
                            }if (txtFIDesc.isEmpty()) {
                                foodItemDescLayout.setError("Enter Description");
                            } else {
                                foodItemDescLayout.setErrorEnabled(false);
                                foodItemDescLayout.setError("");
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
                            Map<String,Object> restoMenuItems = new HashMap<>();
                            restoMenuItems.put("restoFIName", txtFIName);
                            restoMenuItems.put("restoFICategory", txtFICategory);
                            restoMenuItems.put("restoFIDesc", txtFIDesc);
                            restoMenuItems.put("restoFIAvail", txtFIAvail);
                            restoMenuItems.put("restoFIPrice", txtFIPrice);
                            restoMenuItems.put("restoFIImgUrl", imgTxt.getText().toString().trim());
                            restoMenuItems.put("restoFIId", rmiList.getRestoFIId());


                            String userId = fUser.getUid();
                            fStore.collection("establishments").document(userId).collection("resto-menu-items").document(rmiList.getRestoFIId()).set(restoMenuItems)
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

        //Delete from resto-menu-items
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setTitle("DELETE MENU ITEM")
                        .setMessage("Do you want to Delete " + rmiList.getRestoFIName()+ "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog = new ProgressDialog(context);
                                progressDialog.setCancelable(false);
                                progressDialog.setTitle("Deleting...");
                                progressDialog.show();

                                String userId = fUser.getUid();
                                fStore.collection("establishments").document(userId).collection("resto-menu-items").document(rmiList.getRestoFIId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, rmiList.getRestoFIName() + " Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                restoMenuItemsArrayList.remove(position);
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




    //item count of the items
    @Override
    public int getItemCount() {

        return restoMenuItemsArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        //refer all elements where data will be populated
        TextView name, category, desc, avail, price;
        ImageView img, update, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restoRVName);
            category = itemView.findViewById(R.id.restoRVCategory);
            desc = itemView.findViewById(R.id.restoRVDesc);
            avail = itemView.findViewById(R.id.restoRVAvailability);
            price = itemView.findViewById(R.id.restoRVPrice);
            img = itemView.findViewById(R.id.restoFItemImg);
            update = itemView.findViewById(R.id.restoUpdateBtn);
            delete = itemView.findViewById(R.id.restoDeleteBtn);
        }
    }



}
