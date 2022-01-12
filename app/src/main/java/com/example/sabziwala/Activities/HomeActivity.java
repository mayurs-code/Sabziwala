package com.example.sabziwala.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.sabziwala.Fragments.DashboardFragment;
import com.example.sabziwala.Fragments.LoadingFragment;
import com.example.sabziwala.Fragments.NoInternetFragment;
import com.example.sabziwala.Fragments.NotificationFragment;
import com.example.sabziwala.Fragments.OrdersFragment;
import com.example.sabziwala.Fragments.SettingFragment;
import com.example.sabziwala.R;
import com.example.sabziwala.Service.OnRequestResponseListener;
import com.example.sabziwala.Service.communicator.Connector;
import com.example.sabziwala.Service.communicator.ServerCommunicator;
import com.example.sabziwala.Service.request.GetProfileRequest;
import com.example.sabziwala.Service.request.UpdateCoordinatesRequest;
import com.example.sabziwala.Service.response.ChangeActiveStatusResponse;
import com.example.sabziwala.Service.response.GetProfileResponse;
import com.example.sabziwala.Service.response.GetProfileResponseData;
import com.example.sabziwala.Service.response.WebErrorResponse;
import com.example.sabziwala.Service.response.WebResponse;
import com.example.sabziwala.Utilities.AppLogger;
import com.example.sabziwala.Utilities.AppSettings;
import com.example.sabziwala.Utilities.Constants;
import com.example.sabziwala.Utilities.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements OnRequestResponseListener {

    public static BottomNavigationView bottomNavigationView;
    final Handler handler = new Handler();
    final int delay = 60000;
    Fragment selectedFragment = null;
    Switch status_switch;
    TextView tvAddress;
    TextView tvOnline;
    Geocoder geocoder;
    List<Address> addresses;
    Connector connector;
    MaterialCardView mcTopBar;
    private Fragment dashboardFragment;
    private Fragment ordersFragment;
    private Fragment notificationFragment;
    private Fragment settingsFragment;
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
    public static LatLng latlng=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        Connector connector = Connector.getConnector();
        Connector connector = new Connector();        checkPermissions();
        initViews();

    }
    LoadingFragment loadingFragment = new LoadingFragment();
    private boolean alreadyLoaded=false;

    @Override
    public void onResume() {
        super.onResume();
        if(!alreadyLoaded)
            loadingFragment.show(getSupportFragmentManager(), "");
        methods();
    }

    private void repeatedMethod() {
        getDeviceLocation();
        handler.postDelayed(coordsRunnable, delay);
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        tvAddress = findViewById(R.id.tvAddress);
        mcTopBar = findViewById(R.id.mcTopBar);
        status_switch = findViewById(R.id.status_switch);
        tvOnline = findViewById(R.id.tvOnline);

    }

    private void methods() {
        repeatedMethod();
        connectorGetUserProfile();
        getDeviceLocation();

    }

    private void checkPermissions() {
        boolean permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (permissionGranted) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

    }

    private void setFragments() {
        dashboardFragment = new DashboardFragment();
        ordersFragment = new OrdersFragment();
        notificationFragment = new NotificationFragment();
        settingsFragment = new SettingFragment();
    }

    private void listners() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setFragments();
                selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.bottom_dashboard:
                        mcTopBar.setVisibility(View.VISIBLE);

                        selectedFragment = dashboardFragment;
                        break;
                    case R.id.bottom_orders:
                        mcTopBar.setVisibility(View.VISIBLE);

                        selectedFragment = ordersFragment;
                        break;
                    case R.id.bottom_notification:
                        mcTopBar.setVisibility(View.GONE);

                        selectedFragment = notificationFragment;
                        break;
                    case R.id.bottom_setting:
                        mcTopBar.setVisibility(View.GONE);
                        selectedFragment = settingsFragment;
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commitAllowingStateLoss();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.bottom_dashboard);
        status_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectorSwitchUserStatus();
            }
        });

    }

    private void connectorSwitchUserStatus() {
        //        Connector connector = Connector.getConnector();
        Connector connector = new Connector();
        ServerCommunicator.changeStatus(connector, AppSettings.getSessionKey(this));
        connector.setOnRequestResponseListener(this);
    }

    private void connectorGetUserProfile() {
        //        Connector connector = Connector.getConnector();
        Connector connector = new Connector();
        GetProfileRequest getProfileRequest = new GetProfileRequest();
        getProfileRequest.setUser_id(AppSettings.getUId(this));
        ServerCommunicator.getProfile(connector, getProfileRequest, AppSettings.getSessionKey(this));
        connector.setOnRequestResponseListener(this);
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

    private void getAddress(LatLng latLng) {
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Log.d("getAddress", "onPlaceSelected: " + address + " " + city + " " + state + " " + country + " " + postalCode + " " + knownName);
            tvAddress.setText(address);
            this.latlng=latLng;
            updateCoordinatesConnectorInit(latLng);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);

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
                            Toast.makeText(HomeActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alreadyLoaded=true;

                        loadingFragment.dismisss();
                        listners();
                        setUserStatus(responseBody.getData());
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.ShowToast(HomeActivity.this, "" + responseBody.getMessage());
                    }
                });
            }

        }
        if (webResponse instanceof ChangeActiveStatusResponse) {
            final ChangeActiveStatusResponse responseBody = (ChangeActiveStatusResponse) webResponse;
            if (responseBody.getStatus()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        connectorGetUserProfile();
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.ShowToast(HomeActivity.this, "" + responseBody.getMessage());
                    }
                });
            }

        }

    }

    private void setUserStatus(GetProfileResponseData data) {
        status_switch.setChecked(data.getStatus().equals("1"));
        if (data.getStatus().equals("1")) {
            tvOnline.setText(R.string.online);
            tvOnline.setTextColor(getResources().getColor(R.color.colorPrimary,null));

        } else {
            tvOnline.setText(R.string.offline);
            tvOnline.setTextColor(getResources().getColor(R.color.grey,null));
        }


    }

    @Override
    public void onUploadComplete(WebResponse webResponse) {

    }

    @Override
    public void onVFRClientException(WebErrorResponse edErrorData) {
        AppLogger.e(Utils.getTag(), edErrorData.getMessage());
        runOnUiThread(new Runnable() {
            public void run() {
                Utils.ShowToast(HomeActivity.this, edErrorData.getMessage());
            }
        });
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
    public void onBackPressed() {
        try {
            if (bottomNavigationView.getSelectedItemId() == R.id.bottom_dashboard )
                super.onBackPressed();
            else {
                 bottomNavigationView.setSelectedItemId(R.id.bottom_dashboard );
            }
        } catch (Exception e) {
             bottomNavigationView.setSelectedItemId(R.id.bottom_dashboard);

        }

    }
}
