package com.example.sabziwala.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabziwala.Adapters.InventoryProductsAdapter;
import com.example.sabziwala.Fragments.LoadingFragment;
import com.example.sabziwala.Fragments.NoInternetFragment;
import com.example.sabziwala.R;
import com.example.sabziwala.Service.OnRequestResponseListener;
import com.example.sabziwala.Service.communicator.Connector;
import com.example.sabziwala.Service.communicator.ServerCommunicator;
import com.example.sabziwala.Service.request.GetInventoryRequest;
import com.example.sabziwala.Service.response.GetInventoryResponse;
import com.example.sabziwala.Service.response.GetInventoryResponseData;
import com.example.sabziwala.Service.response.WebErrorResponse;
import com.example.sabziwala.Service.response.WebResponse;
import com.example.sabziwala.Utilities.AppSettings;
import com.example.sabziwala.Utilities.Constants;
import com.example.sabziwala.Utilities.Utils;

import java.util.List;

public class InventoryActivity extends AppCompatActivity implements OnRequestResponseListener {

    RecyclerView rvInventory;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        rvInventory = findViewById(R.id.rvInventory);
        ivBack = findViewById(R.id.ivBack);
    }
    LoadingFragment loadingFragment = new LoadingFragment();

    @Override
    public void onResume() {
        super.onResume();
        loadingFragment.show(getSupportFragmentManager(), "");
        methods();
    }


    private void methods() {
        getInventoryConnectorInit();
        listners();
    }

    private void listners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getInventoryConnectorInit() {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();        GetInventoryRequest request = new GetInventoryRequest();
        request.setUser_id(AppSettings.getUId(this));
        ServerCommunicator.getInventory(connector, request, AppSettings.getSessionKey(this));
        connector.setOnRequestResponseListener(this);
    }

    @Override
    public void onAddMoreResponse(WebResponse webResponse) {

    }

    @Override
    public void onHttpResponse(WebResponse webResponse) {
        if (webResponse instanceof GetInventoryResponse) {
            final GetInventoryResponse responseBody = (GetInventoryResponse) webResponse;
            if (responseBody.getStatus()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingFragment.dismisss();
                        setInventoryData(responseBody.getData());
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.ShowToast(InventoryActivity.this, "" + responseBody.getMessage());
                    }
                });
            }

        }
    }

    private void setInventoryData(List<GetInventoryResponseData> data) {
        InventoryProductsAdapter adapter = new InventoryProductsAdapter(this, data);
        rvInventory.setAdapter(adapter);
        rvInventory.setLayoutManager(new LinearLayoutManager(this));
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
            dialogFragment.show(getSupportFragmentManager(),""+ Constants.incrementalID++);
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