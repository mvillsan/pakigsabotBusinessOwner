package com.capstone.pakigsabotbusinessowner.ReservationHistory.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.ReservationHistory.LoadReservationHistory;
import com.capstone.pakigsabotbusinessowner.ReservationHistory.Model.CompletedRHistoryModel;
import com.capstone.pakigsabotbusinessowner.ReservationHistory.ViewReservationHistoryDentalClinic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CompletedRHistoryAdapter extends RecyclerView.Adapter<CompletedRHistoryAdapter.MyViewHolder>{
    // creating variables for our ArrayList and context
    Context context;
    ArrayList<CompletedRHistoryModel> completedReservationsArrayList;

    //Initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    public CompletedRHistoryAdapter() {
        //empty constructor needed
    }

    public CompletedRHistoryAdapter(Context context, ArrayList<CompletedRHistoryModel> completedReservationsArrayList) {
        this.context = context;
        this.completedReservationsArrayList = completedReservationsArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore = FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public CompletedRHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.reservation_history_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull CompletedRHistoryAdapter.MyViewHolder holder, int position) {
        //creating  object of ReservationsModel class and setting data to the textviews from the ReservationsModel class
        CompletedRHistoryModel completedRHistoryList = completedReservationsArrayList.get(position);
        holder.date.setText(completedRHistoryList.getCompReserveDateIn());
        holder.time.setText(completedRHistoryList.getCompReserveTime());
        holder.custName.setText(completedRHistoryList.getCompRCustFName() + " " + completedRHistoryList.getCompRCustLName());
        holder.reservationName.setText(completedRHistoryList.getCompReserveName());
        holder.pax.setText(completedRHistoryList.getCompReservePax());

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
                reserveDate.setText(reserveDate.getText().toString() + completedRHistoryList.getCompReserveDateIn() + "   Time: " + completedRHistoryList.getCompReserveTime());
                customerName.setText(customerName.getText().toString() + completedRHistoryList.getCompRCustFName() + " " + completedRHistoryList.getCompRCustLName());
                reservedET.setText(reservedET.getText().toString() + completedRHistoryList.getCompReserveName());
                adultPaxET.setText(adultPaxET.getText().toString() + completedRHistoryList.getCompAdultPax() + "   Child: " + completedRHistoryList.getCompChildPax() + "  Infant: " + completedRHistoryList.getCompInfantPax());
                petPaxET.setText(petPaxET.getText().toString() + completedRHistoryList.getCompPetPax());
                feeET.setText(feeET.getText().toString() + completedRHistoryList.getCompFee());
                dateTransacET.setText(dateTransacET.getText().toString() + completedRHistoryList.getCompDateOfTransaction());


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
                fStore.collection("completed-reservations").document(completedRHistoryList.getCompReserveAutoId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                completedReservationsArrayList.remove(position);
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
        return completedReservationsArrayList.size();
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
