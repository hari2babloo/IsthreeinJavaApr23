package com.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a3x3conect.mobile.isthreeinjava.VouchersList;
import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.Currentorder;
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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Paypage extends AppCompatActivity {


    TinyDB tinyDB;
    ProgressDialog pd;
    RecyclerView mRVFishPrice;
    TableLayout tableLayout;
    TextView btmtotal,grdtotal,voucher;
    TextView state;

    double walletbal;
    List<DataFish2> filterdata2=new ArrayList<DataFish2>();
    private AdapterFish Adapter;
    String radiostatus,payamount;
    Button home;
    double s;
    String mMessage,jobid;
    String paymentmode;

    TextView jobidtxt,status,date,grantotal,custid,invoice,walletbalancetxt,baltopaytxt,amountpaidtxt;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paypage);
        tinyDB = new TinyDB(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mMessage = tinyDB.getString("data");
        jobid = tinyDB.getString("jobid");

        getwalletbalance();
        Log.e("mmessage",jobid);
        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
        tableLayout = (TableLayout)findViewById(R.id.tabl);
        btmtotal = (TextView)findViewById(R.id.btmtotal);
        grdtotal = (TextView)findViewById(R.id.grdtotal);
        walletbalancetxt = (TextView)findViewById(R.id.wallet);
        voucher = (TextView)findViewById(R.id.voucher);
      //  voucher.setPaintFlags(voucher.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        home = (Button)findViewById(R.id.home);
        jobidtxt = (TextView)findViewById(R.id.jobid);
        status = (TextView)findViewById(R.id.delstatus);
        date = (TextView)findViewById(R.id.date);
        grantotal = (TextView)findViewById(R.id.grandtotal);
        custid = (TextView)findViewById(R.id.custid);
        invoice = (TextView)findViewById(R.id.invoice);

        voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Paypage.this, VouchersList.class);

               // intent.putExtra("totalamt",)
                startActivity(intent);

