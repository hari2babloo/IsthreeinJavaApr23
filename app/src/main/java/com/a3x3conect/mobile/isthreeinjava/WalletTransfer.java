package com.a3x3conect.mobile.isthreeinjava;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.R;
import com.rengwuxian.materialedittext.MaterialEditText;
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
import java.util.concurrent.TimeUnit;

public class WalletTransfer extends AppCompatActivity {
    
    Button validate,send;
    MaterialEditText userdata,amount;
    ProgressDialog pd;
    String mMessage,beneficiaryId;
    TinyDB tinyDB;
    TextView bal;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_transfer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tinyDB = new TinyDB(this);
        validate = (Button)findViewById(R.id.validate);
        send = (Button)findViewById(R.id.send);
        userdata = (MaterialEditText) findViewById(R.id.userdata);
        amount=(MaterialEditText) findViewById(R.id.amount);
        send.setVisibility(View.GONE);
        amount.setVisibility(View.GONE);
        bal  = (TextView)findViewById(R.id.bal);
        bal.setText(getResources().getString(R.string.rupee)+tinyDB.getString("walletbal"));
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (tinyDB.getString("custid").equalsIgnoreCase(userdata.getText().toString())){


                    final Dialog openDialog = new Dialog(WalletTransfer.this);
                    openDialog.setContentView(R.layout.alert);
                    openDialog.setTitle("wrong details");
                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                    dialogTextContent.setText("Please enter correct details");
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

                }else {

                    Validate();

                }
            }
        });
    }

    private void Validate() {

        pd = new ProgressDialog(WalletTransfer.this);
        pd.setMessage("Validating Details..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
            //postdat.put("customerId", tinyDB.getString("custid"));

            postdat.put("customerId",userdata.getText().toString());

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("data",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"Verifyuser")
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
                        final Dialog openDialog = new Dialog(WalletTransfer.this);
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


                pd.cancel();
                pd.dismiss();
                mMessage = response.body().string();
                Log.e("mmessage",mMessage.toString());

                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                JSONObject jsonObject = new JSONObject(mMessage);

                                if (jsonObject.getString("statusCode").equalsIgnoreCase("1")){


                                    beneficiaryId = jsonObject.getString("beneficiaryId");



                                   // userdata.setKeyListener(null);
                                    userdata.setEnabled(false);
                                    amount.setVisibility(View.VISIBLE);
                                    send.setVisibility(View.VISIBLE);
                                    validate.setVisibility(View.GONE);



                                    send.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {



                                            if (Double.parseDouble(tinyDB.getString("walletbal"))<Double.parseDouble(amount.getText().toString())){

                                                final Dialog openDialog = new Dialog(WalletTransfer.this);
                                                openDialog.setContentView(R.layout.alert);
                                                openDialog.setTitle("Warning");
                                                TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                                dialogTextContent.setText("Your Wallet Balance is less than the entered amount.");
                                                ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                                dialogImage.setBackgroundResource(R.drawable.warning);
                                                Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                                dialogCloseButton.setVisibility(View.GONE);
                                                Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                                                dialogno.setText("OK");


                                                dialogno.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        openDialog.dismiss();
                                                        amount.getText().clear();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                                                    }
                                                });


                                                openDialog.setCancelable(false);
                                                openDialog.show();



                                            }else {

                                            sendmoney();
                                            }

                                        }
                                    });


                                }
                                else {

                                    final Dialog openDialog = new Dialog(WalletTransfer.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Required");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Please Enter Valid Details");
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    dialogImage.setBackgroundResource(R.drawable.warning);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                                    dialogno.setText("OK");


                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

                                            userdata.getText().clear();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                                        }
                                    });


                                    openDialog.setCancelable(false);
                                    openDialog.show();


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("Resy",mMessage);
                            // Toast.makeText(Wallet.this, mMessage, Toast.LENGTH_SHORT).show();


                        }
                    });
                }
                else  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        pd.cancel();
                        pd.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Dialog openDialog = new Dialog(WalletTransfer.this);
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
                });
            }
        });
    }

    private void sendmoney() {

        pd = new ProgressDialog(WalletTransfer.this);
        pd.setMessage("Sending Money..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
            //postdat.put("customerId", tinyDB.getString("custid"));

            postdat.put("customerId",tinyDB.getString("custid"));
            postdat.put("beneficiaryId",beneficiaryId);
            postdat.put("transferAmount",amount.getText().toString());

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("data",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"customerWalletTransfer")
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
                        final Dialog openDialog = new Dialog(WalletTransfer.this);
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


                pd.cancel();
                pd.dismiss();
                mMessage = response.body().string();
                Log.e("mmessage",mMessage.toString());

                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {

                                JSONObject jsonObject = new JSONObject(mMessage);

                                if (jsonObject.getString("statusCode").equalsIgnoreCase("1")){


                                    final Dialog openDialog = new Dialog(WalletTransfer.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Succesful");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Your Transaction is succesful.Amount Transferred");
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    dialogImage.setBackgroundResource(R.drawable.success);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                                    dialogno.setText("OK");


                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();
                                            Intent intent = new Intent(WalletTransfer.this,Dashpage.class);
                                            startActivity(intent);

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                                        }
                                    });


                                    openDialog.setCancelable(false);
                                    openDialog.show();



                                }
                                else {

                                    final Dialog openDialog = new Dialog(WalletTransfer.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Failed");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Your Transaction is unsuccesful.Please Try Again");
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    dialogImage.setBackgroundResource(R.drawable.failure);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

                                    dialogno.setText("OK");


                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(WalletTransfer.this,Dashpage.class);
                                                startActivity(intent);
                                        }
                                    });


                                    openDialog.setCancelable(false);
                                    openDialog.show();



                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("Resy",mMessage);
                            // Toast.makeText(Wallet.this, mMessage, Toast.LENGTH_SHORT).show();


                        }
                    });
                }
                else  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        pd.cancel();
                        pd.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Dialog openDialog = new Dialog(WalletTransfer.this);
                                openDialog.setContentView(R.layout.alert);
                                openDialog.setTitle("Error");
                                TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                dialogTextContent.setText("Sorry,Currently this service is not available");
                                ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                dialogImage.setBackgroundResource(R.drawable.warning);
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
