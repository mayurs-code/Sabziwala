package com.example.sabziwala.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sabziwala.Activities.OrderMapActivity;
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
import com.example.sabziwala.Utilities.AppSettings;
import com.example.sabziwala.Utilities.Constants;
import com.example.sabziwala.Utilities.Utils;
import com.google.android.material.button.MaterialButton;

public class HandpickAvailabilityDialogFragment extends DialogFragment implements OnRequestResponseListener {
    private final GetOrdersResponseData getOrdersResponseData;
    private final boolean fromMap;
    private final EventHandler handler;
    private MaterialButton mbCancel;
    private MaterialButton mbConfirm;

    public HandpickAvailabilityDialogFragment(GetOrdersResponseData getOrdersResponseData, boolean fromMap, EventHandler handler) {
        this.getOrdersResponseData = getOrdersResponseData;
        this.fromMap = fromMap;
        this.handler = handler;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_handpick_availability, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mbCancel = view.findViewById(R.id.mbCancel);
        mbConfirm = view.findViewById(R.id.mbConfirm);

    }
    LoadingFragment loadingFragment = new LoadingFragment();

    @Override
    public void onResume() {
        super.onResume();
        methods();
    }

    private void methods() {
        listners();

    }

    private void connectorChangeStatusHandpick(String s) {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setOrder_id(getOrdersResponseData.getOrder_id());
        request.setOrder_status(s);
        ServerCommunicator.updateOrderStatus(connector, request, AppSettings.getSessionKey(getActivity()));
        connector.setOnRequestResponseListener(this);
    }

    private void listners() {
        mbConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingFragment.show(getChildFragmentManager(), "");

                connectorChangeStatusHandpick("6");
                getOrdersResponseData.setOrder_status("6");
                Intent i = new Intent(getActivity(), OrderMapActivity.class);
                i.putExtra("GetOrdersResponseData", getOrdersResponseData);
                startActivity(i);
                dismiss();
            }
        });
        mbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    @Override
    public void onAddMoreResponse(WebResponse webResponse) {

    }

    @Override
    public void onHttpResponse(WebResponse webResponse) {
        if (webResponse instanceof UpdateOrderStatusResponse) {
            final UpdateOrderStatusResponse responseBody = (UpdateOrderStatusResponse) webResponse;
            if (responseBody.getStatus()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingFragment.dismisss();
                        if (fromMap) {
                            handler.handle();
                            dismiss();

                        } else {
                            Intent i = new Intent(getActivity(), OrderMapActivity.class);
                            i.putExtra("GetOrdersResponseData", getOrdersResponseData);
                            startActivity(i);
                            dismiss();
                        }

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

    @Override
    public void onUploadComplete(WebResponse webResponse) {

    }

    @Override
    public void onVFRClientException(WebErrorResponse edErrorData) {

    }

    @Override
    public void onAuthException() {

    }

    NoInternetFragment dialogFragment=new NoInternetFragment();

    @Override
    public void onNoConnectivityException(String message) {

        if(message.equals("-1")){
            dialogFragment.show(getChildFragmentManager(),""+ Constants.incrementalID++);
        }if(message.equals("1")){
            try{
                dialogFragment.dismiss();
            }catch (Exception e){

            }
        }
    }
    @Override
    public void onNoCachedDataAvailable() {

    }
}
