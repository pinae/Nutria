package de.ct.nutria.foodSelector;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NutriaHeadlessNetworkFragment extends Fragment {

    private class RequestTask extends AsyncTask<String, Integer, RequestTask.Result> {

        private NutriaRequestCallback<String> hostCallback;

        RequestTask(NutriaRequestCallback<String> callback) {
            setCallback(callback);
        }

        void setCallback(NutriaRequestCallback<String> callback) {
            hostCallback = callback;
        }

        /**
         * Wrapper class that serves as a union of a result value and an exception. When the
         * download task has completed, either the result value or exception can be a non-null
         * value. This allows you to pass exceptions to the UI thread that were thrown during
         * doInBackground().
         */
        class Result {
            public String resultValue;
            public Exception exception;
            public Result(String resultValue) {
                this.resultValue = resultValue;
            }
            public Result(Exception exception) {
                this.exception = exception;
            }
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute() {
            if (hostCallback != null) {
                NetworkInfo networkInfo = hostCallback.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                    // If no connectivity, cancel task and update Callback with null data.
                    hostCallback.updateFromRequest(null);
                    cancel(true);
                }
            }
        }

        @Override
        protected RequestTask.Result doInBackground(String... urls) {
            Result result = null;
            if (!isCancelled() && urls != null && urls.length > 0) {
                String urlString = urls[0];
                String payloadString = urls[1];
                try {
                    URL url = new URL(urlString);
                    String resultString = loadUrl(url, payloadString.getBytes("UTF-8"));
                    if (resultString != null) {
                        result = new Result(resultString);
                    } else {
                        throw new IOException("No response received.");
                    }
                } catch(Exception e) {
                    result = new Result(e);
                }
            }
            return result;
        }

        /**
         * Given a URL, sets up a connection and gets the HTTP response body from the server.
         * If the network request is successful, it returns the response body in String form.
         * Otherwise, it will throw an IOException.
         */
        private String loadUrl(URL url, byte[] payload) throws IOException {
            InputStream stream = null;
            HttpsURLConnection connection = null;
            String result = null;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("POST");
                OutputStream connectionOutputStream = connection.getOutputStream();
                connectionOutputStream.write(payload);
                connectionOutputStream.close();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoInput(true);
                // Open communications link (network traffic occurs here).
                connection.connect();
                publishProgress(NutriaRequestCallback.Progress.CONNECT_SUCCESS);
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                // Retrieve the response body as an InputStream.
                stream = connection.getInputStream();
                publishProgress(NutriaRequestCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
                if (stream != null) {
                    // Converts Stream to String.
                    result = readStream(stream);
                }
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        private String readStream(InputStream inputStream) {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(inputStream,
                        "UTF-8"), 8);
            } catch (UnsupportedEncodingException e) {
                // Use standard encoding if UTF-8 is somehow not supported.
                reader = new BufferedReader(new InputStreamReader(inputStream),
                        8);
            }
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            } catch (IOException e) {
                sb.append("\n");
                sb.append("IOError while building this String. This is an error.");
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(Result result) {
            if (result != null && hostCallback != null) {
                if (result.exception != null) {
                    hostCallback.updateFromRequest(result.exception.getMessage());
                } else if (result.resultValue != null) {
                    hostCallback.updateFromRequest(result.resultValue);
                }
                hostCallback.finishRequest();
            }
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(Result result) {
        }
    }

    public static final String TAG = "NutriaHeadlessNetworkFragment";

    private static final String URL_KEY = "UrlKey";
    private static final String URL_PAYLOAD_KEY = "UrlPayloadKey";

    private NutriaRequestCallback<String> hostCallback;
    private RequestTask requestTask;
    private String urlString;
    private String payloadString;

    public static NutriaHeadlessNetworkFragment getInstance(FragmentManager fragmentManager) {
        NutriaHeadlessNetworkFragment networkFragment = (NutriaHeadlessNetworkFragment)
                fragmentManager.findFragmentByTag(NutriaHeadlessNetworkFragment.TAG);
        if (networkFragment == null) {
            networkFragment = new NutriaHeadlessNetworkFragment();
            Bundle args = new Bundle();
            args.putString(URL_KEY, "");
            args.putString(URL_PAYLOAD_KEY, "{}");
            networkFragment.setArguments(args);
            fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        }
        return networkFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        urlString = getArguments().getString(URL_KEY);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Host Activity will handle callbacks from task.
        hostCallback = (NutriaRequestCallback<String>) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity to avoid memory leak.
        hostCallback = null;
    }

    @Override
    public void onDestroy() {
        // Cancel task when Fragment is destroyed.
        cancelRequest();
        super.onDestroy();
    }

    public void doQuery(CharSequence query) throws JSONException {
        cancelRequest();
        urlString = "http://localhost:8000/json/find";
        JSONObject payload = new JSONObject();
        payload.put("name", query.toString());
        payload.put("count", 50);
        payloadString = payload.toString();
        Bundle args = new Bundle();
        args.putString(URL_KEY, urlString);
        args.putString(URL_PAYLOAD_KEY, payloadString);
        this.setArguments(args);
        requestTask = new RequestTask(hostCallback);
        requestTask.execute(urlString, payloadString);
    }

    public void cancelRequest() {
        if (requestTask != null) {
            requestTask.cancel(true);
        }
    }
}
