package thegreat.stanislav.com.findthatdressv1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by stanislav on 6/21/16.
 */
public class ResultActivity extends MainActivity {

    ListView list;
    LazyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        String result = intent.getStringExtra("string"); //if it's a string you stored.

        List<String> links_list = json_string_2_click_link(result);
        List<String> images_list = json_string_2_image_link(result);


        String[] Arr_url_img = images_list.toArray(new String[0]);
        String[] Arr_url = links_list.toArray(new String[0]);

        Integer bla = Arr_url_img.length;

        Log.i("test",bla.toString());

        list=(ListView)findViewById(R.id.listView);
        adapter=new LazyAdapter(this,Arr_url_img,Arr_url);
        list.setAdapter(adapter);


    }



    public  List<String> json_string_2_click_link(String jsonstring) {

        JSONObject json_result;

        ArrayList<String> links_list = new ArrayList<String>();
        ArrayList<String> images_list = new ArrayList<String>();


        try {
            json_result = new JSONObject(jsonstring);
            Log.i("test","json created successfully");

            JSONArray items = json_result.getJSONArray("items");


            for (int i=0; i<items.length();i++) {
                JSONObject item = items.getJSONObject(i);
                JSONArray sim_results = item.getJSONArray("similar_results");

                for (int j=0; j<sim_results.length();j++) {
                    JSONObject sim_result = sim_results.getJSONObject(j);
//                    Log.i("test",sim_result.toString());
                    String click_url = sim_result.getString("clickUrl");
//                    Log.i("test",click_url);
                    JSONObject images = sim_result.getJSONObject("images");
                    String image_url = images.getString("Medium");
//                    Log.i("test","IMAGE LINK: " + image);

                    links_list.add(click_url);

                }

            }



        }
        catch (Exception exception) {
            Log.e("test","json_result exception handled");
        }

        return links_list;
    }


    public  List<String> json_string_2_image_link(String jsonstring) {

        JSONObject json_result;

        ArrayList<String> images_list = new ArrayList<String>();


        try {
            json_result = new JSONObject(jsonstring);
            Log.i("test","json created successfully");

            JSONArray items = json_result.getJSONArray("items");


            for (int i=0; i<items.length();i++) {
                JSONObject item = items.getJSONObject(i);
                JSONArray sim_results = item.getJSONArray("similar_results");

                for (int j=0; j<sim_results.length();j++) {
                    JSONObject sim_result = sim_results.getJSONObject(j);
//                    Log.i("test",sim_result.toString());
                    String click_url = sim_result.getString("clickUrl");
//                    Log.i("test",click_url);
                    JSONObject images = sim_result.getJSONObject("images");
                    String image_url = images.getString("Medium");
//                    Log.i("test","IMAGE LINK: " + image);

                    images_list.add(image_url);

                }

            }



        }
        catch (Exception exception) {
            Log.e("test","json_result exception handled");
        }

        return images_list;
    }


}