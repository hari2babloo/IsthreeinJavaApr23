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
import android.widget.Toast;

import com.a3x3conect.mobile.isthreeinjava.MyOrderDetails;
import com.a3x3conect.mobile.isthreeinjava.SummaryReport;
import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.Currentorder;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.Models.modelmyorders;
import com.example.hari.isthreeinjava.Pickup;
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

public class TabCurrentOrder extends Fragment {


    View view;

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    ProgressDialog pd;


    String mMessage,canceljobid;
    List<Currentorder> jobOrder ;
    private AdapterFish Adapter;
    RecyclerView mRVFishPrice;

    TextView status,date;
   // Button cancelpickup;

    ArrayList<DataFish2> filterdata2 = new ArrayList<>();
    ArrayList<DataFish2> filterdata3 = new ArrayList<>();


    TinyDB tinyDB;

    String jobid;


    public TabCurrentOrder() {
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

        view = inflater.inflate(R.layout.tabcurrentorders, container, false);
        tinyDB  = new TinyDB(getContext());
        //tinyDB  = new TinyDB(getContext());
        mRVFishPrice = (RecyclerView)view.findViewById(R.id.fishPriceList);

        status = (TextView)view.findViewById(R.id.textView);
        status.setVisibility(View.GONE);
     //   getmyorders();
      //  date = (TextView)view.findViewById(R.id.date);
//        status = (TextView)view.findViewById(R.id.status);
//        cancelpickup = (Button)view.findViewById(R.id.cancelorder);
//
//
//        cancelpickup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog openDialog = new Dialog(getContext());
//                openDialog.setContentView(R.layout.alert);
//                openDialog.setTitle("Cancel Order");
//                TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
//                dialogTextContent.setText("Do you really want to cancel pickup?");
//                ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
//                Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
//
//                dialogCloseButton.setText("Yes I want to");
//
//                Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
//
//                dialogno.setText("NO");
//
//
//                dialogCloseButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        openDialog.dismiss();
//                        Cancelschedule();
//                    }
//                });
//                dialogno.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        openDialog.dismiss();
//
//
//
////                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
////                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
////                                                startActivity(intent);
//                    }
//                });
//
//
//
//                openDialog.show();
//            }
//        });
        getmyorders();


        return view;


    }

    private void Cancelschedule() {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Cancel Order");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", canceljobid);
//                        postdat.put("customerId", "C0016");
//            postdat.put("jobId", "2000309051221");
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("Postdata2",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"cancelJobOrder")
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

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
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


