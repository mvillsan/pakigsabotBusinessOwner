package com.capstone.pakigsabotbusinessowner.Reservations.Adapter;

import static android.graphics.Color.rgb;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Reservations.EmailToCustomer;
import com.capstone.pakigsabotbusinessowner.Reservations.Model.ReservationsModel;
import com.capstone.pakigsabotbusinessowner.Services.LoadServices;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.MyViewHolder> {
    // creating variables for our ArrayList and context
    Context context;
    ArrayList<ReservationsModel> reservationsArrayList;

    //Initializing variables
    ProgressDialog progressDialog;

    //For swipe refresh to load updates
    SwipeRefreshLayout reservationsSwipeRefresh;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    public ReservationsAdapter() {
        //empty constructor needed
    }

    public ReservationsAdapter(Context context, ArrayList<ReservationsModel> reservationsArrayList) {
        this.context = context;
        this.reservationsArrayList = reservationsArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore =FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }


    @NonNull
    @Override
    public ReservationsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.reservations_items,parent, false);
        return  new MyViewHolder(itemView);
    }


    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ReservationsAdapter.MyViewHolder holder, int position) {
        //creating  object of ReservationsModel class and setting data to the textviews from the ReservationsModel class
        ReservationsModel reservationList = reservationsArrayList.get(position);
        holder.date.setText(reservationList.getReserveDateIn());
        holder.time.setText(reservationList.getReserveTime());
        holder.custName.setText(reservationList.getCustFName() + " " + reservationList.getCustLName());
        holder.reservationName.setText(reservationList.getReserveName());
        holder.pax.setText(reservationList.getReservePax());
        holder.stats.setText(reservationList.getrStatus_default());
        holder.markAsComplete.setVisibility(View.GONE);

        //Change text color to GREEN  if status is CONFIRMED
        //Show the option to MARK AS COMPLETE if reservation is CONFIRMED
        if(holder.stats.getText().toString().equals("Confirmed")) {
            holder.stats.setTextColor(rgb(0,128,0));
            holder.markAsComplete.setVisibility(View.VISIBLE);
        }
        else
            //Set text color to RED if status is PENDING
            holder.stats.setTextColor(rgb(255,0,0));

        //Show reservation details and update status
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.reservationName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.reservation_details_popup))
                        .setExpanded(true,1100)
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
                Button confirmBtnPopUp = viewDP.findViewById(R.id.confirmBtnPopUpDAEC);
                Button cancelBtnPopUp = viewDP.findViewById(R.id.cancelBtnPopUpDAEC);
                TextView reasonTV = viewDP.findViewById(R.id.reasonTV);
                EditText reasonET = viewDP.findViewById(R.id.reasonET);
                Button submitReasonBtn = viewDP.findViewById(R.id.submitReasonBtn);
                TextView customerNumTV = viewDP.findViewById(R.id.custNumberTV);
                reasonTV.setVisibility(View.GONE);
                reasonET.setVisibility(View.GONE);
                submitReasonBtn.setVisibility(View.GONE);
                customerNumTV.setVisibility(View.GONE);


                //Retrieving Data
                reserveDate.setText(reserveDate.getText().toString() + reservationList.getReserveDateIn() + "   Time: " + reservationList.getReserveTime());
                customerName.setText(customerName.getText().toString() + reservationList.getCustFName() + " " + reservationList.getCustLName());
                reservedET.setText(reservedET.getText().toString() + reservationList.getReserveName());
                adultPaxET.setText(adultPaxET.getText().toString() + reservationList.getAdultPax() + "   Child: " + reservationList.getChildPax()  + "  Infant: " + reservationList.getInfantPax());
                petPaxET.setText(petPaxET.getText().toString() + reservationList.getPetPax());
                feeET.setText(feeET.getText().toString() + reservationList.getFee());
                dateTransacET.setText(dateTransacET.getText().toString() + reservationList.getDateOfTransaction());
                customerNumTV.setText(reservationList.getCust_MobileNum());

                //Confirm Reservation
                confirmBtnPopUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*Intent in = new Intent(context.getApplicationContext(), ReserveConfirmMessageDC.class);
                        context.startActivity(in);
