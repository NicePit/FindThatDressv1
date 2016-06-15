package thegreat.stanislav.com.findthatdressv1; /**
 * Created by stanislav on 6/2/16.
 */




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



class MyAsyncTask extends AsyncTask<JSONObject, Void, String> {

    private Exception exception;

    private Context mContext;

    ProgressDialog mProgress;
    private TaskComplete mCallback;

    public MyAsyncTask(Context context){
        this.mContext = context;
        this.mCallback = (TaskComplete) context;

    }

    @Override
    public void onPreExecute() {
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Please wait...");
        mProgress.show();
    }

//    private OnTaskCompleted listener;
//
//    public MyAsyncTask(OnTaskCompleted listener){
//        this.listener=listener;
//    }


    protected String doInBackground(JSONObject... objects) {
        HttpURLConnection connection = null;
        Log.i("test","start: success");
        try {
            URL url = new URL("https://extremeli.trendi.guru/api/images");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
            streamWriter.write(objects[0].toString());
            streamWriter.flush();
            StringBuilder stringBuilder = new StringBuilder();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String response = null;
                while ((response = bufferedReader.readLine()) != null) {
                    stringBuilder.append(response + "\n");
                }
                bufferedReader.close();

                Log.i("test", stringBuilder.toString());
                return stringBuilder.toString();
            } else {
                Log.i("test", connection.getResponseMessage());
                return null;
            }
        } catch (Exception exception) {
            Log.e("test", exception.toString());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    protected void onPostExecute(String result) {
        // TODO: check this.exception
        // TODO: do something with the feed
        super.onPostExecute(result);
        mProgress.dismiss();
        //This is where you return data back to caller
        mCallback.onTaskComplete(result);
//        Log.i("test", result);
//        Http_Get.Get_relevance("http://fazz.co/img/demo/gettyimages-490421970.jpg");

//        String TRUE = "true";
//        String FALSE = "false";
//        if ((result.toLowerCase().contains(TRUE.toLowerCase())) && (result.toLowerCase().contains(FALSE.toLowerCase()))) {
//            Log.i("test", "Image is not in database");
//        } else {
//            Log.i("test", "Image is in database");
//        }


    }

}




