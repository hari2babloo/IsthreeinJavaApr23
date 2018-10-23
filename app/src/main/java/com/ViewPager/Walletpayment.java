package com.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.TinyDB;

import com.example.hari.isthreeinjava.R;
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

public class Walletpayment extends AppCompatActivity {

    TextView walletbalancetxt,payamounttxt;
    ProgressDialog pd;
    TinyDB tinydb;

    String mMessage,payamount,paymentmode;

    double dwall,damt;
    Button pay;

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walletpayment);
        tinydb = new TinyDB(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        payamount = tinydb.getString("payamount");
        damt = Double.parseDouble(payamount);

        
        walletbalancetxt = (TextView)findViewById(R.id.walletbalancetxt);
        payamounttxt = (TextView)findViewById(R.id.paymentbalancetxt);
        pay = (Button)findViewById(R.id.pay);
        payamounttxt.setText("Pay amount: "+getResources().getString(R.string.rupee)+payamount);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calculations();

            }
        });
        getwalletbalance();


        
    }

    private void getwalletbalance() {


        pd = new ProgressDialog(Walletpayment.this);
        pd.setMessage("Getting your wallet balance..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("userId", tinydb.getString("custid"));

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"checkWallet")
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
                        final Dialog openDialog = new Dialog(Walletpayment.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Looks like your device is offline");
                        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                        Button ok = (Button)openDialog.findViewById(R.id.dialog_button);
                        ok.setVisibility(View.GONE);
                        Button cancel = (Button)openDialog.findViewById(R.id.cancel);

                        cancel.setText("OK");


                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Walletpayment.class);
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

                            Log.e("com/wallet",mMessage);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject json =  new JSONObject(mMessage);

                                String s = json.getString("statusCode");

                                if (s.equalsIgnoreCase("0")){

                                    walletbalancetxt.setText( "Wallet Balance: "+R.string.rupee+"0.00");
                                }

                                else if (s.equalsIgnoreCase("1")){

                                    walletbalancetxt.setText( "Wallet Balance: "+getResources().getString(R.string.rupee)+ json.getString("availableFunds"));


                                    dwall = Double.parseDouble(json.getString("availableFunds"));



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




    private void calculations() {

        if (dwall>damt){


            final Dialog openDialog = new Dialog(Walletpayment.this);
            openDialog.setContentView(R.layout.alert);
            openDialog.setTitle("Payments");
            TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
            dialogTextContent.setText("Amount of "+getResources().getString(R.string.rupee)+payamount+ " will be deducted from your balance");
            ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
            Button ok = (Button)openDialog.findViewById(R.id.dialog_button);
            //ok.setVisibility(View.GONE);
            Button cancel = (Button)openDialog.findViewById(R.id.cancel);

            cancel.setText("CANCEL");
            ok.setText("OK");
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    paymentmode = "Wallet Transfer";
                    proceedtopayment();
                    openDialog.dismiss();

                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Walletpayment.class);
//                                                startActivity(intent);
                }
            });


            openDialog.setCancelable(false);

            openDialog.show();

        }

        else {


            final Dialog openDialog = new Dialog(Walletpayment.this);
            openDialog.setContentView(R.layout.alert);
            openDialog.setTitle("Payments");
            TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
            dialogTextContent.setText("You have insufficent funds.  \n\n Press OK if you want to deduct amount from your wallet balance \n\n Press CANCEL  if you like to pay full amount in cash?");
            ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
            Button ok = (Button)openDialog.findViewById(R.id.dialog_button);
           // ok.setVisibility(View.GONE);
            Button cancel = (Button)openDialog.findViewById(R.id.cancel);

            cancel.setText("CANCEL");
            ok.setText("OK");
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    paymentmode = "Wallet Transfer";
                    proceedtopayment();

                    openDialog.dismiss();

                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog.dismiss();
                    paymentmode = "Cash on Delivery";
                    proceedtopayment();


//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Walletpayment.class);
//                                                startActivity(intent);
                }
            });



            openDialog.setCancelable(false);
            openDialog.show();

        }



        }

    private void proceedtopayment() {



            pd = new ProgressDialog(Walletpayment.this);
            pd.setMessage("Getting your wallet balance..");
            pd.setCancelable(false);
            pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
            JSONObject postdat = new JSONObject();

            try {
                postdat.put("customerId", tinydb.getString("custid"));
                postdat.put("jobId", tinydb.getString("jobid"));
                postdat.put("paymentMode", paymentmode);
                postdat.put("amountPayable", damt);


            } catch(JSONException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

            Log.e("paymentset",postdat.toString());
            final Request request = new Request.Builder()
                    .url(getString(R.string.baseurl)+"payment")
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
                            final Dialog openDialog = new Dialog(Walletpayment.this);
                            openDialog.setContentView(R.layout.alert);
                            openDialog.setTitle("No Internet");
                            TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                            dialogTextContent.setText("Looks like your device is offline");
                            ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                            Button ok = (Button)openDialog.findViewById(R.id.dialog_button);
                          //  ok.setVisibility(View.GONE);
                            Button cancel = (Button)openDialog.findViewById(R.id.cancel);

                            cancel.setText("OK");


                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Walletpayment.class);
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
                    Log.e("com/wallet",mMessage);
                    if (response.isSuccessful()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.e("com/wallet",mMessage);
//                                 Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject json =  new JSONObject(mMessage);

                                    String s = json.getString("statusCode");
                                    double bal = json.getDouble("balanceAmountToPay");

                                    if (s.equalsIgnoreCase("0")){


                                        final Dialog openDialog = new Dialog(Walletpayment.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("Payments");
                                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                        dialogTextContent.setText("Transaction Failed.\n Please try again.");
                                        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                        Button ok = (Button)openDialog.findViewById(R.id.dialog_button);
                                        //ok.setVisibility(View.GONE);
                                        Button cancel = (Button)openDialog.findViewById(R.id.cancel);
                                        cancel.setVisibility(View.GONE);

                                        ok.setText("OK");
                                        ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                openDialog.dismiss();

                                                Intent intent = new Intent(Walletpayment.this, Dashpage.class);
                                                startActivity(intent);

                                            }
                                        });



                                        openDialog.setCancelable(false);
                                        openDialog.show();

//                                        walletbalancetxt.setText( "Wallet Balance: "+R.string.rupee+"0.00");
                                    }

                                    else if (s.equalsIgnoreCase("1")){

                                        if (bal==0){

                                            final Dialog openDialog = new Dialog(Walletpayment.this);
                                            openDialog.setContentView(R.layout.alert);
                                            openDialog.setTitle("Payments");
                                            TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                                  dialogTextContent.setText("Transaction Succesful.\n Thanks for chosing our services. \n\n ISTHREE");
                                            ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                            Button ok = (Button)openDialog.findViewById(R.id.dialog_button);
                                            //ok.setVisibility(View.GONE);
                                            Button cancel = (Button)openDialog.findViewById(R.id.cancel);
                                                cancel.setVisibility(View.GONE);

                                            ok.setText("OK");
                                            ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    openDialog.dismiss();

                                                    Intent intent = new Intent(Walletpayment.this, Dashpage.class);
                                                    startActivity(intent);

                                                }
                                            });


                                            openDialog.setCancelable(false);

                                            openDialog.show();

                                        }
                                        else {

                                            final Dialog openDialog = new Dialog(Walletpayment.this);
                                            openDialog.setContentView(R.layout.alert);
                                            openDialog.setTitle("Payments");
                                            TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);

                                            dialogTextContent.setText("Transaction Pending, Please pay balance amount of "+getResources().getString(R.string.rupee)+ String.valueOf(bal)+" to the delivery agent");
                                            ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                            Button ok = (Button)openDialog.findViewById(R.id.dialog_button);
                                           // ok.setVisibility(View.GONE);
                                            Button cancel = (Button)openDialog.findViewById(R.id.cancel);

                                            cancel.setVisibility(View.GONE);

                                            ok.setText("OK");
                                            ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    openDialog.dismiss();

                                                    Intent intent = new Intent(Walletpayment.this, Dashpage.class);
                                                    startActivity(intent);

                                                }
                                            });


                                            openDialog.setCancelable(false);

                                            openDialog.show();


                                        }



//                                        walletbalancetxt.setText( "Wallet Balance: "+getResources().getString(R.string.rupee)+ json.getString("availableFunds"));


//                                        dwall = Double.parseDouble(json.getString("availableFunds"));



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

