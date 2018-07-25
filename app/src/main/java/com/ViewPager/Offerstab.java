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
import com.a3x3conect.mobile.isthreeinjava.MyOrders;
import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.Currentorder;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.Models.modelmyorders;
import com.example.hari.isthreeinjava.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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


public class Offerstab extends Fragment {

    View view;

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    ProgressDialog pd;
    String mMessage,canceljobid;
   // List<Currentorder> jobOrder ;
    RecyclerView mRVFishPrice;
    TextView bal;
    TinyDB tinyDB;
    TextView status;
    private AdapterFish Adapter;
    ArrayList<DataFish2> filterdata2 = new ArrayList<>();

   public Offerstab(){


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

        view = inflater.inflate(R.layout.offerstab, container, false);
        status = (TextView)view.findViewById(R.id.voucherstatustxt);
        status.setVisibility(View.GONE);
        mRVFishPrice = (RecyclerView)view.findViewById(R.id.fishPriceList) ;

        tinyDB  = new TinyDB(getContext());
       // bal = (TextView)view.findViewById(R.id.bal);
        getvouchers();
        return view;


    }
    private void getvouchers() {


        pd = new ProgressDialog(getContext());
        pd.setMessage("Getting available offers..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerEmail", tinyDB.getString("custEmail"));
            postdat.put("customerId", tinyDB.getString("custid"));

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("offerspost",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"offers")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                pd.cancel();
                pd.dismiss();
                String mMessage = e.getMessage().toString();
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
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //pd.cancel();
               // pd.dismiss();
                mMessage = response.body().string();
                if (response.isSuccessful()){
                    getActivity().runOnUiThread(new Runnable() {
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
                                    status.setText(msg);
                                    status.setVisibility(View.VISIBLE);
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
                else getActivity().runOnUiThread(new Runnable() {
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

            JSONArray jsonArray = jsonObject.getJSONArray("offers");

            for (int j=0;j<jsonArray.length();j++){

                DataFish2 ss = new DataFish2(jsonArray.getString(j));
                filterdata2.add(ss);
            }
            Log.e("jsonarray",jsonArray.toString());
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

        public String msg;



        public DataFish2(String msg){

            this.msg = msg;


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
            View view = inflater.inflate(R.layout.offerslistrow, parent, false);
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

            myHolder.vmesage.setText(current.msg);

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

            TextView vmesage;




            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);

                vmesage = (TextView)itemView.findViewById(R.id.msg);



                //  id= (TextView)itemView.findViewById(R.id.id);
            }


        }



    }
}
