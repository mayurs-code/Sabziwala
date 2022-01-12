package com.example.sabziwala.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sabziwala.R;
import com.example.sabziwala.Service.response.GetOrdersResponseData;
import com.google.android.material.button.MaterialButton;

public class CompletedHandpickFragment extends DialogFragment {
    private final GetOrdersResponseData getOrdersResponseData;
    private MaterialButton mbViewOrder;
    private TextView tvBack;

    public CompletedHandpickFragment(GetOrdersResponseData getOrdersResponseData) {
        this.getOrdersResponseData = getOrdersResponseData;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(false);
        return inflater.inflate(R.layout.dialog_completed_handpick, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mbViewOrder = view.findViewById(R.id.mbViewOrder);
        tvBack = view.findViewById(R.id.tvBack);
        methods();

    }

    private void methods() {
        listners();

    }

    private void listners() {
        mbViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                dismiss();
            }
        });
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                dismiss();
            }
        });

    }


}
