package com.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.a3x3conect.mobile.isthreeinjava.MyOrderDetails;
import com.a3x3conect.mobile.isthreeinjava.MyOrders;
import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.Models.modelmyorders;
import com.example.hari.isthreeinjava.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hari on 22/3/18.
 */

public class TabMyOrders extends Fragment {



    View view;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    
    ProgressDialog pd;
    

    TextView status;
    String mMessage;
    List<modelmyorders> jobOrder ;
    private AdapterFish Adapter;
    RecyclerView mRVFishPrice;

    ArrayList<DataFish2> filterdata2 = new ArrayList<>();


    TinyDB tinyDB;
    public TabMyOrders() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.tabmyorders, container, false);
        tinyDB  = new TinyDB(getContext());
        mRVFishPrice = (RecyclerView)view.findViewById(R.id.fishPriceList);
        status = (TextView)view.findViewById(R.id.textView);
        status.setVisibility(View.GONE);
        getmyorders();


        return view;


    }

    private void getmyorders() {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Getting Your Orders..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("serviceName", tinyDB.getString("serviceName"));

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"customerInvoices")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                pd.cancel();
                pd.dismiss();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(getContext());
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Looks like your device is offline");
                        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                        dialogCloseButton.setVisibility(View.GONE);
                        Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                        dialogno.setText("OK");


                        dialogno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                            }
                        });
                        openDialog.setCancelable(false);
                        openDialog.show();
                    }
                });

                String mMessage = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                mMessage = response.body().string();
                if (response.isSuccessful()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(mMessage);

                                for(int j = 0; j < jsonArray.length(); j++){

                                    String ss = jsonArray.get(j).toString();

                                    JSONObject  jsonObject = new JSONObject(ss);

                                    if(jsonObject.optString("statusCode").equalsIgnoreCase("0")){

                                        status.setText("You have no Orders to display");

                                        status.setVisibility(View.VISIBLE);
                                        mRVFishPrice.setVisibility(View.GONE);

                                        pd.cancel();
                                        pd.dismiss();
                                        break;


                                    }
                                    else {

                                        TraverseData();
                                        break;
                                    }
                                }









                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            Log.e("Resy",mMessage);
                            // Toast.makeText(getContext(), mMessage, Toast.LENGTH_SHORT).show();


                        }
                    });
                }
                else  getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        });
    }

    private void TraverseData() {

        Log.e("myorders",mMessage);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<modelmyorders>>(){}.getType();
        jobOrder = (List<modelmyorders>)  gson.fromJson(mMessage,listType);



        for(int j = 0; j < jobOrder.size(); j++){

            DataFish2 dd = new DataFish2(jobOrder.get(j).getDate(),jobOrder.get(j).getDeliveryStatus(),jobOrder.get(j).getInvoiceId(),jobOrder.get(j).getServiceName());
            filterdata2.add(dd);

        }


        Adapter = new AdapterFish(getContext(), filterdata2);
        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.setHasFixedSize(false);
        mRVFishPrice.scrollToPosition(0);
        //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));





    }


    public class DataFish2 {
        public String date;
        public String status;
        public String invoicenum;
        public String serviceName;



        public DataFish2(String date,String status,String invoicenum,String serviceName){

            this.date = date;
            this.status = status;
            this.invoicenum = invoicenum;
            this.serviceName = serviceName;

        }

    }


    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<DataFish2> data2 = Collections.emptyList();

        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<DataFish2> data5) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data2 = data5;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.myordersrow, parent, false);
            final MyHolder holder = new MyHolder(view);



            return holder;
        }


        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final MyHolder myHolder = (MyHolder) holder;



            //   mRVFishPrice.scrollToPosition(position);
            //    holder.setIsRecyclable(true);

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getContext(),MyOrderDetails.class);

                    intent.putExtra("message",mMessage);
                    intent.putExtra("position",position);
                    startActivity(intent);


                }
            });
            final DataFish2 current = data2.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);



//            myHolder.servicename.setText(current.serviceName);

            if(current.serviceName != null && !current.serviceName.isEmpty()) {
                if (current.serviceName.equalsIgnoreCase("ironing")){

                    myHolder.serviceimg.setText("I");

                    myHolder.servicename.setText("Ironing");
                }
                else if (current.serviceName.equalsIgnoreCase("washAndPress")){
                    myHolder.servicename.setText("Wash and Press");
                    myHolder.serviceimg.setText("W");
                }
                else if (current.serviceName.equalsIgnoreCase("dryCleaning")){

                    myHolder.servicename.setText("Dry Cleaning");
                    myHolder.serviceimg.setText("D");
//                    myHolder.serviceimg.setColorFilter(R.color.colorAccent);
                }

            }
            else {

                myHolder.servicename.setText("Ironing");
                myHolder.serviceimg.setText("I");
            }

            if (current.status.equalsIgnoreCase("PICKUP-CONFIRMED") ||current.status.equalsIgnoreCase("JOB-FINISHED") ){
                myHolder.status.setText(current.status);
                myHolder.status.setTextColor(Color.parseColor("#006600"));


            }

            else if (current.status.equalsIgnoreCase("CUSTOMER NOT AVAILABLE")){
                myHolder.status.setText(current.status);
                myHolder.status.setTextColor(Color.parseColor("#cc0000"));


            }

            else {
                myHolder.status.setText(current.status);

            }

            myHolder.date.setText(current.date);
            pd.cancel();
            pd.dismiss();

        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data2.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView date;
            TextView status,servicename,serviceimg;
            //ImageView ;


            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                date = (TextView)itemView.findViewById(R.id.date);
                status = (TextView)itemView.findViewById(R.id.status);
                servicename = (TextView)itemView.findViewById(R.id.servicename);
                serviceimg  = (TextView) itemView.findViewById(R.id.serviceimg);
                //  id= (TextView)itemView.findViewById(R.id.id);
            }


        }



    }
}
