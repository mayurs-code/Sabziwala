package com.example.sabziwala.Activities;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.sabziwala.Fragments.CompletedHandpickFragment;
import com.example.sabziwala.Fragments.ConfirmationDialogFragment;
import com.example.sabziwala.Fragments.HandpickAvailabilityDialogFragment;
import com.example.sabziwala.Fragments.LoadingFragment;
import com.example.sabziwala.Fragments.NoInternetFragment;
import com.example.sabziwala.R;
import com.example.sabziwala.Service.EventHandler;
import com.example.sabziwala.Service.OnRequestResponseListener;
import com.example.sabziwala.Service.communicator.Connector;
import com.example.sabziwala.Service.communicator.ServerCommunicator;
import com.example.sabziwala.Service.request.GetProfileRequest;
import com.example.sabziwala.Service.request.OrderDetailRequest;
import com.example.sabziwala.Service.request.UpdateCoordinatesRequest;
import com.example.sabziwala.Service.request.UpdateOrderStatusRequest;
import com.example.sabziwala.Service.response.GetOrdersResponseData;
import com.example.sabziwala.Service.response.GetProfileResponse;
import com.example.sabziwala.Service.response.GetProfileResponseData;
import com.example.sabziwala.Service.response.OrderDetailResponse;
import com.example.sabziwala.Service.response.UpdateOrderStatusResponse;
import com.example.sabziwala.Service.response.WebErrorResponse;
import com.example.sabziwala.Service.response.WebResponse;
import com.example.sabziwala.Utilities.AppSettings;
import com.example.sabziwala.Utilities.Constants;
import com.example.sabziwala.Utilities.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderMapActivity extends FragmentActivity implements OnMapReadyCallback, EventHandler, OnRequestResponseListener, RoutingListener, GoogleApiClient.OnConnectionFailedListener {

    final Handler handler = new Handler();
    final int delay = 20000;
    TextView tvAddress;
    TextView tvScheduleTime;
    TextView tvUserName;
    TextView tvTimeLeft;
    TextView tvTotalDistance;
    MaterialCardView mcShowAll;
    LinearProgressIndicator timeProgress;
    TextView tvOrderType;
    TextView tvUserAddress;
    MaterialCardView mcOrderCard;
    ImageView ivUserImage;
    ImageView ivCall;
    MaterialButton mbCancel;
    MaterialButton mbAccept;
    MaterialButton mbStartDirection;
    MaterialButton mbReached;
    MaterialButton mbHandpickDone;
    LinearLayout llo5;
    LinearLayout llo6;
    LinearLayout llo7;
    LinearLayout llo8;
    List<Address> addresses;
    LoadingFragment loadingFragment = new LoadingFragment();
    NoInternetFragment dialogFragment = new NoInternetFragment();
    private GoogleMap mMap;
    private GetOrdersResponseData getOrdersResponseData;
    private LatLng sabziwalaLatlang;
    private LatLng userlaLatlang;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    Runnable coordsRunnable = new Runnable() {
        @Override
        public void run() {
            {
                getDeviceLocation();
                handler.postDelayed(this, delay);
            }
        }
    };
    private List<Polyline> polylines = null;

    public static float getKmFromLatLong(LatLng l1, LatLng l2) {
        Location loc1 = new Location("");
        loc1.setLatitude(l1.latitude);
        loc1.setLongitude(l1.longitude);
        Location loc2 = new Location("");
        loc2.setLatitude(l2.latitude);
        loc2.setLongitude(l2.longitude);
        float distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters / 1000;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getOrdersResponseData = (GetOrdersResponseData) getIntent().getSerializableExtra("GetOrdersResponseData");
        tvAddress = findViewById(R.id.tvAddress);
        mbStartDirection = findViewById(R.id.mbStartDirection);
        mbReached = findViewById(R.id.mbReached);
        mbHandpickDone = findViewById(R.id.mbHandpickDone);
        tvUserName = findViewById(R.id.tvUserName);
        ivCall = findViewById(R.id.ivCall);
        mcOrderCard = findViewById(R.id.mcOrderCard);
        tvOrderType = findViewById(R.id.tvOrderType);
        ivUserImage = findViewById(R.id.ivUserImage);
        mcShowAll = findViewById(R.id.mcShowAll);
        tvTotalDistance = findViewById(R.id.tvTotalDistance);
        tvTimeLeft = findViewById(R.id.tvTimeLeft);
        tvUserAddress = findViewById(R.id.tvUserAddress);
        mbCancel = findViewById(R.id.mbCancel);
        timeProgress = findViewById(R.id.timeProgress);
        mbAccept = findViewById(R.id.mbAccept);
        llo5 = findViewById(R.id.llAcceptReject);
        llo6 = findViewById(R.id.llo6);
        llo7 = findViewById(R.id.llo7);
        llo8 = findViewById(R.id.llo8);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(OrderMapActivity.this);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadingFragment.show(getSupportFragmentManager(), "");
        methods();
    }

    private void methods() {
        handle();
        repeatedMethod();
        listners();
        getDeviceLocation();
    }

    private void updateBottomCard() {
        connectorOrdersInit();
    }

    private void connectorOrdersInit() {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();
        OrderDetailRequest orderDetailRequest = new OrderDetailRequest();
        orderDetailRequest.setOrder_id(getOrdersResponseData.getOrder_id());
        ServerCommunicator.giveOrderDetails(connector, orderDetailRequest, AppSettings.getSessionKey(this));
        connector.setOnRequestResponseListener(this);
    }

    private void connectorGetUserProfile(String id) {
        //        Connector connector = Connector.getConnector();
        Connector connector = new Connector();
        GetProfileRequest getProfileRequest = new GetProfileRequest();
        getProfileRequest.setUser_id(id);
        ServerCommunicator.getProfile(connector, getProfileRequest, AppSettings.getSessionKey(this));
        connector.setOnRequestResponseListener(this);
    }

    private void listners() {
        mbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmationDialogFragment dialogFragment = new ConfirmationDialogFragment(getOrdersResponseData, true, OrderMapActivity.this);
                dialogFragment.show(getSupportFragmentManager(), "ConfirmationDialogFragment");

            }
        });
        mbAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HandpickAvailabilityDialogFragment dialogFragment = new HandpickAvailabilityDialogFragment(getOrdersResponseData, true, OrderMapActivity.this);
                dialogFragment.show(getSupportFragmentManager(), "HandpickAvailabilityDialogFragment");


            }
        });
        mbStartDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectorChangeStatusHandpick("7");

            }
        });
        mbReached.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectorChangeStatusHandpick("7");

            }
        });
        mbHandpickDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectorChangeStatusHandpick("8");


            }
        });
        mcShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCamera();
            }
        });

    }

    private void connectorChangeStatusHandpick(String s) {
        loadingFragment.show(getSupportFragmentManager(), "1");
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();
        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setOrder_id(getOrdersResponseData.getOrder_id());
        request.setOrder_status(s);
        ServerCommunicator.updateOrderStatus(connector, request, AppSettings.getSessionKey(this));
        connector.setOnRequestResponseListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapFunctions();
    }

    private void mapFunctions() {

    }

    @Override
    public void handle() {
        updateBottomCard();


    }

    @Override
    public void handle(int position) {
        if (position == 9) {
            connectorChangeStatusHandpick("9");
        }

    }

    @Override
    public void handle(String id) {
        connectorChangeStatusHandpickReject(id + "");

    }

    private void connectorChangeStatusHandpickReject(String id) {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();
        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setOrder_id(id);
        request.setOrder_status("9");
        ServerCommunicator.updateOrderStatus(connector, request, AppSettings.getSessionKey(this));
        loadingFragment.show(getSupportFragmentManager(), "2");
        connector.setOnRequestResponseListener(this);
    }

    @Override
    public void onAddMoreResponse(WebResponse webResponse) {

    }

    private void repeatedMethod() {
        getDeviceLocation();
        handler.postDelayed(coordsRunnable, delay);
    }

    @Override
    public void onHttpResponse(WebResponse webResponse) {
        if (webResponse instanceof OrderDetailResponse) {
            final OrderDetailResponse responseBody = (OrderDetailResponse) webResponse;
            if (responseBody.getStatus()) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingFragment.dismisss();
                        getOrdersResponseData.setOrder_status(responseBody.getData().getOrder_status());
                        setData();
                    }
                });


            } else {
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.ShowToast(OrderMapActivity.this, "" + responseBody.getMessage());
                    }
                });
            }
        }
        if (webResponse instanceof UpdateOrderStatusResponse) {
            final UpdateOrderStatusResponse responseBody = (UpdateOrderStatusResponse) webResponse;
            if (responseBody.getStatus()) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingFragment.dismisss();
                        connectorOrdersInit();
                    }
                });


            } else {
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.ShowToast(OrderMapActivity.this, "" + responseBody.getMessage());
                    }
                });
            }
        }
        if (webResponse instanceof GetProfileResponse) {
            final GetProfileResponse responseBody = (GetProfileResponse) webResponse;
            if (responseBody.getStatus()) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUserData(responseBody.getData());
                    }
                });


            } else {
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.ShowToast(OrderMapActivity.this, "" + responseBody.getMessage());
                    }
                });
            }
        }
    }

    private void setUserData(GetProfileResponseData data) {
        tvUserName.setText(("" + data.getFull_name()));
        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OrderMapActivity.this, "NOT IMPLEMENTED", Toast.LENGTH_SHORT).show();
                String userNumber = data.getPhone_number();
            }
        });
    }

    private void setData() {
        tvUserAddress.setText(getOrdersResponseData.getOther_address());
        tvUserName.setText(("USER " + getOrdersResponseData.getUser_id()));
        connectorGetUserProfile(getOrdersResponseData.getUser_id());
        userlaLatlang = new LatLng(Double.parseDouble(getOrdersResponseData.getLat()), Double.parseDouble(getOrdersResponseData.getLag()));
        mMap.addMarker(new MarkerOptions()
                .position(userlaLatlang)
                .title("Order")
                .icon(Utils.bitmapDescriptorFromVector(this, R.drawable.location_pin)));
        setCamera();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                long seconds = 0;
                long minutes = 0;
                long hours = 0;
                long days = 0;
                if (getOrdersResponseData.getSchedule_date() == null) {
                    minutes = Utils.getDifference(Utils.toDate(getOrdersResponseData.getSchedule_date(), getOrdersResponseData.getSchedule_time()));
                    seconds = Utils.getDifferenceSec(Utils.toDate(getOrdersResponseData.getSchedule_date(), getOrdersResponseData.getSchedule_time()));
                    hours = Utils.getDifferenceHour(Utils.toDate(getOrdersResponseData.getSchedule_date(), getOrdersResponseData.getSchedule_time()));
                    days = Utils.getDifferenceDays(Utils.toDate(getOrdersResponseData.getSchedule_date(), getOrdersResponseData.getSchedule_time()));
                    tvOrderType.setText(R.string.scheduled_order);
                } else {
                    minutes = Utils.getDifference(Utils.toDate(getOrdersResponseData.getCreated_date().split(" ")[0], getOrdersResponseData.getCreated_date().split(" ")[0]));
                    seconds = Utils.getDifferenceSec(Utils.toDate(getOrdersResponseData.getCreated_date().split(" ")[0], getOrdersResponseData.getCreated_date().split(" ")[0]));
                    hours = Utils.getDifferenceHour(Utils.toDate(getOrdersResponseData.getCreated_date().split(" ")[0], getOrdersResponseData.getCreated_date().split(" ")[0]));
                    days = Utils.getDifferenceDays(Utils.toDate(getOrdersResponseData.getCreated_date().split(" ")[0], getOrdersResponseData.getCreated_date().split(" ")[1]));
                    tvOrderType.setText(R.string.recent_order);

                }
                String timeleft = "";
                if (Math.abs(days) > 1) {
                    timeleft = Math.abs(days) + "days, " + Math.abs(hours % 12) + "hours";

                } else if (Math.abs(hours) >= 1) {
                    timeleft = Math.abs(hours) + "hours, " + Math.abs(minutes % 60) + "minutes";

                } else if (Math.abs(minutes) >= 1) {
                    timeleft = Math.abs(minutes) + "minutes, " + Math.abs(seconds % 60) + "seconds";
                } else {
                    timeleft = seconds % 60 + "seconds";

                }

                tvTimeLeft.setText(timeleft);


                if (getOrdersResponseData.getOrder_status().equals("5"))
                    if (seconds > 300) {
                        connectorChangeStatusHandpickReject(getOrdersResponseData.getOrder_id());
                        getOrdersResponseData.setOrder_status("9");
                        h.removeCallbacks(this);
                    } else if (seconds > 0) {

                        int progress = (int) (seconds * 0.3);
                        timeProgress.setProgress(progress);
                    }

                h.postDelayed(this, 1000);
            }
        }, 1000);

        switch (getOrdersResponseData.getOrder_status()) {
            case "5":
                llo5.setVisibility(View.VISIBLE);
                llo6.setVisibility(View.GONE);
                llo7.setVisibility(View.GONE);
                llo8.setVisibility(View.GONE);
                ivCall.setVisibility(View.GONE);
                break;
            case "6":
                llo5.setVisibility(View.GONE);
                llo6.setVisibility(View.VISIBLE);
                llo7.setVisibility(View.GONE);
                llo8.setVisibility(View.GONE);
                ivCall.setVisibility(View.VISIBLE);
                break;
            case "7":
                llo5.setVisibility(View.GONE);
                llo6.setVisibility(View.GONE);
                llo7.setVisibility(View.GONE);
                llo8.setVisibility(View.VISIBLE);
                ivCall.setVisibility(View.VISIBLE);
                break;
            case "8":
                llo5.setVisibility(View.GONE);
                llo6.setVisibility(View.GONE);
                llo7.setVisibility(View.GONE);
                ivCall.setVisibility(View.GONE);
                llo8.setVisibility(View.GONE);
                CompletedHandpickFragment dialogFragment = new CompletedHandpickFragment(getOrdersResponseData);
                dialogFragment.show(getSupportFragmentManager(), "CompletedHandpickFragment");
                break;
            case "9":
                llo5.setVisibility(View.GONE);
                llo6.setVisibility(View.GONE);
                llo7.setVisibility(View.GONE);
                llo8.setVisibility(View.GONE);
                ivCall.setVisibility(View.GONE);
                break;

        }

    }

    private void setCamera() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(userlaLatlang);
        builder.include(sabziwalaLatlang);
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        mMap.animateCamera(cu);
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
            dialogFragment.show(getSupportFragmentManager(), "" + Constants.incrementalID++);
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

    private void getAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Log.d("getAddress", "onPlaceSelected: " + address + " " + city + " " + state + " " + country + " " + postalCode + " " + knownName);
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("You")
                    .icon(Utils.bitmapDescriptorFromVector(this, R.drawable.rickshaw)));
            LatLng userLatlang = new LatLng(Double.parseDouble(getOrdersResponseData.getLat()), Double.parseDouble(getOrdersResponseData.getLag()));
            mMap.addMarker(new MarkerOptions()
                    .position(userLatlang)
                    .title("USER " + getOrdersResponseData.getUser_id())
                    .icon(Utils.bitmapDescriptorFromVector(this, R.drawable.location_pin)));
            this.sabziwalaLatlang = latLng;
            tvAddress.setText(address);
            tvTotalDistance.setText(String.format("%.2f", getKmFromLatLong(sabziwalaLatlang, userLatlang)) + " KM");
            Findroutes(sabziwalaLatlang, userLatlang);
            updateCoordinatesConnectorInit(latLng);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCoordinatesConnectorInit(LatLng latLng) {
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();
        UpdateCoordinatesRequest request = new UpdateCoordinatesRequest();
        request.setLatitude(latLng.latitude + "");
        request.setLongitude(latLng.longitude + "");
        ServerCommunicator.updateCoordinates(connector, request, AppSettings.getSessionKey(this));
        connector.setOnRequestResponseListener(this);
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                getAddress(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));

                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        getAddress(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);

                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }


                        } else {
                            Toast.makeText(OrderMapActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void Findroutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            Toast.makeText(this, "Unable to get location", Toast.LENGTH_LONG).show();
        } else {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyDicF68Wm6vUjNFcQs3UehVImPPJa_wYTM")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
//        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
//        Toast.makeText( this,"Finding Route...",Toast.LENGTH_LONG).show();
    }

    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < route.size(); i++) {

            if (i == shortestRouteIndex) {
                polyOptions.color(getResources().getColor(R.color.colorPrimary, null));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);

            } else {

            }

        }

    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(sabziwalaLatlang, userlaLatlang);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Findroutes(sabziwalaLatlang, userlaLatlang);

    }
}