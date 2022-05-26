package com.capstone.pakigsabotbusinessowner.DentalClinic.PromoAndDeals;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DentalClinicPromoAndDealsAdapter extends RecyclerView.Adapter<DentalClinicPromoAndDealsAdapter.MyViewHolder>{
    //creating variables for Context and ArrayList
    Context context;
    ArrayList<DentalClinicPromoAndDealsModel> dentalClinicPADArrayList;

    //initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;


    public DentalClinicPromoAndDealsAdapter() {
        //empty constructor needed
    }

    //creating constructor for the adapter class
    public DentalClinicPromoAndDealsAdapter(Context context, ArrayList<DentalClinicPromoAndDealsModel> dentalClinicPADArrayList) {
        this.context = context;
        this.dentalClinicPADArrayList = dentalClinicPADArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore =FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.promo_and_deals_items,parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //creating  object of DentalClinicPromoAndDealsModel class and setting data to the textviews from the DentalClinicPromoAndDealsModel class
        DentalClinicPromoAndDealsModel dcPADList = dentalClinicPADArrayList.get(position);

        holder.name.setText(dcPADList.getDentalPADName());
        holder.desc.setText(dcPADList.getDentalPADDesc());
        holder.startDate.setText(dcPADList.getDentalPADStartDate());
        holder.endDate.setText(dcPADList.getDentalPADEndDate());

        //Update an item from the dental procedures
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.endDate.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_promo_and_deals_items_popup))
                        .setExpanded(true,900)
                        .create();

                dialogPlus.show();

                //References
                View viewPAD = dialogPlus.getHolderView();
                TextInputLayout padNameLayout = viewPAD.findViewById(R.id.updatePADNameLayout);
                TextInputLayout padDescLayout = viewPAD.findViewById(R.id.updatePADDescLayout);
                TextInputLayout padStartDateLayout = viewPAD.findViewById(R.id.updatePADStartDateLayout);
                TextInputLayout padEndDateLayout = viewPAD.findViewById(R.id.updatePADEndDateLayout);
                TextInputEditText nameTxt = viewPAD.findViewById(R.id.promoDealsNameTxt);
                TextInputEditText descTxt = viewPAD.findViewById(R.id.promoDealsDescTxt);
                TextInputEditText startDateTxt = viewPAD.findViewById(R.id.promoDealsStartDateTxt);
                TextInputEditText endDateTxt = viewPAD.findViewById(R.id.promoDealsEndDateTxt);
                Button updateBtnPopUp = viewPAD.findViewById(R.id.updateBtnPopUp);

                //Retrieving Data
                nameTxt.setText(dcPADList.getDentalPADName());
                descTxt.setText(dcPADList.getDentalPADDesc());
                startDateTxt.setText(dcPADList.getDentalPADStartDate());
                endDateTxt.setText(dcPADList.getDentalPADEndDate());

                updateBtnPopUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Initialization of variables
                        String txtPADName = nameTxt.getText().toString();
                        String txtPADDesc = descTxt.getText().toString();
                        String txtPADStartDate = startDateTxt.getText().toString();
                        String txtPADEndDate = endDateTxt.getText().toString();

                        //Validations
                        if(txtPADName.isEmpty() || txtPADDesc.isEmpty() || txtPADStartDate.isEmpty() || txtPADEndDate.isEmpty()){
                            Toast.makeText(context, "Some fields are EMPTY.", Toast.LENGTH_SHORT).show();
                        }else {
                            if (txtPADName.isEmpty()) {
                                padNameLayout.setError("Enter Name of Promo and Deals");
                            } else {
                                Boolean validName = txtPADName.matches("[A-Za-z][A-Za-z ]*+");
                                if (!validName) {
                                    padNameLayout.setError("Invalid Promo and Deals Name");
                                } else {
                                    padNameLayout.setErrorEnabled(false);
                                    padNameLayout.setError("");
                                }
                            }
                            if (txtPADDesc.isEmpty()) {
                                padDescLayout.setError("Enter Description");
                            } else {
                                padDescLayout.setErrorEnabled(false);
                                padDescLayout.setError("");
                            }
                            if (txtPADStartDate.isEmpty()) {
                                padStartDateLayout.setError("Enter Promo and Deals Start Date");
                            } else {
                                padStartDateLayout.setErrorEnabled(false);
                                padStartDateLayout.setError("");
                            }
                            if (txtPADEndDate.isEmpty()) {
                                padEndDateLayout.setError("Enter Promo and Deals End Date");
                            } else {
                                padEndDateLayout.setErrorEnabled(false);
                                padEndDateLayout.setError("");
                            }

                            //To save updates to the database
                            Map<String, Object> dentalPAD = new HashMap<>();
                            dentalPAD.put("dentalPADName", txtPADName);
                            dentalPAD.put("dentalPADDesc", txtPADDesc);
                            dentalPAD.put("dentalPADStartDate", txtPADStartDate);
                            dentalPAD.put("dentalPADEndDate", txtPADEndDate);
                            dentalPAD.put("dentalPADId", dcPADList.getDentalPADId());


                            String userId = fAuth.getCurrentUser().getUid();
                            fStore.collection("establishments").document(userId).collection("dental-clinic-promo-and-deals").document(dcPADList.getDentalPADId()).set(dentalPAD)
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
                            notifyItemChanged(position);
                        }
                    }
                });
            }
        });

        //Delete from dental-procedures
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setTitle("DELETE PROCEDURE")
                        .setMessage("Do you want to Delete " + dcPADList.getDentalPADName()+ "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog = new ProgressDialog(context);
                                progressDialog.setCancelable(false);
                                progressDialog.setTitle("Deleting...");
                                progressDialog.show();

                                String userId = fAuth.getCurrentUser().getUid();
                                fStore.collection("establishments").document(userId).collection("dental-clinic-promo-and-deals").document(dcPADList.getDentalPADId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, dcPADList.getDentalPADName() + " Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                dentalClinicPADArrayList.remove(position);
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
        return dentalClinicPADArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        //refer all elements where data will be populated
        TextView name, desc, startDate, endDate;
        ImageView update, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.promoDealsRVName);
            desc = itemView.findViewById(R.id.promoDealsRVDesc);
            startDate = itemView.findViewById(R.id.promoDealsRVStartDate);
            endDate = itemView.findViewById(R.id.promoDealsRVEndDate);
            update = itemView.findViewById(R.id.promoDealsUpdateBtn);
            delete = itemView.findViewById(R.id.promoDealsDeleteBtn);
        }
    }
}



