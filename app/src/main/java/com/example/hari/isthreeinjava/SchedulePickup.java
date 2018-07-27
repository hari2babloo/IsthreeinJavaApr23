package com.example.hari.isthreeinjava;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Trace;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.a3x3conect.mobile.isthreeinjava.ExistingData;
import com.example.hari.isthreeinjava.Models.Sigin;
import com.example.hari.isthreeinjava.Models.Tariff;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.Models.Userprofile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SchedulePickup extends AppCompatActivity {

    TextView selecteddate,adress,landmark,city,state,pin,phone,textView5,thursdaymsg,expresstxt;
    Button datebtn,changeadress,confirmpickup;
    Calendar myCalendar;
    CheckBox checkBox;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    String mMessage,custid;
    String timeStamp;
    List<Userprofile> userprofiles;
    TinyDB tinyDB;
    View v1,v2;
    ProgressDialog pd;
    LinearLayout linearLayout;
    SimpleDateFormat sdf,sdf2;
    String exprsval = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_pickup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        datebtn = (Button)findViewById(R.id.datebtn);
        changeadress = (Button)findViewById(R.id.changeadress);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        confirmpickup = (Button)findViewById(R.id.confirm);
        selecteddate = (TextView)findViewById(R.id.dates);
        adress = (TextView)findViewById(R.id.address);
        landmark = (TextView)findViewById(R.id.landmark);
        expresstxt = (TextView)findViewById(R.id.expresstxt);
        city = (TextView)findViewById(R.id.city);
        state = (TextView)findViewById(R.id.state);
        pin = (TextView)findViewById(R.id.pin);
        phone = (TextView)findViewById(R.id.phone);
        textView5 = (TextView)findViewById(R.id.textView5);
        thursdaymsg = (TextView)findViewById(R.id.thursdaymsg);
        thursdaymsg.setVisibility(View.GONE);
        linearLayout = (LinearLayout)findViewById(R.id.adressss);
        linearLayout.setVisibility(View.GONE);
        changeadress.setVisibility(View.GONE);
        confirmpickup.setVisibility(View.GONE);
        adress.setVisibility(View.GONE);
        landmark.setVisibility(View.GONE);
        city.setVisibility(View.GONE);
        state.setVisibility(View.GONE);
        pin.setVisibility(View.GONE);
        phone.setVisibility(View.GONE);
        textView5.setVisibility(View.GONE);
        selecteddate.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);
        expresstxt.setVisibility(View.GONE);

        myCalendar = Calendar.getInstance();
        tinyDB = new TinyDB(this);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }

        };
        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SchedulePickup.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        changeadress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent  intent = new Intent(SchedulePickup.this,ChangeAddress.class);
                startActivity(intent);
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){

                    Toast.makeText(SchedulePickup.this, "Express Delivery Enabled", Toast.LENGTH_SHORT).show();
                    exprsval = "1";
                }
                else {

                    Toast.makeText(SchedulePickup.this, "Express Delivery Disabled", Toast.LENGTH_SHORT).show();
                    exprsval = "0";
                }
            }
        });
        confirmpickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleProcess();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "EEE, MMM d, yyyy";
        String myFormat2 = "yyyy-MM-dd HH:mm:ss";
        //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf2 = new SimpleDateFormat(myFormat2, Locale.US);
        Log.e("selectdate",sdf.format( myCalendar.getTime()));



        String timeStamp = new SimpleDateFormat("EEE, MMM d, yyyy").format(Calendar.getInstance().getTime());
        linearLayout.setVisibility(View.VISIBLE);

        try {
            Date now = sdf.parse(timeStamp);
            Date selected = sdf.parse(sdf.format(myCalendar.getTime()));
            if(selected.after(now)){
                System.out.println("Correct");


                selecteddate.setVisibility(View.VISIBLE);
                changeadress.setVisibility(View.VISIBLE);


//                if (myCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY){
//
//                    Log.e("day","Thursday");
//
//                    myCalendar.add(Calendar.DATE,1);
//
//                    Log.e("added day",sdf.format( myCalendar.getTime()));
//                   // thursdaymsg.setVisibility(View.VISIBLE);
//
//
//                    final Dialog openDialog = new Dialog(SchedulePickup.this);
//                    openDialog.setContentView(R.layout.alert);
//                    openDialog.setTitle("Note");
//                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
//                    dialogTextContent.setTextColor(getResources().getColor(R.color.red));
//                    dialogTextContent.setText("Please note, there will be no pickup or delivery on THURSDAYS as it is a weekly holiday for our operations. \n So, Your pickup will be scheduled for Friday.");
//                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
//                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
//                  //  dialogCloseButton.setVisibility(View.GONE);
//                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
//
//                    dialogCloseButton.setText("RESCHEDULE");
//
//                    dialogCloseButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            openDialog.dismiss();
//                            Intent intent = getIntent();
//                            finish();
//                            startActivity(intent);
//
//                        }
//                    });
//                    dialogno.setText("OK");
//
//
//                    dialogno.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            openDialog.dismiss();
//
////                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
////                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
////                                                startActivity(intent);
//                        }
//                    });
//
//
//
//                    openDialog.show();
//
//                }
//
//                else {
//                    Log.e("day","notThursday");
//
//                }

                datebtn.setText(sdf.format(myCalendar.getTime()));
                 selecteddate.setText( sdf.format(myCalendar.getTime()));
              //  selecteddate.setVisibility(View.GONE);
                datebtn.setVisibility(View.GONE);

                getaddress();
            }
            // before() will return true if and only if date1 is before date2
            if(selected.before(now)){
                System.out.println("Please select correct date");
                Toast.makeText(this, "You Cannot Select previous day", Toast.LENGTH_SHORT).show();
            }

            //equals() returns true if both the dates are equal
            if(selected.equals(now)){

                selecteddate.setVisibility(View.VISIBLE);
                changeadress.setVisibility(View.VISIBLE);

                //               v1.setVisibility(View.VISIBLE);

                //             v2.setVisibility(View.VISIBLE);

                datebtn.setText(sdf.format(myCalendar.getTime()));
                selecteddate.setText(sdf.format(myCalendar.getTime()));
                //  selecteddate.setVisibility(View.GONE);
                datebtn.setVisibility(View.GONE);

                getaddress();
//                Toast.makeText(this, "Schedule not possible today", Toast.LENGTH_SHORT).show();
                System.out.println("You cannot select today's day");
            }

         } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void getaddress() {


        pd = new ProgressDialog(SchedulePickup.this);
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
                        final Dialog openDialog = new Dialog(SchedulePickup.this);
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
                             userprofiles = (List<Userprofile>) gson.fromJson(reader,listType);
                            for(int i = 0; i < userprofiles.size(); i++){

                                changeadress.setVisibility(View.VISIBLE);
                                confirmpickup.setVisibility(View.VISIBLE);
                                adress.setVisibility(View.VISIBLE);
                                landmark.setVisibility(View.VISIBLE);
                                city.setVisibility(View.VISIBLE);
                                state.setVisibility(View.VISIBLE);
                                pin.setVisibility(View.VISIBLE);
                                phone.setVisibility(View.VISIBLE);
                                textView5.setVisibility(View.VISIBLE);
                                checkBox.setVisibility(View.VISIBLE);
                                expresstxt.setVisibility(View.VISIBLE);
                                thursdaymsg.setVisibility(View.VISIBLE);
                                adress.setText(userprofiles.get(i).getAddress());
                                landmark.setText(userprofiles.get(i).getLandMark());
                                city.setText(userprofiles.get(i).getCity());
                                state.setText(userprofiles.get(i).getState());
                                pin.setText(userprofiles.get(i).getPincode());
                                phone.setText(userprofiles.get(i).getPhoneNo());
                                custid = userprofiles.get(i).getUserName();
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

    private void ScheduleProcess() {
        pd = new ProgressDialog(SchedulePickup.this);
        pd.setMessage("Scheduling your Pickup");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        try {

            timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
            String timeStamp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            postdat.put("customerId", custid);
            postdat.put("expressDelivery",exprsval);
            postdat.put("status", "PICKUP-REQUESTED");
            postdat.put("jobid", timeStamp);
            postdat.put("pickupScheduledAt",sdf2.format(myCalendar.getTime()));
            postdat.put("createdAt", timeStamp2);

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("Selecteddate",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"schedulePickup")
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mmessage = e.getMessage().toString();
                pd.cancel();
                pd.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(SchedulePickup.this);
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

                mMessage = response.body().string();
                Log.e("mfddsf",mMessage);
                pd.cancel();
                pd.dismiss();
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



                                    final Dialog openDialog = new Dialog(SchedulePickup.this);
                                    openDialog.setContentView(R.layout.schedulealert);
                                    openDialog.setTitle("Schedule");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Pickup Request has been initiated successfully.\n Would you like to fill your order now ?");
                                    TextView note = (TextView)openDialog.findViewById(R.id.note);
                                    note.setVisibility(View.GONE);
                                  //  note.setText("Please note, there will be no pickup or delivery on THURSDAYS as it is a weekly holiday for our operations");
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setText("Yes");
                                    Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
                                    dialogno.setText("Fill later");
                                    //dialogno.setVisibility(View.GONE);

                                    dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();
                                            Intent intent = new Intent(SchedulePickup.this,Pickup.class);
                                            tinyDB.putString("jobid",timeStamp);
                                            intent.putExtra("expressDelivery",exprsval);
//                                            tinyDB.putString("expressDelivery",exprsval);
                                            startActivity(intent);
                                        }
                                    });
                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();
                                            Intent intent = new Intent(SchedulePickup.this,Dashpage.class);
                                            startActivity(intent);
                                        }
                                    });
                                    openDialog.setCancelable(false);
                                    openDialog.show();
           //                         Toast.makeText(SchedulePickup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();

                                    Log.e("json",sss);
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
