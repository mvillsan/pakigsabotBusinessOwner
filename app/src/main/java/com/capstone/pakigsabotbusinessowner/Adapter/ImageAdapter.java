/*
package com.capstone.pakigsabotbusinessowner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.pakigsabotbusinessowner.R;
import com.capstone.pakigsabotbusinessowner.Upload.UploadResortImgDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{
    private Context context;
    private List<UploadResortImgDetails> imgUploads;

    public ImageAdapter(Context contextC, List<UploadResortImgDetails> uploads){
        context = contextC;
        imgUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.imgresort_item, parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        UploadResortImgDetails uploadCurrent = imgUploads.get(position);
        Picasso.with(context)
                .load(uploadCurrent.getResortImgUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
        holder.txtviewName.setText(uploadCurrent.getResortRFName());
        holder.numOfPersonTxt.setText((int)uploadCurrent.getResortCapac());
        holder.descTxt.setText(uploadCurrent.getResortDesc());
        holder.rateTxt.setText((int) uploadCurrent.getResortRate());
    }

    @Override
    public int getItemCount() {
        return imgUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        TextView txtviewName,numOfPersonTxt, descTxt, rateTxt;
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_uploaded);
            txtviewName = itemView.findViewById(R.id.nameImgTxt);
            numOfPersonTxt = itemView.findViewById(R.id.capacityTxt);
            descTxt = itemView.findViewById(R.id.descriptionTxt);
            rateTxt = itemView.findViewById(R.id.rateeTxt);
        }
    }
}
*/
