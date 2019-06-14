package com.thomas_oducayen.mobileapplication;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_REQUEST_CODE = 1234;
    private static final String[] permission = {ACCESS_COARSE_LOCATION};
    private boolean locationPermissionGranted;

    private TrafficCamActivity camerasActivity;
    private Location mLastLocation;
    private static boolean WIFIconnected = false;
    private static boolean mobileConnected = false;

    //list of cameras
    List<Camera> cameraData = new ArrayList<>();
    private boolean mAddressRequested;
    private LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationCallback();
        loadCameraData();
        getLocation();
        getLocationPermission();
        getDeviceLocation();

    }

    public void getDeviceLocation() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (locationPermissionGranted) {
                Task<Location> location = fusedLocationClient.getLastLocation();

                location.addOnSuccessListener(
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    mMap.setMyLocationEnabled(true);

                                    LatLng myLocation = new LatLng(location.getLatitude(),
                                            location.getLongitude());

                                    //mMap.setMinZoomPreference(10); // zoom to city level
                                    mMap.addMarker(new MarkerOptions().position(myLocation)
                                            .title("Thomas's Location"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 11.2f));

                                }
                            }
                        }
                );
            }
        } catch (SecurityException e) {

            Log.e("Security Exception ", "Get device location");
        }
    }

    private void getLocationPermission() {

        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_REQUEST_CODE);
        }
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d("LOCATION","onLocationResult");
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    mLastLocation = location;
                    updateUI();
                }
            }
        };
    }

    public void getLocation() {
        Log.d("LOCATION","getLocation");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                ==PackageManager.PERMISSION_GRANTED){
            Log.d("LOCATION","permissionGranted");
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d("LOCATION","gotLocation");

                            // Got last known location. In some rare situations this can be null.
                            if(location != null) {
                                // Logic to handle location object
                                mLastLocation = location;
                                updateUI();
                            }
                        }
                    });
        } else {
            Log.d("LOCATION","permissionNotGranted");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                // No explanation needed, we can request the permission.
                Log.d("LOCATION","requestPermission");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    public void updateUI() {
        if (mLastLocation == null) {
            // get location updates
            Log.d("LOCATION", "startLocationUpdates");
            getDeviceLocation();
        } else {

            // initiate geocode request
            if (mAddressRequested) {
                getDeviceLocation();
            }
            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            LatLng myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.setMinZoomPreference(10); // zoom to city level
            mMap.addMarker(new MarkerOptions().position(myLocation)
                    .title("My current location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        }
    }

    public void loadCameraData() {

        String dataUrl = "http://brisksoft.us/ad340/traffic_cameras_merged.json";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET, dataUrl, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("Cameras 1", response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject camera = response.getJSONObject(i);

                        double[] coords = {camera.getDouble("ypos"), camera.getDouble("xpos")};

                        Camera c = new Camera(
                                camera.getString("cameralabel"),
                                camera.getJSONObject("imageurl").getString("url"),
                                camera.getString("ownershipcd"),
                                coords
                        );
                        cameraData.add(c);
                        Log.i("Camera data", c.toString());

                    }
                    showCameraMarkers();

                } catch (JSONException e) {
                    Log.d("CAMERAS error", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
            }
        });
        queue.add(jsonReq);
    }

    public boolean checkNetworkConnections() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            WIFIconnected = networkInfo.getType() == connectivityManager.TYPE_WIFI;
            mobileConnected = networkInfo.getType() == connectivityManager.TYPE_MOBILE;
            if (WIFIconnected) {
                Log.i("WIFI connected", "successfully");
                return true;
            } else if (mobileConnected) {
                Log.i("Mobile connected ", "successfuly");
                return true;
            }
        } else {
            Log.i("Connection status ", "No connection");
            return false;
        }
        return false;
    }

    public void showCameraMarkers() {

        Log.i("CAMERA", cameraData.toString());


        for(Camera camera: cameraData){
            Log.i("CAMERA DATA", camera.toString());
            LatLng position = new LatLng(camera.getCoords()[0], camera.getCoords()[1]);
            Marker m = mMap.addMarker(new MarkerOptions().position(position).title(camera.getLabel()).snippet(camera.getImage()));
            m.setTag(camera);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("Location", "On MapReady");

        //// Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}