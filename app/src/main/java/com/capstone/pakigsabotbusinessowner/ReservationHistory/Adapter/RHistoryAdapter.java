package com.capstone.pakigsabotbusinessowner.ReservationHistory.Adapter;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.ReservationHistory.Model.RHistoryModel;
import com.capstone.pakigsabotbusinessowner.Reservations.LoadReservationsDentalClinic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

public class RHistoryAdapter extends RecyclerView.Adapter<RHistoryAdapter.MyViewHolder>{
    // creating variables for our ArrayList and context
    Context context;
    ArrayList<RHistoryModel> rHistoryArrayList;

    //Initializing variables
    ProgressDialog progressDialog;

    //For swipe refresh to load updates
    SwipeRefreshLayout reservationsSwipeRefresh;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    public RHistoryAdapter() {
        //empty constructor needed
    }
    public RHistoryAdapter(Context context, ArrayList<RHistoryModel> rHistoryArrayList) {
        this.context = context;
        this.rHistoryArrayList = rHistoryArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore =FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public RHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.reservation_history_items,parent, false);
        return  new MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RHistoryAdapter.MyViewHolder holder, int position) {
        //creating  object of ReservationsModel class and setting data to the textviews from the ReservationsModel class
        RHistoryModel rHistoryList = rHistoryArrayList.get(position);
        holder.date.setText(rHistoryList.getReserveDateIn());
        holder.time.setText(rHistoryList.getReserveTime());
        holder.custName.setText(rHistoryList.getCustFName() + " " + rHistoryList.getCustLName());
        holder.reservationName.setText(rHistoryList.getReserveName());
        holder.pax.setText(rHistoryList.getReservePax());

        //Show reservation details and update status
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.reservationName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.reservation_details_popup))
                        .setExpanded(true, 800)
                        .create();

                dialogPlus.show();

                //References::
                View viewDP = dialogPlus.getHolderView();
                TextView reserveDate = viewDP.findViewById(R.id.reserveDateTV);
                TextView customerName = viewDP.findViewById(R.id.customerName);
                TextView reservedET = viewDP.findViewById(R.id.reservedNameTV);
                TextView feeET = viewDP.findViewById(R.id.feeTV);
                TextView adultPaxET = viewDP.findViewById(R.id.adultPaxET);
                TextView petPaxET = viewDP.findViewById(R.id.petPaxET);
                TextView dateTransacET = viewDP.findViewById(R.id.dateTransacTV);
                Button closeRHistoryDetails = viewDP.findViewById(R.id.closeRHistoryBtn);


                //Retrieving Data
                reserveDate.setText(reserveDate.getText().toString() + rHistoryList.getReserveDateIn() + "   Time: " + rHistoryList.getReserveTime());
                customerName.setText(customerName.getText().toString() + rHistoryList.getCustFName() + " " + rHistoryList.getCustLName());
                reservedET.setText(reservedET.getText().toString() + rHistoryList.getReserveName());
                adultPaxET.setText(adultPaxET.getText().toString() + rHistoryList.getAdultPax() + "   Child: " + rHistoryList.getChildPax() + "  Infant: " + rHistoryList.getInfantPax());
                petPaxET.setText(petPaxET.getText().toString() + rHistoryList.getPetPax());
                feeET.setText(feeET.getText().toString() + rHistoryList.getFee());
                dateTransacET.setText(dateTransacET.getText().toString() + rHistoryList.getDateOfTransaction());

                closeRHistoryDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         Intent in = new Intent(context.getApplicationContext(), LoadReservationsDentalClinic.class);
                        context.startActivity(in);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return rHistoryArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our textviews and imageview
        TextView date, custName, reservationName, pax, time;
        ImageView details, email;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.reserveDateTV);
            time = itemView.findViewById(R.id.reserveTimeTV);
            custName = itemView.findViewById(R.id.custNameTV);
            reservationName = itemView.findViewById(R.id.reservedNameTV);
            pax = itemView.findViewById(R.id.paxTV);
            details = itemView.findViewById(R.id.reserveInfoBtn);
            email = itemView.findViewById(R.id.sendEmailToCustBtn);
        }
    }
}
