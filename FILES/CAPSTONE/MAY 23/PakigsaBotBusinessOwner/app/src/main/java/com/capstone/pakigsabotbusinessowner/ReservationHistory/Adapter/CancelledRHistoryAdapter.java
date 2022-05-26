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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.ReservationHistory.LoadReservationHistory;
import com.capstone.pakigsabotbusinessowner.ReservationHistory.Model.CancelledRHistoryModel;
import com.capstone.pakigsabotbusinessowner.ReservationHistory.Model.CompletedRHistoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import kotlin.Suppress;

public class CancelledRHistoryAdapter extends RecyclerView.Adapter<CancelledRHistoryAdapter.MyViewHolder> {
    // creating variables for our ArrayList and context
    Context context;
    ArrayList<CancelledRHistoryModel> cancelledReservationsArrayList;

    //Initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    public CancelledRHistoryAdapter() {
        //empty constructor needed
    }

    public CancelledRHistoryAdapter(Context context, ArrayList<CancelledRHistoryModel> cancelledReservationsArrayList) {
        this.context = context;
        this.cancelledReservationsArrayList = cancelledReservationsArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore = FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public CancelledRHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.reservation_history_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull CancelledRHistoryAdapter.MyViewHolder holder, int position) {
        //creating  object of ReservationsModel class and setting data to the textviews from the ReservationsModel class
        CancelledRHistoryModel cancelledRHistoryList = cancelledReservationsArrayList.get(position);
        holder.date.setText(cancelledRHistoryList.getCancelReserveDateIn());
        holder.time.setText(cancelledRHistoryList.getCancelReserveTime());
        holder.custName.setText(cancelledRHistoryList.getCancelRCustFName()+ " " + cancelledRHistoryList.getCancelRCustLName());
        holder.reservationName.setText(cancelledRHistoryList.getCancelReserveName());
        holder.pax.setText(cancelledRHistoryList.getCancelReservePax());

        //Show reservation details and update status
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.reservationName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.reservation_history_details_popup))
                        .setExpanded(true, 700)
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
                Button closeBtn = viewDP.findViewById(R.id.closeRHistoryBtn);

                //Retrieving Data
                reserveDate.setText(reserveDate.getText().toString() + cancelledRHistoryList.getCancelReserveDateIn() + "   Time: " + cancelledRHistoryList.getCancelReserveTime());
                customerName.setText(customerName.getText().toString() + cancelledRHistoryList.getCancelRCustFName() + " " + cancelledRHistoryList.getCancelRCustLName());
                reservedET.setText(reservedET.getText().toString() + cancelledRHistoryList.getCancelReserveName());
                adultPaxET.setText(adultPaxET.getText().toString() + cancelledRHistoryList.getCancelAdultPax() + "   Child: " + cancelledRHistoryList.getCancelChildPax() + "  Infant: " + cancelledRHistoryList.getCancelInfantPax());
                petPaxET.setText(petPaxET.getText().toString() + cancelledRHistoryList.getCancelPetPax());
                feeET.setText(feeET.getText().toString() + cancelledRHistoryList.getCancelFee());
                dateTransacET.setText(dateTransacET.getText().toString() + cancelledRHistoryList.getCancelDateOfTransaction());


                //Close  Reservation Details
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(context.getApplicationContext(), LoadReservationHistory.class);
                        context.startActivity(in);
                    }
                });
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete the reservation from the reservations collection
                fStore.collection("cancelled-reservations").document(cancelledRHistoryList.getCancelReserveAutoId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                cancelledReservationsArrayList.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(context.getApplicationContext(), "Reservation successfully DELETED.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Failed to Delete!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return cancelledReservationsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our textviews and imageview
        TextView date, custName, reservationName, pax, time;
        ImageView details, delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.reserveDateTV);
            time = itemView.findViewById(R.id.reserveTimeTV);
            custName = itemView.findViewById(R.id.custNameTV);
            reservationName = itemView.findViewById(R.id.reservedNameTV);
            pax = itemView.findViewById(R.id.paxTV);
            details = itemView.findViewById(R.id.reserveInfoBtn);
            delete = itemView.findViewById(R.id.deleteRHistoryBtn);
        }
    }
}
