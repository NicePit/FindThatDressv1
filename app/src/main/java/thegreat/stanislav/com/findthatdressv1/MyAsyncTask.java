package thegreat.stanislav.com.findthatdressv1; /**
 * Created by stanislav on 6/2/16.
 */




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

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
        mProgress.setMessage("Please wait...It can take up to 3 minutes. Good luck:)");
        mProgress.show();
        mProgress.setCancelable(false);
        mProgress.setCanceledOnTouchOutside(false);
    }


    protected String doInBackground(JSONObject... objects) {


    int i=0;
    String answer_POST;
    String answer_GET = "";

    while (i<9) {

        try {

            answer_POST = Http_Post.send_data(objects[0]);
            i+=1;

            if (answer_POST.toLowerCase().contains("true}, \"success\": true}".toLowerCase())) {

                answer_GET = Http_Get.get_relevance();
                break;
            }
            else {

                if (i==9) {
                    answer_GET = "";
                    break;
                }
                Thread.sleep(20000);
            }

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    return answer_GET;
    }


    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        mProgress.dismiss();
        //This is where you return data back to caller
        mCallback.onTaskComplete(result);

    }

}




