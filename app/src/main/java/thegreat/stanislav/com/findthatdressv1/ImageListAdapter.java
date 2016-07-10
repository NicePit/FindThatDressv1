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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class ImageListAdapter extends ArrayAdapter {


    private Context context;
    private LayoutInflater inflater;
    private String[] imageUrls;
    private String [] prices = {};

    public ImageListAdapter(Context context, String[] imageUrls, String [] prices) {
        super(context, R.layout.row_listview_item, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;
        this.prices = prices;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.row_listview_item, parent, false);
        }

        TextView text=(TextView)convertView.findViewById(R.id.text);;
        ImageView image=(ImageView)convertView.findViewById(R.id.image);

        Picasso
                .with(context)
                .load(imageUrls[position])
                .into(image);

        text.setText("Click to buy for <=");
        String price;
        if (prices[position].contains(".0")) {
            price = prices[position].replace(".0","");
        }
        else {
            price = prices[position];
        }
        Spannable price_red = new SpannableString(price);
        price_red.setSpan(new ForegroundColorSpan(Color.RED), 0, price.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.append(price_red);
        text.append("$");

        return convertView;
    }
}