                pd.cancel();
                pd.dismiss();
                final String mMessage = response.body().string();
                if (response.isSuccessful()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tinyDB.remove("expressDelivery");

                            Log.e("cancel",mMessage);

                            try {
                                JSONObject jsonObject = new JSONObject(mMessage);

                                if(jsonObject.getString("statusCode").equalsIgnoreCase("1")){


                                    final Dialog openDialog = new Dialog(getContext());
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Status");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(jsonObject.getString("status"));
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                                    dialogno.setText("OK");


                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getContext(),Dashpage.class);
                                            startActivity(intent);
                                        }
                                    });


                                    openDialog.setCancelable(false);

                                    openDialog.show();
                                }

                                else {

                                    final Dialog openDialog = new Dialog(getContext());
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Status");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(jsonObject.getString("status"));
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                                    dialogno.setText("OK");


                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
//                                                startActivity(intent);
                                        }
                                    });



                                    openDialog.setCancelable(false);
                                    openDialog.show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();


                        }
                    });
                }
                else getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        });
    }

    private void getmyorders() {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Getting Your Current Order..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinyDB.getString("custid"));

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getCustomerPendingRequests")
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


               // String mMessage = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {



                mMessage = response.body().string();

                Log.e("mMessage",mMessage);


                if (response.isSuccessful()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                JSONArray jsonArray = new JSONArray(mMessage);

                                for(int K = 0; K < jsonArray.length(); K++){

                                    String ss = jsonArray.get(K).toString();

                                    JSONObject  jsonObject = new JSONObject(ss);

                                    if (jsonObject.optString("statusCode").equalsIgnoreCase("0")){

                                        status.setText("You have no Orders to display");

                                        status.setVisibility(View.VISIBLE);
                                        mRVFishPrice.setVisibility(View.GONE);
                                        pd.cancel();
                                        pd.dismiss();
                                        break;

                                    }

                                    else {

                                        Traversedata();
                                        break;
                                    }
//
//                                        jobid = jsonObject.getString("jobid");
//                                        status.setText("You have requested pickup on  " +jsonObject.getString("pickupScheduledAt")+".\n Our pickup agent will reach you shortly." );
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

    private void Traversedata() {



        Gson gson = new Gson();
        Type listType = new TypeToken<List<Currentorder>>(){}.getType();
        jobOrder = (List<Currentorder>)  gson.fromJson(mMessage,listType);


        for(int j = 0; j < jobOrder.size(); j++){

            DataFish2 dd = new DataFish2(jobOrder.get(j).getPickupScheduledAt(),jobOrder.get(j).getStatus(),jobOrder.get(j).getInvoiceId(),jobOrder.get(j).getExpressDelivery());
            filterdata2.add(dd);

        }


        //   Collections.reverse(filterdata2);
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
        public String expressdelivery;



        public DataFish2(String date,String status,String invoicenum,String expressdelivery){

            this.date = date;
            this.status = status;
            this.invoicenum = invoicenum;
            this.expressdelivery = expressdelivery;

        }

    }


    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<DataFish2> data2 = Collections.emptyList();

        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, ArrayList<DataFish2> data5) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data2 = data5;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.currentorderrow, parent, false);
            final AdapterFish.MyHolder holder = new AdapterFish.MyHolder(view);
            return holder;
        }


        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final AdapterFish.MyHolder myHolder = (AdapterFish.MyHolder) holder;

            final DataFish2 current = data2.get(position);



            //   mRVFishPrice.scrollToPosition(position);
            //    holder.setIsRecyclable(true);

         //   Collections.reverse(jobOrder);
            myHolder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    canceljobid = jobOrder.get(position).getJobid();

//                    Toast.makeText(context,jobOrder.get(position).getJobid(), Toast.LENGTH_SHORT).show();
                    final Dialog openDialog = new Dialog(getContext());
                    openDialog.setContentView(R.layout.alert);
                    openDialog.setTitle("Cancel Order");
                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                    dialogTextContent.setText("Do you really want to cancel order?");
                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);

                    dialogCloseButton.setText("Yes, I want to");

                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                    dialogno.setText("NO");


                    dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDialog.dismiss();
                            Cancelschedule();
                        }
                    });
                    dialogno.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialog.dismiss();

