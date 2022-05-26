package com.example.pakigsabot.Clinics.DentalClinic.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pakigsabot.Clinics.DentalClinic.Models.DentalClinicModel;
import com.example.pakigsabot.DentalAndEyeClinics.DentalClinicReservation;
import com.example.pakigsabot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DentalClinicAdapter extends RecyclerView.Adapter<DentalClinicAdapter.MyViewHolder>{
    //creating variables for Context and ArrayList
    Context context;
    ArrayList<DentalClinicModel> dentalClinicEstArrayList;

    //Initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    public DentalClinicAdapter() {
        //empty constructor needed
    }

    public DentalClinicAdapter(Context context, ArrayList<DentalClinicModel> dentalClinicModelArrayList) {
        this.context = context;
        this.dentalClinicEstArrayList = dentalClinicModelArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore = FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public DentalClinicAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.dental_and_eye_est_item,parent, false);
        return  new MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull DentalClinicAdapter.MyViewHolder holder, int position) {
        //creating  object of DentalClinicModel class and setting data to the textviews from the DentalClinicModel class
        DentalClinicModel dclist = dentalClinicEstArrayList.get(position);
        holder.name.setText(dclist.getEst_Name());
        holder.address.setText(dclist.getEst_address());
        Glide.with(context).load(dclist.getEst_image()).into(holder.img);

        //Update an item to the Dental Clinic
        holder.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String estID = dclist.getEst_id();
                String estName = dclist.getEst_Name();
                String estAddress = dclist.getEst_address();
                String estImgUrl = dclist.getEst_image();
                String estPhoneNum = dclist.getEst_phoneNum();

                Toast.makeText(context, dclist.getEst_Name() + " Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), DentalClinicReservation.class);
                intent.putExtra("EstID", estID);
                intent.putExtra("EstName", estName);
                intent.putExtra("EstAddress", estAddress);
                intent.putExtra("EstImageUrl", estImgUrl);
                intent.putExtra("EstPhoneNum", estPhoneNum);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dentalClinicEstArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our textviews, imageview, button and rating bar.
        TextView name, address, reviews;
        ImageView img;
        Button selectBtn;
        RatingBar rateStars;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //References
            name = itemView.findViewById(R.id.dentEyeEstRVName);
            address = itemView.findViewById(R.id.dentEyeEstRVAddress);
            img = itemView.findViewById(R.id.dentEyeEstRVImg);
            selectBtn = itemView.findViewById(R.id.selectBtn);
            reviews = itemView.findViewById(R.id.dentEyesEstRVReviews);
            rateStars = itemView.findViewById(R.id.dentEyeEstRVRating);
        }
    }

    public void filterList(List<DentalClinicModel> filteredList){
        dentalClinicEstArrayList = (ArrayList<DentalClinicModel>) filteredList;
        notifyDataSetChanged();
    }
}
