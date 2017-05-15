package com.inside.developed.smartlauncher;

/**
 * Created by Simon on 20.04.2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Notifications extends Fragment {

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    String getdata;
    public Notifications() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        getdata = getResources().getString(R.string.get_data);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        MyAsynkTask asynkTask = new MyAsynkTask();
        asynkTask.execute();


        return view;

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        ArrayList<Automats> arrayList;

        public MyAdapter() {

            arrayList = new ArrayList<>();

            notifyDataSetChanged();
            //  createNotification(getContext());
        }

        public void addNews(Automats news) {
            arrayList.add(news);
            notifyDataSetChanged();

        }
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new ViewHolder(itemView);
        }

        //Кидаем название, текст новости и изображение по местам.
        @Override
        public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {

            holder.locy.setText(arrayList.get(position).getLocationy());
            holder.locx.setText(arrayList.get(position).getLocationx());
            holder.waterlvl.setText(arrayList.get(position).getWaterlevel());
            holder.status.setText(arrayList.get(position).getStatus());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        //Определяем елементы
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView locy;
            TextView locx;
            TextView waterlvl;
            TextView status;


            //Определяем title, article, image;
            public ViewHolder(View itemView) {
                super(itemView);
                locy = (TextView) itemView.findViewById(R.id.locy);
                locx = (TextView) itemView.findViewById(R.id.locx);
                waterlvl = (TextView) itemView.findViewById(R.id.water_lvl);
                status = (TextView) itemView.findViewById(R.id.status);
            }
        }
    }

    class MyAsynkTask extends AsyncTask<Void, Void, StringBuilder> {
        ProgressDialog progressDialog ;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getdata);
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
//                                ListElementsArrayList.add("Location: "+ locationx.toString() + "|" + locationy.toString() +"\n"
//                                        + "Water Level: " + waterlevel.toString() +"\n"
//                                        + "Last Update: "+ lastupdate.toString() +"\n"
//                                        + "Users: " +  usercount.toString() +"\n"
//                                        + "Status: " + status.toString() );
//
//
                                Automats news = new Automats(locationy, locationx,waterlevel ,lastupdate ,usercount ,status );
                                myAdapter.addNews(news);

                                myAdapter.notifyDataSetChanged();
                            }
                 //   adapter.notifyDataSetChanged();



                }
                progressDialog.dismiss();

            } catch (Exception e) {

            }

        }
    }

}