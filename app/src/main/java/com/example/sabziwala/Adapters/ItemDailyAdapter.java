package com.example.sabziwala.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sabziwala.R;
import com.example.sabziwala.Service.DailyEventHandler;
import com.example.sabziwala.Service.response.GetInventoryResponseData;
import com.example.sabziwala.Utilities.AnimationClass;
import com.example.sabziwala.Utilities.Constants;

import java.util.HashSet;
import java.util.List;

public class ItemDailyAdapter extends RecyclerView.Adapter<ItemDailyAdapter.ViewHolder> {

    private final List<GetInventoryResponseData> data;
    private final List<GetInventoryResponseData> allData;
    private final DailyEventHandler handler;
    HashSet<String> set = new HashSet<>();
    Context context;

    public ItemDailyAdapter(Context context, List<GetInventoryResponseData> allData, List<GetInventoryResponseData> data, DailyEventHandler handler) {
        this.context = context;
        this.allData = allData;
        this.data = data;
        this.handler = handler;
        String s = "";
        for (int i = 0; i < allData.size(); i++) {
            for (int j = 0; j < data.size(); j++) {
                if (allData.get(i).getId().equals(data.get(j).getId())) {
                    set.add(data.get(j).getId());
                }
            }

        }
        handler.setSelected(set.toString());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_check_inventory, null));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnimationClass.setAnimationParent(holder.itemView);
        try {
            Glide.with(context).load(Constants.FILES_URL + allData.get(position).getImage()).into(holder.ivImage);
        } catch (Exception e) {

        }
        if (set.contains(allData.get(position).getId())) {
            holder.cbCheck.setChecked(true);
        } else {
            holder.cbCheck.setChecked(false);
        }
        holder.tvName.setText(allData.get(position).getName());
        holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    set.add(allData.get(position).getId());
                } else {
                    set.remove(allData.get(position).getId());
                }
                handler.setSelected(set.toString());
            }
        });


    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivImage;
        CheckBox cbCheck;

        public ViewHolder(View itemView) {
            super(itemView);
            cbCheck = itemView.findViewById(R.id.cbCheck);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}