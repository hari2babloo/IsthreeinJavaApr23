package com.a3x3conect.mobile.isthreeinjava;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.JobOrder;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.R;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SummaryReport extends AppCompatActivity {


    ProgressDialog pd;
    RecyclerView mRVFishPrice;
    TableLayout tableLayout;
    TextView btmtotal,grdtotal,expresmsg;
    List<DataFish2> filterdata2=new ArrayList<DataFish2>();
    private AdapterFish Adapter;
    Button home;
    double s,expressDeliveryCharge;
    TinyDB tinyDB;
    String mMessage2;

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_report);
        tinyDB = new TinyDB(SummaryReport.this);
        home = findViewById(R.id.home);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        expressDeliveryCharge =getIntent().getDoubleExtra("expressDeliveryCharge",0);
        Log.e("expressdeliverycharge", String.valueOf(expressDeliveryCharge));
                home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SummaryReport.this,Dashpage.class);

                startActivity(intent);
            }
        });
        mRVFishPrice = findViewById(R.id.fishPriceList);
        tableLayout = findViewById(R.id.tabl);
        btmtotal = findViewById(R.id.btmtotal);
        grdtotal = findViewById(R.id.grdtotal);
        expresmsg = findViewById(R.id.expresmsg);

        if (expressDeliveryCharge>0){

            expresmsg.setText("Express Delivery Charges of " +getResources().getString(R.string.rupee)+" "+expressDeliveryCharge + " applied.");

        }

        else {

            expresmsg.setVisibility(View.GONE);
        }

     //   filterdata2 =   (ArrayList<DataFish2>)getIntent().getSerializableExtra("FILES_TO_SEND");
        
       getjoborder();
       
    }

    private void getjoborder() {

        pd = new ProgressDialog(SummaryReport.this);
        pd.setMessage("Getting Job Orders..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", tinyDB.getString("jobid"));
            postdat.put("serviceName",tinyDB.getString("serviceName"));
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

Log.e("fsdfsd", String.valueOf(postdat));
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getJobOrder")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                pd.cancel();
                pd.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(SummaryReport.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Looks like your device is offline");
                        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                        dialogCloseButton.setVisibility(View.GONE);
                        Button dialogno = openDialog.findViewById(R.id.cancel);
                        dialogno.setText("OK");
                        dialogno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();

//                                                //                                          Toast.makeText(SummaryReport.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(SummaryReport.this,Dashpage.class);
//                                                startActivity(intent);
                            }
                        });
                        openDialog.show();
                        openDialog.setCancelable(false);
                    }
                });


                mMessage2 = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {


                pd.cancel();
                pd.dismiss();
                mMessage2 = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Log.e("Resy",mMessage2);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            //   TraverseData();

                            try {
                                JSONObject jsonObject = new JSONObject(mMessage2);

                                Double statuscode = jsonObject.optDouble("statusCode");
                                Double jobid = jsonObject.optDouble("jobid");

                                Log.e("jobid,", String.valueOf(statuscode));

                                if (jsonObject.optString("statusCode").equalsIgnoreCase("0")){

                                    final Dialog openDialog = new Dialog(SummaryReport.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("No Data");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("looks like your form is empty");
                                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = openDialog.findViewById(R.id.cancel);
                                    dialogno.setText("OK");
                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

//                                                //                                          Toast.makeText(SummaryReport.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(SummaryReport.this,Dashpage.class);
//                                                startActivity(intent);
                                        }
                                    });

                                    openDialog.setCancelable(false);
                                    openDialog.show();



                                }



                                else {
                                    Gson gson = new Gson();

                                    JobOrder jobOrder = gson.fromJson(mMessage2,JobOrder.class);


//                                String s = jobOrder.getCustomerId();

//                                filterdata2.add(jobOrder);

                                    for (int i= 0; i<jobOrder.getCategory().size(); i++){


                                        DataFish2 ss = new DataFish2("item","qty","price","total");

                                        Float ss2 = Float.parseFloat(jobOrder.getPrice().get(i));

                                        Float ss3 =  Float.parseFloat(jobOrder.getQuantity().get(i));




                                        Float ss4 = ss2 * ss3;
                                        DataFish2 sds = new DataFish2(jobOrder.getCategory().get(i),jobOrder.getQuantity().get(i),jobOrder.getPrice().get(i),String.valueOf(ss4));


                                        filterdata2.add(sds);
                                    }

                                    float sum = 0;
                                    float garmentscount = 0;
                                    for (int i=0;i<filterdata2.size();i++){


                                        float foo = Float.parseFloat(filterdata2.get(i).noofpieces);
                                        float foo3 = Float.parseFloat(filterdata2.get(i).amt);



                                        sum+=foo3;



                                        garmentscount+= foo;


                                        //   quantity.put(filterdata2.get(i).noofpieces);

                                        btmtotal.setText(String.valueOf(Math.round(garmentscount)));
                                        s =  ((0.0/100) *sum)+sum;


                                        grdtotal.setText("Total  " +getResources().getString(R.string.rupee)+String.format("%.2f",s+expressDeliveryCharge));


                                    }
                                }

                                Adapter = new SummaryReport.AdapterFish(SummaryReport.this, filterdata2);
                                Adapter.setHasStableIds(false);
                                mRVFishPrice.setAdapter(Adapter);
                                mRVFishPrice.setHasFixedSize(false);
                                //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
                                //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
                                mRVFishPrice.setLayoutManager(new LinearLayoutManager(SummaryReport.this,LinearLayoutManager.VERTICAL,true));



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        });
    }

    
    public class DataFish2 {
        public String item;
        public String noofpieces;
        public String cost;
        public String amt;


        public DataFish2(String item,String noofpieces,String cost,String amt){

            this.item = item;
            this.noofpieces = noofpieces;
            this.cost = cost;
            this.amt = amt;
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
            View view = inflater.inflate(R.layout.rowform, parent, false);
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
            final DataFish2 current = data2.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);



            myHolder.item.setText(current.item);
            myHolder.noofpices.setText(current.noofpieces);
            myHolder.cost.setText(current.cost);
            myHolder.amount.setText(current.amt);
            myHolder.plus.setVisibility(View.GONE);
//            myHolder.minus.setVisibility(View.GONE);
            myHolder.delete.setVisibility(View.GONE);




        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data2.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView item;
            TextView noofpices;
            TextView cost;
            TextView amount;
            Button plus;
            ImageButton minus;
            ImageButton delete;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                item = itemView.findViewById(R.id.item);
                noofpices = itemView.findViewById(R.id.noofpices);
                cost = itemView.findViewById(R.id.cost);
                amount = itemView.findViewById(R.id.total);
                plus = itemView.findViewById(R.id.plus);
                minus = itemView.findViewById(R.id.minus);
                delete = itemView.findViewById(R.id.del);

                //  id= (TextView)itemView.findViewById(R.id.id);
            }


        }



    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(SummaryReport.this,Dashpage.class);
        startActivity(intent);
    }
}
