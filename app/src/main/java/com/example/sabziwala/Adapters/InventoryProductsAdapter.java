package com.example.sabziwala.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sabziwala.R;
import com.example.sabziwala.Service.response.GetInventoryResponseData;
import com.example.sabziwala.Utilities.AnimationClass;
import com.example.sabziwala.Utilities.Constants;

import java.util.List;

public class InventoryProductsAdapter extends RecyclerView.Adapter<InventoryProductsAdapter.ViewHolder> {

    private final List<GetInventoryResponseData> data;
    Context context;

    public InventoryProductsAdapter(Context context, List<GetInventoryResponseData> data) {
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_inventory, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnimationClass.setAnimationParent(holder.itemView);

        holder.tvName.setText(data.get(position).getName());
        holder.tvVariant.setText(data.get(position).getProduct_variant());
        holder.tvPrice.setText("₹"+data.get(position).getMin_amount() + "- ₹" + data.get(position).getMax_amount());
        try {
            Glide.with(context).load(Constants.FILES_URL + data.get(position).getImage()).into(holder.ivImage);
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPrice;
        TextView tvVariant;
        TextView tvName;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvVariant = itemView.findViewById(R.id.tvVariant);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}


