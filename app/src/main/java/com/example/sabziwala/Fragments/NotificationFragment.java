package com.example.sabziwala.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabziwala.Activities.HomeActivity;
import com.example.sabziwala.Adapters.NotificationAdapter;
import com.example.sabziwala.R;
import com.example.sabziwala.Service.OnRequestResponseListener;
import com.example.sabziwala.Service.communicator.Connector;
import com.example.sabziwala.Service.communicator.ServerCommunicator;
import com.example.sabziwala.Service.response.NotificationResponse;
import com.example.sabziwala.Service.response.NotificationResponseData;
import com.example.sabziwala.Service.response.WebErrorResponse;
import com.example.sabziwala.Service.response.WebResponse;
import com.example.sabziwala.Utilities.AppSettings;
import com.example.sabziwala.Utilities.Constants;
import com.example.sabziwala.Utilities.Utils;

import java.util.List;

public class NotificationFragment extends Fragment implements OnRequestResponseListener {

    LinearLayout llEmpty;
    RecyclerView rvNotifications;
    ImageView ivBack;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notification, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        llEmpty = view.findViewById(R.id.llEmpty);
        ivBack = view.findViewById(R.id.ivBack);
        rvNotifications = view.findViewById(R.id.rvNotifications);
        super.onViewCreated(view, savedInstanceState);
    }
    LoadingFragment loadingFragment = new LoadingFragment();

    @Override
    public void onResume() {
        super.onResume();
        loadingFragment.show(getChildFragmentManager(), "");
        methods();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void methods() {
        connectorInit();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.bottomNavigationView.setSelectedItemId(R.id.bottom_dashboard);
            }
        });


    }

    private void connectorInit() {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();        ServerCommunicator.getNotifications(connector, AppSettings.getSessionKey(getContext()));
        connector.setOnRequestResponseListener(this);
    }


    @Override
    public void onAddMoreResponse(WebResponse webResponse) {


    }

    @Override
    public void onHttpResponse(WebResponse webResponse) {
        if (webResponse instanceof NotificationResponse) {
            final NotificationResponse responseBody = (NotificationResponse) webResponse;
            if (responseBody.getStatus()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingFragment.dismisss();
                        if(responseBody.getData().size()<=0){
                            llEmpty.setVisibility(View.VISIBLE);

                        }else {
                            llEmpty.setVisibility(View.GONE);
                            setAdapter(responseBody.getData());
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

    private void setAdapter(List<NotificationResponseData> data) {
        NotificationAdapter adapter=new NotificationAdapter(getActivity(),data);
        rvNotifications.setAdapter(adapter);
        rvNotifications.setLayoutManager(new LinearLayoutManager(getActivity()));

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