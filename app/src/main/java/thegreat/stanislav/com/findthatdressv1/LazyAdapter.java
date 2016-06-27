package thegreat.stanislav.com.findthatdressv1;

/**
 * Created by stanislav on 6/23/16.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class LazyAdapter extends BaseAdapter {

    private String [] prices = {};
    private Activity activity;
    private String[] data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public LazyAdapter(Activity a, String[] d, String [] arr_prices) {

        prices = arr_prices;
        Log.i("test","Lazy price is:" + prices[0]);
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }



    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.row_listview_item, null);

        TextView text=(TextView)vi.findViewById(R.id.text);;
        ImageView image=(ImageView)vi.findViewById(R.id.image);

        imageLoader.DisplayImage(data[position], image);

        Spannable price = new SpannableString(prices[position]);
        price.setSpan(new ForegroundColorSpan(Color.RED), 0, price.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setText("Click to buy for ");
        text.append(price);
        text.append(" $");
        return vi;
    }
}
