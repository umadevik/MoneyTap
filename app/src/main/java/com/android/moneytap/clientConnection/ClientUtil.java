package com.android.moneytap.clientConnection;

import android.util.Log;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;


public class ClientUtil {

    private static final String TAG = "ClientUtil";


    static HttpParams httpParams;

    private static final DefaultHttpClient client = buildClient();

    private static DefaultHttpClient buildClient() {
        try {

            // create logger

            httpParams = new BasicHttpParams();

            httpParams.setParameter("Connection", "keep-alive");

            httpParams
                    .setParameter("Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

            httpParams
                    .setParameter(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");


            return new DefaultHttpClient(httpParams);

        } catch (Throwable ex) {

            Log.e(TAG, ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static DefaultHttpClient getCurrentClient() {
        return client;
    }
}