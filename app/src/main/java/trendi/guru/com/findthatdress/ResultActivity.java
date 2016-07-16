package trendi.guru.com.findthatdress;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by stanislav on 6/21/16.
 */
public class ResultActivity extends MainActivity {

    ListView list;
    ImageListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        String result = intent.getStringExtra("string"); //if it's a string you stored.

        List<String> links_list = json_string_2_click_link(result);
        List<String> images_list = json_string_2_image_link(result);
        List<String> prices_list = json_string_2_price(result);




        final String[] Arr_url_img = images_list.toArray(new String[0]);
        final String[] Arr_url = links_list.toArray(new String[0]);
        final String[] Arr_price = prices_list.toArray(new String[0]);


        list=(ListView)findViewById(R.id.listView);
        adapter=new ImageListAdapter(this,Arr_url_img,Arr_price);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String url = Arr_url[position];
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });



    }





    public  List<String> json_string_2_click_link(String jsonstring) {

        JSONObject json_result;

        ArrayList<String> links_list = new ArrayList<String>();



        try {
            json_result = new JSONObject(jsonstring);

            JSONArray items = json_result.getJSONArray("items");


            for (int i=0; i<items.length();i++) {
                JSONObject item = items.getJSONObject(i);
                JSONArray sim_results = item.getJSONArray("similar_results");

                for (int j=0; j<sim_results.length();j++) {
                    JSONObject sim_result = sim_results.getJSONObject(j);

                    String click_url = sim_result.getString("clickUrl");

                    links_list.add(click_url);

                }

            }



        }
        catch (Exception exception) {

        }

        return links_list;
    }


    public  List<String> json_string_2_image_link(String jsonstring) {

        JSONObject json_result;

        ArrayList<String> images_list = new ArrayList<String>();


        try {
            json_result = new JSONObject(jsonstring);


            JSONArray items = json_result.getJSONArray("items");


            for (int i=0; i<items.length();i++) {
                JSONObject item = items.getJSONObject(i);
                JSONArray sim_results = item.getJSONArray("similar_results");

                for (int j=0; j<sim_results.length();j++) {
                    JSONObject sim_result = sim_results.getJSONObject(j);


                    JSONObject images = sim_result.getJSONObject("images");
                    String image_url = images.getString("Medium");

                    images_list.add(image_url);

                }

            }



        }
        catch (Exception exception) {

        }

        return images_list;
    }


    public  List<String> json_string_2_price(String jsonstring) {

        JSONObject json_result;

        ArrayList<String> price_list = new ArrayList<String>();


        try {
            json_result = new JSONObject(jsonstring);


            JSONArray items = json_result.getJSONArray("items");


            for (int i=0; i<items.length();i++) {
                JSONObject item = items.getJSONObject(i);
                JSONArray sim_results = item.getJSONArray("similar_results");

                for (int j=0; j<sim_results.length();j++) {
                    JSONObject sim_result = sim_results.getJSONObject(j);
                    JSONObject price = sim_result.getJSONObject("price");

                    String usd_price = price.getString("price");


                    price_list.add(usd_price);


                }

            }



        }
        catch (Exception exception) {
        }

        return price_list;
    }


}