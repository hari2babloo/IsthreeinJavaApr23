package com.example.hari.isthreeinjava;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.a3x3conect.mobile.isthreeinjava.GPSTracker;
import com.a3x3conect.mobile.isthreeinjava.LocationTrack;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.Models.Userprofile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
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
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChangeAddress extends AppCompatActivity {

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    MaterialEditText door,landmark,pin;
    EditText city;
    Button changeaddr;

    String spinnertext,pickupzone;
    String mMessage;

    ProgressDialog pd;

    TinyDB tinyDB;
    Context mContext;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    double longitude;
    double latitude;
    Spinner spinner;
    List<Userprofile> userprofiles;

    GPSTracker gps;

    ArrayList<String> location = new ArrayList<>();
    ArrayList<String> locationid = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_address);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tinyDB = new TinyDB(this);

        mContext = this;
        location.add("Select your nearest location");

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChangeAddress.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {


            //Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, ChangeAddress.this);

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                // \n is for new line
  //                 Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }
        GetFormData();

        door = findViewById(R.id.door);
        landmark = findViewById(R.id.land);
        city = findViewById(R.id.city);
        spinner = findViewById(R.id.spinner);
        pin = findViewById(R.id.pin);
        changeaddr = findViewById(R.id.change);

        changeaddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(door.getText().toString())){

                    door.setError("please fill this field");
                }
                else if (TextUtils.isEmpty(landmark.getText().toString())){

                    landmark.setError("please fill this field");
                }
                else if (TextUtils.isEmpty(city.getText().toString())){

                    city.setError("please fill this field");
                }
                else if (TextUtils.isEmpty(pin.getText().toString())){

                    pin.setError("please fill this field");
                }


                else if(spinnertext.equalsIgnoreCase("Select your nearest location")){



                    TextView errorText = (TextView)spinner.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select your nearest location");

                }
                else{

                    Submitdata();
                }
            }
        });
    }


    private void GetFormData() {


        pd = new ProgressDialog(ChangeAddress.this);
        pd.setMessage("Getting Form Data");
        pd.setCancelable(false);
        pd.show();

        final   OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"serviceLocations")
                .get()
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
                        final Dialog openDialog = new Dialog(ChangeAddress.this);
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

