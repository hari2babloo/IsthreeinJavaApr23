package com.a3x3conect.mobile.isthreeinjava;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ViewPager.Offerstab;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.R;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Notifications extends AppCompatActivity {

    TextView nonotificationtxt,cleartxt;
    ProgressDialog pd;
    RecyclerView mRVFishPrice;
    TinyDB tinyDB;
    String mMessage;
    private AdapterFish Adapter;
    ArrayList<DataFish2> filterdata2 = new ArrayList<>();
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        tinyDB = new TinyDB(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nonotificationtxt = (TextView)findViewById(R.id.nonotificationtxt);
        cleartxt = (TextView)findViewById(R.id.clear);

        nonotificationtxt.setVisibility(View.GONE);
        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList) ;


        cleartxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                clearnotification();
            }
        });


        getnotifications();
    }

    private void clearnotification() {


        pd = new ProgressDialog(this);
        pd.setMessage("Clearing Notifications list..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
//            postdat.put("customerEmail", tinyDB.getString("custEmail"));
            postdat.put("userId", tinyDB.getString("custid"));

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("offerspost",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"clearUserNotifications")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                pd.cancel();
                pd.dismiss();
                String mMessage = e.getMessage().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(Notifications.this);
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
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //pd.cancel();
                // pd.dismiss();
                mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("Offersmsg",mMessage);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            try {

//                                JSONArray jsonArray = new JSONArray(mMessage);
                                JSONObject json =  new JSONObject(mMessage);

                                String s = json.getString("statusCode");


                                if (s.equalsIgnoreCase("0")){

                                    String msg = json.getString("statusMessage");
                                    nonotificationtxt.setText(msg);
                                    nonotificationtxt.setVisibility(View.VISIBLE);
                                    cleartxt.setVisibility(View.GONE);
                                    mRVFishPrice.setVisibility(View.GONE);

                                    pd.cancel();
                                    pd.dismiss();



                                }

                                else if (s.equalsIgnoreCase("1")){

                                    String msg = json.getString("statusMessage");
                                    nonotificationtxt.setText(msg);
                                    nonotificationtxt.setVisibility(View.VISIBLE);
                                    cleartxt.setVisibility(View.GONE);
                                    mRVFishPrice.setVisibility(View.GONE);

                                    pd.cancel();
                                    pd.dismiss();
                                   // TraverseData();



                                }
                                Log.e("s",s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.cancel();
                        pd.dismiss();


                    }
                });
            }
        });

    }

    private void getnotifications() {


        pd = new ProgressDialog(this);
        pd.setMessage("Getting Notifications list..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
//            postdat.put("customerEmail", tinyDB.getString("custEmail"));
            postdat.put("userId", tinyDB.getString("custid"));

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("offerspost",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getUserNotifications")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                pd.cancel();
                pd.dismiss();
                String mMessage = e.getMessage().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(Notifications.this);
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
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //pd.cancel();
                // pd.dismiss();
                mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("Offersmsg",mMessage);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            try {

//                                JSONArray jsonArray = new JSONArray(mMessage);
                                JSONObject json =  new JSONObject(mMessage);

                                String s = json.getString("statusCode");


                                if (s.equalsIgnoreCase("0")){

                                    String msg = json.getString("statusMessage");
                                    nonotificationtxt.setText(msg);
                                    nonotificationtxt.setVisibility(View.VISIBLE);
                                    cleartxt.setVisibility(View.GONE);
                                    mRVFishPrice.setVisibility(View.GONE);
                                    pd.cancel();
                                    pd.dismiss();

                                }

                                else if (s.equalsIgnoreCase("1")){


                                    TraverseData();



                                }
                                Log.e("s",s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.cancel();
                        pd.dismiss();


                    }
                });
            }
        });


    }

    private void TraverseData() {


        try {
            JSONObject jsonObject = new JSONObject(mMessage);

            JSONArray jsonArray = jsonObject.getJSONArray("userNotifications");

            JSONObject ss2 = null;
            for (int j = 0; j < jsonArray.length(); j++) {

                ss2 = jsonArray.getJSONObject(j);

                DataFish2 ss = new DataFish2(ss2.getString("notification"), ss2.getString("notificationType"), ss2.getString("notifiedDate"));
                filterdata2.add(ss);
            }
            Log.e("jsonarray", String.valueOf(ss2));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Gson gson = new Gson();
//        Type listType = new TypeToken<List<modelmyorders>>(){}.getType();
//        jobOrder = (List<modelmyorders>)  gson.fromJson(mMessage,listType);
//
//
//
//        for(int j = 0; j < jobOrder.size(); j++){
//
//            DataFish2 dd = new DataFish2(jobOrder.get(j).getDate(),jobOrder.get(j).getDeliveryStatus(),jobOrder.get(j).getInvoiceId());
//            filterdata2.add(dd);
//
//        }


        Adapter = new AdapterFish(Notifications.this, filterdata2);
        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.setHasFixedSize(false);
        mRVFishPrice.scrollToPosition(0);
        //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(Notifications.this,LinearLayoutManager.VERTICAL,false));





    }


    public class DataFish2 {

        public String notification;
        public String notificationType;
        public String notifiedDate;



        public DataFish2(String notification,String notificationType,String notifiedDate){

            this.notification = notification;
            this.notificationType = notificationType;
            this.notifiedDate = notifiedDate;


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
            View view = inflater.inflate(R.layout.notification_item, parent, false);
            final AdapterFish.MyHolder holder = new AdapterFish.MyHolder(view);



            return holder;
        }


        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final AdapterFish.MyHolder myHolder = (AdapterFish.MyHolder) holder;



            //   mRVFishPrice.scrollToPosition(position);
            //    holder.setIsRecyclable(true);

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Toast.makeText(context, "Cl", Toast.LENGTH_SHORT).show();


                }
            });
            final DataFish2 current = data2.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);

            myHolder.msg.setText(current.notification);
//            myHolder.heading.setText(current.notificationType);

            if (current.notificationType.equalsIgnoreCase("orderStatus")){

                myHolder.heading.setText("ORDER STATUS");


            }

            else           if (current.notificationType.equalsIgnoreCase("signup")){

                myHolder.heading.setText("SIGNUP SUCCESSFUL");


            }

            else           if (current.notificationType.equalsIgnoreCase("wallet")){

                myHolder.heading.setText("WALLET");


            }

            else if (TextUtils.isDigitsOnly(current.notificationType)){

                myHolder.heading.setText("FEEDBACK");

            }

            myHolder.date.setText(current.notifiedDate);

            // myHolder.date.setText(current.date);
            pd.cancel();
            pd.dismiss();

        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data2.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {

            TextView msg,heading,date;




            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);

                msg = (TextView)itemView.findViewById(R.id.msg);
                heading = (TextView)itemView.findViewById(R.id.heading);
                date = (TextView)itemView.findViewById(R.id.date);



                //  id= (TextView)itemView.findViewById(R.id.id);
            }


        }



    }
}
