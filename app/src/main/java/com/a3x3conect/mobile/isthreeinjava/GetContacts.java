package com.a3x3conect.mobile.isthreeinjava;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hari.isthreeinjava.Dashpage;
import com.example.hari.isthreeinjava.Models.TinyDB;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onegravity.contactpicker.ContactElement;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.group.Group;
import com.onegravity.contactpicker.picture.ContactPictureType;



import com.example.hari.isthreeinjava.R;
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
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GetContacts extends BaseActivity {
//    private static final int REQUEST_CODE = 1;
//    Button getcontacts;
private static final String EXTRA_DARK_THEME = "EXTRA_DARK_THEME";
    private static final String EXTRA_GROUPS = "EXTRA_GROUPS";
    private static final String EXTRA_CONTACTS = "EXTRA_CONTACTS";

    RecyclerView mRVFishPrice;
    private static final int REQUEST_CONTACT = 0;
    ArrayList<DataFish> filterdata2=new ArrayList<DataFish>();

    ArrayList<String> phn = new ArrayList<String>();
    private AdapterFish Adapter;
    ArrayList<DataFish> filterdata=new ArrayList<DataFish>();
    private boolean mDarkTheme;
    private List<Contact> mContacts;
    private List<Group> mGroups;
    ProgressDialog pd;
    Snackbar snackbar;
    String stringphno;
    TinyDB tinydb;
    Button button;
    TextView pickedconta;
    RelativeLayout relativeLayoutbtm;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // read parameters either from the Intent or from the Bundle
        if (savedInstanceState != null) {
           // mDarkTheme = savedInstanceState.getBoolean(EXTRA_DARK_THEME);
            mGroups = (List<Group>) savedInstanceState.getSerializable(EXTRA_GROUPS);
            mContacts = (List<Contact>) savedInstanceState.getSerializable(EXTRA_CONTACTS);
        }
        else {
            Intent intent = getIntent();
            //mDarkTheme = intent.getBooleanExtra(EXTRA_DARK_THEME, false);
            mGroups = (List<Group>) intent.getSerializableExtra(EXTRA_GROUPS);
            mContacts = (List<Contact>) intent.getSerializableExtra(EXTRA_CONTACTS);
        }

        //setTheme(mDarkTheme ? R.style.Theme_Dark : R.style.Theme_Light);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // set layout
        setContentView(R.layout.get_contacts);
        mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList2);
        tinydb = new TinyDB(this);
        send = (Button)findViewById(R.id.send);
        relativeLayoutbtm = (RelativeLayout)findViewById(R.id.relativebottom);
        pickedconta = (TextView)findViewById(R.id.pickedconta);
        relativeLayoutbtm.setVisibility(View.GONE);


        //send.setVisibility(View.GONE);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senddatatoserver();
            }
        });

        // configure "pick contact(s)" button
         button = (Button) findViewById(R.id.pick_contact);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {



                @Override
                public void onClick(View v) {

                    phn.clear();
                    filterdata2.clear();
                    Intent intent = new Intent(GetContacts.this, ContactPickerActivity.class)
                            //.putExtra(ContactPickerActivity.EXTRA_THEME, mDarkTheme ? R.style.Theme_Dark : R.style.Theme_Light)
                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name())
                            .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.PHONE.toString())
                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                            //.putExtra(ContactPickerActivity.EXTRA_ONLY_CONTACTS_WITH_PHONE,true)
                            .putExtra(ContactPickerActivity.EXTRA_LIMIT_REACHED_MESSAGE,3)
                            .putExtra(ContactPickerActivity.EXTRA_SELECT_CONTACTS_LIMIT,3)
                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name());

                    startActivityForResult(intent, REQUEST_CONTACT);
                }
            });
        }
        else {
            finish();
        }

        // populate contact list
     //   populateContactList(mGroups, mContacts);