//                // get prompts.xml view
//                LayoutInflater li = LayoutInflater.from(Paypage.this);
//                View promptsView = li.inflate(R.layout.voucheralert, null);
//
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                        Paypage.this);
//
//                // set prompts.xml to alertdialog builder
//                alertDialogBuilder.setView(promptsView);
//
//                final EditText userInput = (EditText) promptsView
//                        .findViewById(R.id.editTextDialogUserInput);
//
//                state = (TextView)promptsView.findViewById(R.id.status);
//
//                state.setVisibility(View.GONE);
//
//                // set dialog message
//                alertDialogBuilder
//                        .setCancelable(false)
//                        .setPositiveButton("VERIFY",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        // get user input and set it to result
//                                        // edit text
//                                       // result.setText(userInput.getText());
//                                    }
//                                })
//                        .setNegativeButton("Cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//
//                // create alert dialog
//                AlertDialog alertDialog = alertDialogBuilder.create();
//
//                // show it
//                alertDialog.show();

            }

        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (walletbal==0){

                    final String[] items = {"Cash on Delivery"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Paypage.this);//ERROR ShowDialog cannot be resolved to a type
                    builder.setTitle("Payment Mode");
                    builder.setSingleChoiceItems(items, -1,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {

                                    radiostatus = items[item];

                                    //  Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                                }
                            });

                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            dialog.dismiss();
                            dialog.cancel();

                        }
                    });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if (radiostatus!=null && !radiostatus.isEmpty()){

                                dialog.dismiss();
                                dialog.cancel();

//                                if (radiostatus.equalsIgnoreCase("Wallet")){
//
//
////                                final Dialog openDialog = new Dialog(Paypage.this);
////                                openDialog.setContentView(R.layout.alert);
////                                openDialog.setTitle("Message");
////                                TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
////                                dialogTextContent.setText("Thanks for choosing our service.\n Please check your wallet balance");
////                                ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
////                                Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
////                                dialogCloseButton.setVisibility(View.GONE);
////                                Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
////                                dialogno.setText("OK");
////
////
////                                dialogno.setOnClickListener(new View.OnClickListener() {
////                                    @Override
////                                    public void onClick(View v) {
////                                        openDialog.dismiss();
//
////                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(Paypage.this,Walletpayment.class);
//                                    tinyDB.putString("payamount",payamount);
//                                    startActivity(intent);
////                                    }
////                                });
////
////
////
////                                openDialog.show();
////                            Submitstatus();
//                                }
//                                else
                                if (radiostatus.equalsIgnoreCase("Cash on Delivery")){



                                    proceedtopayment();
                                }



//                            Submitstatus();



                            }
                            else {

                                builder.show();


                                Toast.makeText(Paypage.this, "Select Payment Mode", Toast.LENGTH_SHORT).show();


                            }


                        }
                    });

                    builder.show();
                }

                else {

                    radiostatus= "Wallet Transfer";
                    proceedtopayment();

                }

            }
        });

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Currentorder>>(){}.getType();
        List<Currentorder> jobOrder = (List<Currentorder>)  gson.fromJson(mMessage,listType);





        //JobOrder jobOrder = gson.fromJson(mMessage,JobOrder.class);

        for (int i= 0; i<jobOrder.size(); i++){





            if (jobOrder.get(i).getJobid().equalsIgnoreCase(jobid)){

                Log.e("workinh", String.valueOf(jobOrder.get(i).getCategory()));

                DataFish2 ss = new DataFish2("item","qty","price","total");

//    Float ss2 = Float.parseFloat(jobOrder.get(i).getPrice().get(i));

                //  Float ss3 =  Float.parseFloat(jobOrder.get(i).getQuantity().get(i));

                //        Float ss4 = ss2 * ss3;
//            DataFish2 sds = new DataFish2(jobOrder.get(i).getCategory().get(),jobOrder.get(i).getQuantity(),jobOrder.get(i).getPrice(),String.valueOf(ss4));


                for (int j=0; j<jobOrder.get(i).getQty().size();j++){

                    Log.e("dsadas",jobOrder.get(i).getCategory().get(j));

                    Log.e("dsadas",jobOrder.get(i).getPrice().get(j));
                    Log.e("dsadas",jobOrder.get(i).getQty().get(j));
                    Log.e("dsadas",jobOrder.get(i).getSubTotal().get(j));

                    date.setText(jobOrder.get(i).getPickupScheduledAt());
                    jobidtxt.setText(jobOrder.get(i).getJobid());
                    status.setText(jobOrder.get(i).getStatus());
                    custid.setText(jobOrder.get(i).getCustomerId());
                    invoice.setText(jobOrder.get(i).getInvoiceId());


                    DataFish2 sds = new DataFish2(jobOrder.get(i).getCategory().get(j),jobOrder.get(i).getQty().get(j),jobOrder.get(i).getPrice().get(j),jobOrder.get(i).getSubTotal().get(j));



                    filterdata2.add(sds);


                }



                // /  DataFish2 sds = new DataFish2(jobOrder.getCategory().get(i),jobOrder.getQuantity().get(i),jobOrder.getPrice().get(i),String.valueOf(ss4));


                //   filterdata2.add(sds);
            }
//

        }

        float sum = 0;
        float garmentscount = 0;
        for (int i=0;i<filterdata2.size();i++){


            float foo = Float.parseFloat(filterdata2.get(i).noofpieces);
            float foo3 = Float.parseFloat(filterdata2.get(i).amt);



            sum+=foo3;



            garmentscount+= foo;


            //   quantity.put(filterdata2.get(i).noofpieces);

            btmtotal.setText(String.valueOf(Math.round(garmentscount)));
            s =  ((0.0/100) *sum)+sum;

            payamount= String.format("%.2f",s);

            tinyDB.putString("total", String.format("%.2f",s));


            grdtotal.setText(getResources().getString(R.string.rupee)+String.format("%.2f",s));


        }


        Adapter = new AdapterFish(this, filterdata2);
        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.setHasFixedSize(false);
        //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));

    }

    public class DataFish2 {
        public String item;
        public String noofpieces;
        public String cost;
        public String amt;


        public DataFish2(String item,String noofpieces,String cost,String amt){

            this.item = item;
            this.noofpieces = noofpieces;
            this.cost = cost;
            this.amt = amt;
        }

    }


    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<DataFish2> data2 = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<DataFish2> data5) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data2 = data5;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.rowform, parent, false);
            final AdapterFish.MyHolder holder = new AdapterFish.MyHolder(view);
            return holder;
        }


        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final AdapterFish.MyHolder myHolder = (AdapterFish.MyHolder) holder;
            //   mRVFishPrice.scrollToPosition(position);
            //    holder.setIsRecyclable(true);
            final DataFish2 current = data2.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);



            myHolder.item.setText(current.item);
            myHolder.noofpices.setText(current.noofpieces);
            myHolder.cost.setText(current.cost);
            myHolder.amount.setText(current.amt);
            myHolder.plus.setVisibility(View.GONE);
//            myHolder.minus.setVisibility(View.GONE);
            myHolder.delete.setVisibility(View.GONE);




        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data2.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView item;
            TextView noofpices;
            TextView cost;
            TextView amount;
            Button plus;
            ImageButton minus;
            ImageButton delete;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                item = (TextView)itemView.findViewById(R.id.item);
                noofpices = (TextView)itemView.findViewById(R.id.noofpices);
                cost = (TextView)itemView.findViewById(R.id.cost);
                amount = (TextView)itemView.findViewById(R.id.total);
                plus = (Button)itemView.findViewById(R.id.plus);
                minus = (ImageButton)itemView.findViewById(R.id.minus);
                delete = (ImageButton)itemView.findViewById(R.id.del);

                //  id= (TextView)itemView.findViewById(R.id.id);
            }


        }



    }


    private void proceedtopayment() {



        pd = new ProgressDialog(Paypage.this);
        pd.setMessage("processing payment..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", tinyDB.getString("jobid"));
            postdat.put("paymentMode", radiostatus);
            postdat.put("amountPayable", payamount);


        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());

        Log.e("paymentset",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"payment")
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
                        final Dialog openDialog = new Dialog(Paypage.this);
                        openDialog.setContentView(R.layout.alert);
                        openDialog.setTitle("No Internet");
                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                        dialogTextContent.setText("Looks like your device is offline");
                        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                        Button ok = (Button)openDialog.findViewById(R.id.dialog_button);
                        //  ok.setVisibility(View.GONE);
                        Button cancel = (Button)openDialog.findViewById(R.id.cancel);

                        cancel.setText("OK");


                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog.dismiss();

