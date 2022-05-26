package com.example.pakigsabot.DentalAndEyeClinics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakigsabot.BuildConfig;
import com.example.pakigsabot.R;
import com.example.pakigsabot.Resorts.ResortReservationSuccess;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.cancel.OnCancel;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.error.ErrorInfo;
import com.paypal.checkout.error.OnError;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.Order;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PayPalButton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EyeClinicSelectedProcedures extends AppCompatActivity {
    //Initialization of variables
    Button reserveBtn;
    EditText eyeReserveNotes;
    ImageView selProcIcon, imgBackBtn;
    DatePickerDialog.OnDateSetListener opticalDateSetListener;
    Spinner reservationTimeSpinner;
    private int cust_numReservations, est_numReservations;
    List<String> listEstablishments, listReservationName, listCheckInDates, listCheckInTime;
    TextView lblSelEyeProcName, lblSelEyeRate, reservationDatePicker,
            timeSlotTxt,  amounttoPayTxt, messageTxt, dateError, timeError;
    String estId, selProcId, selProcName, selProcRate, selProcImageUrl,
            reserveAutoId, cust_Id, cust_FName, cust_LName, custSubStatus, cust_EmailAdd, cust_MobileNum,
            notes, rStatus_default, reserveDateStr, reserveTimeStr, amount, estName, estEmailAdd, dateToday;

    // initializing our variable for firebase
    // firestore and getting its instance.
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    DocumentReference documentReference, docRef, documentRef, docuRef;

    PayPalButton payPalButton;
    //Paypal Client ID::
    private static final String YOUR_CLIENT_ID ="AY2ERQska0p9RKv5PsknVYST7KmgiuIbOUXJT-Kk2IEP8mcBH_QaocNBwlAlt0y72WM3W5w6TKJfoxe7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_clinic_selected_procedures);

        //references
        refs();

        //Getting procedure details
        getProcedureDetails();

        //Getting customer's details
        getCustomerData();

        //Getting establishment's total num of reservations
        getEstNumReservations();

        //Generation of random IDs
        reserveAutoId = UUID.randomUUID().toString();

        reservationDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EyeClinicSelectedProcedures.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        opticalDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //Previous dates will be disabled
                dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                dialog.show();
            }
        });
        opticalDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String mon = ""+month;
                String dy = ""+day;

                if(month < 10){
                    mon = "0" + month;
                }
                if(day < 10){
                    dy  = "0" + day ;
                }
                String date = mon + "/" + dy + "/" + year;

                //Sets the text date from the  date picker
                reservationDatePicker.setText(date);
            }
        };

        //For reservation time
        displayReservationTime();


        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes = eyeReserveNotes.getText().toString();
                reserveDateStr = reservationDatePicker.getText().toString();
                reserveTimeStr = timeSlotTxt.getText().toString();

                //Getting the date today for transaction date.
                Date date2 = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String todayDateStr = df.format(date2);

                //Check Missing Fields
                if (notes.isEmpty() || reserveDateStr.isEmpty() || reserveTimeStr.isEmpty()) {
                    Toast.makeText(EyeClinicSelectedProcedures.this, "You MUST input ALL Fields", Toast.LENGTH_SHORT).show();
                } //Check if date has been selected
                else if(reserveDateStr.equals("Tap to select a date")){
                    dateError.setError("");
                    dateError.setVisibility(View.VISIBLE);
                    Toast.makeText(EyeClinicSelectedProcedures.this, "Please select a date.", Toast.LENGTH_SHORT).show();
                } //Check if reserve date is date today
                else if(reserveDateStr.equalsIgnoreCase(todayDateStr)){
                    dateError.setError("");
                    dateError.setVisibility(View.VISIBLE);
                    Toast.makeText(EyeClinicSelectedProcedures.this, "Reserve a Slot Ahead of Time!", Toast.LENGTH_SHORT).show();
                }//Check if time is already reserved
                else{
                    dateError.setVisibility(View.GONE);
                    checkDuplicateTime();
                }

            }
        });

        //Paypal Integration::
        CheckoutConfig config = new CheckoutConfig(
                getApplication(),
                YOUR_CLIENT_ID,
                Environment.SANDBOX,
                String.format("%s://paypalpay", BuildConfig.APPLICATION_ID),
                CurrencyCode.PHP,
                UserAction.PAY_NOW,
                new SettingsConfig(
                        true,
                        false
                )
        );
        PayPalCheckout.setConfig(config);

        payPalButton.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.PHP)
                                                        .value(amount)
                                                        .build()
                                        )
                                        .build()
                        );
                        Order order = new Order(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", result));
                                //Save reservation details to firestore::
                                saveReservationDB();
                                //Redirected to Success Page
                                /*success();*/
                                Intent intent = new Intent(getApplicationContext(), EyeClinicReservationSuccess.class);
                                intent.putExtra("EstablishmentID", estId);
                                intent.putExtra("EstablishmentName", estName);
                                intent.putExtra("EstablishmentEmail", estEmailAdd);
                                startActivity(intent);
                            }
                        });
                    }
                },
                new OnCancel() {
                    @Override
                    public void onCancel() {
                        Log.d("OnCancel", "Buyer cancelled the PayPal experience.");
                    }
                },
                new OnError() {
                    @Override
                    public void onError(@NotNull ErrorInfo errorInfo) {
                        Log.d("OnError", String.format("Error: %s", errorInfo));
                    }
                }
        );

        //Back to Dental Clinic Establishment Details
        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eyeClinicReservation();
            }
        });
    }

    private void getProcedureDetails() {
        //Getting data from recyclerview::
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            selProcId = extra.getString("ProcId");
            selProcName = extra.getString("ProcName");
            selProcRate = extra.getString("ProcRate");
            selProcImageUrl = extra.getString("ProcImageUrl");
            estId = extra.getString("ProcEstId");
        }

        //Display selected Dental Procedure Details:
        lblSelEyeProcName.setText(selProcName);
        lblSelEyeRate.setText("Rate: Php " + selProcRate);
        Picasso.get().load(selProcImageUrl).into(selProcIcon);
    }

    private void refs() {
        selProcIcon = findViewById(R.id.opticalProcIcon);
        lblSelEyeProcName = findViewById(R.id.lblSelEyeProcName);
        lblSelEyeRate = findViewById(R.id.lblSelEyeProcRate);
        eyeReserveNotes = findViewById(R.id.eyeNotesEditTxt);
        reservationDatePicker = findViewById(R.id.opticalDatePicker);
        reservationTimeSpinner = findViewById(R.id.eyeTimeSlotSpinner);
        dateError = findViewById(R.id.dateErrorTV);
        timeError = findViewById(R.id.timeErrorTV);
        timeSlotTxt = findViewById(R.id.timeSlotTxt);
        amounttoPayTxt = findViewById(R.id.amountToPay);
        imgBackBtn = findViewById(R.id.opticalReservBackBtn);
        reserveBtn = findViewById(R.id.reserveBtnEC);
        messageTxt = findViewById(R.id.dentMessageTxt);
        amounttoPayTxt.setVisibility(View.GONE);
        messageTxt.setVisibility(View.GONE);
        payPalButton = findViewById(R.id.payPalButton);
        payPalButton.setVisibility(View.GONE);
    }

    private void eyeClinicReservation() {
        Intent intent = new Intent(getApplicationContext(), EyeClinicReservation.class);
        startActivity(intent);
    }

    private void getCustomerData() {
        //Fetching Data from FireStore DB
        fAuth = FirebaseAuth.getInstance();
        cust_Id = fAuth.getCurrentUser().getUid();

        documentReference = fStore.collection("customers").document(cust_Id);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                cust_numReservations = value.getLong("cust_numOfReservations").intValue();
                cust_FName = value.getString("cust_fname");
                cust_LName = value.getString("cust_lname");
                cust_EmailAdd = value.getString("cust_email");
                cust_MobileNum = value.getString("cust_phoneNum");
                custSubStatus = value.getString("cust_status");

            }
        });
    }

    private void getEstNumReservations() {
        //Fetching Data from FireStore DB

        documentRef = fStore.collection("establishments").document(estId);
        documentRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                est_numReservations = value.getLong("est_numOfReservation").intValue();
                estName = value.getString("est_Name");
                estEmailAdd = value.getString("est_email");
            }
        });
    }


    private void displayReservationTime() {
        Spinner reservationTimeSpinner = (Spinner) findViewById(R.id.eyeTimeSlotSpinner);
        //Creating an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> timeSlotAdapter = new ArrayAdapter<String>(EyeClinicSelectedProcedures.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.timeSlotStringArray));

        //Specifying  the layout to use when the list of choices appears
        timeSlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Applying the adapter to the spinner
        reservationTimeSpinner.setAdapter(timeSlotAdapter);

        reservationTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeSlotTxt = (TextView) reservationTimeSpinner.getSelectedView();
                reserveTimeStr = timeSlotTxt.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void checkDuplicateTime() {
        try {
            //Check first whether customer's number of reservations <= 3;
            //If customer's number of reservations > 3 cannot reserve more unless PREMIUM user.
            if (custSubStatus.equals("Free")) {
                if(reservationDatePicker.equals("Tap to select a date")) {
                    Toast.makeText(EyeClinicSelectedProcedures.this, "Please select a date.", Toast.LENGTH_SHORT).show();
                }if (cust_numReservations >= 3) {
                    Toast.makeText(EyeClinicSelectedProcedures.this, "Susbcribe to Pakigsa-Bot Premium NOW, to Reserve More!", Toast.LENGTH_SHORT).show();
                } else { //List of establishments::
                    fStore.collection("reservations").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        listEstablishments = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            listEstablishments.add(document.getString("reserv_est_Name"));
                                        }
                                        Log.d("TAG", listEstablishments.toString());
                                    } else {
                                        Log.d("TAG", "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                    //List of Reservation Name::
                    fStore.collection("reservations").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        listReservationName = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            listReservationName.add(document.getString("reserv_name"));
                                        }
                                        Log.d("TAG", listReservationName.toString());
                                    } else {
                                        Log.d("TAG", "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                    //List of Check-in Dates::
                    fStore.collection("reservations").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        listCheckInDates = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            listCheckInDates.add(document.getString("reserv_dateIn"));
                                        }
                                        Log.d("TAG", listCheckInDates.toString());
                                    } else {
                                        Log.d("TAG", "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                    //List of Reservation Time::
                    fStore.collection("reservations").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        listCheckInTime = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            listCheckInTime.add(document.getString("reserv_timeCheckIn"));
                                        }
                                        Log.d("TAG", listCheckInTime.toString());
                                    } else {
                                        Log.d("TAG", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    Log.d("TAG", estName + selProcName + reserveDateStr + reserveTimeStr);

                    if (listEstablishments.contains(estName) && listReservationName.contains(selProcName) && listCheckInDates.contains(reserveDateStr) && listCheckInTime.contains(reserveTimeStr)) {
                        messageTxt.setVisibility(View.VISIBLE);
                        messageTxt.setText("FULLY BOOKED! Select another TIME.");
                        timeError.setError("");
                        timeError.setVisibility(View.VISIBLE);
                    } else {
                        timeError.setVisibility(View.GONE);
                        messageTxt.setVisibility(View.VISIBLE);
                        messageTxt.setText("Time Slot Available!");

                        //Setting Up amount to be paid
                        amounttoPayTxt.setVisibility(View.VISIBLE);
                        if (cust_numReservations <= 9) {
                            amounttoPayTxt.setText("100");
                        } else if (cust_numReservations >= 10 && cust_numReservations <= 24) {
                            amounttoPayTxt.setText("75");
                        } else if (cust_numReservations >= 25 && cust_numReservations <= 44) {
                            amounttoPayTxt.setText("50");
                        } else if (cust_numReservations >= 45) {
                            amounttoPayTxt.setText("40");
                        }
                        amount = amounttoPayTxt.getText().toString();
                        //Paypal Payment::
                        payPalButton.setVisibility(View.VISIBLE);
                    }
                }
            } else {//Premium Subscription
                //List of establishments::
                fStore.collection("reservations").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    listEstablishments = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        listEstablishments.add(document.getString("reserv_est_Name"));
                                    }
                                    Log.d("TAG", listEstablishments.toString());
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });

                //List of Reservation Name::
                fStore.collection("reservations").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    listReservationName = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        listReservationName.add(document.getString("reserv_name"));
                                    }
                                    Log.d("TAG", listReservationName.toString());
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });

                //List of Check-in Dates::
                fStore.collection("reservations").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    listCheckInDates = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        listCheckInDates.add(document.getString("reserv_dateIn"));
                                    }
                                    Log.d("TAG", listCheckInDates.toString());
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });

                //List of Reservation Time::
                fStore.collection("reservations").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    listCheckInTime = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        listCheckInTime.add(document.getString("reserv_timeCheckIn"));
                                    }
                                    Log.d("TAG", listCheckInTime.toString());
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });

                if (listEstablishments.contains(estName) && listReservationName.contains(selProcName) && listCheckInDates.contains(reserveDateStr) && listCheckInTime.contains(reserveTimeStr)) {
                    messageTxt.setVisibility(View.VISIBLE);
                    messageTxt.setText("FULLY BOOKED! Select another TIME.");
                    timeError.setError("");
                    timeError.setVisibility(View.VISIBLE);
                } else {
                    timeError.setVisibility(View.GONE);
                    messageTxt.setVisibility(View.VISIBLE);
                    messageTxt.setText("Procedure Available!");

                    //Setting Up amount to be paid
                    amounttoPayTxt.setVisibility(View.VISIBLE);
                    if (cust_numReservations <= 9) {
                        amounttoPayTxt.setText("100");
                    } else if (cust_numReservations >= 10 && cust_numReservations <= 24) {
                        amounttoPayTxt.setText("75");
                    } else if (cust_numReservations >= 25 && cust_numReservations <= 44) {
                        amounttoPayTxt.setText("50");
                    } else if (cust_numReservations >= 45) {
                        amounttoPayTxt.setText("40");
                    }
                    amount = amounttoPayTxt.getText().toString();
                    //Paypal Payment::
                    payPalButton.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception exception) {
            Toast.makeText(EyeClinicSelectedProcedures.this, "Please Re-check your Reservation Details!", Toast.LENGTH_SHORT).show();
        }
    }


    public void saveReservationDB() {

        //Parsing of variables::
        rStatus_default = "Pending";
        reserveDateStr = reservationDatePicker.getText().toString();
        reserveTimeStr = timeSlotTxt.getText().toString();


        //Getting the date today for transaction date.
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        dateToday = df.format(date);

        //Updating customer's number of reservations::
        docRef = fStore.collection("customers").document(cust_Id);
        //Increment +1 the current number of reservations::
        cust_numReservations = cust_numReservations + 1;

        Map<String, Object> editedNumReservations = new HashMap<>();
        editedNumReservations.put("cust_numOfReservations", cust_numReservations);
        docRef.update(editedNumReservations).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EyeClinicSelectedProcedures.this, "No Changes has been made", Toast.LENGTH_SHORT).show();
            }
        });

        //Updating establishment's number of reservations::
        docuRef = fStore.collection("establishments").document(estId);
        //Increment +1 the current number of reservations::
        est_numReservations = est_numReservations + 1;
        Map<String, Object> editedNumReservationsEst = new HashMap<>();
        editedNumReservationsEst.put("est_numOfReservation", est_numReservations);
        docuRef.update(editedNumReservationsEst).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EyeClinicSelectedProcedures.this, "No Changes has been made", Toast.LENGTH_SHORT).show();
            }
        });

        //Save reservation details::
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("reserv_id", reserveAutoId);
        reservation.put("reserv_status", rStatus_default);
        reservation.put("reserv_fee", amount);
        reservation.put("reserv_adultPax", "1");
        reservation.put("reserv_childPax", "0");
        reservation.put("reserv_infantPax", "0");
        reservation.put("reserv_petPax", "0");
        reservation.put("reserv_pax", "1");
        reservation.put("reserv_name", selProcName);
        reservation.put("reserv_notes", notes);
        reservation.put("reserv_timeCheckIn", reserveTimeStr);
        reservation.put("reserv_dateIn", reserveDateStr);
        reservation.put("reserv_dateCheckOut", reserveDateStr);
        reservation.put("reserv_cust_ID", cust_Id);
        reservation.put("reserv_cust_FName", cust_FName);
        reservation.put("reserv_cust_LName", cust_LName);
        reservation.put("reserv_cust_phoneNum", cust_MobileNum);
        reservation.put("reserv_cust_emailAdd", cust_EmailAdd);
        reservation.put("reserv_est_ID", estId);
        reservation.put("reserv_est_Name", estName);
        reservation.put("reserv_est_emailAdd", estEmailAdd);
        reservation.put("reserv_transactionDate", dateToday);
        reservation.put("reserv_confirmedDate", " ");
        reservation.put("reserv_preOrder_items", "");
        reservation.put("reserv_totalAmt", "");

        fStore.collection("reservations").document(reserveAutoId).set(reservation);
        Toast.makeText(EyeClinicSelectedProcedures.this, "Reservation Request Sent", Toast.LENGTH_SHORT).show();
    }
}