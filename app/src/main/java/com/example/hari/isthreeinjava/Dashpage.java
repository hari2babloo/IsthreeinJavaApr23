package com.example.hari.isthreeinjava;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ViewPager.FeedbackNotification;
import com.ViewPager.WalletHead;
import com.a3x3conect.mobile.isthreeinjava.GetContacts;
import com.a3x3conect.mobile.isthreeinjava.NewProcess.DryCleaning;
import com.a3x3conect.mobile.isthreeinjava.NewProcess.Ironing;
import com.a3x3conect.mobile.isthreeinjava.NewProcess.WashandIron;
import com.a3x3conect.mobile.isthreeinjava.Notifications;
import com.a3x3conect.mobile.isthreeinjava.OrderHead;
import com.a3x3conect.mobile.isthreeinjava.Profilepic;
import com.a3x3conect.mobile.isthreeinjava.Userprofile;
import com.a3x3conect.mobile.isthreeinjava.WalletTransfer;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.google.firebase.iid.FirebaseInstanceId;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashpage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton pick, placeorder, myorders, wallet, offers, btnironing, btnwashandiron, btndrycleaning;
    String mMessage;
    Button referandearnbtn;

    DatabaseHelper dbHelper;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    TinyDB tinydb;

    String feedbackkey;
    int notificatoincount;
    TextView name;
    ProgressDialog pd;
    CheckBox chk2, chk3, chk4, chk5;
    TextView walletbal;
    CircleImageView profpic;
    SharedPreferences sharedPreferences;
    ArrayList<String> feedbackCategory = new ArrayList<String>();

    RatingBar ratingBar;
    EditText entertxt;
    Button submit;
    Dialog openDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashpage);
        name = findViewById(R.id.name);
        Bundle bundle = getIntent().getExtras();
        tinydb = new TinyDB(this);
        referandearnbtn = findViewById(R.id.referandearn);
        referandearnbtn.setVisibility(View.GONE);
       // offers = findViewById(R.id.offers);
        btnironing = findViewById(R.id.ironing);
        btnwashandiron = findViewById(R.id.washandpress);
        btndrycleaning = findViewById(R.id.drycleaning);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e("Sharedpref", sharedPreferences.getString("type", ""));


        notificatoincount = sharedPreferences.getInt("count", 0);

        Log.e("notifiactioncout", String.valueOf(notificatoincount));


        feedbackkey = sharedPreferences.getString("type", "");


        if (feedbackkey.equalsIgnoreCase("")) {


        } else {

            openDialog = new Dialog(Dashpage.this);
            openDialog.setContentView(R.layout.feedbackalert);
            //  openDialog.setTitle("No Internet");


            ratingBar = openDialog.findViewById(R.id.ratingBar);
            entertxt = openDialog.findViewById(R.id.editText2);
            submit = openDialog.findViewById(R.id.button2);

            chk2 = openDialog.findViewById(R.id.checkBox2);
            chk3 = openDialog.findViewById(R.id.checkBox3);
            chk4 = openDialog.findViewById(R.id.checkBox4);
            chk5 = openDialog.findViewById(R.id.checkBox5);
            feedbackCategory.add(0, "");
            feedbackCategory.add(1, "");
            feedbackCategory.add(2, "");
            feedbackCategory.add(3, "");

            chk2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (chk2.isChecked()) {

                        feedbackCategory.add(0, chk2.getText().toString());

                    } else {


                        feedbackCategory.add(0, "");
                    }

                }
            });


            chk3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chk3.isChecked()) {

                        feedbackCategory.add(1, chk3.getText().toString());

                    } else {


                        feedbackCategory.add(1, "");
                    }

                }
            });
            chk4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chk4.isChecked()) {

                        feedbackCategory.add(2, chk4.getText().toString());

                    } else {


                        feedbackCategory.add(2, "");
                    }

                }
            });

            chk5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (chk5.isChecked()) {

                        feedbackCategory.add(3, chk5.getText().toString());

                    } else {


                        feedbackCategory.add(3, "");
                    }
                }
            });


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String str = entertxt.getText().toString();
                    if (str != null && !str.isEmpty()) {

                        userFeeback();

                    } else {


                        entertxt.setError("Should not be blank");
                    }
                    // openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                }
            });


            openDialog.setCancelable(false);
            openDialog.show();


        }


        btnironing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Dashpage.this, Ironing.class);
                startActivity(intent);
            }
        });

        btnwashandiron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                appserviceStatus("washAndPress");

            }
        });

        btndrycleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appserviceStatus("dryCleaning");

            }
        });

        referandearnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashpage.this, GetContacts.class);
                startActivity(intent);
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name.setText("Welcome " + tinydb.getString("name") + "  (" + tinydb.getString("custid") + ")");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView nav_user = hView.findViewById(R.id.textView);
        walletbal = hView.findViewById(R.id.wallet);
        profpic = hView.findViewById(R.id.profilepic);
        String encodedImage = tinydb.getString("profilepic");
        if (encodedImage != null && !encodedImage.isEmpty()) {
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            //  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //  decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            profpic.setImageBitmap(decodedByte);

        }


