package com.example.sabziwala.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabziwala.Adapters.ItemDailyAdapter;
import com.example.sabziwala.Fragments.LoadingFragment;
import com.example.sabziwala.Fragments.NoInternetFragment;
import com.example.sabziwala.R;
import com.example.sabziwala.Service.DailyEventHandler;
import com.example.sabziwala.Service.OnRequestResponseListener;
import com.example.sabziwala.Service.communicator.Connector;
import com.example.sabziwala.Service.communicator.ServerCommunicator;
import com.example.sabziwala.Service.request.GetInventoryRequest;
import com.example.sabziwala.Service.request.UpdateInventoryRequest;
import com.example.sabziwala.Service.response.GetInventoryResponse;
import com.example.sabziwala.Service.response.GetInventoryResponseData;
import com.example.sabziwala.Service.response.UpdateInventoryResponse;
import com.example.sabziwala.Service.response.WebErrorResponse;
import com.example.sabziwala.Service.response.WebResponse;
import com.example.sabziwala.Utilities.AppSettings;
import com.example.sabziwala.Utilities.Constants;
import com.example.sabziwala.Utilities.Utils;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class DailyListActivity extends AppCompatActivity implements OnRequestResponseListener, DailyEventHandler {

    int currentCall = 0;
    private List<GetInventoryResponseData> allList;
    private String selected;
    private RecyclerView rvInventory;
    private MaterialButton mbAdd;
    private CheckBox cbCheck;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_list);
        rvInventory=findViewById(R.id.rvInventory);
        cbCheck=findViewById(R.id.cbCheck);
        ivBack=findViewById(R.id.ivBack);
        mbAdd=findViewById(R.id.mbAdd);
        currentCall = 0;

    }
    LoadingFragment loadingFragment = new LoadingFragment();

    @Override
    public void onResume() {
        super.onResume();
        loadingFragment.show(getSupportFragmentManager(), "");
        methods();
    }


    private void methods() {
        getAllInventoryConnectorInit();
        listners();
    }

    private void listners() {
        mbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInventoryConnectorInit();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getAllInventoryConnectorInit() {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();

        GetInventoryRequest request = new GetInventoryRequest();
        request.setUser_id(AppSettings.getUId(this));
        ServerCommunicator.getAllInventory(connector, request, AppSettings.getSessionKey(this));
        connector.setOnRequestResponseListener(this);
    }

    private void updateInventoryConnectorInit() {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();        UpdateInventoryRequest request = new UpdateInventoryRequest();
        request.setInventory_product_id(selected);
        ServerCommunicator.updateInventory(connector, request, AppSettings.getSessionKey(this));
        connector.setOnRequestResponseListener(this);
    }

    private void getDailyInventoryConnectorInit() {
        currentCall = 1;
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
                        if (currentCall == 0) {
                            allList = responseBody.getData();
                            getDailyInventoryConnectorInit();
                        } else {
                            setList(allList, responseBody.getData());
                            cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if(b){
                                        setList(allList,allList);
                                    }else {
                                        setList(allList,new ArrayList<>());

                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.ShowToast(DailyListActivity.this, "" + responseBody.getMessage());
                    }
                });
            }

        }if (webResponse instanceof UpdateInventoryResponse) {
            final UpdateInventoryResponse responseBody = (UpdateInventoryResponse) webResponse;
            if (responseBody.getStatus()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.ShowToast(DailyListActivity.this, "" + responseBody.getMessage());
                        onBackPressed();


                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.ShowToast(DailyListActivity.this, "" + responseBody.getMessage());
                    }
                });
            }

        }
    }

    private void setList(List<GetInventoryResponseData> allList, List<GetInventoryResponseData> data) {
        currentCall = 0;
        ItemDailyAdapter adapter = new ItemDailyAdapter(this, allList, data, this);
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

    @Override
    public void setSelected(String str) {

        this.selected = str.substring(1,str.length()-1);
    }
}