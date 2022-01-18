package com.example.sabziwala.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sabziwala.Fragments.NoInternetFragment;
import com.example.sabziwala.R;
import com.example.sabziwala.Service.OnRequestResponseListener;
import com.example.sabziwala.Service.communicator.Connector;
import com.example.sabziwala.Service.communicator.ServerCommunicator;
import com.example.sabziwala.Service.request.UserLoginRequest;
import com.example.sabziwala.Service.response.UserLoginResponse;
import com.example.sabziwala.Service.response.UserLoginResponseData;
import com.example.sabziwala.Service.response.WebErrorResponse;
import com.example.sabziwala.Service.response.WebResponse;
import com.example.sabziwala.Utilities.AppSettings;
import com.example.sabziwala.Utilities.Constants;
import com.example.sabziwala.Utilities.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity implements OnRequestResponseListener {


    ImageView back;
    TextView btnContinue;
    EditText etPassword;
    EditText etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        back = findViewById(R.id.ivBack);
        btnContinue = findViewById(R.id.btnContinue);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhone.getText().toString().trim().length() != 10) {
                    Utils.ShowToast(LoginActivity.this, "Invalid Phone Number");
                    return;
                }
                if (etPassword.getText().toString().trim().isEmpty()) {
                    Utils.ShowToast(LoginActivity.this, "Empty Password Field");

                    return;
                }
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        connectorInit(etPhone.getText().toString().trim(), etPassword.getText().toString().trim(),task.getResult());

                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void connectorInit(String phoneNumber, String password, String result) {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();        UserLoginRequest request = new UserLoginRequest();
        request.setPhone_number(phoneNumber);
        request.setPassword(password);
        request.setFcm_token(result);
        ServerCommunicator.userLogin(connector, request);
        connector.setOnRequestResponseListener(this);
    }

    @Override
    public void onAddMoreResponse(WebResponse webResponse) {

    }

    @Override
    public void onHttpResponse(WebResponse webResponse) {
        if (webResponse instanceof UserLoginResponse) {
            final UserLoginResponse responseBody = (UserLoginResponse) webResponse;
            if (responseBody.getStatus()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        saveUserData(responseBody.getData());
                        AppSettings.setSessionKey(LoginActivity.this, responseBody.getToken());
                        AppSettings.setUserToken(LoginActivity.this, responseBody.getToken());
                        Intent i=new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(i);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();

                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.ShowToast(LoginActivity.this, "" + responseBody.getMessage());
                    }
                });
            }

        }
    }

    private void saveUserData(UserLoginResponseData data) {
        AppSettings.setPhone(LoginActivity.this, data.getPhone_number());
        AppSettings.setLogin(LoginActivity.this, true);
        AppSettings.setUserName(LoginActivity.this, data.getFull_name());
        AppSettings.setUId(LoginActivity.this, data.getUser_id());
        AppSettings.setUserEmail(LoginActivity.this, data.getEmail());
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