//                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(Puckup.this,Walletpayment.class);
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
                Log.e("com/wallet",mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("com/wallet",mMessage);
//                                 Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject json =  new JSONObject(mMessage);

                                String s = json.getString("statusCode");
                                double bal = json.getDouble("balanceAmountToPay");

                                if (s.equalsIgnoreCase("0")){


                                    final Dialog openDialog = new Dialog(Paypage.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Payments");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Transaction Failed.\n Please try again.");
                                    ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                    Button ok = (Button)openDialog.findViewById(R.id.dialog_button);
                                    //ok.setVisibility(View.GONE);
                                    Button cancel = (Button)openDialog.findViewById(R.id.cancel);
                                    cancel.setVisibility(View.GONE);

                                    ok.setText("OK");
                                    ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            openDialog.dismiss();

                                            Intent intent = new Intent(Paypage.this, Dashpage.class);
                                            startActivity(intent);

                                        }
                                    });


                                    openDialog.setCancelable(false);

                                    openDialog.show();


//                                        walletbalancetxt.setText( "Wallet Balance: "+R.string.rupee+"0.00");
                                }

                                else if (s.equalsIgnoreCase("1")){

                                    if (bal==0){

                                        final Dialog openDialog = new Dialog(Paypage.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("Payments");
                                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                        dialogTextContent.setText("Transaction Succesful.\n Thanks for chosing our services. \n\n ISTHREE");
                                        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                        Button ok = (Button)openDialog.findViewById(R.id.dialog_button);
                                        //ok.setVisibility(View.GONE);
                                        Button cancel = (Button)openDialog.findViewById(R.id.cancel);
                                        cancel.setVisibility(View.GONE);

                                        ok.setText("OK");
                                        ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                openDialog.dismiss();

                                                Intent intent = new Intent(Paypage.this, Dashpage.class);
                                                startActivity(intent);

                                            }
                                        });

                                        openDialog.setCancelable(false);


                                        openDialog.show();

                                    }
                                    else {

                                        final Dialog openDialog = new Dialog(Paypage.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("Payments");
                                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);

                                        dialogTextContent.setText("Transaction Pending, Please pay balance amount of "+getResources().getString(R.string.rupee)+ String.valueOf(bal)+" to the delivery agent");
                                        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
                                        Button ok = (Button)openDialog.findViewById(R.id.dialog_button);
                                        // ok.setVisibility(View.GONE);
                                        Button cancel = (Button)openDialog.findViewById(R.id.cancel);

                                        cancel.setVisibility(View.GONE);

                                        ok.setText("OK");
                                        ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                openDialog.dismiss();

                                                Intent intent = new Intent(Paypage.this, Dashpage.class);
                                                startActivity(intent);

                                            }
                                        });



                                        openDialog.setCancelable(false);
                                        openDialog.show();


                                    }



//                                        walletbalancetxt.setText( "Wallet Balance: "+getResources().getString(R.string.rupee)+ json.getString("availableFunds"));


//                                        dwall = Double.parseDouble(json.getString("availableFunds"));



                                }
                                Log.e("s",s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.cancel();
                        pd.dismiss();


                    }
                });
            }
        });



    }






    private void getwalletbalance() {


        pd = new ProgressDialog(Paypage.this);
        pd.setMessage("Getting your wallet balance..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();

        try {
            postdat.put("userId", tinyDB.getString("custid"));

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"checkWallet")
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
                        final Dialog openDialog = new Dialog(Paypage.this);
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
                pd.cancel();
                pd.dismiss();
                mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.e("com/wallet",mMessage);
                            // Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject json =  new JSONObject(mMessage);

                                String s = json.getString("statusCode");

                                if (s.equalsIgnoreCase("0")){

                                    walletbal = 0;

                                   walletbalancetxt.setText(R.string.rupee+"0.00");
                                }

                                else if (s.equalsIgnoreCase("1")){

    //                                tinydb.putString("walletbal",json.getString("availableFunds"));
  //
                                    walletbal = json.getDouble("availableFunds");

                                    Log.e("walletbalcene", String.valueOf(walletbal));

                                    walletbalancetxt.setText(getResources().getString(R.string.rupee)+ json.getString("availableFunds"));

                                }
                                Log.e("s",s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                else runOnUiThread(new Runnable() {
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
