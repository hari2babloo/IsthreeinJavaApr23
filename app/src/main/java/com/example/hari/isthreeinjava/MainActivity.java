package com.example.hari.isthreeinjava;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hari.isthreeinjava.Models.Tariff;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    Spinner farelist;
    EditText qty;
    TextView amt;
    ImageButton plus,minus;
    ProgressDialog pd;

    private  Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gson = new Gson();
        amt = (TextView)findViewById(R.id.amt);
        qty = (EditText)findViewById(R.id.qty);
        farelist = (Spinner)findViewById(R.id.farelist);
        plus = (ImageButton)findViewById(R.id.plus);
        minus = (ImageButton)findViewById(R.id.minus);
        getdata();
    }

    private void getdata() {


        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Getting Details");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"alltariff")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                pd.dismiss();
                pd.cancel();

                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog openDialog = new Dialog(MainActivity.this);
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
            public void onResponse(final Response response) throws IOException {

                final String mMessage = response.body().string();

                pd.dismiss();
                pd.cancel();

                Log.w("Response", mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Type listType = new TypeToken<List<Tariff>>(){}.getType();
                            final List<Tariff> tarif = (List<Tariff>)  gson.fromJson(mMessage,listType);

                            final ArrayList<String> dd = new ArrayList<>();
                            for(int i = 0; i < tarif.size(); i++){
                                tarif.get(i).getType();
                                dd.add(tarif.get(i).getType());
                                    Log.e("Dta",dd.toString());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                                    android.R.layout.simple_spinner_item,dd);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            farelist.setAdapter(adapter);
                            farelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Object item = parent.getItemAtPosition(position);
                                    String s = tarif.get(position).getPrice();
 //                                   Toast.makeText(MainActivity.this,s, Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                        }
                    });

                }

                else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });
                }
            }

        });

    }
}
