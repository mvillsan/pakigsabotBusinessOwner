package com.capstone.pakigsabotbusinessowner.NavigationFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Reservations.ReservationDetails;
import com.capstone.pakigsabotbusinessowner.Services.AddServiceResort;
import com.capstone.pakigsabotbusinessowner.Services.EditServiceResort;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ServicesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServicesFragment newInstance(String param1, String param2) {
        ServicesFragment fragment = new ServicesFragment();
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
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        //References:
        ImageView addRoomBtnServices = (ImageView) view.findViewById(R.id.addRoomBtnServices);
        ImageView editRoomAzul = (ImageView) view.findViewById(R.id.editRoomAzul);

        addRoomBtnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRoom();
                /*FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                // Create and show the dialog.
                ReservationDetails newFragment = new ReservationDetails ();
                newFragment.show(ft, "ReservationDetails");*/

              /*  ReservationDetails dialog = new ReservationDetails();
                dialog.show(getActivity().getSupportFragmentManager(), "ReservationDetails");*/
            }
        });

        editRoomAzul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editRoom();
            }
        });
        return view;
    }

    private void addRoom(){
        Intent in = new Intent(getActivity(), AddServiceResort.class);
        in.putExtra("addRoom", "addRoom");
        startActivity(in);
    }

    private void editRoom(){
        Intent in = new Intent(getActivity(), EditServiceResort.class);
        in.putExtra("editRoom", "editRoom");
        startActivity(in);
    }
}