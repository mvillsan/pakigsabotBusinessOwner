package com.capstone.pakigsabotbusinessowner.Resort.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Resort.Models.ResortRoomModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResortRoomAdapter extends RecyclerView.Adapter<ResortRoomAdapter.MyViewHolder>{
    // creating variables for our ArrayList and context
    Context context;
    ArrayList<ResortRoomModel> resortRFArrayList;

    //Initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    public ResortRoomAdapter() {
        //Empty constructor needed
    }

    // creating constructor for our adapter class
    public ResortRoomAdapter(Context context, ArrayList<ResortRoomModel> resortRFArrayList) {
        this.context = context;
        this.resortRFArrayList = resortRFArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore = FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ResortRoomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        View itemView = LayoutInflater.from(context).inflate(R.layout.resort_room_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ResortRoomAdapter.MyViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        ResortRoomModel list = resortRFArrayList.get(position);
        holder.name.setText(list.getResortRFName());
        holder.capacity.setText(list.getResortCapac());
        holder.desc.setText(list.getResortDesc());
        holder.rate.setText(list.getResortRate());
        Glide.with(context).load(list.getResortImgUrl()).into(holder.img);

        //Update an item to the Resort Rooms
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_resort_room_popup))
                        .setExpanded(true,900)
                        .create();

                dialogPlus.show();

                //References::
                View viewDP = dialogPlus.getHolderView();
                EditText nameTxt = viewDP.findViewById(R.id.nameTxt);
                EditText numOfPersonTxt = viewDP.findViewById(R.id.numOfPersonTxt);
                EditText descTxt = viewDP.findViewById(R.id.descTxt);
                EditText rateTxt = viewDP.findViewById(R.id.rateTxt);
                EditText imgTxt = viewDP.findViewById(R.id.imgTxt);
                Button updateBtnPopUp = viewDP.findViewById(R.id.updateBtnPopUp);

                //Retrieving Data
                nameTxt.setText(list.getResortRFName());
                numOfPersonTxt.setText(list.getResortCapac());
                descTxt.setText(list.getResortDesc());
                rateTxt.setText(list.getResortRate());
                imgTxt.setText(list.getResortImgUrl());

                updateBtnPopUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Initialization of variables
                        String nameTxtStr = nameTxt.getText().toString();
                        String numOfPersonTxtStr = numOfPersonTxt.getText().toString();
                        String descTxtStr = descTxt.getText().toString();
                        String rateTxtStr = rateTxt.getText().toString();
                        String imgTxtStr = imgTxt.getText().toString();

                        //Validations::
                        if (nameTxtStr.isEmpty()) {
                            Toast.makeText(context, "Enter Name of the Room", Toast.LENGTH_SHORT).show();
                        } else if (numOfPersonTxtStr.isEmpty()) {
                            Toast.makeText(context, "Enter Capacity", Toast.LENGTH_SHORT).show();
                        } else if (descTxtStr.isEmpty()) {
                            Toast.makeText(context, "Enter Description", Toast.LENGTH_SHORT).show();
                        } else if (rateTxtStr.isEmpty()) {
                            Toast.makeText(context, "Enter Rate", Toast.LENGTH_SHORT).show();
                        } else if (imgTxtStr.isEmpty()) {
                            Toast.makeText(context, "Enter Image Link", Toast.LENGTH_SHORT).show();
                        }else{
                            //To save updates to the database
                            Map<String,Object> resortServices = new HashMap<>();
                            resortServices.put("resortRFName", nameTxt.getText().toString().trim());
                            resortServices.put("resortCapac", numOfPersonTxt.getText().toString().trim());
                            resortServices.put("resortDesc", descTxt.getText().toString().trim());
                            resortServices.put("resortRate",rateTxt.getText().toString().trim());
                            resortServices.put("resortImgUrl", imgTxt.getText().toString().trim());
                            resortServices.put("resortRFID", list.getResortRFID());

                            String userID = fAuth.getCurrentUser().getUid();
                            fStore.collection("establishments").document(userID).collection("resort-rooms").document(list.getResortRFID()).set(resortServices)
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

        //Delete an item to the Resort Rooms
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setTitle("DELETE A ROOM")
                        .setMessage("Do you want to Delete the " + list.getResortRFName() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog = new ProgressDialog(context);
                                progressDialog.setCancelable(false);
                                progressDialog.setTitle("Deleting...");
                                progressDialog.show();

                                String userID = fAuth.getCurrentUser().getUid();
                                fStore.collection("establishments").document(userID).collection("resort-rooms").document(list.getResortRFID())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, list.getResortRFName() + " Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                resortRFArrayList.remove(position);
                                                notifyItemRemoved(position);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Failed to Deleted!", Toast.LENGTH_SHORT).show();
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
        // returning the size of our array list.
        return resortRFArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // creating variables for our textviews and imageview.
        TextView name, capacity,desc,rate;
        ImageView img, delete, update;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //References::
            name = itemView.findViewById(R.id.resortRFNameTV);
            capacity = itemView.findViewById(R.id.resortCapacTV);
            desc = itemView.findViewById(R.id.resortDescTV);
            rate = itemView.findViewById(R.id.resortRateTV);
            img = itemView.findViewById(R.id.imgResort);
            update = itemView.findViewById(R.id.resortUpdateBtn);
            delete = itemView.findViewById(R.id.resortDeleteBtn);
        }
    }
}
