package com.example.sabziwala.Service.communicator;

import android.util.Log;

import com.example.sabziwala.Service.OnRequestResponseListener;
import com.example.sabziwala.Service.response.WebErrorResponse;
import com.example.sabziwala.Service.response.WebResponse;
import com.example.sabziwala.Utilities.AppLogger;
import com.example.sabziwala.Utilities.Constants;
import com.example.sabziwala.Utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Connector {
    public static final int METHOD_POST = 1001;
    public static final int METHOD_PUT = 1002;
    public static final int METHOD_DELETE = 1003;
    public static final int METHOD_GET = 1004;
    public static final String JSON_CONTENT_TYPE = "application/json";
    private static Connector connector = null;
    private static OkHttpClient client = null;
    private String server = Constants.API_URL;
    private OnRequestResponseListener onRequestResponseListener;

    public Connector() {

//        connector = new Connector();
        client = new OkHttpClient();

    }

    public static Connector getAttendanceList() {
        if (connector == null) {
            connector = new Connector();
            client = new OkHttpClient();

        }
        return connector;
    }

//    public static Connector getConnector() {
//        if (connector == null) {
//            connector = new Connector();
//            client = new OkHttpClient();
//        }
//        return connector;
//    }

    public OnRequestResponseListener getOnRequestResponseListener() {
        return onRequestResponseListener;
    }

    public void setOnRequestResponseListener(OnRequestResponseListener onRequestResponseListener) {
        this.onRequestResponseListener = onRequestResponseListener;

    }

    public <T> void callServer(String URL, int method, String payLoad, String sessionKey, final Class<T> resultClass) {

        String finalURL = Constants.API_URL + URL;

        Request request = new Request.Builder()
                .url(finalURL)
                .header("Content-Type", "application/json") // .addHeader("Content-Type", "application/json")
                .header("Accept", "*/*")
                .header("sessionkey", sessionKey)
//                .header("sessionkey", "fd6bda01f4eebaa6a7c726ef2a0a4b02")

                .build();
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        Log.d("8989", " Url " + finalURL);
        Log.d("9898", " Payload: " + payLoad);
        Log.d("9898", " sessionkey: " + sessionKey);
        RequestBody body;
        switch (method) {
            case METHOD_GET:

                break;
            case METHOD_POST:
//                RequestBody requestBody = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("title", "Square Logo")
//                        .addFormDataPart("image", "logo-square.png",
//                                RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
//                        .build();
//
//                Request request = new Request.Builder()
//                        .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
//                        .url(finalURL)
//                        .header("Content-Type", "application/json")
//                        .header("Accept", "*/*")
//                        .post(requestBody)
//                        .build();
                body = RequestBody.create(JSON, payLoad);
                request = new Request.Builder()
                        .url(finalURL)
                        .header("Content-Type", "application/json")
                        .header("Accept", "*/*")
                        .header("sessionkey", sessionKey)
//                        .header("sessionkey", "fd6bda01f4eebaa6a7c726ef2a0a4b02")
                        .post(body)
                        .build();
                break;
            case METHOD_PUT:
                body = RequestBody.create(JSON, payLoad);
                request = new Request.Builder()
                        .url(finalURL)
                        .header("Content-Type", "application/json")
                        .header("Accept", "*/*")
                        .put(body)
                        .build();
                break;
            case METHOD_DELETE:
                break;
        }


        Call call = client.newCall(request);
        if (true) {
            call.enqueue(new Callback() {
                public void onResponse(Call call, Response response)
                        throws IOException {
                    try {
                        JSONObject responseObj = new JSONObject(response.body().string());
                        AppLogger.i("TAGS", "Called From: " + onRequestResponseListener.getClass().getSimpleName() + " responseObj: " + responseObj);
                        if(responseObj.get("status").equals(401)){
                            AppLogger.i("TAGS", "Casssssslled From: " + onRequestResponseListener.getClass().getSimpleName() + " responseObj: " + responseObj);

                            onRequestResponseListener.onNoConnectivityException("-2");

                        }
                        WebResponse webResponse = (WebResponse) Utils.getGson().fromJson(responseObj.toString(), resultClass);
                        onRequestResponseListener.onHttpResponse(webResponse);
                        onRequestResponseListener.onNoConnectivityException("1");

                        //   AppLogger.i("TAG", "responseObj1: " + "Call http");

                    } catch (Exception e) {
                        //  e.printStackTrace();

                        WebErrorResponse response1 = new WebErrorResponse();
                        response1.setStatus(0);
                        response1.setMessage(e.getMessage());
                        if (e.getMessage().contains("Unable to resolve host")) {
                            onRequestResponseListener.onNoConnectivityException("-1");

                        } else if (e.getMessage().contains("failed to connect to")) {
                            onRequestResponseListener.onNoConnectivityException("-1");

                        } else if (e.getLocalizedMessage().equals("timeout")) {
                            onRequestResponseListener.onNoConnectivityException("-1");

                        }
                    }
                }

                public void onFailure(Call call, IOException e) {
                    try {
                        WebErrorResponse response = new WebErrorResponse();
                        response.setStatus(0);
                        response.setMessage(e.getMessage());
                        if (e.getMessage().contains("Unable to resolve host")) {
                            onRequestResponseListener.onNoConnectivityException("-1");

                        } else if (e.getMessage().contains("failed to connect to")) {
                            onRequestResponseListener.onNoConnectivityException("-1");

                        } else if (e.getLocalizedMessage().equals("timeout")) {
                            onRequestResponseListener.onNoConnectivityException("-1");

                        } else {
                            onRequestResponseListener.onVFRClientException(response);
                        }
                        Log.i("TAG", "login failed: " + e.getLocalizedMessage());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }
            });
        } else onRequestResponseListener.onNoConnectivityException("-1");
    }

    public <T> void callServerForMultipart(String URL, int method, MultipartBody payLoad, final Class<T> resultClass) {

        String finalURL = Constants.API_URL + URL;

        Request request = new Request.Builder()
                .url(finalURL)
                .header("Content-Type", "application/json") // .addHeader("Content-Type", "application/json")
                .header("Accept", "*/*")
                .build();
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        Log.d("8989", " Url " + finalURL);
        Log.d("9898", " Payload: " + payLoad);

        switch (method) {
            case METHOD_GET:

                break;
            case METHOD_POST:
              /*  RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title", "Square Logo")
                        .addFormDataPart("image", "logo-square.png",
                                RequestBody.create(null, new File("website/static/logo-square.png")))
                        .build();
                RequestBody body = RequestBody.create(JSON, payLoad);
*/
                request = new Request.Builder()
                        .url(finalURL)
                        .header("Content-Type", "application/json")
                        .header("Accept", "*/*")
                        .post(payLoad)
                        .build();
//                RequestBody body = RequestBody.create(JSON, payLoad);
//                request = new Request.Builder()
//                        .url(finalURL)
//                        .header("Content-Type", "application/json")
//                        .header("Accept", "*/*")
//                        .post(body)
//                        .build();
                break;
            case METHOD_PUT:
                break;
            case METHOD_DELETE:
                break;
        }


        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response)
                    throws IOException {
                try {
                    JSONObject responseObj = new JSONObject(response.body().string());
                    AppLogger.i(Utils.getTag(), "responseObj: " + responseObj);
                    WebResponse webResponse = (WebResponse) Utils.getGson().fromJson(responseObj.toString(), resultClass);
                    onRequestResponseListener.onHttpResponse(webResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Call call, IOException e) {
                try {
                    WebErrorResponse response = new WebErrorResponse();
                    response.setStatus(0);
                    response.setMessage(e.getMessage());
                    onRequestResponseListener.onVFRClientException(response);
                    AppLogger.e("TAG", "exception.... " + e.getLocalizedMessage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

}