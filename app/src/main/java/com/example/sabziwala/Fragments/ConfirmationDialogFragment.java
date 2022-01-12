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

import com.example.sabziwala.Activities.HomeActivity;
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
import com.example.sabziwala.Utilities.Utils;
import com.google.android.material.button.MaterialButton;

public class ConfirmationDialogFragment extends DialogFragment  {
    private final GetOrdersResponseData getOrdersResponseData;
    private final boolean fromMap;
    private   EventHandler handler;
    private MaterialButton mbCancel;
    private MaterialButton mbConfirm;

    public ConfirmationDialogFragment(GetOrdersResponseData getOrdersResponseData, boolean fromMap, EventHandler handler) {
        this.getOrdersResponseData = getOrdersResponseData;
        this.fromMap = fromMap;
        this.handler = handler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_confirm, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mbCancel = view.findViewById(R.id.mbCancel);
        mbConfirm = view.findViewById(R.id.mbConfirm);
        methods();

    }

    private void methods() {
        listners();

    }

    private void listners() {
        mbConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.handle(getOrdersResponseData.getOrder_id());
                dismiss();
            }
        });
        mbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.handle();
                dismiss();
            }
        });

    }
}