//        getcontacts = (Button)findViewById(R.id.contacts);
//
//        getcontacts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getcontacts();
//            }
//        });

    }

    private void senddatatoserver() {


        pd = new ProgressDialog(GetContacts.this);
        pd.setMessage("Sending Referral SMS..");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        JSONObject postdat = new JSONObject();
        JSONArray referralnames = new JSONArray();
        JSONArray referralphnos = new JSONArray();

        for (int i = 0; i < filterdata2.size(); i++) {
             referralnames.put(filterdata2.get(i).name);
                referralphnos.put(filterdata2.get(i).phone);
        }
        try {
            postdat.put("referrerId",tinydb.getString("custid"));
            postdat.put("referrerName",tinydb.getString("name"));
            postdat.put("referrerMobile",tinydb.getString("custphno"));
            postdat.put("referralNames",referralnames);
            postdat.put("referralMobiles",referralphnos);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,postdat.toString());
        Log.e("image",postdat.toString());
        final Request request = new Request.Builder()
                .url(getString(R.string.baseurl)+"referUser")
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

                        final Dialog openDialog = new Dialog(GetContacts.this);
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
                                                Intent intent = new Intent(GetContacts.this,Dashpage.class);
                                                startActivity(intent);
                            }
                        });


                        openDialog.setCancelable(false);
                        openDialog.show();



                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {

            final String mMessage = response.body().string();
                Log.e("mfddsf",mMessage);
                pd.dismiss();
                pd.cancel();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                JSONObject jsonObject = new JSONObject(mMessage);
                                if (jsonObject.getString("statusCode").equalsIgnoreCase("0")){
                                    JSONArray jsonArray = jsonObject.getJSONArray("existingNumbers");
                                    Log.e("jsonarray", String.valueOf(jsonArray));
                                    String numbers = String.valueOf(jsonArray);
//                                    numbers = String.replaceAll("["," ", numbers);
                                    numbers= numbers.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\"","");
                                    Log.e("NUMBER", numbers);
                                    final Dialog openDialog = new Dialog(GetContacts.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Referral");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Referral couldn't be sent to "  +numbers+  "  as the given number is already registered or referred by someone.");
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
                                                Intent intent = new Intent(GetContacts.this,Dashpage.class);
                                                startActivity(intent);
                                        }
                                    });
                                    openDialog.setCancelable(false);
                                    openDialog.show();
                                }

                                else if (jsonObject.getString("statusCode").equalsIgnoreCase("1")){
                                    final Dialog openDialog = new Dialog(GetContacts.this);
                                    openDialog.setContentView(R.layout.alert);
                                    openDialog.setTitle("Referral");
                                    TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                                    dialogTextContent.setText("Referrals sent Succesfully.");
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
                                                Intent intent = new Intent(GetContacts.this,Dashpage.class);
                                                startActivity(intent);
                                        }
                                    });
                                    openDialog.setCancelable(false);
                                    openDialog.show();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putBoolean(EXTRA_DARK_THEME, mDarkTheme);
        if (mGroups != null) {
            outState.putSerializable(EXTRA_GROUPS, (Serializable) mGroups);
        }
        if (mContacts != null) {
            outState.putSerializable(EXTRA_CONTACTS, (Serializable) mContacts);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK && data != null &&
                (data.hasExtra(ContactPickerActivity.RESULT_GROUP_DATA) ||
                        data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA))) {
            // we got a result from the contact picker --> show the picked contacts
            //mGroups = (List<Group>) data.getSerializableExtra(ContactPickerActivity.RESULT_GROUP_DATA);
            mContacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
            populateContactList(mContacts);

        }
    }
    private void populateContactList(List<Contact> contacts) {
        // we got a result from the contact picker --> show the picked contacts
        //TextView contactsView = (TextView) findViewById(R.id.contacts);
        SpannableStringBuilder result = new SpannableStringBuilder();

        //Toast.makeText(this, "Contacts without Phone numbers/Duplicates ll not be added.", Toast.LENGTH_SHORT).show();
        if ((contacts != null) && !contacts.isEmpty()){
            for (int i = 0; i < contacts.size(); i++) {

                if (contacts.get(i).getPhone(0)!=null && !contacts.get(i).getPhone(0).isEmpty() && contacts.get(i).getPhone(0).length()>5){
                    stringphno = contacts.get(i).getPhone(0).replaceAll("\\s+", "");
                    stringphno = stringphno.replaceAll("-","");
                    stringphno = stringphno.substring(stringphno.length() - 10);
                    Log.e("Segregared",stringphno);
                }

                if (stringphno!=null&& !stringphno.isEmpty() && stringphno.length()>5 ){

//            if (contacts.get(i).getPhone(0)!=null&& !contacts.get(i).getPhone(0).isEmpty() && contacts.get(i).getPhone(0).length()>5 ){
                    if (phn.contains(stringphno)){
                        Log.e("Duplicate phn",stringphno);
                        View parentLayout = findViewById(android.R.id.content);
                        snackbar = Snackbar.make(parentLayout,"You have added duplicate Contacts",Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        //  Toast.makeText(this, "You have added duplicate Contacts", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        phn.add(stringphno);

                        Log.e("noDuplicate phn",stringphno);
                        DataFish mm = new DataFish(contacts.get(i).getFirstName(),stringphno);
                        filterdata2.add(mm);
                    }

                }

                else {

                    View parentLayout = findViewById(android.R.id.content);
                    snackbar = Snackbar.make(parentLayout,"Provide Correct Contacts Details",Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    // Toast.makeText(this, "Provide Correct Contacts", Toast.LENGTH_SHORT).show();
                }

            }
        }

        else {

            View parentLayout = findViewById(android.R.id.content);
            snackbar = Snackbar.make(parentLayout,"Provide Correct Contacts Details",Snackbar.LENGTH_SHORT);
            snackbar.show();
        }


        AddtoList();

            String regexStr = "[0-9]";
//           if (filterdata2.contains(mm)){
//               Log.e("duplicates","Duplicate");
//           }
//           else

               //{

//            if (filterdata2.equals(contacts.get(i).getPhone(0))){
//
//                Log.e("COntains Duplicate phn",contacts.get(i).getPhone(0));
//            }
//
//            else {
//
//                Log.e( "COntains noDuplicate mm",contacts.get(i).getPhone(0));
//            }
//
//            if (filterdata2.equals(mm)){
//
//                Log.e("COntains Duplicate mm",contacts.get(i).getPhone(0));
//            }
//
//            else{
//
//                Log.e("COntains noDuplicate mm",contacts.get(i).getPhone(0));
//            }
//
//
//
//
//            if (contacts.get(i).getPhone(0)!=null&& !contacts.get(i).getPhone(0).isEmpty() && contacts.get(i).getPhone(0).length()>5 && !filterdata2.contains(mm) ){
//
//
//
//
//
//            }






            //mContacts.get(i).getPhone(0);
            //  montacts.get(i).getFirstName();

      //  }
//


//        try {
//            if (groups != null && ! groups.isEmpty()) {
//                result.append("GROUPS\n");
//                for (Group group : groups) {
//                    populateContact(result, group, "");
//                    for (Contact contact : group.getContacts()) {
//
//
//
//                        populateContact(result, contact, "    ");
//                    }
//
//
//
//                }
//            }
//            if (contacts != null && ! contacts.isEmpty()) {
//                result.append("CONTACTS\n");
//                for (Contact contact : contacts) {
//
//
////                    contacts.get(0)
//                    populateContact(result, contact, "");
//                }
//
//
//                for (int i = 0; i < contacts.size(); i++) {
//
//
//
//
//                    DataFish mm = new DataFish(contacts.get(i).getFirstName(),contacts.get(i).getPhone(0));
//
//
//                    //mContacts.get(i).getPhone(0);
//                  //  mContacts.get(i).getFirstName();
//
//                    filterdata2.add(mm);
//                }
//
//                Log.e("filterdata", String.valueOf(filterdata2));
//
//
//
//
//                Log.e("ContactsData", String.valueOf(contacts.get(0).getPhone(0)));
//            }
//
//
//        }
//        catch (Exception e) {
//            result.append(e.getMessage());
//        }
//
//        contactsView.setText(result);

    }

//    private void populateContact(SpannableStringBuilder result, ContactElement element, String prefix) {
//        //int start = result.length();
//        String displayName = element.getDisplayName();
//
//
//            AddtoList();
//
//
//        Long displayphone = element.getId();
//
//        //result.append(element.getDisplayName());
//       // Log.e("phne", String.valueOf(displayName)+ element.);
//        result.append(prefix);
//        result.append(displayName + "\n");
//        //result.setSpan(new BulletSpan(15), start, result.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//    }
    private void AddtoList() {

        if (filterdata2.size()>0){
            relativeLayoutbtm.setVisibility(View.VISIBLE);
           // button.setVisibility(View.GONE);
        }


        else {
            relativeLayoutbtm.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }
        //qty.setText("");
        Adapter = new AdapterFish(GetContacts.this,filterdata2);
        Adapter.setHasStableIds(false);
        mRVFishPrice.setAdapter(Adapter);
        mRVFishPrice.setHasFixedSize(false);
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(GetContacts.this,LinearLayoutManager.VERTICAL,false));
//        float sum = 0;
//        for (int i = 0; i < filterdata2.size(); i++) {
//
//            //Float dd = Float.parseFloat(filterdata2.get(i).amt);
//          //  sum += dd;
//        }
        //  btmamt.setText("Sub Total = " +String.valueOf(sum));

    }

    private class DataFish {

        public String name;
        public String phone;


        public DataFish(String Name, String Phone) {
            name = Name;
            phone = Phone;

        }
    }

    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<DataFish> data2 = Collections.emptyList();
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;
        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<DataFish> data5) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data2 = data5;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.getcontactstemplate, parent, false);
            final MyHolder holder = new MyHolder(view);
            return holder;
        }


        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final MyHolder myHolder = (MyHolder) holder;
            //   mRVFishPrice.scrollToPosition(position);
            //    holder.setIsRecyclable(true);
            final DataFish current = data2.get(position);
            //  holder.getLayoutPosition();
            //    setHasStableIds(true);


