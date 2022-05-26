package com.example.pakigsabot.DentalAndEyeClinics.Adapters.DentalClinic;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pakigsabot.DentalAndEyeClinics.Models.DentalClinic.DentalClinicProceduresModel;
import com.example.pakigsabot.DentalAndEyeClinics.Models.DentalClinic.DentalClinicPromoAndDealsModel;
import com.example.pakigsabot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DentalClinicPromoAndDealsAdapter extends RecyclerView.Adapter<DentalClinicPromoAndDealsAdapter.MyViewHolder> {
    // creating variables for our ArrayList and context
    Context context;
    ArrayList<DentalClinicPromoAndDealsModel> dentalPADArrayList;

    //Initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    public DentalClinicPromoAndDealsAdapter() {
        //empty constructor needed
    }

    public DentalClinicPromoAndDealsAdapter(Context context, ArrayList<DentalClinicPromoAndDealsModel> dentalPADArrayList) {
        this.context = context;
        this.dentalPADArrayList = dentalPADArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore = FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public DentalClinicPromoAndDealsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.promo_and_deals_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull DentalClinicPromoAndDealsAdapter.MyViewHolder holder, int position) {
        DentalClinicPromoAndDealsModel dcPADList = dentalPADArrayList.get(position);

        holder.name.setText(dcPADList.getDentalPADName());
        holder.desc.setText(dcPADList.getDentalPADDesc());
        holder.startDate.setText(dcPADList.getDentalPADStartDate());
        holder.endDate.setText(dcPADList.getDentalPADEndDate());
    }

    @Override
    public int getItemCount() {
        return dentalPADArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //refer all elements where data will be populated
        TextView name, desc, startDate, endDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.promoDealsRVName);
            desc = itemView.findViewById(R.id.promoDealsRVDesc);
            startDate = itemView.findViewById(R.id.promoDealsRVStartDate);
            endDate = itemView.findViewById(R.id.promoDealsRVEndDate);
        }
    }
}
