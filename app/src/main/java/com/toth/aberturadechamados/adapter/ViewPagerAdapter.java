package com.toth.aberturadechamados.adapter;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.toth.aberturadechamados.R;
import com.toth.aberturadechamados.model.HttpConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> imagens;
//    private int[] imagens;
    private String API_URL;

    public ViewPagerAdapter(Context context, ArrayList<String> imagens) {
        this.context = context;
        this.imagens = imagens;
    }

    @Override
    public int getCount() {
        return imagens.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

//        API_URL = context.getString(R.string.api_key);
//
//        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = layoutInflater.inflate(R.layout.viewpager_layout, null);
//        ImageView imageView = view.findViewById(R.id.imageViewPager);
//
        String url = "http://192.168.2.128/WebChamadosServ/APIChamados/img/5be97b5501760.jpg";
//        Picasso.get().load(API_URL+imagens[position]).into(imageView);
//
//        ViewPager vp = (ViewPager) container;
//        vp.addView(view, 0);
//        return view;

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(lp);
        container.addView(ll);

        ImageView iv = new ImageView(context);
//        iv.setImageResource(imagens[position]);
        Picasso.get().load(API_URL+imagens.get(position)).into(iv);
        ll.addView(iv);

//        TextView tv = new TextView(context);
//        tv.setText("Carro "+(position +1));
//        ll.addView(tv);

        Log.i("Script", "Build: Carro: "+(position + 1));

        return(iv);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        Log.d("Script", "Destroy foto "+(position+1));
//        container.removeView((View)object);
//    }

}