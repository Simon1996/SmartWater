package com.inside.developed.smartlauncher;

/**
 * Created by Simon on 20.04.2017.
 */

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;


public class Map extends Fragment implements OnMapReadyCallback, PlaceSelectionListener, GoogleMap.OnMyLocationChangeListener {

    GPSTracker gps;
    private View rootView;
    private MapView mapView;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    GoogleMap map;
    Marker marker;
    private static final LatLng UKRAINE = new LatLng(49,32);
    LatLng latLng;
    double valuex, valuey;
    String getdatastr, waterlevelstr, statusstr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) rootView.findViewById(R.id.search_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        getdatastr = getResources().getString(R.string.get_data);
        waterlevelstr = getResources().getString(R.string.waterlevel);
        statusstr = getResources().getString(R.string.status);

        MapsInitializer.initialize(this.getActivity());

        autocompleteFragment = new SupportPlaceAutocompleteFragment();
        autocompleteFragment.setOnPlaceSelectedListener(this);

        MyAsynkTask asynkTask = new MyAsynkTask();
        asynkTask.execute();

        return rootView;
    }

    class MyAsynkTask extends AsyncTask<Void, Void, StringBuilder> {
        ProgressDialog progressDialog ;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getdatastr);
            progressDialog.show();
        }
        @Override
        //работа в бекграунде
        protected StringBuilder doInBackground(Void... voids) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL url = new URL(Strings.AUTOMAT_GET);
                URLConnection uc = url.openConnection();
                uc.connect();
                BufferedInputStream in = new BufferedInputStream(uc.getInputStream());
                int ch;
                while ((ch = in.read()) != -1) {
                    stringBuilder.append((char) ch);
                }
            } catch (Exception e) {
            }
            return stringBuilder;
        }

        @Override
        protected void onPostExecute(StringBuilder stringBuilder) {

            try {

                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray array = jsonObject.getJSONArray("automates");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String locationy = object.getString("locationy");
                    String locationx = object.getString("locationx");
                    String waterlevel = object.getString("waterlevel");
                    String lastupdate = object.getString("lastupdate");
                    String usercount = object.getString("usercount");
                    String status = object.getString("status");
                    valuey = Double.parseDouble(locationy);
                    valuex = Double.parseDouble(locationx);
                    marker = map.addMarker(new MarkerOptions()
                           .position(new LatLng(valuex, valuey))
                           .title(waterlevelstr+" "+waterlevel + "/60")
                           .snippet(statusstr + " " + status));
                latLng = new LatLng(valuex, valuey);
                }
                progressDialog.dismiss();

            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        double dist = 100000;
        Location target = new Location("target");
        for(LatLng point : new LatLng[]{latLng}) {
            target.setLatitude(valuex);
            target.setLongitude(valuey);
            if(location.distanceTo(target) <= dist) {
                Toast.makeText(getActivity().getApplicationContext(),"FIND AUTOMAT",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        try {

            gps = new GPSTracker(getActivity());

            if (gps.canGetLocation) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                LatLng sydney = new LatLng(latitude, longitude);
                map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

                map.setMyLocationEnabled(true);
                map.setOnMyLocationChangeListener(this);

            } else {
                gps.showSettingsAlert();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (map != null) {

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(UKRAINE, 4));
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        getActivity().getSupportFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPlaceSelected(Place place) {

    }

    @Override
    public void onError(Status status) {

    }
}