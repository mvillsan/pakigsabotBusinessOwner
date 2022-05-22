package com.capstone.pakigsabotbusinessowner.EyeClinic.Adapter;

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
import com.capstone.pakigsabotbusinessowner.EyeClinic.Model.EyeClinicProcedures;
import com.capstone.pakigsabotbusinessowner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EyeClinicAdapter extends RecyclerView.Adapter<EyeClinicAdapter.MyViewHolder> {
    //creating variables for Context and ArrayList
    Context context;
    ArrayList <EyeClinicProcedures> opticalProceduresArrayList;

    //initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    public EyeClinicAdapter()
    {
        //Empty constructor needed
    }

    //creating constructor for the adapter class
    public EyeClinicAdapter(Context context, ArrayList<EyeClinicProcedures> opticalProceduresArrayList) {
        this.context = context;
        this.opticalProceduresArrayList = opticalProceduresArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore = FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }


    @NonNull
    @Override
    public EyeClinicAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.dental_and_eye_procedures_items,parent, false);
        return  new EyeClinicAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //creating  object of DentalClinicProcedures class and setting data to the textviews from the DentalClinicProcedures class
        EyeClinicProcedures opList = opticalProceduresArrayList.get(position);

        holder.name.setText(opList.getOpticalPRName());
        holder.desc.setText(opList.getOpticalPRDesc());
        holder.rate.setText(opList.getOpticalPRRate());
        Picasso.with(context)
                .load(opList.getOpticalPRImgUrl())
                .fit()
                .centerCrop()
                .into(holder.img);

        //Update an item from the dental procedures
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_dental_and_eye_procedures_popup))
                        .setExpanded(true,900)
                        .create();

                dialogPlus.show();

                //References
                View viewDP = dialogPlus.getHolderView();
                TextInputLayout procNameLayout = viewDP.findViewById(R.id.updateProcNameLayout);
                TextInputLayout procDescLayout = viewDP.findViewById(R.id.updateProcDescLayout);
                TextInputLayout procRateLayout = viewDP.findViewById(R.id.updateProcRateLayout);
                TextInputLayout imgLayout = viewDP.findViewById(R.id.imgLayout);
                TextInputEditText nameTxt = viewDP.findViewById(R.id.nameTxt);
                TextInputEditText descTxt = viewDP.findViewById(R.id.descTxt);
                TextInputEditText rateTxt = viewDP.findViewById(R.id.rateTxt);
                TextInputEditText imgTxt = viewDP.findViewById(R.id.imgTxt);
                Button updateBtnPopUp = viewDP.findViewById(R.id.updateBtnPopUp);

                //Retrieving Data
                nameTxt.setText(opList.getOpticalPRName());
                descTxt.setText(opList.getOpticalPRDesc());
                rateTxt.setText(opList.getOpticalPRRate());
                imgTxt.setText(opList.getOpticalPRImgUrl());

                updateBtnPopUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Initialization of variables
                        String txtProcName = nameTxt.getText().toString();
                        String txtProcDesc = descTxt.getText().toString();
                        String txtProcRate = rateTxt.getText().toString();
                        String txtImg = imgTxt.getText().toString();
                        Glide.with(context).load(txtImg).into(holder.img);

                        //Validations
                        if(txtProcName.isEmpty() || txtProcDesc.isEmpty() || txtProcRate.isEmpty()){
                            Toast.makeText(context, "Some fields are EMPTY.", Toast.LENGTH_SHORT).show();
                        }else{
                            if(txtProcName.isEmpty()) {
                                procNameLayout.setError("Enter Name of Procedure");
                            } else {
                                Boolean  validName = txtProcName.matches("[A-Za-z][A-Za-z ]*+");
                                if (!validName) {
                                    procNameLayout.setError("Invalid Procedure Name");
                                } else {
                                    procNameLayout.setErrorEnabled(false);
                                    procNameLayout.setError("");
                                }
                            }if (txtProcDesc.isEmpty()) {
                                procDescLayout.setError("Enter Description");
                            } else {
                                procDescLayout.setErrorEnabled(false);
                                procDescLayout.setError("");
                            }if (txtProcRate.isEmpty()) {
                                procRateLayout.setError("Enter Rate");
                            } else {
                                procRateLayout.setErrorEnabled(false);
                                procRateLayout.setError("");
                            } if (txtImg.isEmpty()) {
                                imgLayout.setError("No image selected");
                            }
                            else {
                                imgLayout.setErrorEnabled(false);
                                imgLayout.setError("");
                            }

                            //To save updates to the database
                            Map<String,Object> opticalServices = new HashMap<>();
                            opticalServices.put("opticalPRName", txtProcName);
                            opticalServices.put("opticalPRDesc", txtProcDesc);
                            opticalServices.put("opticalPRRate", txtProcRate);
                            opticalServices.put("opticalPRImgUrl", imgTxt.getText().toString().trim());
                            opticalServices.put("opticalPRId", opList.getOpticalPRId());


                            String userId = fAuth.getCurrentUser().getUid();
                            fStore.collection("establishments").document(userId).collection("optical-procedures").document(opList.getOpticalPRId()).set(opticalServices)
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

        //Delete from optical-procedures
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setTitle("DELETE PROCEDURE")
                        .setMessage("Do you want to Delete " + opList.getOpticalPRName()+ "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog = new ProgressDialog(context);
                                progressDialog.setCancelable(false);
                                progressDialog.setTitle("Deleting...");
                                progressDialog.show();

                                String userId = fAuth.getCurrentUser().getUid();
                                fStore.collection("establishments").document(userId).collection("optical-procedures").document(opList.getOpticalPRId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, opList.getOpticalPRName() + " Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                opticalProceduresArrayList.remove(position);
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

        return opticalProceduresArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        //refer all elements where data will be populated
        TextView name, desc, rate;
        ImageView img, update, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dentEyeRVName);
            desc = itemView.findViewById(R.id.dentEyeRVDesc);
            rate = itemView.findViewById(R.id.dentEyeRVRate);
            img = itemView.findViewById(R.id.dentEyeImgProcedure);
            update = itemView.findViewById(R.id.dentEyeUpdateBtn);
            delete = itemView.findViewById(R.id.dentEyeDeleteBtn);
        }
    }
}
