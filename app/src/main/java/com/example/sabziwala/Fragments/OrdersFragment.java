package com.example.sabziwala.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabziwala.Adapters.RecentOrdersAdapter;
import com.example.sabziwala.R;
import com.example.sabziwala.Service.EventHandler;
import com.example.sabziwala.Service.OnRequestResponseListener;
import com.example.sabziwala.Service.communicator.Connector;
import com.example.sabziwala.Service.communicator.ServerCommunicator;
import com.example.sabziwala.Service.request.GetOrdersRequest;
import com.example.sabziwala.Service.request.UpdateOrderStatusRequest;
import com.example.sabziwala.Service.response.GetOrdersResponse;
import com.example.sabziwala.Service.response.GetOrdersResponseData;
import com.example.sabziwala.Service.response.UpdateOrderStatusResponse;
import com.example.sabziwala.Service.response.WebErrorResponse;
import com.example.sabziwala.Service.response.WebResponse;
import com.example.sabziwala.Utilities.AppLogger;
import com.example.sabziwala.Utilities.AppSettings;
import com.example.sabziwala.Utilities.Constants;
import com.example.sabziwala.Utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment implements OnRequestResponseListener, EventHandler {
    public static int orderType = 5;//8=old/completed,5=new,9=rejected
    LoadingFragment loadingFragment = new LoadingFragment();
    NoInternetFragment dialogFragment = new NoInternetFragment();
    private RecyclerView rvRecentOrders;
    private TextView tvPendingOrder;
    private TextView oldOrders;
    private TextView tvOrdersHeading;
    private Activity context;
    private boolean alreadyLoaded = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvRecentOrders = view.findViewById(R.id.rvRecentOrders);
        tvPendingOrder = view.findViewById(R.id.tvPendingOrder);
        oldOrders = view.findViewById(R.id.tvOldOrders);
        tvOrdersHeading = view.findViewById(R.id.tvOrdersHeading);
        context = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadingFragment.show(getChildFragmentManager(), "");
        System.out.println("ShowingDialog " + alreadyLoaded);
        methods();
    }

    @Override
    public void onPause() {
        super.onPause();
        alreadyLoaded = false;
    }

    private void methods() {
        setData();
        listners();
        initOrdersConnector();
    }

    private void setData() {
        switch (orderType) {
            case -1:
                tvOrdersHeading.setText(R.string.total_order);
                oldOrders.setText(R.string.view_old_orders);

                break;
            case 5:
                tvOrdersHeading.setText(R.string.new_orders);
                oldOrders.setText(R.string.view_old_orders);

                break;
            case 9:
                tvOrdersHeading.setText(R.string.rejected_orders);
                oldOrders.setText(R.string.view_old_orders);

                break;
            case 8:
                tvOrdersHeading.setText(R.string.old_orders);
                oldOrders.setText(R.string.view_new_orders);

                break;
        }
    }

    private void listners() {
        oldOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orderType != 8) {
                    orderType = 8;
                    oldOrders.setText(R.string.view_new_orders);

                } else {
                    orderType = 5;
                    oldOrders.setText(R.string.view_old_orders);
                }
                setData();
                initOrdersConnector();
            }
        });
    }

    private void initOrdersConnector() {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();
        GetOrdersRequest getOrdersRequest = new GetOrdersRequest();
        getOrdersRequest.setRole("Sabjiwala");
        ServerCommunicator.getOrdersList(connector, getOrdersRequest, AppSettings.getSessionKey(context));
        connector.setOnRequestResponseListener(this);
    }

    @Override
    public void onAddMoreResponse(WebResponse webResponse) {

    }

    @Override
    public void onHttpResponse(WebResponse webResponse) {
        if (webResponse instanceof GetOrdersResponse) {
            final GetOrdersResponse responseBody = (GetOrdersResponse) webResponse;
            if (responseBody.getStatus()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingFragment.dismisss();
                        alreadyLoaded = false;

                        setOrdersAdapter(responseBody.getData());
                    }

                });


            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.ShowToast(getActivity(), "" + responseBody.getMessage());
                    }
                });
            }

        }
        {
            if (webResponse instanceof UpdateOrderStatusResponse) {
                final UpdateOrderStatusResponse responseBody = (UpdateOrderStatusResponse) webResponse;
                if (responseBody.getStatus()) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initOrdersConnector();
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

    }

    private void setOrdersAdapter(List<GetOrdersResponseData> data) {
        List<GetOrdersResponseData> temp = new ArrayList<>();

        if (orderType == 5)
            for (int i = 0; i < data.size(); i++) {
                if (!(data.get(i).getOrder_status().equals("9") || data.get(i).getOrder_status().equals("8") || data.get(i).getOrder_status().equals("4"))) {
                    temp.add(data.get(i));
                }
            }
        if (orderType == 8)
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getOrder_status().equals("8")) {
                    temp.add(data.get(i));
                }
            }
        if (orderType == 9)
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getOrder_status().equals("9")) {
                    temp.add(data.get(i));
                }
            }
        if (orderType == -1) {
            temp.addAll(data);
        }
        if (temp.size() == 0) {
            tvPendingOrder.setVisibility(View.VISIBLE);
        } else tvPendingOrder.setVisibility(View.GONE);

        RecentOrdersAdapter recentOrdersAdapter = new RecentOrdersAdapter(getActivity(), temp, this);
        rvRecentOrders.setAdapter(recentOrdersAdapter);
        rvRecentOrders.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    @Override
    public void onUploadComplete(WebResponse webResponse) {

    }

    @Override
    public void onVFRClientException(WebErrorResponse edErrorData) {

        AppLogger.e(Utils.getTag(), edErrorData.getMessage());
        context.runOnUiThread(new Runnable() {
            public void run() {
                Utils.ShowToast(context, edErrorData.getMessage());
            }
        });
    }

    @Override
    public void onAuthException() {

    }

    @Override
    public void onNoConnectivityException(String message) {

        if (message.equals("-1")) {
            dialogFragment.show(getChildFragmentManager(), "" + Constants.incrementalID++);
        }
        if (message.equals("1")) {
            try {
                dialogFragment.dismiss();
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onNoCachedDataAvailable() {

    }

    @Override
    public void handle() {
        initOrdersConnector();
    }

    private void connectorChangeStatusHandpickReject(String id) {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();
        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setOrder_id(id);
        request.setOrder_status("9");
        ServerCommunicator.updateOrderStatus(connector, request, AppSettings.getSessionKey(context));
        connector.setOnRequestResponseListener(this);
    }

    @Override
    public void handle(int position) {
    }

    @Override
    public void handle(String id) {
        connectorChangeStatusHandpickReject(id + "");
    }
}
