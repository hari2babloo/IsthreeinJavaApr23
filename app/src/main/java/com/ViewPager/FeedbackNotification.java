package com.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FeedbackNotification extends AppCompatActivity {


    RatingBar ratingBar;
    EditText entertxt;
    Button submit;

    ProgressDialog pd;
    TinyDB tinydb;


    String jobid = "empty",mMessage;

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedbacknotification);

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        entertxt =(EditText)findViewById(R.id.editText2);
        submit = (Button)findViewById(R.id.button2);
        tinydb = new TinyDB(this);
        Intent intent = getIntent();


        jobid = intent.getStringExtra("type");

        Log.e("type",jobid);
       // jobid = tinydb.getString("type");

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
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinydb.getString("custid"));
            postdat.put("feedbackMessage", entertxt.getText().toString());
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
