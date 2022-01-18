package com.example.sabziwala.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabziwala.R;
import com.example.sabziwala.Service.response.NotificationResponseData;
import com.example.sabziwala.Utilities.AnimationClass;
import com.example.sabziwala.Utilities.Utils;

import java.util.Date;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<NotificationResponseData> data;
    Context context;

    public NotificationAdapter(Context context, List<NotificationResponseData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification, null));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        AnimationClass.setAnimationParent(viewHolder.itemView);

        viewHolder.tvTitle.setText(data.get(position).getTitle());
        viewHolder.tvSubtitle.setText(data.get(position).getBody());
        Date date = Utils.toDate(data.get(position).getCreated_date().split(" ")[0], data.get(position).getCreated_date().split(" ")[1]);
        long mins = Utils.getDifference(date);
        long sec = Utils.getDifferenceSec(date);
        long days = Utils.getDifferenceDays(date);
        long hours = Utils.getDifferenceHour(date);
        if (sec < 60) {
            viewHolder.tvTime.setText(sec + " sec ago");
        } else if (mins < 60) {
            viewHolder.tvTime.setText(mins + " min ago");
        } else if (hours < 24) {
            viewHolder.tvTime.setText(hours + " hrs ago");
        } else {
            viewHolder.tvTime.setText(days + "day ago");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubtitle;
        TextView tvTitle;
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
