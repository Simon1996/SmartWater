package com.inside.developed.smartlauncher;

/**
 * Created by Simon on 20.04.2017.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Notifications extends Fragment {


    ListView listview;
    String[] ListElements = new String[] {};
    List<String> ListElementsArrayList;
    ArrayAdapter<String> adapter;
    public Notifications() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        MyAsynkTask asynkTask = new MyAsynkTask();
        asynkTask.execute();
        listview = (ListView) view.findViewById(R.id.listView1);

        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));


        adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, ListElementsArrayList);

        listview.setAdapter(adapter);

        return view;

    }

    class MyAsynkTask extends AsyncTask<Void, Void, StringBuilder> {
        ProgressDialog progressDialog ;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Get data, wait please....");
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
                            if(status.contains("bad")) {
                                ListElementsArrayList.add("Location: "+ locationx.toString() + "|" + locationy.toString() +"\n"
                                        + "Water Level: " + waterlevel.toString() +"\n"
                                        + "Last Update: "+ lastupdate.toString() +"\n"
                                        + "Users: " +  usercount.toString() +"\n"
                                        + "Status: " + status.toString() );


                            }
                    adapter.notifyDataSetChanged();

                }
                progressDialog.dismiss();

            } catch (Exception e) {

            }

        }
    }



}