//
//            if (filterdata2.size()>0){
//
//                relativeLayoutbtm.setVisibility(View.VISIBLE);
//            }
            myHolder.name.setText(current.name);
            myHolder.number.setText(current.phone);


            myHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                   Toast.makeText(context, Integer.toString(position), Toast.LENGTH_SHORT).show();

                    filterdata2.remove(position);
                    phn.remove(position);


                    if (filterdata2.size()>0){

                        relativeLayoutbtm.setVisibility(View.VISIBLE);
                       // button.setVisibility(View.GONE);
                    }
                    else {

                        relativeLayoutbtm.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                    }

                    //  dd.add(current.item);
                    //                  tarif.add("fsd","rtt","trer");
                    //                 Adapter.notifyDataSetChanged();

                    Adapter = new AdapterFish(GetContacts.this, filterdata2);
                    Adapter.setHasStableIds(false);
                    mRVFishPrice.setAdapter(Adapter);
                    mRVFishPrice.setHasFixedSize(false);
                    mRVFishPrice.setLayoutManager(new LinearLayoutManager(GetContacts.this,LinearLayoutManager.VERTICAL,false));
                }
            });




        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data2.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView number;
            ImageView delete;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                name = (TextView)itemView.findViewById(R.id.name);
                number = (TextView)itemView.findViewById(R.id.number);
                delete = (ImageView) itemView.findViewById(R.id.delete);
                //  id= (TextView)itemView.findViewById(R.id.id);
            }


        }



    }