//                                                //                                          Toast.makeText(Pickup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Pickup.this,Dashpage.class);
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

                mMessage = response.body().string();
                pd.cancel();
                pd.dismiss();

                Log.e("result",mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                getaddress();

                               // location.add("noid");
                                JSONArray jsonArray = new JSONArray(mMessage);

                                for (int i = 0; i<jsonArray.length();i++){
                                    JSONObject json_data = jsonArray.getJSONObject(i);



                                    location.add(json_data.getString("location"));
                                   // locationid.add(json_data.getString("partnerId"));

                                    Log.e("location",json_data.getString("location"));
                                }

                                setspinner();

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

    private void setspinner() {

        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item,location);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#163053"));

                spinnertext = location.get(position);

                pickupzone = location.get(position);
//                locationTrack = new LocationTrack(ChangeAddress.this);
//
//                if (locationTrack.canGetLocation()) {
//
//
//                    longitude = locationTrack.getLongitude();
//                    latitude = locationTrack.getLatitude();
//
//                               Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
//                } else {
//
//                    locationTrack.showSettingsAlert();
//                }

                Log.e("selected item", location.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getaddress() {


        pd = new ProgressDialog(ChangeAddress.this);
        pd.setMessage("Getting Address");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
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
                        final Dialog openDialog = new Dialog(ChangeAddress.this);
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
                            Type listType = new TypeToken<List<Userprofile>>(){}.getType();
                            userprofiles = gson.fromJson(reader,listType);
                            for(int i = 0; i < userprofiles.size(); i++){

                                door.setText(userprofiles.get(i).getAddress());
                                landmark.setText(userprofiles.get(i).getLandMark());


//                                adress.setText(userprofiles.get(i).getAddress());
//                                landmark.setText(userprofiles.get(i).getLandMark());
//                                city.setText(userprofiles.get(i).getCity());
//                                state.setText(userprofiles.get(i).getState());
//                                pin.setText(userprofiles.get(i).getPincode());
//                                phone.setText(userprofiles.get(i).getPhoneNo());
//                                custid = userprofiles.get(i).getUserName();
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

    private void Submitdata() {

        pd = new ProgressDialog(ChangeAddress.this);
        pd.setMessage("Changing your address");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerID",tinyDB.getString("custid"));
            postdat.put("address", door.getText().toString());
             postdat.put("city","Hyderabad");
            postdat.put("country", "INDIA");
             postdat.put("landMark", landmark.getText().toString());
            postdat.put("lat", latitude);
            postdat.put("longi", longitude);
             postdat.put("pincode", pin.getText().toString());
            postdat.put("state", "Telangana");
            postdat.put("pickupZone",pickupzone);


        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        Log.e("fdsfdsfs",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"changeAddress")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage().toString();
                pd.cancel();
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(ChangeAddress.this);
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

                mMessage = response.body().string();
                pd.cancel();
                pd.dismiss();

                Log.e("Respomse",mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonResponse = null;
                            try {

                                JSONArray itemArray = new JSONArray(mMessage);
                                for (int i = 0; i < itemArray.length(); i++) {
                                    // int value=itemArray.getInt(i);
                                    String sss = itemArray.getString(i);
                                    jsonResponse = new JSONObject(sss);

                                    jsonResponse.getString("status");
                                    jsonResponse.getString("statusCode");

                                    Log.e("Json", jsonResponse.getString("status"));
                                    if (jsonResponse.getString("statusCode").equalsIgnoreCase("0")){

                                        final Dialog openDialog = new Dialog(ChangeAddress.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("Error");
                                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                        dialogTextContent.setText(jsonResponse.getString("status"));
                                        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                        Button dialogno = openDialog.findViewById(R.id.cancel);
                                        dialogno.setVisibility(View.GONE);

                                        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
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

                                    else if (jsonResponse.getString("statusCode").equalsIgnoreCase("1")){


                                        tinyDB.putString("pickupZone",pickupzone);



                                        final Dialog openDialog = new Dialog(ChangeAddress.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("Success");
                                        TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                        dialogTextContent.setText(jsonResponse.getString("status"));
                                        ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                        Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                        Button dialogno = openDialog.findViewById(R.id.cancel);
                                        dialogno.setVisibility(View.GONE);

                                        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                openDialog.dismiss();

                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ChangeAddress.this,Dashpage.class);
                                                startActivity(intent);
                                            }
                                        });
                                        openDialog.setCancelable(false);
                                        openDialog.show();
                                        //

                                    }



                                }


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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    // contacts-related task you need to do.

                    gps = new GPSTracker(mContext, ChangeAddress.this);

                    // Check if GPS enabled
                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        // \n is for new line
//                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gps.showSettingsAlert();
                    }

                } else {


                    final Dialog openDialog = new Dialog(ChangeAddress.this);
                    openDialog.setContentView(R.layout.alert);
                    openDialog.setTitle("Permission Request");
                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                    dialogTextContent.setText("Isthree needs your location for providing better pickup and delivery services.So,Please allow to access your location.");
                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                    dialogImage.setBackgroundResource(R.drawable.warning);
                    dialogImage.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.warning));
//              dialogImage.setBackground(this.getDrawable(ContextCompat.R.drawable.failure));
                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                    dialogCloseButton.setVisibility(View.GONE);
                    Button dialogno = openDialog.findViewById(R.id.cancel);

                    dialogno.setText("OK");


                    dialogno.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialog.dismiss();
                            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ChangeAddress.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                            } else {


                                //Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
                                gps = new GPSTracker(mContext, ChangeAddress.this);

                                // Check if GPS enabled
                                if (gps.canGetLocation()) {

                                    latitude = gps.getLatitude();
                                    longitude = gps.getLongitude();

                                    // \n is for new line
 //                                   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                                } else {
                                    // Can't get location.
                                    // GPS or network is not enabled.
                                    // Ask user to enable GPS/network in settings.
                                    gps.showSettingsAlert();
                                }
                            }

                            //                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                        }
                    });


                    openDialog.setCancelable(false);
                    openDialog.show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    //                   Toast.makeText(mContext, "You need to grant permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
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
