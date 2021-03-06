package com.a3x3conect.mobile.isthreeinjava;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.example.hari.isthreeinjava.Models.modelmyorders;
import com.example.hari.isthreeinjava.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyOrderDetails extends AppCompatActivity {



    ProgressDialog pd;
    String mMessage;

    Button home;
    TextView jobid,status,date,grantotal,custid,totalqt,expresscharg,expresschargetxt,grandtotalamt,washqtyvalue,ironingchargesvalue,deliveryonhangervalue;
    TableRow deliveronhanger,washcharges,washquantity;

    TinyDB tinyDB;

    List<modelmyorders> jobOrder ;
    float sum = 0;
    int totalcount = 0;
    double d=0;
    TextView grdtotal;
    private AdapterFish Adapter;
    RecyclerView mRVFishPrice;
    Integer position;
    ArrayList<DataFish2> filterdata2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        home = findViewById(R.id.home);

        grdtotal = findViewById(R.id.grdtotal);
        totalqt = findViewById(R.id.btmtotal);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyOrderDetails.this, Dashpage.class);
                startActivity(intent);
            }
        });
        mRVFishPrice = findViewById(R.id.fishPriceList);
        jobid = findViewById(R.id.jobid);
        status = findViewById(R.id.delstatus);
        date = findViewById(R.id.date);
        grantotal = findViewById(R.id.grandtotal);
        expresscharg = findViewById(R.id.expresscharg);
        expresschargetxt = findViewById(R.id.expresscharg2);
        grandtotalamt = findViewById(R.id.grandtotalamt);
        custid = findViewById(R.id.custid);
        washqtyvalue = findViewById(R.id.washqtyvalue);
        ironingchargesvalue = findViewById(R.id.ironingchargesvalue);
        deliveryonhangervalue = findViewById(R.id.deliveryonhangervalue);
        deliveronhanger = findViewById(R.id.deliveronhanger);
        washcharges = findViewById(R.id.washcharges);

        washquantity = findViewById(R.id.washquantity);

        Bundle bundle = getIntent().getExtras();
        mMessage = bundle.getString("message");
        position = bundle.getInt("position");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<modelmyorders>>(){}.getType();
        jobOrder = gson.fromJson(mMessage,listType);

        jobOrder.get(position).getDate();


        jobid.setText(jobOrder.get(position).getJobid());
        date.setText(jobOrder.get(position).getDate());
        status.setText(jobOrder.get(position).getDeliveryStatus());
        grantotal.setText(jobOrder.get(position).getGrandTotal());
        custid.setText(jobOrder.get(position).getCustomerId());



        for(int j = 0; j < jobOrder.get(position).getCategory().size(); j++){

            //           Log.e("String",jobOrder.get(j).toString());


       //     DataFish2 dd = new DataFish2("","","","");

            DataFish2 dd = new DataFish2(jobOrder.get(position).getCategory().get(j),jobOrder.get(position).getPrice().get(j),jobOrder.get(position).getQuantity().get(j),jobOrder.get(position).getSubTotal().get(j));


            Log.e("subtotal",jobOrder.get(position).getSubTotal().get(j));


            filterdata2.add(dd);

            Log.e("sum", String.valueOf(sum));






        }
        List<String> s =  jobOrder.get(position).getSubTotal();
        if (jobOrder.get(position).getDeliverOnHanger().equalsIgnoreCase("1")){

            deliveryonhangervalue.setText("YES");
        }

        else {

            deliveronhanger.setVisibility(View.GONE);



        }

        if (jobOrder.get(position).getServiceName().equalsIgnoreCase("washAndPress")){

            washqtyvalue.setText(jobOrder.get(position).getWashQuantity() + " Kg(s)");
            ironingchargesvalue.setText(jobOrder.get(position).getWashServiceCharge());




        }
        else {

            washcharges.setVisibility(View.GONE);
            //  washqtyvalue.setVisibility(View.GONE);
            // ironingchargesvalue.setVisibility(View.GONE);
            washquantity.setVisibility(View.GONE);


        }
        for (int i=0; i<s.size();i++){


            float foo = Float.parseFloat(jobOrder.get(position).getSubTotal().get(i));

            int foo2 = Integer.parseInt((jobOrder.get(position).getQuantity().get(i)));

            totalcount+=foo2;
            sum+=foo;



        }

         d = Double.valueOf(jobOrder.get(position).getExpressDeliveryCharge());
        grdtotal.setText(getResources().getString(R.string.rupee)+String.format("%.2f",sum));
        expresscharg.setText(getResources().getString(R.string.rupee)+jobOrder.get(position).getExpressDeliveryCharge());

        if (jobOrder.get(position).getExpressDeliveryCharge().equalsIgnoreCase("0")){

            expresscharg.setVisibility(View.GONE);
            expresschargetxt.setVisibility(View.GONE);

        }



        grandtotalamt.setText( getResources().getString(R.string.rupee)+jobOrder.get(position).getGrandTotal());

        totalqt.setText(String.valueOf(totalcount));


        Log.e("log",jobOrder.get(position).getDate());

//        for(int j = 0; j < jobOrder.size(); j++){
//
//
//            //           Log.e("String",jobOrder.get(j).toString());
//
//
//            MyOrders.DataFish2 dd = new MyOrders.DataFish2(jobOrder.get(j).getDate(),jobOrder.get(j).getDeliveryStatus(),jobOrder.get(j).getInvoiceId());
//
//            filterdata2.add(dd);
//
//
//
//
//
//        }


        Adapter = new AdapterFish(MyOrderDetails.this, filterdata2);
        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.setHasFixedSize(false);
        //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(MyOrderDetails.this,LinearLayoutManager.VERTICAL,true));
    }

    public class DataFish2 {
        public String category;
        public String price;
        public String quantity;
        public String subtotal;



        public DataFish2(String category,String price,String quantity,String subtotal){

            this.category = category;
            this.price = price;
            this.quantity = quantity;
            this.subtotal = subtotal;
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



            myHolder.item.setText(current.category);
            myHolder.noofpices.setText(current.quantity);
            myHolder.cost.setText(current.price);
            myHolder.amount.setText(current.subtotal);
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
                item = itemView.findViewById(R.id.item);
                noofpices = itemView.findViewById(R.id.noofpices);
                cost = itemView.findViewById(R.id.cost);
                amount = itemView.findViewById(R.id.total);
                plus = itemView.findViewById(R.id.plus);
                minus = itemView.findViewById(R.id.minus);
                delete = itemView.findViewById(R.id.del);

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
