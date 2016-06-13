package thegreat.stanislav.com.findthatdressv1;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stanislav on 6/6/16.
 */

public class Http_Get {


    private static String result;
    private static final String Str_Server = "https://extremeli.trendi.guru/api/images?imageUrl=";

    public static String Get_relevance(String image_url) {


        Log.i("get","started");
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(Str_Server+image_url);

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            StringBuilder stringBuilder = new StringBuilder();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader streamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String response = null;
                while ((response = bufferedReader.readLine()) != null) {
                    stringBuilder.append(response + "\n");
                }
                bufferedReader.close();

                result = stringBuilder.toString();

        }
        }
            catch (Exception e) {
            e.printStackTrace();
                Log.e("get","Error");
                return null;

        }
        Log.i("get",result);
        return result;
    }
}