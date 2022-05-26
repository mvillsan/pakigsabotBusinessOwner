package com.capstone.pakigsabotbusinessowner.ServiceHours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

public class ServiceHours extends AppCompatActivity {
    Spinner estTimeSlotSpinner;
    EditText time;
    Button saveTimeSlot;
    DatabaseReference spinnerRef;
    ArrayList<String>spinnerList;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_hours);

        refs();

        spinnerList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(ServiceHours.this,
                android.R.layout.simple_spinner_dropdown_item,spinnerList);

        saveTimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = time.getText().toString();

                String key = spinnerRef.push().getKey();

                spinnerRef.child(key).setValue(value);
                time.setText("");
                spinnerList.clear();
                adapter.notifyDataSetChanged();


                Toast.makeText(ServiceHours.this, "SAVED!", Toast.LENGTH_SHORT).show();

            }
        });
        estTimeSlotSpinner.setAdapter(adapter);

        spinnerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item:snapshot.getChildren()){
                    spinnerList.add(item.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void refs() {
        estTimeSlotSpinner = findViewById(R.id.timeSlotSpinner);
        time = findViewById(R.id.serviceHourETTime);
        saveTimeSlot = findViewById(R.id.serviceHSaveBtn);
    }


}