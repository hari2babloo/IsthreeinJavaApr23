package com.a3x3conect.mobile.isthreeinjava;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ViewPager.Paypage;
import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.Models.Vouchermodels;
import com.example.hari.isthreeinjava.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class VouchersList extends AppCompatActivity {

    TinyDB tinyDB;
    View view;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    ProgressDialog pd;
        Button paybutton;
        Snackbar snackbar;

    TextView status;
    String mMessage;
    int vouposition;
    Double voucheramount,walletamount,totalamount,payableamount,afterwalletdedamount;
    List<Vouchermodels> jobOrder ;
    private AdapterFish Adapter;
    RecyclerView mRVFishPrice;
    TextView totaltxt,wallettxt,vouchervaluetxt,payableamttxt;
    ArrayList<DataFish2> filterdata2 = new ArrayList<>();
RelativeLayout relativeLayout4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vouchers_list);

        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);

        relativeLayout4 = (RelativeLayout)findViewById(R.id.relativeLayout3);
        relativeLayout4.setVisibility(View.GONE);
        tinyDB = new TinyDB(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        totaltxt = (TextView)findViewById(R.id.totaltxt);
        wallettxt = (TextView)findViewById(R.id.wallettxt);
        vouchervaluetxt = (TextView)findViewById(R.id.vouchervalue);
        payableamttxt = (TextView)findViewById(R.id.payableamt);

    payableamttxt.setText(getText(R.string.rupee)+tinyDB.getString("total"));
        totaltxt.setText(getText(R.string.rupee)+tinyDB.getString("total"));
        totalamount = Double.valueOf(tinyDB.getString("total"));
        walletamount = Double.valueOf(tinyDB.getString("walletbal"));
        wallettxt.setText(getText(R.string.rupee)+tinyDB.getString("walletbal"));
        paybutton = (Button)findViewById(R.id.pay);
       // paybutton.setText("Pay  "+getString(R.string.rupee)+tinyDB.getString("total"));
       // paybutton.setVisibility(View.GONE);
        afterwalletdedamount = Double.valueOf(tinyDB.getString("total"));
        getmyvouchers();
        paybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedtopayment();

            }
        });

    }

    private void proceedtopayment() {
        pd = new ProgressDialog(VouchersList.this);
        pd.setMessage("processing payment..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        try {
            postdat.put("customerId", tinyDB.getString("custid"));
            postdat.put("jobId", tinyDB.getString("jobid"));
            postdat.put("paymentMode", "Voucher Redemption");
            postdat.put("amountPayable", totalamount);
            postdat.put("voucherID",jobOrder.get(vouposition).getVoucherID());
            postdat.put("voucherSerialNumber", jobOrder.get(vouposition).getVoucherSerialNumber());
            postdat.put("voucherCode", jobOrder.get(vouposition).getVoucherCode());
            postdat.put("voucherValue", jobOrder.get(vouposition).getVoucherValue());
            postdat.put("voucherStartDate", jobOrder.get(vouposition).getVoucherStartDate());
            postdat.put("voucherExpiryDate", jobOrder.get(vouposition).getVoucherExpiryDate());
            postdat.put("voucherUserName", jobOrder.get(vouposition).getVoucherUserName());
            postdat.put("voucherUserEmail", jobOrder.get(vouposition).getVoucherUserEmail());
            postdat.put("voucherUserMobile", jobOrder.get(vouposition).getVoucherUserMobile());
            postdat.put("voucherStatus", jobOrder.get(vouposition).getVoucherStatus());
            postdat.put("voucherType", jobOrder.get(vouposition).getVoucherType());
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
                        final Dialog openDialog = new Dialog(VouchersList.this);
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
                                if (s.equalsIgnoreCase("0")){
                                    final Dialog openDialog = new Dialog(VouchersList.this);
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
                                            Intent intent = new Intent(VouchersList.this, Dashpage.class);
                                            startActivity(intent);
                                        }
                                    });
                                    openDialog.show();
//                                        walletbalancetxt.setText( "Wallet Balance: "+R.string.rupee+"0.00");
                                }
                                else
                                    if (s.equalsIgnoreCase("2")){
                                    final Dialog openDialog = new Dialog(VouchersList.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Payments");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText(json.getString("statusMessage"));
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
                                            Intent intent = new Intent(VouchersList.this, Paypage.class);
                                            startActivity(intent);
                                        }
                                    });
                                    openDialog.show();
