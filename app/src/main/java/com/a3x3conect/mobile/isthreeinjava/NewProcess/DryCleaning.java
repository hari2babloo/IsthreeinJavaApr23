package com.a3x3conect.mobile.isthreeinjava.NewProcess;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.a3x3conect.mobile.isthreeinjava.OrderHead;
import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.Pickup;
import com.example.hari.isthreeinjava.R;
import com.example.hari.isthreeinjava.SchedulePickup;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DryCleaning extends AppCompatActivity {

    ImageButton pick,placeorder,myorders;
    ImageButton call;
    ProgressDialog pd;
    TinyDB tinydb;
    String mMessage;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drycleaning_dashpage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        tinydb = new TinyDB(this);
        pick = (ImageButton)findViewById(R.id.pickup);
        placeorder = (ImageButton)findViewById(R.id.placeordr);
        myorders = (ImageButton)findViewById(R.id.myorders);

        myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DryCleaning.this, OrderHead.class);
                tinydb.putString("serviceName","dryCleaning");
                startActivity(intent);
            }
        });

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Dashpage.this, "", Toast.LENGTH_SHORT).show();
                FindJobId();
//                Intent mainIntent = new Intent(Dashpage.this,Puckup.class);
//                Dashpage.this.startActivity(mainIntent);
//                Dashpage.this.finish();
            }
        });

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindJobId2();

            }
        });

    }

    private void FindJobId2() {
        pd = new ProgressDialog(DryCleaning.this);
        pd.setMessage("Getting Job Status..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinydb.getString("custid"));
            postdat.put("serviceName", "dryCleaning");

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getJobStatus")
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
                        final Dialog openDialog = new Dialog(DryCleaning.this);
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
//                                                Intent intent = new Intent(Puckup.this,DryCleaning.class);
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
                pd.cancel();
                pd.dismiss();
                mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("Resy",mMessage);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject json =  new JSONObject(mMessage);

                                String s = json.getString("statusCode");

                                if (s.equalsIgnoreCase("0")){


                                    tinydb.putString("jobid",json.getString("jobid"));
                                    tinydb.putDouble("expressDeliveryCharge",json.getDouble("expressDeliveryCharge"));
                                    final Dialog openDialog = new Dialog(DryCleaning.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Initiate Pickup");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("You haven't initiated any pickup please initiate a pickup request");
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();
                                        }
                                    });
                                    dialogCloseButton.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            openDialog.dismiss();
                                            Intent intent = new Intent(DryCleaning.this,SchedulePickup.class);
                                            tinydb.putString("serviceName","dryCleaning");
                                            startActivity(intent);
                                        }
                                    });

                                    openDialog.setCancelable(false);
                                    openDialog.show();

                                }

                                else if (s.equalsIgnoreCase("1")){
                                    tinydb.putString("jobid",json.getString("jobid"));


                                    ///  tinydb.putString("expressDelivery",json.getString("expressDelivery"));

                                    Intent intent = new Intent(DryCleaning.this, Pickup.class);
                                    tinydb.putDouble("expressDeliveryCharge",json.getDouble("expressDeliveryCharge"));
                                    tinydb.putString("serviceName","dryCleaning");
                                    intent.putExtra("expressDelivery",json.getString("expressDelivery"));
                                    startActivity(intent);


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

    private void FindJobId() {


        pd = new ProgressDialog(DryCleaning.this);
        pd.setMessage("Getting Job Status..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinydb.getString("custid"));
            postdat.put("serviceName", "dryCleaning");

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getJobStatus")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                pd.dismiss();
                pd.cancel();
                String mMessage = e.getMessage().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(DryCleaning.this);
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
//                                                Intent intent = new Intent(Puckup.this,DryCleaning.class);
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

                pd.cancel();
                pd.dismiss();

                mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("Resy",mMessage);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject json =  new JSONObject(mMessage);

                                String s = json.getString("statusCode");

                                if (s.equalsIgnoreCase("0")){


                                    tinydb.putString("jobid",json.getString("jobid"));
                                    Intent intent = new Intent(DryCleaning.this,SchedulePickup.class);
                                    tinydb.putString("serviceName","dryCleaning");
                                    startActivity(intent);
                                }

                                else if (s.equalsIgnoreCase("1")){

                                    Log.e("resfsdf",mMessage);
                                    final Dialog openDialog = new Dialog(DryCleaning.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Pickup Already Initiated");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("You have already initiated your pickup, your pickup is on the way");
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
                                    dialogno.setVisibility(View.GONE);

                                    dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();
                                        }
                                    });
//                                    dialogCloseButton.setOnClickListener(new View.OnClickListener(){
//                                        @Override
//                                        public void onClick(View v) {
//                                            // TODO Auto-generated method stub
//                                            openDialog.dismiss();
//                                            Intent intent = new Intent(DryCleaning.this,SchedulePickup.class);
//                                            startActivity(intent);
//                                        }
//                                    });

                                    openDialog.setCancelable(false);
                                    openDialog.show();

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


                    }
                });
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
