package com.example.pakigsabot.ShareApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pakigsabot.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ShareAppToFB extends AppCompatActivity {
    ImageView pakigsabotShareApp;
    Button shareFBBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_app_to_fb);

        refs();

        BitmapDrawable bitmapDrawable = (BitmapDrawable) pakigsabotShareApp.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        shareFBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(bitmap);
            }
        });
    }

    private void refs() {
        pakigsabotShareApp = findViewById(R.id.pakigsabotShareAppImg);
        shareFBBtn = findViewById(R.id.shareToFacebookBtn);
    }

    private void shareImage(Bitmap bitmap) {
        Intent in = new Intent(Intent.ACTION_SEND);
        in.setType("text/plain");
        Uri bitmapUri;
        String textToShare = "Sample Share Pakigsa-Bot";
        bitmapUri = saveImage(bitmap, getApplicationContext());
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        in.putExtra(Intent.EXTRA_TEXT, "Sample Test Pakigsa-Bot App" + "https://drive.google.com/drive/folders/1veO7RB2KDO2E0g7UmPkc-Nf_kkIQWz-x?usp=sharing");
        in.putExtra(Intent.EXTRA_SUBJECT, "New App");

        startActivity(Intent.createChooser(in, "Share Pakigsa-Bot"));
    }

    private static Uri saveImage(Bitmap image, Context context) {
        File imagesFolder = new File(context.getCacheDir(), "images");
        Uri uri = null;
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "shared_images.jpg");

            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()),
                    "com.example.pakigsabot" + ".provider", file);
        } catch (IOException e) {
            Log.d("TAG", "Exception" + e.getMessage());
        }
        return uri;

    }
}