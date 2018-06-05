package com.wallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;

import android.os.Build;
import android.support.annotation.Nullable;
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
import android.widget.TextView;

import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.Models.WalletHistorymodel;
import com.example.hari.isthreeinjava.R;

import com.squareup.okhttp.MediaType;


public class Wallet extends Fragment {

    View view;

    TextView bal;
    Button btnwallettransfer;
    ProgressDialog pd;
    String mMessage;
    RecyclerView mRVFishPrice;

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    TinyDB tinyDB;

    public Wallet(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.wallet, container, false);

            tinyDB  = new TinyDB(getContext());

    bal = (TextView)view.findViewById(R.id.bal);



        return view;


    }


//    private void gethistory() {
//
//        pd = new ProgressDialog(Wallet.this);
//        pd.setMessage("Getting Your history..");
//        pd.setCancelable(false);
//        pd.show();
//
//        final OkHttpClient okHttpClient = new OkHttpClient();
//        JSONObject postdat = new JSONObject();
//
//        try {
//            postdat.put("customerId", tinyDB.getString("custid"));
//
//        } catch(JSONException e){
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
//
//        Log.e("data",postdat.toString());
//        final Request request = new Request.Builder()
//                .url(getString(R.string.baseurl)+"walletHistory")
//                .post(body)
//                .build();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//
//                pd.cancel();
//                pd.dismiss();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final Dialog openDialog = new Dialog(Wallet.this);
//                        openDialog.setContentView(R.layout.alert);
//                        openDialog.setTitle("No Internet");
//                        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
//                        dialogTextContent.setText("Looks like your device is offline");
//                        ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
//                        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
//                        dialogCloseButton.setVisibility(View.GONE);
//                        Button dialogno = (Button)openDialog.findViewById(R.id.cancel);
//
//                        dialogno.setText("OK");
//
//
//                        dialogno.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                openDialog.dismiss();
//
////                                                //                                          Toast.makeText(Puckup.this, jsonResponse.getString("status"), Toast.LENGTH_SHORT).show();
////                                                Intent intent = new Intent(Puckup.this,Dashpage.class);
////                                                startActivity(intent);
//                            }
//                        });
//
//
//
//                        openDialog.show();
//
//                    }
//                });
//
//
//                String mMessage = e.getMessage().toString();
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//
//
//                pd.cancel();
//                pd.dismiss();
//                mMessage = response.body().string();
//                Log.e("mmessage",mMessage.toString());
//
//                if (response.isSuccessful()){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            try {
//
//                                JSONArray jsonArray = new JSONArray(mMessage);
//
//                                for(int j = 0; j < jsonArray.length(); j++){
//
//                                    String ss = jsonArray.get(j).toString();
//
//                                    JSONObject  jsonObject = new JSONObject(ss);
//
//                                    if(jsonObject.optString("statusCode").equalsIgnoreCase("0")){
//
//
//                                        mRVFishPrice.setVisibility(View.GONE);
//                                        break;
//
//
//                                    }
//                                    else {
//
//                                        TraverseData();
//                                        break;
//                                    }
//                                }
//
//
//
//
//
//
//
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//
//                            Log.e("Resy",mMessage);
//                            // Toast.makeText(Wallet.this, mMessage, Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    });
//                }
//                else  runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                    }
//                });
//            }
//        });
//    }
//
//    private void TraverseData() {
//
//
//        Gson gson = new Gson();
//        Type listType = new TypeToken<List<WalletHistorymodel>>(){}.getType();
//        jobOrder = (List<WalletHistorymodel>)  gson.fromJson(mMessage,listType);
//
//
//
//        for(int j = 0; j < jobOrder.size(); j++){
//
//            DataFish2 dd = new DataFish2(jobOrder.get(j).getJobId(),jobOrder.get(j).getTransactionAmount(),jobOrder.get(j).getTransactionTime(),jobOrder.get(j).getTransactionType(),jobOrder.get(j).getAmountPayable(),  jobOrder.get(j).getBalanceAddedToWallet());
//            filterdata2.add(dd);
//
//        }
//
//
//        Adapter = new AdapterFish(Wallet.this, filterdata2);
//        Adapter.setHasStableIds(false);
//        mRVFishPrice.setAdapter(Adapter);
//        mRVFishPrice.setHasFixedSize(false);
//        mRVFishPrice.scrollToPosition(0);
//        //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
//        //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
//        mRVFishPrice.setLayoutManager(new LinearLayoutManager(Wallet.this,LinearLayoutManager.VERTICAL,false));
//
//
//
//
//    }
//
//    public class DataFish2 {
//        public String one;
//        public String two;
//        public String three;
//
//        public String four;
//        public String five;
//        public String six;
//
//
//
//
//
//        public DataFish2(String one,String two,String three,String four,String five,String six){
//
//            this.one = one;
//            this.two = two;
//            this.three = three;
//            this.four = four;
//            this.five = five;
//            this.six = six;
//
//        }
//
//    }
//
//
//    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//        List<DataFish2> data2 = Collections.emptyList();
//
//        int currentPos = 0;
//        private Context context;
//        private LayoutInflater inflater;
//        // create constructor to innitilize context and data sent from MainActivity
//        public AdapterFish(Context context, List<DataFish2> data5) {
//            this.context = context;
//            inflater = LayoutInflater.from(context);
//            this.data2 = data5;
//        }
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = inflater.inflate(R.layout.rowwallethistory, parent, false);
//            final AdapterFish.MyHolder holder = new AdapterFish.MyHolder(view);
//
//
//
//            return holder;
//        }
//
//
//        // Bind data
//        @Override
//        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//
//            // Get current position of item in recyclerview to bind data and assign values from list
//            final AdapterFish.MyHolder myHolder = (AdapterFish.MyHolder) holder;
//
//
//
//            //   mRVFishPrice.scrollToPosition(position);
//            //    holder.setIsRecyclable(true);
//
//            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent intent = new Intent(Wallet.this,MyOrderDetails.class);
//
//                    intent.putExtra("message",mMessage);
//                    intent.putExtra("position",position);
//                    startActivity(intent);
//
//
//                }
//            });
//            final DataFish2 current = data2.get(position);
//            //  holder.getLayoutPosition();
//            //    setHasStableIds(true);
//
//
//            myHolder.one.setText(current.one);
//            myHolder.two.setText(current.two);
//            myHolder.three.setText(current.three);
//            myHolder.four.setText(current.two);
//
////            if (current.four.equalsIgnoreCase("DEBIT")){
////
////                myHolder.four.setTextColor(Color.RED);
////                myHolder.four.setText(current.four);
////            }
////            else if (current.four.equalsIgnoreCase("CREDIT")){
////                myHolder.four.setText(current.four);
////                myHolder.four.setTextColor(Color.GREEN);
////
////            }
//
//            myHolder.five.setText(current.five);
//            myHolder.six.setText(current.six);
//
//
//        }
//
//        // return total item from List
//        @Override
//        public int getItemCount() {
//            return data2.size();
//        }
//
//
//        class MyHolder extends RecyclerView.ViewHolder {
//            TextView one,two,three,four,five,six;
//
//
//
//            // create constructor to get widget reference
//            public MyHolder(View itemView) {
//                super(itemView);
//                one = (TextView)itemView.findViewById(R.id.one);
//                two = (TextView)itemView.findViewById(R.id.two);
//                three = (TextView)itemView.findViewById(R.id.three);
//                four = (TextView)itemView.findViewById(R.id.four);
//                five = (TextView)itemView.findViewById(R.id.five);
//                six = (TextView)itemView.findViewById(R.id.six);
//
//
//                //  id= (TextView)itemView.findViewById(R.id.id);
//            }
//
//
//        }
//
//
//
//    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
