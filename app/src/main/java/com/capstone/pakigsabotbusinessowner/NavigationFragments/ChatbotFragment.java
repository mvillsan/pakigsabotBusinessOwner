package com.capstone.pakigsabotbusinessowner.NavigationFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capstone.pakigsabotbusinessowner.Chatbot.ChatbotConversation2;
import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Services.ServicesResort;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatbotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatbotFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatbotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatbotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatbotFragment newInstance(String param1, String param2) {
        ChatbotFragment fragment = new ChatbotFragment();
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
        View view = inflater.inflate(R.layout.fragment_chatbot, container, false);

        //References:
        TextView homeMenuTxt = (TextView) view.findViewById(R.id.homeMenuTxt);

        homeMenuTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatbotConversation2();
            }
        });

        return view;
    }

    private void chatbotConversation2(){
        Intent in = new Intent(getActivity(), ChatbotConversation2.class);
        in.putExtra("chatbotConvo2", "chatbotConvo2");
        startActivity(in);
    }
}