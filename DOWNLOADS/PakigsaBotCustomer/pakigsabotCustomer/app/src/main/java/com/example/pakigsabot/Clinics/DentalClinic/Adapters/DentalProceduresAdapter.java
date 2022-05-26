package com.example.pakigsabot.Clinics.DentalClinic.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pakigsabot.Clinics.DentalClinic.Models.DentalProceduresModel;
import com.example.pakigsabot.DentalAndEyeClinics.DentalClinicReservation;
import com.example.pakigsabot.DentalAndEyeClinics.Models.DentalClinic.DentalClinicProceduresModel;
import com.example.pakigsabot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DentalProceduresAdapter extends RecyclerView.Adapter<DentalProceduresAdapter.MyViewHolder>{
    // creating variables for our ArrayList and context
    Context context;
    ArrayList<DentalProceduresModel> dentalProceduresArrayList;

    //Initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    public DentalProceduresAdapter(DentalClinicReservation context, ArrayList<DentalClinicProceduresModel> dentalProceduresArrayList) {
        //empty constructor needed
    }

    public DentalProceduresAdapter(Context context, ArrayList<DentalProceduresModel> dentalProceduresArrayList) {
        this.context = context;
        this.dentalProceduresArrayList = dentalProceduresArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore = FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public DentalProceduresAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.dent_and_eye_procedures_items,parent,false);
        return new MyViewHolder(itemView);

    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull DentalProceduresAdapter.MyViewHolder holder, int position) {
        //creating  object of DentalProceduresModel class and setting data to the textviews from the DentalProceduresModel class
       DentalProceduresModel dpList = dentalProceduresArrayList.get(position);

        holder.name.setText(dpList.getDentalPRName());
        holder.desc.setText(dpList.getDentalPRDesc());
        holder.rate.setText(dpList.getDentalPRRate());
        Glide.with(context).load(dpList.getDentalPRImgUrl()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return dentalProceduresArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //refer all elements where data will be populated
        TextView name, desc, rate;
        ImageView img;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //References
            name = itemView.findViewById(R.id.dentEyeRVName);
            desc = itemView.findViewById(R.id.dentEyeRVDesc);
            rate = itemView.findViewById(R.id.dentEyeRVRate);
            img = itemView.findViewById(R.id.dentEyeRVProcImg);

        }
    }
}
