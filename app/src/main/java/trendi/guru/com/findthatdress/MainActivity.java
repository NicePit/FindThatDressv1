package trendi.guru.com.findthatdress;


//Author: Stanley Barabanov, 2016


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TaskComplete {

    private ImageView imgPreview;

    public static String img_url;
    public static Uri fileUri;
    public static String filename = "";

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int DISPLAY_RESULTS_REQUEST_CODE = 200;
    private static final int BROWSE_PIC_REQUEST_CODE = 300;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final String YOUR_APP_ID = "CF8CC0AC-FDC5-22EA-FFA8-29836A3B2200";
    private static final String YOUR_SECRET_KEY = "F12946D5-1F47-AD94-FF38-7CB8FABF8E00";

    private static final String IMAGE_DIRECTORY_NAME = "FindDress";

    private static final String link_base = "https://api.backendless.com/CF8CC0AC-FDC5-22EA-FFA8-29836A3B2200/v1/files/mypics/";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgPreview = (ImageView) findViewById(R.id.imageView);

        String appVersion = "v1";
        Backendless.initApp(this, YOUR_APP_ID, YOUR_SECRET_KEY, appVersion);


        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }



        Intent receivedIntent = getIntent();
        Uri receivedUri = receivedIntent.getParcelableExtra(Intent.EXTRA_STREAM);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), receivedUri);
            imgPreview.setImageBitmap(bitmap);
            Upload_image(rotate_picture(bitmap));
        } catch (Exception e) {
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
        Send_http_request();
    }

    public void onClick3(View view) {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, BROWSE_PIC_REQUEST_CODE);

    }


    public void Send_http_request() {

        if (filename.length() > 0) {

            img_url = link_base+filename;
            try {

                JSONObject json = makeJSON(img_url);

                new MyAsyncTask(this).execute(json);

            } catch (Exception exception) {
            }
        }
        else {
            Toast.makeText(this,"Please, take or browse a picture",Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onTaskComplete(String result) {

        if (result.equals("")) {
            filename = "";
            imgPreview.setImageResource(R.drawable.f_icon);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.f_notification);
            mBuilder.setContentTitle("Find that dress");
            mBuilder.setContentText("Wooops! Overtime error  =( Try later or choose another pic");
            Intent resultIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);

    // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    // notificationID allows you to update the notification later on.
            mNotificationManager.notify(1, mBuilder.build());
        }

        else {

            Intent intent = new Intent(this, ResultActivity.class);

            intent.putExtra("string", result);

            startActivityForResult(intent, DISPLAY_RESULTS_REQUEST_CODE);

        }

    }


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


    private void Upload_image (Bitmap bitmap) {
        try {


           String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());

            filename = "IMG" + timeStamp + ".jpg";

            Backendless.Files.Android.upload(rotate_picture(bitmap),
                    Bitmap.CompressFormat.JPEG,
                    100,
                    filename,
                    "mypics", new AsyncCallback<BackendlessFile>()

                    {
                        @Override
                        public void handleResponse( final BackendlessFile backendlessFile )
                        {}

                        @Override
                        public void handleFault( BackendlessFault backendlessFault )
                        {}
                    });

        }

        catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Error, can not upload image on server", Toast.LENGTH_SHORT)
                    .show();
        }


    }

    private void previewCapturedImage() {



        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 8;

        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                options);

        Upload_image(rotate_picture(bitmap));

        imgPreview.setImageBitmap(rotate_picture(bitmap));

    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    private static Bitmap rotate_picture (Bitmap bitmap) {

        Bitmap adjustedBitmap = bitmap;

        try {

            ExifInterface exif = new ExifInterface(fileUri.getPath());
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotation != 0f) {matrix.preRotate(rotationInDegrees);}
            adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        }
        catch (Exception ex) {}
        return adjustedBitmap;
    }



    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {

        Uri test_uri = Uri.fromFile(getOutputMediaFile(type));
        return test_uri;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            previewCapturedImage();

        }
        else if (requestCode == DISPLAY_RESULTS_REQUEST_CODE) {
            Toast.makeText(this,"Try again?",Toast.LENGTH_SHORT).show();
        }

        // if image is browsed from phone => preview is set up within onActivityResult


        else if (requestCode == BROWSE_PIC_REQUEST_CODE && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();


            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            fileUri = Uri.parse(picturePath);

            cursor.close();


            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 3;

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath,options);

            Upload_image(bitmap);

            imgPreview.setImageBitmap(rotate_picture(bitmap));

        }


        else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Action is cancelled", Toast.LENGTH_SHORT)
                    .show();
        }
//        if (resultCode == RESULT_CANCELED) {
//            Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_SHORT)
//                    .show();
//        }
    }



}