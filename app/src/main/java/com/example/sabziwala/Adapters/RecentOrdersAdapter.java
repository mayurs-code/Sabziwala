package com.example.sabziwala.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabziwala.Activities.HomeActivity;
import com.example.sabziwala.Activities.OrderMapActivity;
import com.example.sabziwala.Fragments.ConfirmationDialogFragment;
import com.example.sabziwala.Fragments.HandpickAvailabilityDialogFragment;
import com.example.sabziwala.R;
import com.example.sabziwala.Service.EventHandler;
import com.example.sabziwala.Service.OnRequestResponseListener;
import com.example.sabziwala.Service.communicator.Connector;
import com.example.sabziwala.Service.communicator.ServerCommunicator;
import com.example.sabziwala.Service.request.UpdateOrderStatusRequest;
import com.example.sabziwala.Service.response.GetOrdersResponseData;
import com.example.sabziwala.Service.response.UpdateOrderStatusResponse;
import com.example.sabziwala.Service.response.WebErrorResponse;
import com.example.sabziwala.Service.response.WebResponse;
import com.example.sabziwala.Utilities.AnimationClass;
import com.example.sabziwala.Utilities.AppLogger;
import com.example.sabziwala.Utilities.AppSettings;
import com.example.sabziwala.Utilities.Utils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;


public class RecentOrdersAdapter extends RecyclerView.Adapter<RecentOrdersAdapter.ViewHolder> implements OnRequestResponseListener {
    private final List<GetOrdersResponseData> data;
    private final EventHandler handler;
    Context context;
    String session;

    public RecentOrdersAdapter(Context context, List<GetOrdersResponseData> data, EventHandler handler) {
        this.context = context;
        this.data = data;
        this.handler = handler;
    }

    public static float getKmFromLatLong(LatLng l2) {
        if (HomeActivity.latlng != null) {
            LatLng l1 = HomeActivity.latlng;
            Location loc1 = new Location("");
            loc1.setLatitude(l1.latitude);
            loc1.setLongitude(l1.longitude);
            Location loc2 = new Location("");
            loc2.setLatitude(l2.latitude);
            loc2.setLongitude(l2.longitude);
            float distanceInMeters = loc1.distanceTo(loc2);
            return distanceInMeters / 1000;
        } else return -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recent_orders, null));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnimationClass.setAnimationParent(holder.itemView);

        holder.tvAddress.setText(data.get(position).getOther_address());
        holder.tvScheduleTime.setText(data.get(position).getSchedule_date() + " " + data.get(position).getSchedule_time());
        holder.tvUserName.setText(data.get(position).getFull_name());
        Location location;
        LatLng latLng = new LatLng(Double.parseDouble(data.get(position).getLat()), Double.parseDouble(data.get(position).getLag()));
        holder.tvTotalDistance.setText(String.format("%.2f", getKmFromLatLong(latLng)) + " KM");
        if (data.get(position).getSchedule_date() == null) {
            holder.tvOrderType.setText(R.string.recent_order);
        } else {
            holder.tvOrderType.setText(R.string.recent_order);

        }

