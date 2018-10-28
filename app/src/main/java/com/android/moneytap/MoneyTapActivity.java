package com.android.moneytap;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.moneytap.adapter.MoneyTapAdapter;
import com.android.moneytap.alertmessage.AlertMessage;
import com.android.moneytap.clientConnection.ClientUtil;
import com.android.moneytap.networkconnection.ConnectionDetector;
import com.android.moneytap.pojo.DataModel;
import com.android.moneytap.progressdialog.ProgressBar;
import com.android.moneytap.serviceUrl.MoneyTypeServerUrl;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Umadevi Kuppanageri
 */
public class MoneyTapActivity extends AppCompatActivity {

    private static final String TAG = MoneyTapActivity.class.getSimpleName();

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    MoneyTapAdapter moneyTapAdapter;

    ConnectionDetector connectionDetector;
    private boolean isInternetPresent;


    HttpResponse response;
    HttpGet httpget;

    JSONObject jsonObj;
    int status;

    ProgressBar progressBar;
    Activity context;
    String jsonStr;
    AlertMessage alertMessage;
    private ArrayList<DataModel> dataModelArrayList;

    DataModel dataModel;
    String thumbinalUrl, desc, name, imgUrl;
    int index;
    JSONObject jObj;
    private DividerItemDecoration dividerItemDecoration;
    JSONObject imageUrl;
    JSONObject terms;
    int widht;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_tap);

        context = this;

        progressBar = new ProgressBar(context);

        alertMessage = new AlertMessage(context);

        dataModelArrayList = new ArrayList<>();


        initGui();
    }

    private void initGui() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        connectionDetector = new ConnectionDetector(MoneyTapActivity.this);
        isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            Log.i(TAG, "####### Checking the internet connection ::"
                    + "Internet connection is Avilable");

            // Calling async task to get json
            new MoneyTapBackgroundTask().execute();
        } else {

            Log.i(TAG, "############ No internet connection #############");
            alertMessage.showAlertDialogNetworkChecking();
        }

    }

    // Asynchronous task for LogoutBackground task
    private class MoneyTapBackgroundTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            progressBar.createProgressDialog();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                synchronized (this) {
                    Log.i(TAG, "Inside doInBackground()");

                    getData();

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (progressBar.dialog != null) {

                progressBar.dismissProgressDialog();
            }

        }

        protected String getData() {

            try {

                String moneyTypeUrl = MoneyTypeServerUrl.MONEYTYPE;

                Log.i(TAG, "URL" + moneyTypeUrl);

                HttpClient client = ClientUtil.getCurrentClient();

                httpget = new HttpGet(moneyTypeUrl);

                response = client.execute(httpget);

                Log.i(TAG, "SERVER STATUS CODE :: " + response.getStatusLine().getStatusCode());

                Log.i(TAG, "SERVER RESPONSE :: " + response);

                status = response.getStatusLine().getStatusCode();

                if (status >= 200 && status < 300) {

                    jsonStr = inputStreamToString(response.getEntity().getContent()).toString();

                    Log.i(TAG, "SERVER RESPONSE :: " + jsonStr);

                    if (jsonStr != null) {

                        jsonObj = new JSONObject(jsonStr);
                        Log.i(TAG, "jsonObj = " + jsonObj.toString());
                        JSONObject continueJSON = jsonObj.getJSONObject("continue");
                        Log.i(TAG, "continueJSON = " + continueJSON.toString());

                        JSONObject queryJSON = jsonObj.getJSONObject("query");
                        Log.i(TAG, "queryJSON = " + queryJSON.toString());

                        // Getting JSON Array node
                        JSONArray pages = queryJSON.getJSONArray("pages");
                        Log.i(TAG, "pages = " + queryJSON.toString());

                        // looping through All Pages
                        for (int i = 0; i < pages.length(); i++) {
                            jObj = pages.getJSONObject(i);
                            index = jObj.getInt("index");
                            name = jObj.getString("title");
                            Log.i(TAG, "name::" + name);
                            // thumbnail node is JSON Object
                            if (jObj.has("thumbnail")) {
                                imageUrl = jObj.getJSONObject("thumbnail");
                                Log.i(TAG, "imageUrl  in side if block " + imageUrl);

                                if (imageUrl != null && !imageUrl.toString().isEmpty()) {
                                    thumbinalUrl = imageUrl.getString("source");
                                    Log.i(TAG, "thumbinalUrl in side if block" + thumbinalUrl);
                                }


                            } else if (jObj.isNull("thumbnail")) {
                                imageUrl = null;
                                thumbinalUrl = imgUrl;
                                Log.i(TAG, "thumbinalUrl is null in side else block " + thumbinalUrl);
                            }

                            if (jObj.has("terms")) {

                                terms = jObj.getJSONObject("terms");
                                Log.i(TAG, "terms = " + terms);
                                JSONArray descJsonArray = terms.getJSONArray("description");
                                for (int j = 0; j < descJsonArray.length(); j++) {
                                    desc = descJsonArray.getString(j);
                                    Log.i(TAG, "description = " + desc);
                                }

                            } else if (jObj.isNull("terms")) {
                                terms = null;
                                Log.i(TAG, "terms is null in side else block " + terms);
                            }
                            dataModel = new DataModel();
                            dataModel.setImage(thumbinalUrl);
                            dataModel.setName(name);
                            dataModel.setDescription(desc);
                            dataModel.setId_(index);

                            Log.i(TAG, "set the values for datamodel::" + dataModel);

                            dataModelArrayList.add(dataModel);
                            Log.i(TAG, "add the values into dataModelArrayList" + dataModelArrayList + dataModelArrayList.size());
                        }


                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar.dismissProgressDialog();
                                Log.i(TAG, "Dismiss the ProgressDialog");
                                ;
                                displayData();

                            }

                        });

                    }


                } else {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            alertMessage.showAlertDialogServerIsnotResponding();
                            progressBar.dismissProgressDialog();
                            Log.i(TAG, "Dismiss the ProgressDialog");
                            ;
                            finish();

                        }

                    });
                }


            } catch (HttpHostConnectException e) {

                Log.e(TAG, "HttpHostConnectException in logOutMethod() of TAG" + e.getMessage());
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {

                        alertMessage.showAlertDialogServerIsnotResponding();
                        progressBar.dismissProgressDialog();
                        Log.i(TAG, "Dismiss the ProgressDialog");

                        finish();
                    }
                });

            } catch (JSONException e) {
                // TODO: handle exception
                Log.e(TAG, "JSONException ::" + e.getMessage());
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        alertMessage.showAlertDialogServerIsnotResponding();
                        progressBar.dismissProgressDialog();
                        Log.i(TAG, "Dismiss the ProgressDialog");
                        finish();

                    }
                });
            } catch (ConnectTimeoutException e) {

                Log.e(TAG, "ConnectTimeoutException in LogOutMethod() of TAG" + e.getMessage());
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {

                        alertMessage.showAlertDialogServerIsnotResponding();
                        progressBar.dismissProgressDialog();
                        Log.i(TAG, "Dismiss the ProgressDialog");
                        finish();

                    }
                });

            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "UnsupportedEncodingException " + e.getMessage());
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        alertMessage.showAlertDialogServerIsnotResponding();
                        progressBar.dismissProgressDialog();
                        Log.i(TAG, "Dismiss the ProgressDialog");
                        finish();


                    }
                });

            } catch (ClientProtocolException e) {
                Log.e(TAG, "ClientProtocolException" + e.getMessage());
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        alertMessage.showAlertDialogServerIsnotResponding();
                        progressBar.dismissProgressDialog();
                        Log.i(TAG, "Dismiss the ProgressDialog");
                        finish();

                    }
                });

            } catch (IOException e) {
                Log.e(TAG, "IOException" + e.getMessage());
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        alertMessage.showAlertDialogServerIsnotResponding();
                        progressBar.dismissProgressDialog();
                        Log.i(TAG, "Dismiss the ProgressDialog");
                        finish();

                    }
                });

            }
            return jsonStr;
        }
    }

    private void displayData() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);


        if (dataModelArrayList != null) {
            if (dataModelArrayList.isEmpty()) {

                Log.i(TAG, "dataModelArrayList iff block "
                        + dataModelArrayList.toString());

            } else {
                Log.i(TAG, "dataModelArrayList "
                        + dataModelArrayList.toString());

                moneyTapAdapter = new MoneyTapAdapter(
                        dataModelArrayList, context);

            }
            recyclerView.setAdapter(moneyTapAdapter);
        }
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        // Read response until the end
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
                // is.close();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();

        }
        // Return full string
        return total;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                moneyTapAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        return true;
    }

}
