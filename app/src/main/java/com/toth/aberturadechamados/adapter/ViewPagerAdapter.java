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
    private String[] imagens;
//    private int idChamado;
//    private Integer[] imagens;
    private String API_URL;

    public ViewPagerAdapter(Context context, String[] imagens) {
        this.context = context;
        this.imagens = imagens;
    }

    @Override
    public int getCount() {
        return imagens.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        API_URL = context.getString(R.string.api_key);

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.viewpager_layout, null);
        ImageView imageView = view.findViewById(R.id.imageViewPager);
        Picasso.get().load(API_URL+imagens[position]).into(imageView);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

//        LinearLayout li = new LinearLayout(context);
//        li.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        li.setLayoutParams(lp);
//        container.addView(li);
//
//        ImageView iv = new ImageView(context);
////        iv.setImageResource(imagens[position]);
//        Picasso.get().load(imagens[position]).into(iv);
//        li.addView(iv);
//
//        TextView tx = new TextView(context);
//        tx.setText("Foto:"+(position+1));
//        li.addView(tx);
//
//        Log.d("Script", "Build foto "+(position+1));
//
//        return li;
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