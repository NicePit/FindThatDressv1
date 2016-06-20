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
import java.util.Timer;
import java.util.TimerTask;


class MyAsyncTask extends AsyncTask<JSONObject, Void, String> {

//    private Exception exception;

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


    protected String doInBackground(JSONObject... objects) {


    int i=0;
    String answer;

    while (i<5) {

        try {

            answer = Http_Post.send_data(objects[0]);
            i+=1;

            if (answer.toLowerCase().contains("true}, \"success\": true}".toLowerCase())) {
                Log.i("test","Answer is TRUE, continue to GET method");
                return Http_Get.get_relevance();

            }
            Thread.sleep(60000);

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    Log.i("test","Answer is FALSE");
    return "";

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




