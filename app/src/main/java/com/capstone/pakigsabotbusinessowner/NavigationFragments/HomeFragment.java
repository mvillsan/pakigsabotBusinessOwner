package com.capstone.pakigsabotbusinessowner.NavigationFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.pakigsabotbusinessowner.NotificationAlerts.CustomerCancelled;
import com.capstone.pakigsabotbusinessowner.NotificationAlerts.CustomerResched;
import com.capstone.pakigsabotbusinessowner.Profile.Profile;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.SignIn;
import com.capstone.pakigsabotbusinessowner.Translate.TranslateFilipino;


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
        ImageButton profileBtn = (ImageButton) view.findViewById(R.id.profileBtn);
        ImageButton signOutBtn = (ImageButton) view.findViewById(R.id.signoutHomeBtn);
        ImageView customerCancelled = (ImageView) view.findViewById(R.id.noUpcomingRsrvBtn);
        TextView customerResched = (TextView) view.findViewById(R.id.upcomingReservesTxt);
        ImageView translateBtn = (ImageView) view.findViewById(R.id.translateBtn);

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileScreen();
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

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateFil();
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
        Intent in = new Intent(getActivity(), SignIn.class);
        in.putExtra("signin", "signin");
        startActivity(in);
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

    public void  translateFil(){
        Intent in = new Intent(getActivity(), TranslateFilipino.class);
        in.putExtra("transFil", "transFil");
        startActivity(in);
    }
}