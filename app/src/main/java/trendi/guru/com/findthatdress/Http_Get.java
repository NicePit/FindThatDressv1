package trendi.guru.com.findthatdress;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by stanislav on 6/6/16.
 */

public class Http_Get extends MainActivity {

    private static String result;
    private static final String Str_Server = "https://extremeli.trendi.guru/api/images?imageUrl=";

    public static String get_relevance() {

        String IMG_URL = img_url;
        URL url;
        HttpURLConnection urlConnection = null;
        try {

            url = new URL(Str_Server+IMG_URL);

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

                return null;

        }

            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

        return result;
    }
}
