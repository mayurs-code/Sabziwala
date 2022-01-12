package com.example.sabziwala.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sabziwala.Activities.HomeActivity;
import com.example.sabziwala.Activities.InventoryActivity;
import com.example.sabziwala.Activities.SplashActivity;
import com.example.sabziwala.R;
import com.example.sabziwala.Service.OnRequestResponseListener;
import com.example.sabziwala.Service.communicator.Connector;
import com.example.sabziwala.Service.communicator.ServerCommunicator;
import com.example.sabziwala.Service.request.GetProfileRequest;
import com.example.sabziwala.Service.response.GetProfileResponse;
import com.example.sabziwala.Service.response.GetProfileResponseData;
import com.example.sabziwala.Service.response.WebErrorResponse;
import com.example.sabziwala.Service.response.WebResponse;
import com.example.sabziwala.Utilities.AppSettings;
import com.example.sabziwala.Utilities.Constants;
import com.example.sabziwala.Utilities.Utils;

public class SettingFragment extends Fragment implements OnRequestResponseListener {
    LinearLayout llAllOrders;
    LinearLayout llCheckInventory;
    LinearLayout llAboutApp;
    LinearLayout llHelpAndSupport;
    LinearLayout llPrivacy;
    LinearLayout llTerms;
    LinearLayout llLogOut;
    TextView tvUserName;
    TextView tvEmail;
    ImageView ivProfile;
    Activity context;
    LoadingFragment loadingFragment = new LoadingFragment();
    boolean alreadyLoaded = true;
    NoInternetFragment dialogFragment = new NoInternetFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        llAllOrders = view.findViewById(R.id.llAllOrders);
        llCheckInventory = view.findViewById(R.id.llCheckInventory);
        llAboutApp = view.findViewById(R.id.llAboutApp);
        llHelpAndSupport = view.findViewById(R.id.llHelpAndSupport);
        llPrivacy = view.findViewById(R.id.llPrivacy);
        llTerms = view.findViewById(R.id.llTerms);
        llLogOut = view.findViewById(R.id.llLogOut);
        tvUserName = view.findViewById(R.id.tvUserName);
        ivProfile = view.findViewById(R.id.ivProfile);
        tvEmail = view.findViewById(R.id.tvEmail);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!alreadyLoaded)
            loadingFragment.show(getChildFragmentManager(), "");
        methods();
    }

    private void methods() {
        connectorProfileInit();
        listners();
    }

    private void setData(GetProfileResponseData data) {
        tvEmail.setText(data.getEmail());
        tvUserName.setText(data.getFull_name());
        Glide.with(context).load(Constants.FILES_URL + data.getProfile_image()).into(ivProfile);
    }

    private void connectorProfileInit() {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();
        GetProfileRequest getProfileRequest = new GetProfileRequest();
        getProfileRequest.setUser_id(AppSettings.getUId(context));
        ServerCommunicator.getProfile(connector, getProfileRequest, AppSettings.getSessionKey(context));
        connector.setOnRequestResponseListener(this);

    }

    private void listners() {
        llAllOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.bottomNavigationView.setSelectedItemId(R.id.bottom_orders);
            }
        });
        llCheckInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), InventoryActivity.class);
                startActivity(i);
            }
        });
        llAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        llHelpAndSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        llPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        llTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        llLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSettings.clearPrefs(getActivity());
                Intent intent = new Intent(getActivity(), SplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onAddMoreResponse(WebResponse webResponse) {

    }

    @Override
    public void onHttpResponse(WebResponse webResponse) {
        if (webResponse instanceof GetProfileResponse) {
            final GetProfileResponse responseBody = (GetProfileResponse) webResponse;
            if (responseBody.getStatus()) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogFragment.dismiss();
                        alreadyLoaded = false;
                        setData(responseBody.getData());

                    }
                });


            } else {
                context.runOnUiThread(new Runnable() {
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
}
