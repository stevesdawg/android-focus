package com.example.focusapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NetworkAsyncTask extends AsyncTask<String, Void, String> {

    public static final String REQUEST_METHOD = "POST";
    public  static final int READ_TIMEOUT = 3000;
    public static final int CONNECTION_TIMEOUT = 15000;
    public static final String CHARSET = "UTF-8";
    private AsyncResponse delegate = null;

    public NetworkAsyncTask(AsyncResponse d) {
        delegate = d;
    }

    @Override
    protected String doInBackground(String... strings) {
        // First is server target, Default method is POST
        String url = strings[0];
        String userID = strings[1];
        String token = strings[2];
        String result = null;
        String inputLine = null;

        try {
            URL myurl = new URL(url);
            String query = String.format("userid=%s&token=%s", URLEncoder.encode(userID, CHARSET), URLEncoder.encode(token, CHARSET));

            // create an HTTP connection
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();

            // set http request parameters
            conn.setRequestMethod(REQUEST_METHOD);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Accept-Charset", CHARSET);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + CHARSET);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);

            // Get output stream and write POST data to server
            OutputStream output = conn.getOutputStream();
            output.write(query.getBytes(CHARSET));

            // Get Input Stream
            InputStreamReader streamReader = new InputStreamReader(conn.getInputStream());

            // Create Buffered Reader for stream.
            // Create string builder to create string.
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            output.close();
            reader.close();
            streamReader.close();
            result = stringBuilder.toString();
        } catch(IOException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }

    protected void onPostExecute(String output) {
        this.delegate.uiUpdate(output);
    }
}