//        holder.tvOrderType.setText(data.get(position).getOrder_type());
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                long seconds = 0;
                long minutes = 0;
                long hours = 0;
                long days = 0;
                if (data.get(position).getSchedule_date() != null) {
                    minutes = Utils.getDifference(Utils.toDate(data.get(position).getSchedule_date(), data.get(position).getSchedule_time()));
                    seconds = Utils.getDifferenceSec(Utils.toDate(data.get(position).getSchedule_date(), data.get(position).getSchedule_time()));
                    hours = Utils.getDifferenceHour(Utils.toDate(data.get(position).getSchedule_date(), data.get(position).getSchedule_time()));
                    days = Utils.getDifferenceDays(Utils.toDate(data.get(position).getSchedule_date(), data.get(position).getSchedule_time()));
                    holder.tvOrderType.setText(R.string.scheduled_order);
                } else {
                    minutes = Utils.getDifference(Utils.toDate(data.get(position).getCreated_date().split(" ")[0], data.get(position).getCreated_date().split(" ")[1]));
                    seconds = Utils.getDifferenceSec(Utils.toDate(data.get(position).getCreated_date().split(" ")[0], data.get(position).getCreated_date().split(" ")[1]));
                    hours = Utils.getDifferenceHour(Utils.toDate(data.get(position).getCreated_date().split(" ")[0], data.get(position).getCreated_date().split(" ")[1]));
                    days = Utils.getDifferenceDays(Utils.toDate(data.get(position).getCreated_date().split(" ")[0], data.get(position).getCreated_date().split(" ")[1]));
                    holder.tvOrderType.setText(R.string.recent_order);

                }
                String timeleft = "";
                if (Math.abs(days) > 1) {
                    timeleft = Math.abs(days) + "days, " + Math.abs(hours % 12) + "hours";

                } else if (Math.abs(hours) >= 1) {
                    timeleft = Math.abs(hours) + "hours, " + Math.abs(minutes % 60) + "minutes";

                } else if (Math.abs(minutes) >= 1) {
                    timeleft = Math.abs(minutes) + "minutes, " + Math.abs(seconds % 60) + "seconds";
                } else {
                    timeleft = seconds % 60 + "seconds";

                }
                holder.tvTimeLeft.setText(timeleft);


                if (data.get(position).getOrder_status().equals("5"))
                    if (seconds > 300) {
                        handler.handle(data.get(position).getOrder_id());
                        data.get(position).setOrder_status("9");
                        holder.llAcceptReject.setVisibility(View.GONE);
                        holder.mbGotoMap.setVisibility(View.GONE);
                        holder.tvOrderRejected.setVisibility(View.VISIBLE);
                        h.removeCallbacks(this);
                    } else if (seconds > 0) {

                        int progress = (int) (seconds * 0.3);
                        holder.timeProgress.setProgress(progress);
                    }

                h.postDelayed(this, 1000);
            }
        }, 1000);
        holder.mcOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, OrderMapActivity.class);
                i.putExtra("GetOrdersResponseData", data.get(position));
                context.startActivity(i);
            }
        });
        holder.mbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmationDialogFragment dialogFragment = new ConfirmationDialogFragment(data.get(position), false, handler);
                dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "ConfirmationDialogFragment");

            }
        });
        holder.mbAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandpickAvailabilityDialogFragment dialogFragment = new HandpickAvailabilityDialogFragment(data.get(position), false, null);
                dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "HandpickAvailabilityDialogFragment");


            }
        });
        holder.mbGotoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, OrderMapActivity.class);
                i.putExtra("GetOrdersResponseData", data.get(position));
                context.startActivity(i);
            }
        });

        switch (data.get(position).getOrder_status()) {
            case "5":
                holder.llAcceptReject.setVisibility(View.VISIBLE);
                holder.mbGotoMap.setVisibility(View.GONE);
                holder.tvOrderRejected.setVisibility(View.GONE);
                holder.tvOrderDone.setVisibility(View.GONE);
                break;
            case "6":
                holder.llAcceptReject.setVisibility(View.GONE);
                holder.mbGotoMap.setVisibility(View.VISIBLE);
                holder.tvOrderRejected.setVisibility(View.GONE);
                holder.tvOrderDone.setVisibility(View.GONE);
                break;
            case "7":
                holder.llAcceptReject.setVisibility(View.GONE);
                holder.mbGotoMap.setVisibility(View.VISIBLE);
                holder.tvOrderRejected.setVisibility(View.GONE);
                holder.tvOrderDone.setVisibility(View.GONE);
                break;
            case "8":
                holder.llAcceptReject.setVisibility(View.GONE);
                holder.mbGotoMap.setVisibility(View.GONE);
                holder.tvOrderRejected.setVisibility(View.GONE);
                holder.tvOrderDone.setVisibility(View.VISIBLE);
                break;
            case "9":
                holder.llAcceptReject.setVisibility(View.GONE);
                holder.mbGotoMap.setVisibility(View.GONE);
                holder.tvOrderRejected.setVisibility(View.VISIBLE);
                holder.tvOrderDone.setVisibility(View.GONE);
                break;

        }

    }

    private void connectorChangeStatusHandpick(String id) {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();
        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setOrder_id(id);
        request.setOrder_status("9");
        ServerCommunicator.updateOrderStatus(connector, request, AppSettings.getSessionKey(context));
        connector.setOnRequestResponseListener(this);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onAddMoreResponse(WebResponse webResponse) {

    }

    @Override
    public void onHttpResponse(WebResponse webResponse) {
        if (webResponse instanceof UpdateOrderStatusResponse) {
            final UpdateOrderStatusResponse responseBody = (UpdateOrderStatusResponse) webResponse;
            if (responseBody.getStatus()) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handler.handle();
                    }
                });
            } else {
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.ShowToast(context, "" + responseBody.getMessage());
                    }
                });
            }

        }
    }

    @Override
    public void onUploadComplete(WebResponse webResponse) {

    }

    @Override
    public void onVFRClientException(WebErrorResponse edErrorData) {
        AppLogger.e(Utils.getTag(), edErrorData.getMessage());
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                Utils.ShowToast(((Activity) context), edErrorData.getMessage());
            }
        });
    }

    @Override
    public void onAuthException() {

    }

    @Override
    public void onNoConnectivityException(String message) {

    }

    @Override
    public void onNoCachedDataAvailable() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvAddress;
        TextView tvScheduleTime;
        TextView tvUserName;
        LinearLayout llAcceptReject;
        TextView tvOrderType;
        TextView tvTimeLeft;
        TextView tvTotalDistance;
        TextView tvOrderRejected;
        MaterialCardView mcOrderCard;
        ImageView ivUserImage;
        ImageView tvOrderDone;
        MaterialButton mbCancel;
        MaterialButton mbAccept;
        MaterialButton mbGotoMap;
        LinearProgressIndicator timeProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvScheduleTime = itemView.findViewById(R.id.tvScheduleTime);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            mcOrderCard = itemView.findViewById(R.id.mcOrderCard);
            timeProgress = itemView.findViewById(R.id.timeProgress);
            tvTimeLeft = itemView.findViewById(R.id.tvTimeLeft);
            tvOrderType = itemView.findViewById(R.id.tvOrderType);
            tvTotalDistance = itemView.findViewById(R.id.tvTotalDistance);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            llAcceptReject = itemView.findViewById(R.id.llAcceptReject);
            mbCancel = itemView.findViewById(R.id.mbCancel);
            tvOrderDone = itemView.findViewById(R.id.tvOrderDone);
            mbAccept = itemView.findViewById(R.id.mbAccept);
            tvOrderRejected = itemView.findViewById(R.id.tvOrderRejected);
            mbGotoMap = itemView.findViewById(R.id.mbGotoMap);
        }
    }
}