package com.capstone.pakigsabotbusinessowner.Chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.capstone.pakigsabotbusinessowner.R;

public class ChatbotConversation4 extends AppCompatActivity {

    TextView homeMenuTxt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_conversation4);

        //References:
        refs();

        homeMenuTxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatbotConvo5();
            }
        });
    }

    public void refs(){
        homeMenuTxt2 = findViewById(R.id.homeMenuTxt2);
    }

    private void chatbotConvo5(){
        Intent intent = new Intent(getApplicationContext(), ChatbotConversation5.class);
        startActivity(intent);
    }
}