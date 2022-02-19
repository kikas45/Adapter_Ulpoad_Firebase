package com.example.adapter_recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    TextView textView_desc;
    ImageView imageView;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textView_singe_name);
        imageView = itemView.findViewById(R.id.image_single_view);
        textView_desc = itemView.findViewById(R.id.textView_singe_desc);

    }
}
