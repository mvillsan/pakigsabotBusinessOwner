package com.example.pakigsabot.ShareApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pakigsabot.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;


public class LoginWithFacebook extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_facebook);

        loginButton = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        //Setting the permissions for login
        //We er this permission in the loginbutton because the loginbutton wraps the login manager class
        //When button is clicked, the login is initiated with the permission that is set in the login manager
        loginButton.setPermissions(Arrays.asList("email"));

        //register a callback to loginbutton
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", "Login Successful");
                Toast.makeText(LoginWithFacebook.this, "Login Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.d("TAG", "Login Cancelled");
                Toast.makeText(LoginWithFacebook.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG", "Login Failed");
                Toast.makeText(LoginWithFacebook.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

    }


    //Passes the login result tot he login manager
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}