package com.a3x3conect.mobile.isthreeinjava;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profilepic extends AppCompatActivity {

    Button capture;
    Button gallery;
    private static final int SELECT_PHOTO = 100;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    TinyDB tinyDB;
    ProgressDialog pd;
    ImageView img;
    String mMessage,encoded;
    TinyDB tinydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepic);
        tinyDB = new TinyDB(this);

        tinydb = new TinyDB(this);

        gallery = (Button) findViewById(R.id.gallery);
        capture = (Button)findViewById(R.id.capture);

        CircleImageView profpic = (CircleImageView)findViewById(R.id.profilepic);

        String encodedImage = tinydb.getString("profilepic");



        if(encodedImage != null && !encodedImage.isEmpty()){

            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);




            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            //  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //  decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            profpic.setImageBitmap(decodedByte);

        }



        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);

            }
        });



        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                     encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                    String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), yourSelectedImage, "Title", null);

                    Log.e("Stream", String.valueOf(selectedImage));



getaddress();

                }

            case REQUEST_IMAGE_CAPTURE:

                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                    String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), yourSelectedImage, "Title", null);

 //                   Log.e("Stream", String.valueOf(selectedImage));



                    getaddress();



                   // img.setImageBitmap(imageBitmap);
                }
        }
    }


    private void getaddress() {


        pd = new ProgressDialog(Profilepic.this);
        pd.setMessage("Changing Picture");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        try {
            postdat.put("customerId",tinyDB.getString("custid"));
            postdat.put("pic",encoded);
            postdat.put("picFileType","jpg");

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        Log.e("image",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"changeProfilePicture")
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
                        final Dialog openDialog = new Dialog(Profilepic.this);
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

                            try {
                                JSONObject jsonObject = new JSONObject(mMessage);
                                String status =  jsonObject.getString("status");
                                String statuscode =  jsonObject.getString("statusCode");

                                if (statuscode.equalsIgnoreCase("1")){

                                    tinyDB.putString("profilepic",jsonObject.getString("profilePic"));

                                    final Dialog openDialog = new Dialog(Profilepic.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Success");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Profile Picture updated.");
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

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Profilepic.this,Dashpage.class);
                                                startActivity(intent);
                                        }
                                    });



                                    openDialog.show();



                                }

                                else {


                                    final Dialog openDialog = new Dialog(Profilepic.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Error");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Please Try Again");
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
//                                            Intent intent = new Intent(Profilepic.this,Dashpage.class);
//                                            startActivity(intent);
                                        }
                                    });



                                    openDialog.show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Gson gson = new Gson();



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
