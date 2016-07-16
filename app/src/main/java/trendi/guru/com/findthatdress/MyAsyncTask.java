package trendi.guru.com.findthatdress; /**
 * Created by stanislav on 6/2/16.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;


class MyAsyncTask extends AsyncTask<JSONObject, String, String> {


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
        mProgress.setMessage("Please wait...Process can take up to 5 minutes");
        mProgress.show();
        mProgress.setCancelable(false);
        mProgress.setCanceledOnTouchOutside(false);
    }


    protected String doInBackground(JSONObject... objects) {


    int i=0;
    String answer_POST;
    String answer_GET = "";

    while (i<15) {


        try {

            answer_POST = Http_Post.send_data(objects[0]);
            i+=1;

            if (answer_POST == null || answer_POST.isEmpty()) {
                Handler handler =  new Handler(this.mContext.getMainLooper());
                handler.post( new Runnable(){
                    public void run(){
                        Toast.makeText(mContext, "Sorry! Server is not available. Try later",Toast.LENGTH_LONG).show();
                    }
                });
                break;

            }

            else if (answer_POST.toLowerCase().contains("true}, \"success\": true}".toLowerCase())) {

                answer_GET = Http_Get.get_relevance();
                break;
            }
            else if (i==15) {

                answer_GET = "";
                break;
            }
            else {Thread.sleep(20000);
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