*/
                        String message = "This message serves as a verification that your RESERVATION has been CONFIRMED. Thank you for trusting Pakigsa-Bot!";
                        String number = customerNumTV.getText().toString();

                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse("smsto:" + number)); // This ensures only SMS apps respond
                        intent.putExtra("sms_body", message);
                        context.startActivity(intent);

                        //Getting the date today for confirmation date.
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        String dateToday = df.format(date);

                        //Update reservation status from PENDING to CONFIRMED
                        DocumentReference docRef = fStore.collection("reservations").document(reservationList.getReserveAutoId());
                        docRef.update("reserv_status", "Confirmed")
                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       Toast.makeText(context.getApplicationContext(), "Reservation successfully CONFIRMED!", Toast.LENGTH_LONG).show();
                                   }
                               });
                        docRef.update("reserv_confirmedDate", dateToday);
                    }
                });

                //Cancel Reservation
                cancelBtnPopUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Make Tvs visible
                        reasonTV.setVisibility(View.VISIBLE);
                        reasonET.setVisibility(View.VISIBLE);
                        submitReasonBtn.setVisibility(View.VISIBLE);

                        //Getting the date today for transaction date.
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        String dateToday = df.format(date);

                        submitReasonBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(reasonET.getText().toString().isEmpty()){
                                    Toast.makeText(view.getContext(), "Enter Reason for Cancellation", Toast.LENGTH_SHORT).show();
                                }else{
                                    //Check whether cancellation date  is not earlier than check-in date
                                    try {
                                        Date date1, date2;
                                        SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");
                                        date1 = dates.parse(reservationList.getReserveDateIn());
                                        date2 = dates.parse(dateToday);
                                        long difference = date1.getTime() - date2.getTime();
                                        if (difference == 0) {
                                            Toast.makeText(view.getContext(), "CANCELLATION NOT ALLOWED!", Toast.LENGTH_SHORT).show();

                                        } else {
                                            //Delete the reservation from the reservations collection
                                            fStore.collection("reservations").document(reservationList.getReserveAutoId())
                                                    .delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            progressDialog.dismiss();
                                                            reservationsArrayList.remove(position);
                                                            notifyItemRemoved(position);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(context, "Failed to Delete!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            //Save reservation to the Cancelled Reservations collection
                                            Map<String, Object> cancelled = new HashMap<>();
                                            cancelled.put("cancelReserv_id", reservationList.getReserveAutoId());
                                            cancelled.put("cancelReserv_status", "Cancelled");
                                            cancelled.put("cancelReserv_fee", reservationList.getFee());
                                            cancelled.put("cancelReserv_adultPax", "1");
                                            cancelled.put("cancelReserv_childPax", "0");
                                            cancelled.put("cancelReserv_infantPax", "0");
                                            cancelled.put("cancelReserv_petPax", "0");
                                            cancelled.put("cancelReserv_pax", "1");
                                            cancelled.put("cancelReserv_name", reservationList.getReserveName());
                                            cancelled.put("cancelReserv_notes", reservationList.getNotes());
                                            cancelled.put("cancelReserv_reason", reasonET.getText().toString());
                                            cancelled.put("cancelReserv_timeCheckIn", reservationList.getReserveTime());
                                            cancelled.put("cancelReserv_dateIn", reservationList.getReserveDateIn());
                                            cancelled.put("cancelReserv_dateCheckOut", reservationList.getReserveDateOut());
                                            cancelled.put("cancelReserv_cust_ID", reservationList.getCust_ID());
                                            cancelled.put("cancelReserv_cust_FName", reservationList.getCustFName());
                                            cancelled.put("cancelReserv_cust_LName", reservationList.getCustLName());
                                            cancelled.put("cancelReserv_cust_phoneNum", reservationList.getCust_MobileNum());
                                            cancelled.put("cancelReserv_cust_emailAdd", reservationList.getCust_EmailAddress());
                                            cancelled.put("cancelReserv_est_ID", reservationList.getEstID());
                                            cancelled.put("cancelReserv_est_Name", reservationList.getEstName());
                                            cancelled.put("cancelReserv_est_emailAdd", reservationList.getEst_EmailAddress());
                                            cancelled.put("cancelReserv_transactionDate", reservationList.getDateOfTransaction());
                                            cancelled.put("cancelReserv_cancelledDate", dateToday);
                                            cancelled.put("cancelReserv_preOrder_items", "");
                                            cancelled.put("cancelReserv_totalAmt", "");
                                            fStore.collection("cancelled-reservations").document(reservationList.getReserveAutoId()).set(cancelled);
                                            Toast.makeText(context.getApplicationContext(), "Successfully moved to CANCELLED.", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception exception) {
                                        Toast.makeText(view.getContext(), "Are you sure you want to CANCEL?", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });

      holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String custID = reservationList.getCust_ID();
                String name = reservationList.getCustFName() + " " + reservationList.getCustLName();
                String email = reservationList.getCust_EmailAddress();

                Toast.makeText(context, "Email to " + reservationList.getCustFName() + "  " +  reservationList.getCustLName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), EmailToCustomer.class);
                intent.putExtra("CustomerID", custID);
                intent.putExtra("CustomerName", name);
                intent.putExtra("CustomerEmail", email);
                view.getContext().startActivity(intent);
            }
        });

      holder.markAsComplete.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              //Getting the date today for transaction date.
              Date date = Calendar.getInstance().getTime();
              SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
              String dateToday = df.format(date);

              //Delete the reservation from the reservations collection
              fStore.collection("reservations").document(reservationList.getReserveAutoId())
                      .delete()
                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              progressDialog.dismiss();
                              reservationsArrayList.remove(position);
                              notifyItemRemoved(position);
                          }
                      }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      progressDialog.dismiss();
                      Toast.makeText(context, "Failed to Delete!", Toast.LENGTH_SHORT).show();
                  }
              });


             //Save reservation to the Completed Reservations collection::
              Map<String, Object> completed = new HashMap<>();
              completed.put("completeReserv_id", reservationList.getReserveAutoId());
              completed.put("completeReserv_status", "Completed");
              completed.put("completeReserv_fee", reservationList.getFee());
              completed.put("completeReserv_adultPax", "1");
              completed.put("completeReserv_childPax", "0");
              completed.put("completeReserv_infantPax", "0");
              completed.put("completeReserv_petPax", "0");
              completed.put("completeReserv_pax", "1");
              completed.put("completeReserv_name", reservationList.getReserveName());
              completed.put("completeReserv_notes", reservationList.getNotes());
              completed.put("completeReserv_timeCheckIn", reservationList.getReserveTime());
              completed.put("completeReserv_dateIn", reservationList.getReserveDateIn());
              completed.put("completeReserv_dateCheckOut", reservationList.getReserveDateOut());
              completed.put("completeReserv_cust_ID", reservationList.getCust_ID());
              completed.put("completeReserv_cust_FName", reservationList.getCustFName());
              completed.put("completeReserv_cust_LName", reservationList.getCustLName());
              completed.put("completeReserv_cust_phoneNum", reservationList.getCust_MobileNum());
              completed.put("completeReserv_cust_emailAdd", reservationList.getCust_EmailAddress());
              completed.put("completeReserv_est_ID", reservationList.getEstID());
              completed.put("completeReserv_est_Name", reservationList.getEstName());
              completed.put("completeReserv_est_emailAdd", reservationList.getEst_EmailAddress());
              completed.put("completeReserv_transactionDate", reservationList.getDateOfTransaction());
              completed.put("completeReserv_completedDate", dateToday);
              fStore.collection("completed-reservations").document(reservationList.getReserveAutoId()).set(completed);
              Toast.makeText(context.getApplicationContext(), "Successfully moved to COMPLETED.", Toast.LENGTH_LONG).show();

          }
      });

    }


    @Override
    public int getItemCount() {
        return reservationsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our textviews and imageview
        TextView date, custName, reservationName, pax, time, stats, markAsComplete;
        ImageView details, email;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.reserveDateTV);
            time = itemView.findViewById(R.id.reserveTimeTV);
            custName = itemView.findViewById(R.id.custNameTV);
            reservationName = itemView.findViewById(R.id.reservedNameTV);
            pax = itemView.findViewById(R.id.paxTV);
            stats = itemView.findViewById(R.id.reserveStatusTV);
            details = itemView.findViewById(R.id.reserveInfoBtn);
            email = itemView.findViewById(R.id.sendEmailToCustBtn);
            markAsComplete = itemView.findViewById(R.id.markAsCompleteTV);
        }
    }

    public void filterList(List<ReservationsModel> filteredList){
        reservationsArrayList = (ArrayList<ReservationsModel>) filteredList;
        notifyDataSetChanged();
    }



}
