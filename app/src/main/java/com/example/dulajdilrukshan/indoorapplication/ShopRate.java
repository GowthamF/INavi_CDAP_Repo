package com.example.dulajdilrukshan.indoorapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShopRate extends AppCompatActivity{

    Globals sharedData = Globals.getInstance();

    RatingBar mRatingBar;
    TextView mRatingScale;
    EditText mFeedback;
    Button mSendFeedback;
    StringRequest MyStringRequest;
    RequestQueue MyRequestQueue;
    String username = sharedData.getValue();
    String shopName;
    String review;
    boolean rateSession = false;
    boolean reviewSession = false;

    int shopRate = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_rate);
        MyRequestQueue = Volley.newRequestQueue(this);

        ListView resultsListView = (ListView) findViewById(R.id.results_listview);


//        setRatingUrl = "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/setrating";

//        JSONArray reviews = new JSONArray();
//
//        reviews.get["username"].toString();
//        reviews.get["review"].toString();
//
//
//        for (JSONArray js : reviews){
//            nameAddresses.put(js.get(0).toString(), reviews.get["review"].toString());
//        }
//
//
//        try{
//            JSONArray users = new JSONArray(response)
//
//        }catch (JSONException e)
//        {
//
//        }


        HashMap<String, String> nameAddresses = new HashMap<>();
        nameAddresses.put("Diana", "3214 Broadway Avenue");
        nameAddresses.put("Tyga", "343 Rack City Drive");
        nameAddresses.put("Rich Homie Quan", "111 Everything Gold Way");
        nameAddresses.put("Donna", "789 Escort St");
        nameAddresses.put("Bartholomew", "332 Dunkin St");
        nameAddresses.put("Eden", "421 Angelic Blvd");

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.text1, R.id.text2});


        Iterator it = nameAddresses.entrySet().iterator();
        while (it.hasNext()) {
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultsMap.put("First Line", pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultsMap);
        }

        resultsListView.setAdapter(adapter);


        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingScale = (TextView) findViewById(R.id.tvRatingScale);
        mFeedback = (EditText) findViewById(R.id.etFeedback);
        mSendFeedback = (Button) findViewById(R.id.btnSubmit);


        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Very bad");
                        shopRate = 1;
                        break;
                    case 2:
                        mRatingScale.setText("Need some improvement");
                        shopRate = 2;
                        break;
                    case 3:
                        mRatingScale.setText("Good");
                        shopRate = 3;
                        break;
                    case 4:
                        mRatingScale.setText("Great");
                        shopRate = 4;
                        break;
                    case 5:
                        mRatingScale.setText("Awesome. I love it");
                        shopRate = 5;
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });
        mSendFeedback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    shopName = extras.getString("key");
                    pushRateData();
//                    pushReviewData();
                }
                if (extras == null) {
                    Toast.makeText(ShopRate.this, "Shop Name Cannot be Empty", Toast.LENGTH_LONG).show();
                }

                if (mFeedback.getText().toString().isEmpty()) {
                    Toast.makeText(ShopRate.this, "Please fill in feedback Box", Toast.LENGTH_LONG).show();
                }
//                 else {
//                    mFeedback.setText("");
//                    mRatingBar.setRating(0);
//                    Toast.makeText(ShopRate.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }

    public void pushRateData() {

        final String setRatingUrl = "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/setrating";

        MyStringRequest = new StringRequest(Request.Method.POST, setRatingUrl, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
//                 Server Sends a Response as "1" if data is Success

                    if (response.equals("1")) {

//                        Toast.makeText(getApplicationContext(), " Thank you for sharing your feedback ", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    String err = e.getMessage();
                    Toast.makeText(getApplicationContext(), err, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> FormData = new HashMap<String, String>();


                FormData.put("shopName", shopName);
                FormData.put("userRating", shopRate + "");

                return FormData;

            }
        };
        MyRequestQueue.add(MyStringRequest);


    }

    public void pushReviewData() {
        EditText getReview = (EditText) findViewById(R.id.etFeedback);

        final String review = getReview.getText().toString();

        final String setRatingUrl = "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/setrating";

        MyStringRequest = new StringRequest(Request.Method.POST, setRatingUrl, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
//                 Server Sends a Response as "1" if data is Success

                    if (response.equals("1")) {

                        Toast.makeText(getApplicationContext(), " Thank you for sharing your feedback ", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    String err = e.getMessage();
                    Toast.makeText(getApplicationContext(), err, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> FormData = new HashMap<String, String>();

                FormData.put("username", username);
                FormData.put("review", review);
                FormData.put("shop", shopName);

                return FormData;

            }
        };
        MyRequestQueue.add(MyStringRequest);


    }


}
