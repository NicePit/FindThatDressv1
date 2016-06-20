package thegreat.stanislav.com.findthatdressv1;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by stanislav on 6/20/16.
 */
public class Http_Post {

    public static String send_data(JSONObject json) {
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
            streamWriter.write(json.toString());
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
}
