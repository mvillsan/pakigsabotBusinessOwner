package com.capstone.pakigsabotbusinessowner.NavigationFragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.pakigsabotbusinessowner.MainActivity;
import com.capstone.pakigsabotbusinessowner.NotificationAlerts.CustomerCancelled;
import com.capstone.pakigsabotbusinessowner.NotificationAlerts.CustomerResched;
import com.capstone.pakigsabotbusinessowner.Profile.Profile;
import com.capstone.pakigsabotbusinessowner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.Executor;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //For Database data retrieval
    FirebaseAuth fAuthHome;
    FirebaseFirestore fStoreHome;
    String userId;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //References:
        TextView estName = (TextView) view.findViewById(R.id.estNameTxt);
        TextView userName = (TextView) view.findViewById(R.id.welcomeUserTxt);
        TextView date = (TextView) view.findViewById(R.id.currentDateTxt);
        CircleImageView profileBtn = (CircleImageView) view.findViewById(R.id.profileBtn);
        ImageButton signOutBtn = (ImageButton) view.findViewById(R.id.signoutHomeBtn);
        ImageView customerCancelled = (ImageView) view.findViewById(R.id.noUpcomingRsrvBtn);
        TextView customerResched = (TextView) view.findViewById(R.id.upcomingReservesTxt);

        //Fetching Data from Firestore DB
        fAuthHome = FirebaseAuth.getInstance();
        fStoreHome = FirebaseFirestore.getInstance();
        userId = fAuthHome.getCurrentUser().getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        //Display profile image
        StorageReference profileRef = storageRef.child("business owner/"+userId+"img.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileBtn);
            }
        });

        //Display current date
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date.setText(currentDate);


        //Display establishment name
        DocumentReference documentReference = fStoreHome.collection("establishments").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error)
            {
                estName.setText(documentSnapshot.getString("est_Name"));
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { profileScreen();
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutApp();
            }
        });

        customerCancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerCancelledR();
            }
        });

        customerResched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerReschedR();
            }
        });

        return view;
    }


    public void profileScreen(){
        Intent in = new Intent(getActivity(), Profile.class);
        in.putExtra("profile", "profile");
        startActivity(in);
    }

    public void signOutApp(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getActivity(),"Signed Out from Pakigsa-Bot", Toast.LENGTH_SHORT).show();
        Intent in = new Intent(getActivity(), MainActivity.class);
        in.putExtra("signin", "signin");
        startActivity(in);
        getActivity().finish();
    }

    public void customerCancelledR(){
        Intent in = new Intent(getActivity(), CustomerCancelled.class);
        in.putExtra("alertCC", "alertCC");
        startActivity(in);
    }

    public void customerReschedR(){
        Intent in = new Intent(getActivity(), CustomerResched.class);
        in.putExtra("alertCR", "alertCR");
        startActivity(in);
    }


}