package com.capstone.pakigsabotbusinessowner.EstablishmentRules;

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

public class EstRulesAdapter extends RecyclerView.Adapter<EstRulesAdapter.MyViewHolder> {
    //creating variables for Context and ArrayList
    Context context;
    ArrayList<EstRulesModel> restoERArrayList;

    //initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    public EstRulesAdapter(Context context, ArrayList<EstRulesModel> restoERArrayList) {
        this.context = context;
        this.restoERArrayList = restoERArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore =FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    public EstRulesAdapter() {
    }

    @NonNull
    @Override
    public EstRulesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.rules_items,parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull EstRulesAdapter.MyViewHolder holder, int position) {
        //creating  object of DentalClinicPromoAndDealsModel class and setting data to the textviews from the DentalClinicPromoAndDealsModel class
        EstRulesModel restoERList = restoERArrayList.get(position);

        holder.name.setText(restoERList.getEstRulesName());
        holder.desc.setText(restoERList.getEstRulesDesc());

        //Update an item from the dental procedures
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.desc.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_rules_popup))
                        .setExpanded(true,900)
                        .create();

                dialogPlus.show();

                //References
                View viewPAD = dialogPlus.getHolderView();
                TextInputLayout estRulesNameLayout = viewPAD.findViewById(R.id.updatePADNameLayout);
                TextInputLayout estRulesDescLayout = viewPAD.findViewById(R.id.updatePADDescLayout);
                TextInputEditText nameTxt = viewPAD.findViewById(R.id.promoDealsNameTxt);
                TextInputEditText descTxt = viewPAD.findViewById(R.id.promoDealsDescTxt);

                Button updateBtnPopUp = viewPAD.findViewById(R.id.updateBtnPopUp);

                //Retrieving Data
                nameTxt.setText(restoERList.getEstRulesName());
                descTxt.setText(restoERList.getEstRulesDesc());


                updateBtnPopUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Initialization of variables
                        String txtEstRulesName = nameTxt.getText().toString();
                        String txtEstRulesDesc = descTxt.getText().toString();

                        //Validations::
                        if (txtEstRulesName.isEmpty() || txtEstRulesDesc.isEmpty()) {
                            Toast.makeText(context, "Some fields are EMPTY.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (txtEstRulesName.isEmpty()) {
                                estRulesNameLayout.setError("Enter Name of Rule");
                            } else {
                                Boolean validName = txtEstRulesName.matches("[A-Za-z][A-Za-z ]*+");
                                if (!validName) {
                                    estRulesNameLayout.setError("Invalid Rule Name");
                                } else {
                                    estRulesNameLayout.setErrorEnabled(false);
                                    estRulesNameLayout.setError("");
                                }
                            }
                            if (txtEstRulesDesc.isEmpty()) {
                                estRulesDescLayout.setError("Enter Description");
                            } else {
                                estRulesDescLayout.setErrorEnabled(false);
                                estRulesDescLayout.setError("");
                            }

                            //To save updates to the database
                            Map<String, Object> restoEstRules = new HashMap<>();
                            restoEstRules.put("restoERName", txtEstRulesName);
                            restoEstRules.put("restoERDesc", txtEstRulesDesc);
                            restoEstRules.put("restoERId", restoERList.getEstRulesId());

                            String userId = fAuth.getCurrentUser().getUid();
                            fStore.collection("establishments").document(userId).collection("resto-est-rules").document(restoERList.getEstRulesId()).set(restoEstRules)
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
                        .setTitle("DELETE RULE")
                        .setMessage("Do you want to Delete " + restoERList.getEstRulesName()+ "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog = new ProgressDialog(context);
                                progressDialog.setCancelable(false);
                                progressDialog.setTitle("Deleting...");
                                progressDialog.show();

                                String userId = fAuth.getCurrentUser().getUid();
                                fStore.collection("establishments").document(userId).collection("resto-est-rules").document(restoERList.getEstRulesId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, restoERList.getEstRulesName() + " Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                restoERArrayList.remove(position);
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
        return restoERArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //refer all elements where data will be populated
        TextView name, desc;
        ImageView update, delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.estRulesRVName);
            desc = itemView.findViewById(R.id.estRulesRVDesc);
            update = itemView.findViewById(R.id.estRulesUpdateBtn);
            delete = itemView.findViewById(R.id.estRulesDeleteBtn);
        }
    }
}