//    private void getcontacts() {
//
//        Intent phonebookIntent = new Intent("intent.action.INTERACTION_TOPMENU");
//        phonebookIntent.putExtra("additional", "phone-multi");
//        phonebookIntent.putExtra("maxRecipientCount", 5);
//        phonebookIntent.putExtra("FromMMS", true);
//        startActivityForResult(phonebookIntent, REQUEST_CODE);
//
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if(resultCode==RESULT_OK)
//        {
//
//            if(requestCode == REQUEST_CODE  )
//            {
//
//                Bundle bundle =  data.getExtras();
//
//                String result= bundle.getString("result");
//                ArrayList<String> contacts = bundle.getStringArrayList("result");
//
//
//                Log.e("Hello", "launchMultiplePhonePicker bundle.toString()= " + result.toString() );
//
//            }
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }

//    private void getcontacts() {
//
//        Uri uri = Uri.parse("content://contacts");
//        Intent intent = new Intent(Intent.ACTION_PICK, uri);
//        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
//        startActivityForResult(intent, REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent intent) {
//        if (requestCode == REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Uri uri = intent.getData();
//                String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };
//
//                Cursor cursor = getContentResolver().query(uri, projection,
//                        null, null, null);
//                cursor.moveToFirst();
//
//                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//                String number = cursor.getString(numberColumnIndex);
//
//                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//                String name = cursor.getString(nameColumnIndex);
//
//                Log.d("Details", "ZZZ number : " + number +" , name : "+name);
//
//                Toast.makeText(this, "ZZZ number : " + number +" , name : "+name, Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    };


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
