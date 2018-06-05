package com.a3x3conect.mobile.isthreeinjava;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.R;
import com.example.hari.isthreeinjava.SchedulePickup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;

public class Userprofile extends AppCompatActivity {

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    String mMessage,custid;
    String timeStamp;
    ProgressDialog pd;
    TinyDB tinyDB;
    TextView name,email,phon,address,landmark,city,pin;
    ImageButton nav,call;

    String lat,lon,tel;
    List<com.example.hari.isthreeinjava.Models.Userprofile> userprofiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);
        tinyDB = new TinyDB(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name =  (TextView)findViewById(R.id.custname);
        phon =  (TextView)findViewById(R.id.mobileno);
        email =  (TextView)findViewById(R.id.email);
        address =  (TextView)findViewById(R.id.adressdata);
        landmark =  (TextView)findViewById(R.id.landmark);
        city =  (TextView)findViewById(R.id.city);
        pin =  (TextView)findViewById(R.id.pin);
        nav = (ImageButton)findViewById(R.id.directions);
        call = (ImageButton)findViewById(R.id.call);

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Log.e(mm.getLat(),mm.getLongi());
                String strUri = "http://maps.google.com/maps?q=" +lat + "," +lon + " (" +name.getText() + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                startActivity(intent);
            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+tel));
                startActivity(intent);
            }
        });
        getaddress();
    }


    private void getaddress() {

        pd = new ProgressDialog(Userprofile.this);
        pd.setMessage("Getting Address");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        try {
            postdat.put("customerId",tinyDB.getString("custid"));

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"getUserInfo")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mmessage = e.getMessage().toString();
                pd.dismiss();
                pd.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(Userprofile.this);
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



                        openDialog.show();

                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {

                mMessage = response.body().string();
                Log.e("mfddsf",mMessage);
                pd.dismiss();
                pd.cancel();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();


                            JsonReader reader = new JsonReader(new StringReader(mMessage));
                            reader.setLenient(true);
                            Type listType = new TypeToken<List<com.example.hari.isthreeinjava.Models.Userprofile>>(){}.getType();
                            userprofiles = (List<com.example.hari.isthreeinjava.Models.Userprofile>) gson.fromJson(reader,listType);
                            for(int i = 0; i < userprofiles.size(); i++){

                                address.setText(userprofiles.get(i).getAddress());
                                landmark.setText(userprofiles.get(i).getLandMark());
                                city.setText(userprofiles.get(i).getCity()+", " +userprofiles.get(i).getState());
                            //    state.setText(userprofiles.get(i).getState());
                                pin.setText(userprofiles.get(i).getPincode());
                                phon.setText(userprofiles.get(i).getPhoneNo());
                              name.setText(userprofiles.get(i).getName() +"  (" +userprofiles.get(i).getUserName()+")");
                              email.setText(userprofiles.get(i).getEmail());

                              lat = userprofiles.get(i).getLat();
                              lon = userprofiles.get(i).getLongi();
                              tel= userprofiles.get(i).getPhoneNo();


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
