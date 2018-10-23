package com.example.hari.isthreeinjava;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a3x3conect.mobile.isthreeinjava.FirebaseInstanceIdService;
import com.example.hari.isthreeinjava.Models.Sigin;
import com.example.hari.isthreeinjava.Models.Tariff;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Signin extends AppCompatActivity {

    Button signin;
    MaterialEditText userid,pass;
    TextView signuptxt,forgotpass;
    String mMessage;
    List<Sigin> modelsignin;
    TinyDB tinyDB;
    ProgressDialog pd;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);


        TextView testing = (TextView)findViewById(R.id.marqu2);
        testing.setEllipsize(TextUtils.TruncateAt.MARQUEE);
      //  testing.setMarqueeRepeatLimit(-1);
        testing.setSingleLine(true);
        testing.setSelected(true);


        String s2 =  FirebaseInstanceId.getInstance().getToken();
        if (s2!=null){
            Log.e("ererer",s2);

}

else {

            Log.e("nulltoken","null token");
        }

//
//        if (getIntent().getExtras()!=null){
//
//            for (String key: getIntent().getExtras().keySet()){
//
//                if (key.equals("title"))
//
//                    Log.e("title",getIntent().getExtras().getString(key));
//
//
//                else if (key.equals("message")){
//
//                    Log.e("Message",getIntent().getExtras().getString(key));
//                }
//            }
//        }



        tinyDB = new TinyDB(this);

        Log.e("custid",tinyDB.getString("custid"));
        String s = tinyDB.getString("custid");
        if (s != null && !s.isEmpty()){

            Intent intent = new Intent(Signin.this,Dashpage.class);
            startActivity(intent);
        }

        userid  = (MaterialEditText)findViewById(R.id.userid);
        pass = (MaterialEditText)findViewById(R.id.pass);
        signin = (Button)findViewById(R.id.signin);
        signuptxt = (TextView)findViewById(R.id.signuptext);
        forgotpass = (TextView)findViewById(R.id.forgotpass);



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (userid.getText().toString().isEmpty()){

                    userid.setError("Please Enter User ID");
                }
                else if (pass.getText().toString().isEmpty()){

                    pass.setError("Please Enter Your Password");

                }
                else {
                    Validate();
                }

            }
        });
        signuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Signin.this,Signup.class);
                startActivity(intent);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this,VerifyEmail.class);
                startActivity(intent);

            }
        });
    }

    private void Validate() {
        pd = new ProgressDialog(Signin.this);
        pd.setMessage("Signing in..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("userName", userid.getText().toString());
            postdat.put("password", pass.getText().toString());
            postdat.put("firebaseToken",FirebaseInstanceId.getInstance().getToken());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("data",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"login")
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
                        final Dialog openDialog = new Dialog(Signin.this);
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



                mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("Resy",mMessage);
                           // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            TraverseData();

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



    private void TraverseData() {

        Gson gson = new Gson();

        JsonReader reader = new JsonReader(new StringReader(mMessage));
        reader.setLenient(true);

        Type listType = new TypeToken<List<Sigin>>(){}.getType();
        modelsignin = (List<Sigin>)  gson.fromJson(reader,listType);

        for(int j = 0; j < modelsignin.size(); j++){


            Integer status = modelsignin.get(j).getStatus();
          //  modelsignin.get(j).getStatus();

          if (status.equals(0)){

              pd.cancel();
              pd.dismiss();

              final Dialog openDialog = new Dialog(Signin.this);
              openDialog.setContentView(R.layout.alert);
              openDialog.setTitle("Attention!!");
              TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
              dialogTextContent.setText("Please Enter Valid Credentials");
              ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
              dialogImage.setBackgroundResource(R.drawable.failure);
              dialogImage.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.failure));
//              dialogImage.setBackground(this.getDrawable(ContextCompat.R.drawable.failure));
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
          else if (status.equals(1)){

              pd.cancel();
              pd.dismiss();
              Intent intent = new Intent(Signin.this,Dashpage.class);
//              intent.putExtra("custid",modelsignin.get(j).getUserName());
              tinyDB.putString("custid",modelsignin.get(j).getUserName());
              tinyDB.putString("name",modelsignin.get(j).getName());
              tinyDB.putString("custEmail",modelsignin.get(j).getEmail());
              tinyDB.putString("custphno",modelsignin.get(j).getPhoneNo());
              tinyDB.putString("pickupZone",modelsignin.get(j).getPickupZone());

              if(modelsignin.get(j).getPic() != null){

                  tinyDB.putString("profilepic",  modelsignin.get(j).getPic());

              }




              startActivity(intent);
            //  Log.e("status", String.valueOf(modelsignin.get(j).getStatus()));

          }


        }




    }

    @Override
    public void onBackPressed() {
        final Dialog openDialog = new Dialog(Signin.this);
        openDialog.setContentView(R.layout.alert);
        openDialog.setTitle("Exit app");
        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
        dialogTextContent.setText("Do you want to Close the app?");
        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
        Button dialogno = (Button)openDialog.findViewById(R.id.cancel);

        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
           //     Signin.this.finish();
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
