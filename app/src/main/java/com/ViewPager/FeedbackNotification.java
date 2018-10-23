package com.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.a3x3conect.mobile.isthreeinjava.Feedback;
import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.R;
import com.example.hari.isthreeinjava.Signin;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
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
import java.util.concurrent.TimeUnit;

public class FeedbackNotification extends AppCompatActivity {


    RatingBar ratingBar;
    EditText entertxt;
    Button submit;
    CheckBox chk2,chk3,chk4,chk5;
    ArrayList<String> feedbackCategory = new ArrayList<String>();

    ProgressDialog pd;
    TinyDB tinydb;
    SharedPreferences sharedPreferences;


    String jobid = "empty",mMessage;

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedbacknotification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        feedbackCategory.add(0,"");
        feedbackCategory.add(1,"");
        feedbackCategory.add(2,"");
        feedbackCategory.add(3,"");
        chk2 = (CheckBox)findViewById(R.id.checkBox2);
        chk3 = (CheckBox)findViewById(R.id.checkBox3);
        chk4 = (CheckBox)findViewById(R.id.checkBox4);
        chk5 = (CheckBox)findViewById(R.id.checkBox5);
        entertxt =(EditText)findViewById(R.id.editText2);
        submit = (Button)findViewById(R.id.button2);
        tinydb = new TinyDB(this);
       // Intent intent = getIntent();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e("Sharedpref",sharedPreferences.getString("type",""));
        jobid = sharedPreferences.getString("type","");

        Log.e("type",jobid);
       // jobid = tinydb.getString("type");

        chk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (chk2.isChecked()){

                    feedbackCategory.add(0,chk2.getText().toString());

                }

                else{


                    feedbackCategory.add(0,"");
                }

            }
        });


        chk3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk3.isChecked()){

                    feedbackCategory.add(1,chk3.getText().toString());

                }

                else{


                    feedbackCategory.add(1,"");
                }

            }
        });
chk4.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (chk4.isChecked()){

            feedbackCategory.add(2,chk4.getText().toString());

        }

        else{


            feedbackCategory.add(2,"");
        }

    }
});

chk5.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if (chk5.isChecked()){

            feedbackCategory.add(3,chk5.getText().toString());

        }

        else{


            feedbackCategory.add(3,"");
        }
    }
});

        
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = entertxt.getText().toString();
                if(str != null && !str.isEmpty()) {

                    userFeeback();

                }

                else {


                    Toast.makeText(FeedbackNotification.this, "Please Write Feedback", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    private void userFeeback() {

        pd = new ProgressDialog(FeedbackNotification.this);
        pd.setMessage("Submitting Feedback");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        JSONArray feedbackcat = new JSONArray();


        for (int i=0;i<feedbackCategory.size();i++){


            if (feedbackCategory.get(i).equalsIgnoreCase("")){


            }

            else {

                feedbackcat.put(feedbackCategory.get(i));
//                feedbackcat.add(feedbackCategory.get(i).toString());
            }

           // itemType.put(filterdata2.get(i).item);
        }

        try {
            postdat.put("customerId", tinydb.getString("custid"));
            postdat.put("feedbackMessage", entertxt.getText().toString());
            postdat.put("category",feedbackcat);

            postdat.put("jobId", jobid);
            postdat.put("rating", ratingBar.getRating());

//            postdat.put("firebaseToken", FirebaseInstanceId.getInstance().getToken());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("data",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"userFeedback")
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
                        final Dialog openDialog = new Dialog(FeedbackNotification.this);
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
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("Resy",mMessage);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("type","");
                            editor.apply();


                            final Dialog openDialog = new Dialog(FeedbackNotification.this);
                            openDialog.setContentView(R.layout.alert);
                            openDialog.setTitle("Success");
                            TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                            dialogTextContent.setText("Feeback Submitted Succesfully.");
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
                                                Intent intent = new Intent(FeedbackNotification.this,Dashpage.class);
                                                startActivity(intent);
                                }
                            });


                            openDialog.setCancelable(false);
                            openDialog.show();
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();


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
}
