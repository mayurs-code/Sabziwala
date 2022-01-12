package com.example.sabziwala.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sabziwala.R;
import com.example.sabziwala.Utilities.AppLogger;


public class LoadingFragment extends DialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        dismiss();
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Material);
        if (getParentFragment() != null) {
            AppLogger.e("ShowingDialog +", getParentFragment().getClass().getSimpleName());
        } else{
            AppLogger.e("ShowingDialog +", getActivity().getClass().getSimpleName());

        }

    }
    TextView hiddenText;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hiddenText=view.findViewById(R.id.hiddenText);
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        getDialog().setCancelable(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hiddenText.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hiddenText.setText("Too much traffic...");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hiddenText.setText(R.string.wrong);
                            }
                        }, 8000);
                    }
                }, 8000);
            }
        }, 2500); //the time you want to delay in milliseconds

    }


    public void dismisss() {
        Handler handler = new Handler(Looper.getMainLooper());
        if (getParentFragment() != null) {
            AppLogger.e("ShowingDialog -", getParentFragment().getClass().getSimpleName());
        } else{
            AppLogger.e("ShowingDialog -", "Some Activity" );

        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    dismiss();
                }catch (Exception e){

                }
            }
        }, 100);
    }
}