//                                                //                                          Toast.makeText(ExistingData.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(ExistingData.this,Dashpage.class);
//                                                startActivity(intent);
                        }
                    });


                    openDialog.setCancelable(false);

                    openDialog.show();

                }
            });




            myHolder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    tinyDB.putString("jobid",jobOrder.get(position).getJobid());
                    tinyDB.putString("data",mMessage);
                    Intent intent = new Intent(getContext(), CurrentOrderDetails.class);
                    startActivity(intent);

                }
            });

            myHolder.pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tinyDB.putString("jobid",jobOrder.get(position).getJobid());
                    tinyDB.putString("data",mMessage);
                    Intent intent = new Intent(getContext(), Paypage.class);
                    startActivity(intent);
                }
            });

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//
//                    Intent intent = new Intent(getContext(),MyOrderDetails.class);
//
//                    intent.putExtra("message",mMessage);
//                    intent.putExtra("position",position);
//                    startActivity(intent);
                }
            });

          //  myHolder.status.setText(current.status);

            if (current.expressdelivery.equalsIgnoreCase("1")){

                myHolder.pickuptypemsg.setText("EXPRESS DELIVERY");
               // myHolder.pickuptypemsg.setTextColor(Color.parseColor("#FF3232"));
            }
            else {

                myHolder.pickuptypemsg.setText("NORMAL");
            }
            myHolder.date.setText(current.date);
            if (current.status.equalsIgnoreCase("PICKUP-PROCESSED")){
              //  myHolder.status.setText(current.status);
               // myHolder.status.setTextColor(Color.parseColor("#006600"));
                myHolder.pay.setVisibility(View.VISIBLE);
                myHolder.cancel.setVisibility(View.GONE);
                myHolder.details.setVisibility(View.GONE);
            }
            else
            if (current.status.equalsIgnoreCase("PICKUP-REQUESTED")||current.status.equalsIgnoreCase("PICKUP-INITIATED")||current.status.equalsIgnoreCase("CUSTOMER NOT AT HOME")){
               // myHolder.status.setText(current.status);
               // myHolder.status.setTextColor(Color.parseColor("#d20670"));
                myHolder.pay.setVisibility(View.GONE);
                myHolder.cancel.setVisibility(View.VISIBLE);
                myHolder.details.setVisibility(View.GONE);
            }
            else {


                myHolder.status.setText(current.status);
                //myHolder.status.setTextColor(Color.parseColor("#006600"));
                myHolder.pay.setVisibility(View.GONE);
                myHolder.cancel.setVisibility(View.GONE);
                myHolder.details.setVisibility(View.VISIBLE);

            }


            switch (current.status){


                case "PICKUP-INITIATED":
                    myHolder.status.setTextColor(Color.parseColor("#d20670"));
                    myHolder.status.setText("PICKUP-SCHEDULED");
                   // myHolder.misdate.setText("Initiated on: "+ current.getPickupScheduledAt());
                    break;
                case "PICKUP-REQUESTED":
                    myHolder.status.setTextColor(Color.parseColor("#d20670"));
                    myHolder.status.setText("PICKUP-SCHEDULED");
                   // myHolder.misdate.setText("Requested on: "+ current.getPickupScheduledAt());
                    break;
                case "PICKUP-CONFIRMED":
                    myHolder.status.setTextColor(Color.parseColor("#008000"));
                    myHolder.status.setText("PICKED-UP");
                   // myHolder.misdate.setText("Confirmed on: " +current.getPickupConfirmedDate());
                    break;
                case "PICKUP-CANCELLED":
                    myHolder.status.setTextColor(Color.parseColor("#cc0000"));
                  //  myHolder.misdate.setText("Cancelled on: "+current.getPickupCancelledDate());
                    break;

                case "PICKUP-INPROCESS":
                    myHolder.status.setTextColor(Color.parseColor("#008000"));
                    myHolder.status.setText("IN-PROCESS");
                   // myHolder.misdate.setText("Processed on: " +current.getOrderProcessedDate());


                    break;
                case "PICKUP-PROCESSED":
                    myHolder.status.setTextColor(Color.parseColor("#008000"));
                    myHolder.status.setText("OUT FOR DELIVERY");
                   // myHolder.misdate.setText("Processed on: " +current.getOrderProcessedDate());


                    break;
                case  "JOB-FINISHED":
                    myHolder.status.setTextColor(Color.parseColor("#008000"));
                    myHolder.status.setText("DELIVERED");
                   // myHolder.misdate.setText("Job Finished on: "+current.getJobFinishedDate());
                    break;
                case  "Address does not exist":
                case  "Phone number not reachable":
                case  "Customer does not exist":
                case  "Customer not at home":
                case  "Issue not listed":
                    myHolder.status.setTextColor(Color.parseColor("#cc0000"));
                  //  myHolder.misdate.setText("Cancelled on: "+current.getPickupCancelledDate());
                    break;
            }


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
            TextView status,pickuptypemsg;
            Button pay;
            Button cancel;
            Button details;


            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                date = (TextView)itemView.findViewById(R.id.date);
                status = (TextView)itemView.findViewById(R.id.status);
                pay = (Button)itemView.findViewById(R.id.pay);
                cancel = (Button)itemView.findViewById(R.id.cancel);
                details = (Button)itemView.findViewById(R.id.details);
                pickuptypemsg = (TextView)itemView.findViewById(R.id.pickuptypemsg);

                //  id= (TextView)itemView.findViewById(R.id.id);
            }


        }



    }
}