//                                        walletbalancetxt.setText( "Wallet Balance: "+R.string.rupee+"0.00");
                                }
                                else if (s.equalsIgnoreCase("1")){
                                    double bal = json.getDouble("balanceAmountToPay");
                                    if (bal==0){
                                        final Dialog openDialog = new Dialog(VouchersList.this);
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

                                                Intent intent = new Intent(VouchersList.this, Dashpage.class);
                                                startActivity(intent);

                                            }
                                        });



                                        openDialog.show();

                                    }
                                    else {

                                        final Dialog openDialog = new Dialog(VouchersList.this);
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

                                                Intent intent = new Intent(VouchersList.this, Dashpage.class);
                                                startActivity(intent);

                                            }
                                        });



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

    private void calculations(){
        pd = new ProgressDialog(VouchersList.this);
        pd.setMessage("Applying Calculations..");
        pd.setCancelable(false);
        pd.show();
    }

    private void getmyvouchers() {
        pd = new ProgressDialog(VouchersList.this);
        pd.setMessage("Getting Your Orders..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postdat = new JSONObject();
        try {
            postdat.put("customerEmail",tinyDB.getString("custEmail"));

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"userVouchers")
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
                        final Dialog openDialog = new Dialog(VouchersList.this);
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

                String mMessageejkl = e.getMessage().toString();
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

                            try {

                                JSONArray jsonArray = new JSONArray(mMessage);
                                for(int j = 0; j < jsonArray.length(); j++){
                                    String ss = jsonArray.get(j).toString();
                                    JSONObject  jsonObject = new JSONObject(ss);
                                    if(jsonObject.optString("statusCode").equalsIgnoreCase("0")){
                                       // status.setText("You have no Orders to display");
                                      //  status.setVisibility(View.VISIBLE);
                                        mRVFishPrice.setVisibility(View.GONE);

                                        final Dialog openDialog = new Dialog(VouchersList.this);
                                        openDialog.setContentView(R.layout.alert);
                                        openDialog.setTitle("No Vouchers");
                                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                        dialogTextContent.setText("You have No vouchers to display");
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
                                                Intent intent = new Intent(VouchersList.this,Paypage.class);
                                                startActivity(intent);
                                            }
                                        });
                                        openDialog.setCancelable(false);
                                        openDialog.show();
                                        break;
                                    }
                                    else {
                                        TraverseData();
                                        break;
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("Resy",mMessage);
                            // Toast.makeText(VouchersList, mMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });
    }

    private void TraverseData() {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Vouchermodels>>(){}.getType();
        jobOrder = (List<Vouchermodels>)  gson.fromJson(mMessage,listType);
        for(int j = 0; j < jobOrder.size(); j++){
            DataFish2 dd = new DataFish2(jobOrder.get(j).getVoucherCode(),jobOrder.get(j).getVoucherValue(),jobOrder.get(j).getVoucherExpiryDate(),jobOrder.get(j).getVoucherType());
            filterdata2.add(dd);
        }
        Adapter = new AdapterFish(VouchersList.this, filterdata2);
        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.setHasFixedSize(false);
        mRVFishPrice.scrollToPosition(0);
        //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    public class DataFish2 {
        public String vcode;
        public String vamt;
        public String vexpiry;
        public  String vtype;


        public DataFish2(String vcode,String vamt,String vexpiry,String vtype ){

            this.vcode = vcode;
            this.vamt = vamt;
            this.vexpiry = vexpiry;
            this.vtype = vtype;

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
            View view = inflater.inflate(R.layout.voucherlisttemplate, parent, false);
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



            if (current.vtype.equalsIgnoreCase("2")){


                myHolder.vcode.setText("WELCOME"+current.vamt);

            }
            else {

                myHolder.vcode.setText(current.vcode);
            }

            myHolder.vamount.setText("SAVE  "+ getString(R.string.rupee)+ current.vamt);
            myHolder.vexpiry.setText("Expires on : "+current.vexpiry);
            myHolder.apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    voucheramount = Double.valueOf(filterdata2.get(position).vamt);

                    vouposition = position;
                    if (voucheramount*2<totalamount){

                        vouchervaluetxt.setText(getString(R.string.rupee)+filterdata2.get(position).vamt);
                        Log.e("detaos; ", String.valueOf(voucheramount)+ String.valueOf(totalamount));
                        View parentLayout = findViewById(android.R.id.content);
                        snackbar = Snackbar.make(parentLayout,"Voucher Applied",Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        payableamount =  totalamount-voucheramount;
                        payableamttxt.setText(getString(R.string.rupee)+String.valueOf(payableamount));
//                        paybutton.setText("Pay  "+getString(R.string.rupee)+String.valueOf(payableamount));
                        afterwalletdedamount = walletamount-payableamount;
 /* afterwalletdeductiontxt.setText(getString(R.string.rupee)+String.valueOf(afterwalletdedamount)); */
                        if (walletamount>=0){
                            relativeLayout4.setVisibility(View.VISIBLE);
                        }


//                        paybutton.setText("Pay  "+getString(R.string.rupee)+String.valueOf(afterwalletdedamount));
//                        paybutton.setVisibility(View.VISIBLE);
                    }
                    else {
                        View parentLayout = findViewById(android.R.id.content);
                        snackbar = Snackbar.make(parentLayout,"Minimum Cart Value should be "+getString(R.string.rupee)+String.valueOf(voucheramount*2),Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
            });

//           myHolder.date.setText(current.date);


        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data2.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView vcode;
            TextView vamount;
            TextView vexpiry;
            Button apply;



            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                vcode = (TextView)itemView.findViewById(R.id.voucher);
                vamount = (TextView)itemView.findViewById(R.id.value);
                vexpiry = (TextView)itemView.findViewById(R.id.expirydate);
                apply = (Button)itemView.findViewById(R.id.apply);
                //  id= (TextView)itemView.findViewById(R.id.id);
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
