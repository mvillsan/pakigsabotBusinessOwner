package com.example.pakigsabot.DentalAndEyeClinics.Adapters.EyeClinic;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pakigsabot.DentalAndEyeClinics.Models.EyeClinic.EyeClinicProceduresModel;
import com.example.pakigsabot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EyeClinicProceduresAdapter extends RecyclerView.Adapter<EyeClinicProceduresAdapter.MyViewHolder> {
    // creating variables for our ArrayList and context
    Context context;
    ArrayList<EyeClinicProceduresModel> opticalProceduresArrayList;

    //Initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    public EyeClinicProceduresAdapter() {
        //empty constructor needed
    }

    //creating constructor for thea adapter class
    public EyeClinicProceduresAdapter(Context context, ArrayList<EyeClinicProceduresModel> opticalProceduresArrayList) {
        this.context = context;
        this.opticalProceduresArrayList = opticalProceduresArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore = FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public EyeClinicProceduresAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.dent_and_eye_procedures_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EyeClinicProceduresAdapter.MyViewHolder holder, int position) {
        //creating  object of EyeClinicProceduresModel class class and setting data to the textviews from the EyeClinicProceduresModel class
        EyeClinicProceduresModel ecpList = opticalProceduresArrayList.get(position);

        holder.name.setText(ecpList.getOpticalPRId());
        holder.desc.setText(ecpList.getOpticalPRDesc());
        holder.rate.setText(ecpList.getOpticalPRRate());
        Glide.with(context).load(ecpList.getOpticalPRImgUrl()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return opticalProceduresArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        //refer all elements where data will be populated
        TextView name, desc, rate;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dentEyeRVName);
            desc = itemView.findViewById(R.id.dentEyeRVDesc);
            rate = itemView.findViewById(R.id.dentEyeRVRate);
            img = itemView.findViewById(R.id.dentEyeRVProcImg);
        }
    }
}
