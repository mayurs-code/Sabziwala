package com.example.sabziwala.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sabziwala.Activities.DailyListActivity;
import com.example.sabziwala.Activities.HomeActivity;
import com.example.sabziwala.Activities.InventoryActivity;
import com.example.sabziwala.Activities.SplashActivity;
import com.example.sabziwala.R;
import com.example.sabziwala.Service.OnRequestResponseListener;
import com.example.sabziwala.Service.communicator.Connector;
import com.example.sabziwala.Service.communicator.ServerCommunicator;
import com.example.sabziwala.Service.request.GetOrdersRequest;
import com.example.sabziwala.Service.response.GetOrdersResponse;
import com.example.sabziwala.Service.response.GetOrdersResponseData;
import com.example.sabziwala.Service.response.WebErrorResponse;
import com.example.sabziwala.Service.response.WebResponse;
import com.example.sabziwala.Utilities.AppLogger;
import com.example.sabziwala.Utilities.AppSettings;
import com.example.sabziwala.Utilities.Constants;
import com.example.sabziwala.Utilities.Utils;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DashboardFragment extends Fragment implements OnRequestResponseListener {

    LoadingFragment loadingFragment = new LoadingFragment();
    NoInternetFragment dialogFragment = new NoInternetFragment();
    private TextView tvTotalOrders;
    private TextView tvTotalHandpickOrders;
    private TextView tvRejectedOrders;
    private TextView tvLeftOrders;
    private TextView tvNewOrders;
    private TextView tvOldOrders;
    private TextView tvInventory;
    private MaterialCardView mbRejectedOrders;
    private MaterialCardView mbTotalOrders;
    private MaterialCardView mbHandpickedOrders;
    private MaterialCardView mbLeftOrders;
    private MaterialCardView mcToday;
    private boolean alreadyLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTotalOrders = view.findViewById(R.id.tvTotalOrders);
        tvTotalHandpickOrders = view.findViewById(R.id.tvTotalHandpickOrders);
        tvRejectedOrders = view.findViewById(R.id.tvRejectedOrders);
        tvLeftOrders = view.findViewById(R.id.tvLeftOrders);
        tvNewOrders = view.findViewById(R.id.tvNewOrders);
        tvOldOrders = view.findViewById(R.id.tvOldOrders);
        tvInventory = view.findViewById(R.id.tvInventory);
        mbRejectedOrders = view.findViewById(R.id.mbRejectedOrders);
        mbTotalOrders = view.findViewById(R.id.mbTotalOrders);
        mbHandpickedOrders = view.findViewById(R.id.mbHandpickedOrders);
        mbLeftOrders = view.findViewById(R.id.mbLeftOrders);
        mcToday = view.findViewById(R.id.mcToday);
        methods();
    }

    @Override
    public void onResume() {
        super.onResume();

        loadingFragment.show(getChildFragmentManager(), "");
        methods();
    }

    private void methods() {
        listners();
        initOrdersConnector();
    }

    private void listners() {
        tvOldOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.bottomNavigationView.setSelectedItemId(R.id.bottom_orders);
                OrdersFragment.orderType = 8;
            }
        });
        tvNewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.bottomNavigationView.setSelectedItemId(R.id.bottom_orders);
                OrdersFragment.orderType = 5;
            }
        });
        mbRejectedOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.bottomNavigationView.setSelectedItemId(R.id.bottom_orders);
                OrdersFragment.orderType = 9;
            }
        });
        mbTotalOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.bottomNavigationView.setSelectedItemId(R.id.bottom_orders);
                OrdersFragment.orderType = -1;
            }
        });
        mbHandpickedOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.bottomNavigationView.setSelectedItemId(R.id.bottom_orders);
                OrdersFragment.orderType = 8;
            }
        });
        mbLeftOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.bottomNavigationView.setSelectedItemId(R.id.bottom_orders);
                OrdersFragment.orderType = 5;
            }
        });
        mcToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DailyListActivity.class);
                startActivity(i);
            }
        });
        tvInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), InventoryActivity.class);
                startActivity(i);
            }
        });
    }

    private void initOrdersConnector() {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();
        GetOrdersRequest getOrdersRequest = new GetOrdersRequest();
        getOrdersRequest.setRole("Sabjiwala");
        ServerCommunicator.getOrdersList(connector, getOrdersRequest, AppSettings.getSessionKey(getActivity()));
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
                        alreadyLoaded = true;

                        loadingFragment.dismisss();
                        setOrdersData(responseBody.getData());
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

    }

    private void setOrdersData(List<GetOrdersResponseData> data) {
        int total = data.size();
        int handpickOrders = data.size();
        int rejected = 0;
        int accepted = 0;
        int pending = 0;
        int way = 0;
        int completed = 0;
        int leftOrders = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getOrder_status().equals("5")) {
                pending += 1;
            }
            if (data.get(i).getOrder_status().equals("6")) {
                accepted += 1;
            }
            if (data.get(i).getOrder_status().equals("7")) {
                way += 1;
            }
            if (data.get(i).getOrder_status().equals("8")) {
                completed += 1;
            }
            if (data.get(i).getOrder_status().equals("9")) {
                rejected += 1;
            }
        }
        leftOrders = pending + accepted + way;
        tvTotalOrders.setText("" + total);
        tvTotalHandpickOrders.setText("" + completed);
        tvRejectedOrders.setText("" + rejected);
        tvLeftOrders.setText("" + leftOrders);
    }

    @Override
    public void onUploadComplete(WebResponse webResponse) {

    }

    @Override
    public void onVFRClientException(WebErrorResponse edErrorData) {
        AppLogger.e(Utils.getTag(), edErrorData.getMessage());
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Utils.ShowToast(getActivity(), edErrorData.getMessage());
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
        if (message.equals("-2")) {
            try {
                AppSettings.clearPrefs(getActivity());
                Intent intent = new Intent(getActivity(), SplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } catch (Exception e) {

            }

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
}
