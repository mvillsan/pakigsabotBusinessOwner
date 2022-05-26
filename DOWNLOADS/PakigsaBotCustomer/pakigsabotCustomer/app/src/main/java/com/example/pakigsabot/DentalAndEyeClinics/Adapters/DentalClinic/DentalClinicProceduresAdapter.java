package com.example.pakigsabot.DentalAndEyeClinics.Adapters.DentalClinic;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pakigsabot.DentalAndEyeClinics.DentalClinicReservation;
import com.example.pakigsabot.DentalAndEyeClinics.DentalClinicSelectedProcedures;
import com.example.pakigsabot.DentalAndEyeClinics.Models.DentalClinic.DentalClinicProceduresModel;
import com.example.pakigsabot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DentalClinicProceduresAdapter extends RecyclerView.Adapter<DentalClinicProceduresAdapter.MyViewHolder> {
    // creating variables for our ArrayList and context
    Context context;
    ArrayList<DentalClinicProceduresModel> dentalProceduresArrayList;

    //Initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    public DentalClinicProceduresAdapter() {
    }

    public DentalClinicProceduresAdapter(Context context, ArrayList<DentalClinicProceduresModel> dentalProceduresArrayList) {
        this.context = context;
        this.dentalProceduresArrayList = dentalProceduresArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore = FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public DentalClinicProceduresAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.dent_and_eye_procedures_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DentalClinicProceduresAdapter.MyViewHolder holder, int position) {
        //creating  object of DentalProceduresModel class and setting data to the textviews from the DentalProceduresModel class
        DentalClinicProceduresModel dpList = dentalProceduresArrayList.get(position);

        holder.name.setText(dpList.getDentalPRName());
        holder.desc.setText(dpList.getDentalPRDesc());
        holder.rate.setText(dpList.getDentalPRRate());
        Glide.with(context).load(dpList.getDentalPRImgUrl()).into(holder.img);

        //Select an item to the Dental Clinic
        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String procId = dpList.getDentalPRId();
                String procName = dpList.getDentalPRName();
                String procRate = dpList.getDentalPRRate();
                String procImgUrl = dpList.getDentalPRImgUrl();
                String estId = dpList.getEstId();


                Toast.makeText(context, dpList.getDentalPRName() + " Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), DentalClinicSelectedProcedures.class);
                intent.putExtra("ProcId", procId);
                intent.putExtra("ProcName", procName);
                intent.putExtra("ProcRate", procRate);
                intent.putExtra("ProcImageUrl", procImgUrl);
                intent.putExtra("ProcEstId", estId);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dentalProceduresArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        //refer all elements where data will be populated
        TextView name, desc, rate;
        ImageView img;
        Button select;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dentEyeRVName);
            desc = itemView.findViewById(R.id.dentEyeRVDesc);
            rate = itemView.findViewById(R.id.dentEyeRVRate);
            img = itemView.findViewById(R.id.dentEyeRVProcImg);
            select = itemView.findViewById(R.id.selectProcRVBtn);
        }
    }
}
