package com.capstone.pakigsabotbusinessowner.NavigationFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Services.AddFoodResto;
import com.capstone.pakigsabotbusinessowner.Services.AddServiceResto;
import com.capstone.pakigsabotbusinessowner.Services.EditFoodProdResto;
import com.capstone.pakigsabotbusinessowner.Services.EditServiceResto;
import com.capstone.pakigsabotbusinessowner.Services.ServicesResort;

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
        ImageView resortServices = (ImageView) view.findViewById(R.id.imageView6);
        ImageView addRestoBtnServices = (ImageView) view.findViewById(R.id.addRestoBtnServices);
        ImageView addRestoFoodBtnServices = (ImageView) view.findViewById(R.id.addRestoFoodBtnServices);
        ImageView editIndoorDining = (ImageView) view.findViewById(R.id.editIndoorDining);
        ImageView editFood1 = (ImageView) view.findViewById(R.id.editFood1);

        resortServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resortServicesScreen();
            }
        });

        addRestoBtnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRestoService();
            }
        });

        addRestoFoodBtnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRestoFood();
            }
        });

        editIndoorDining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editIndoorD();
            }
        });

        editFood1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFoodProd();
            }
        });

        return view;
    }

    private void resortServicesScreen(){
        Intent in = new Intent(getActivity(), ServicesResort.class);
        in.putExtra("resortServices", "resortServices");
        startActivity(in);
    }

    private void addRestoService(){
        Intent in = new Intent(getActivity(), AddServiceResto.class);
        in.putExtra("addResortServices", "addResortServices");
        startActivity(in);
    }

    private void addRestoFood(){
        Intent in = new Intent(getActivity(), AddFoodResto.class);
        in.putExtra("addRestoFoodBtnServices", "addRestoFoodBtnServices");
        startActivity(in);
    }

    private void editIndoorD(){
        Intent in = new Intent(getActivity(), EditServiceResto.class);
        in.putExtra("editIndoorD", "editIndoorD");
        startActivity(in);
    }

    private void editFoodProd(){
        Intent in = new Intent(getActivity(), EditFoodProdResto.class);
        in.putExtra("editFoodProd", "editFoodProd");
        startActivity(in);
    }
}