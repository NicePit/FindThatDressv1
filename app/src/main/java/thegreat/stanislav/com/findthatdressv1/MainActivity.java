package thegreat.stanislav.com.findthatdressv1;




//Author: StanleyBar, 2016


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.Files;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements TaskComplete {

    public static String img_url;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static String filename = "";

    private static final String YOUR_APP_ID = "CF8CC0AC-FDC5-22EA-FFA8-29836A3B2200";
    private static final String YOUR_SECRET_KEY = "F12946D5-1F47-AD94-FF38-7CB8FABF8E00";

    private static final String IMAGE_DIRECTORY_NAME = "FindDress";
    public static Uri fileUri;
    private ImageView imgPreview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imgPreview = (ImageView) findViewById(R.id.imageView);

        String appVersion = "v1";
        Backendless.initApp(this, YOUR_APP_ID, YOUR_SECRET_KEY, appVersion );


        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    /*
     * Here we restore the fileUri again
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    public void onClick(View view) {
        captureImage();
    }


    public void onClick2(View view) {
        Send_request();
    }


    public void Send_request() {

        if (filename.length() > 0) {
            String link_base = "https://api.backendless.com/CF8CC0AC-FDC5-22EA-FFA8-29836A3B2200/v1/files/mypics/";
          img_url = link_base+filename;
//          img_url = "http://cdn.posh24.com/images/:complete/p/2018319/l/fun_pics/check_it_out_heres_how_much_celebrities_really_weigh.jpg";
//          img_url = "http://www.aceshowbiz.com/images/wennpic/angelina-jolie-third-annual-women-in-the-world-04.jpg";
//          img_url = "http://develop.backendless.com/console/CF8CC0AC-FDC5-22EA-FFA8-29836A3B2200/appversion/17BD303A-53F9-2E95-FFCF-BAD07389F000/maxddpcccnsnthujiomyfdkbjjjvjgzuayxc/files/view/mypics/IMG20160620_171558.jpg";
            try {

              JSONObject json = makeJSON(img_url);


                new MyAsyncTask(this).execute(json);

            } catch (Exception exception) {
                Log.e("test", exception.toString());
            }
        }
        else {
            Toast.makeText(this,"Take a picture first, motherfucker",Toast.LENGTH_SHORT).show();
        }

    }







    @Override
    public void onTaskComplete(String result) {

//    boolean pic_is_relevant = result.toLowerCase().contains(": false}, \"success\": true".toLowerCase());
//        if (pic_is_relevant)
//            Send_request();
//        else {
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        try {
            JSONObject json_result = new JSONObject(result);
            Log.i("test","json created successfully");
        }
        catch (Exception exception) {
           Log.e("test","json_result exception handled");
        }

//        }
    }


//    public void OnClick3(View view) {
//        Http_Get.Get_relevance("http://fazz.co/img/demo/gettyimages-490421970.jpg");
//    }






    public static JSONObject makeJSON(String link) {

        JSONObject object = new JSONObject();

        List<String> link_list = new ArrayList<String>();
        link_list.add(link);
        JSONArray jsonArray = new JSONArray(link_list);


        try {
            object.put("pageUrl", "stans-java-app.com");
            object.put("imageList", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }





    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void previewCapturedImage() {
        try {

            //imgPreview.setVisibility(View.VISIBLE);


            // bitmap factory
            BitmapFactory.Options options = new BitmapFactory.Options();


            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
            ImageView imgPreview = (ImageView) findViewById(R.id.imageView);

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());

            filename = "IMG" + timeStamp + ".jpg";

            Backendless.Files.Android.upload( bitmap,
                    Bitmap.CompressFormat.JPEG,
                    100,
                    filename,
                    "mypics", new AsyncCallback<BackendlessFile>()

                    {
                        @Override
                        public void handleResponse( final BackendlessFile backendlessFile )
                        {
                        }

                        @Override
                        public void handleFault( BackendlessFault backendlessFault )
                        {}
                    });


            imgPreview.setImageBitmap(bitmap);

        }

            catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Error, can not display the image", Toast.LENGTH_SHORT)
                    .show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            previewCapturedImage();

        }
        else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Cancelled", Toast.LENGTH_SHORT)
                    .show();
        }
//        if (resultCode == RESULT_CANCELED) {
//            Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_SHORT)
//                    .show();
//        }
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


}
