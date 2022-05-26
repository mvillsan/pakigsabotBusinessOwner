package com.example.pakigsabot.Favorites;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pakigsabot.DentalAndEyeClinics.Adapters.DentalAndEyeClinicsAdapter;
import com.example.pakigsabot.DentalAndEyeClinics.Models.DentalAndEyeClinicsModel;
import com.example.pakigsabot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.MyViewHolder> {
    //creating variables for Context and ArrayList
    Context context;
    ArrayList<FavoritesModel> favoriteEstArrayList;

    //Initializing variables
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    public FavoritesAdapter() {
        //empty constructor needed
    }

    public FavoritesAdapter(Context context, ArrayList<FavoritesModel> favoriteEstArrayList) {
        this.context = context;
        this.favoriteEstArrayList = favoriteEstArrayList;
        this.progressDialog = new ProgressDialog(context);
        this.fStore = FirebaseFirestore.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public FavoritesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing layout file to display the items
        View itemView = LayoutInflater.from(context).inflate(R.layout.favorite_est_items,parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.MyViewHolder holder, int position) {
        //creating  object of FavoritesModel class and setting data to the textviews from the FavoritesModel class
        FavoritesModel favEstList = favoriteEstArrayList.get(position);
        holder.name.setText(favEstList.getEstName());
        holder.address.setText(favEstList.getEstAddress());
        Glide.with(context).load(favEstList.getEstImageUrl()).into(holder.img);

        //Delete from favorites
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setTitle("REMOVE ESTABLISHMENT")
                        .setMessage("Do you want to REMOVE " + favEstList.getEstName()+ "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog = new ProgressDialog(context);
                                progressDialog.setCancelable(false);
                                progressDialog.setTitle("Deleting...");
                                progressDialog.show();

                                String userId = fAuth.getCurrentUser().getUid();
                                fStore.collection("customers").document(userId).collection("favorites").document(favEstList.getFavEstId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, favEstList.getEstName() + " Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                favoriteEstArrayList.remove(position);
                                                notifyItemRemoved(position);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Failed to Delete!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteEstArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //refer all elements where data will be populated
        TextView name, address;
        ImageView img;
        Button remove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.favEstRVName);
            address = itemView.findViewById(R.id.favEstRVAddress);
            img = itemView.findViewById(R.id.favImgViewEstRVImg);
            remove = itemView.findViewById(R.id.removeFavEstBtn);
        }
    }
}