//        offers.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Dashpage.this, Offershead.class);
//                startActivity(intent);
//            }
//        });

        profpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashpage.this, Profilepic.class);
                startActivity(intent);
            }
        });
        nav_user.setText(tinydb.getString("name") + "  (" + tinydb.getString("custid") + ")");
        navigationView.setNavigationItemSelectedListener(this);

        getwalletbalance();

        TextView testing = findViewById(R.id.marqu);
        // testing.setVisibility(View.GONE);
        testing.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        testing.setMarqueeRepeatLimit(-1);
        testing.setSingleLine(true);
        testing.setSelected(true);
        pick = findViewById(R.id.pickup);
        placeorder = findViewById(R.id.placeordr);
        myorders = findViewById(R.id.myorders);

//        myorders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Dashpage.this, OrderHead.class);
//
//                tinydb.putString("serviceName","all");
//                startActivity(intent);
//            }
//        });


//        pick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // Toast.makeText(Dashpage.this, "", Toast.LENGTH_SHORT).show();
//                FindJobId();
////                Intent mainIntent = new Intent(Dashpage.this,Puckup.class);
////                Dashpage.this.startActivity(mainIntent);
////                Dashpage.this.finish();
//            }
//        });

//        placeorder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FindJobId2();
//
//            }
//        });
//
//        wallet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {washAndPress
//                Intent intent = new Intent(Dashpage.this,WalletHead.class);
//                startActivity(intent);
//            }
//        });
//
//        supportphone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Dashpage.this,Support.class);
//                startActivity(intent);
//            }
//        });
    }

    private void appserviceStatus(final String service) {


        pd = new ProgressDialog(Dashpage.this);
        pd.setMessage("Checking Status..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);


        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl) + "appServiceStatus")
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                pd.cancel();
                pd.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.e("Resy", mMessage);


                    }
                });


                String mMessage = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {


                pd.cancel();
                pd.dismiss();

                mMessage = response.body().string();
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("appservicestatus", mMessage);

                            try {
                                JSONObject jsonObject = new JSONObject(mMessage);


                                if (jsonObject.getString(service).equalsIgnoreCase("0")) {

                                    final Dialog openDialog = new Dialog(Dashpage.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Status");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setVisibility(View.GONE);

                                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
//                                    dialogImage.setImageResource(getResources().getDrawable(R.drawable.comingsoon));


                                    dialogImage.setBackgroundResource(R.drawable.comingsoon);
                                    dialogImage.getLayoutParams().height = 400;
                                    dialogImage.getLayoutParams().width = 400;

                                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                    dialogCloseButton.setVisibility(View.GONE);
                                    Button dialogno = openDialog.findViewById(R.id.cancel);
                                    dialogno.setVisibility(View.GONE);

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


                                    // openDialog.setCancelable(false);
                                    openDialog.show();


                                } else {

                                    switch (service) {

                                        case "washAndPress":

                                            Intent intent = new Intent(Dashpage.this, WashandIron.class);
                                            startActivity(intent);

                                            break;

                                        case "dryCleaning":

                                            Intent intent2 = new Intent(Dashpage.this, DryCleaning.class);
                                            startActivity(intent2);
                                            break;


                                    }


                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


                        }
                    });
                } else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        });
    }

    private void userFeeback() {

        pd = new ProgressDialog(Dashpage.this);
        pd.setMessage("Submitting Feedback");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();


        JSONArray feedbackcat = new JSONArray();


        for (int i = 0; i < feedbackCategory.size(); i++) {


            if (feedbackCategory.get(i).equalsIgnoreCase("")) {


            } else {

                feedbackcat.put(feedbackCategory.get(i));
//                feedbackcat.add(feedbackCategory.get(i).toString());
            }

            // itemType.put(filterdata2.get(i).item);
        }


        try {
            postdat.put("customerId", tinydb.getString("custid"));
            postdat.put("feedbackMessage", entertxt.getText().toString());
            postdat.put("jobId", feedbackkey);
            postdat.put("category", feedbackcat);
            postdat.put("rating", ratingBar.getRating());

//            postdat.put("firebaseToken", FirebaseInstanceId.getInstance().getToken());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE, postdat.toString());

        Log.e("data2", postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl) + "userFeedback")
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

                        Log.e("Resy", mMessage);
                        final Dialog openDialog = new Dialog(Dashpage.this);
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


                String mMessage = e.getMessage().toString();
            }

            @Override
            public void onResponse(Response response) throws IOException {


                pd.cancel();
                pd.dismiss();

                mMessage = response.body().string();
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("Resy", mMessage);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("type", "");
                            editor.apply();


                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar snackbar = Snackbar.make(parentLayout, "Thanks for your Feedback", Snackbar.LENGTH_SHORT);
                            snackbar.show();

                            openDialog.dismiss();


//                            final Dialog openDialog = new Dialog(Dashpage.this);
//                            openDialog.setContentView(R.layout.alert);
//                            openDialog.setTitle("Success");
//                            TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
//                            dialogTextContent.setText("Feeback Submitted Succesfully.");
//                            ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
//                            Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
//                            dialogCloseButton.setVisibility(View.GONE);
//                            Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
//
//                            dialogno.setText("OK");
//
//
//                            dialogno.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    openDialog.dismiss();
//
////                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
////                                    Intent intent = new Intent(Dashpage.this,Dashpage.class);
////                                    startActivity(intent);
//                                }
//                            });
//
//
//                            openDialog.setCancelable(false);
//                            openDialog.show();
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();


                        }
                    });
                } else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        });
    }


    private void FindJobId2() {
        pd = new ProgressDialog(Dashpage.this);
        pd.setMessage("Getting Job Status..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinydb.getString("custid"));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE, postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl) + "getJobStatus")
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
                        final Dialog openDialog = new Dialog(Dashpage.this);
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
                pd.cancel();
                pd.dismiss();
                mMessage = response.body().string();
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("Resy", mMessage);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject json = new JSONObject(mMessage);

                                String s = json.getString("statusCode");

                                if (s.equalsIgnoreCase("0")) {


                                    tinydb.putString("jobid", json.getString("jobid"));
                                    tinydb.putDouble("expressDeliveryCharge", json.getDouble("expressDeliveryCharge"));
                                    final Dialog openDialog = new Dialog(Dashpage.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Initiate Pickup");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("You haven't initiated any pickup please initiate a pickup request");
                                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                    Button dialogno = openDialog.findViewById(R.id.cancel);

                                    dialogno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openDialog.dismiss();
                                        }
                                    });
                                    dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            openDialog.dismiss();
                                            Intent intent = new Intent(Dashpage.this, SchedulePickup.class);
                                            startActivity(intent);
                                        }
                                    });

                                    openDialog.setCancelable(false);
                                    openDialog.show();

                                } else if (s.equalsIgnoreCase("1")) {
                                    tinydb.putString("jobid", json.getString("jobid"));


                                    ///  tinydb.putString("expressDelivery",json.getString("expressDelivery"));

                                    Intent intent = new Intent(Dashpage.this, Pickup.class);
                                    tinydb.putDouble("expressDeliveryCharge", json.getDouble("expressDeliveryCharge"));
                                    intent.putExtra("expressDelivery", json.getString("expressDelivery"));
                                    startActivity(intent);


                                }
                                Log.e("s", s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else runOnUiThread(new Runnable() {
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


        pd = new ProgressDialog(Dashpage.this);
        pd.setMessage("Getting Job Status..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinydb.getString("custid"));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE, postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl) + "getJobStatus")
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
                        final Dialog openDialog = new Dialog(Dashpage.this);
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

                pd.cancel();
                pd.dismiss();

                mMessage = response.body().string();
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("Resy", mMessage);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject json = new JSONObject(mMessage);

                                String s = json.getString("statusCode");

                                if (s.equalsIgnoreCase("0")) {


                                    tinydb.putString("jobid", json.getString("jobid"));
                                    Intent intent = new Intent(Dashpage.this, SchedulePickup.class);
                                    startActivity(intent);
                                } else if (s.equalsIgnoreCase("1")) {

                                    Log.e("resfsdf", mMessage);
                                    final Dialog openDialog = new Dialog(Dashpage.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Pickup Already Initiated");
                                    TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("You have already initiated your pickup, your pickup is on the way");
                                    ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
                                    Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
                                    Button dialogno = openDialog.findViewById(R.id.cancel);
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
//                                            Intent intent = new Intent(Dashpage.this,SchedulePickup.class);
//                                            startActivity(intent);
//                                        }
//                                    });

                                    openDialog.setCancelable(false);
                                    openDialog.show();

                                }
                                Log.e("s", s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_dash, menu);


        if (notificatoincount > 0) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.notificacopy));

        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(Dashpage.this, GetContacts.class);

            //           tinydb.putString("custid","");
//            try {
//                FirebaseInstanceId.getInstance().deleteInstanceId();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////            FirebaseInstanceId.getInstance().deleteToken();
//            tinydb.clear();
            startActivity(intent);


            // Do something
            return true;
        }

        if (id == R.id.notification) {


            Intent intent = new Intent(Dashpage.this, Notifications.class);


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("count", 0);
            editor.apply();
            //           tinydb.putString("custid","");
//            try {
//                FirebaseInstanceId.getInstance().deleteInstanceId();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////            FirebaseInstanceId.getInstance().deleteToken();
//            tinydb.clear();
            startActivity(intent);


            // Do something
            return true;
        }

//        else if (id==R.id.notification){
//
//
//            Intent intent = new Intent(Dashpage.this,Offershead.class);
//            startActivity(intent);
//
//         //   Toast.makeText(this, "Cross", Toast.LENGTH_SHORT).show();
//        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            final Dialog openDialog = new Dialog(Dashpage.this);
            openDialog.setContentView(R.layout.alert);
            openDialog.setTitle("Exit app");
            TextView dialogTextContent = openDialog.findViewById(R.id.dialog_text);
            dialogTextContent.setText("Do you want to close the app?");
            ImageView dialogImage = openDialog.findViewById(R.id.dialog_image);
            Button dialogCloseButton = openDialog.findViewById(R.id.dialog_button);
            Button dialogno = openDialog.findViewById(R.id.cancel);

            dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog.dismiss();

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    //  Dashpage.this.finish();
//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
//                                                startActivity(intent);
                }
            });

            dialogno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog.dismiss();
                }
            });

            openDialog.setCancelable(false);
            openDialog.show();

        }


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.navmyorder) {

            Intent intent = new Intent(Dashpage.this, OrderHead.class);

            tinydb.putString("serviceName", "all");

            //           tinydb.putString("custid","");

            // tinydb.clear();
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.navwallet) {

            Intent intent = new Intent(Dashpage.this, WalletHead.class);
            startActivity(intent);


//            if (item.getTitle().toString().equalsIgnoreCase("Wallet")){
//
//                item.setTitle("Hello");
//            }


        } else if (id == R.id.navwallettransfr) {
            Intent intent = new Intent(Dashpage.this, WalletTransfer.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {


            Intent intent = new Intent(Dashpage.this, Userprofile.class);

            //           tinydb.putString("custid","");

            // tinydb.clear();
            startActivity(intent);
        }
//        else if (id == R.id.nav_manage) {
//
//        }

        else if (id == R.id.navchngaddr) {


            Intent intent = new Intent(Dashpage.this, ChangeAddress.class);

            //           tinydb.putString("custid","");

            // tinydb.clear();
            startActivity(intent);


        } else if (id == R.id.navchngpass) {

            Intent intent = new Intent(Dashpage.this, VerifyEmail.class);

            //           tinydb.putString("custid","");

            // tinydb.clear();
            startActivity(intent);

        } else if (id == R.id.feedback) {


            Intent intent = new Intent(Dashpage.this, FeedbackNotification.class);

            intent.putExtra("type", "empty");
            // tinydb.putString("type","empty");
            //           tinydb.putString("custid","");

            // tinydb.clear();
            startActivity(intent);


        } else if (id == R.id.navlogout) {


            Intent intent = new Intent(Dashpage.this, Signin.class);

            //           tinydb.putString("custid","");
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();

            } catch (IOException e) {
                e.printStackTrace();
            }
//            FirebaseInstanceId.getInstance().deleteToken();
            //    tinydb.clear();
            tinydb.clear();
            startActivity(intent);

        } else if (id == R.id.referearn) {


            Intent intent = new Intent(Dashpage.this, GetContacts.class);

            //           tinydb.putString("custid","");

            // tinydb.clear();
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getwalletbalance() {


        pd = new ProgressDialog(Dashpage.this);
        pd.setMessage("Getting your wallet balance..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("userId", tinydb.getString("custid"));
            postdat.put("firebaseToken", FirebaseInstanceId.getInstance().getToken());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE, postdat.toString());
        Log.e("Walletpost", postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl) + "checkWallet")
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
                        final Dialog openDialog = new Dialog(Dashpage.this);
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
                pd.cancel();
                pd.dismiss();
                mMessage = response.body().string();
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("com/wallet", mMessage);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject json = new JSONObject(mMessage);

                                String s = json.getString("statusCode");

                                if (s.equalsIgnoreCase("0")) {

                                    walletbal.setText("Wallet Balance: " + R.string.rupee + "0.00");
                                } else if (s.equalsIgnoreCase("1")) {

                                    tinydb.putString("walletbal", json.getString("availableFunds"));

                                    walletbal.setText("Wallet Balance: " + getResources().getString(R.string.rupee) + json.getString("availableFunds"));

                                }
                                Log.e("s", s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else runOnUiThread(new Runnable() {
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
