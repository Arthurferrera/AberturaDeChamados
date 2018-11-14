package com.toth.aberturadechamados.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.toth.aberturadechamados.R;

public class AdapterImg extends PagerAdapter {

    private Context context;
    private int[] imgs;
//    private String[] imgs;
    private String API_URL;

    public AdapterImg(Context context, int[] imgs){
        this.context = context;
        this.imgs = imgs;
    }


    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        API_URL = context.getString(R.string.api_key);

        LinearLayout ll = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(lp);
        container.addView(ll);

        ImageView iv = new ImageView(context);
//        iv.setImageResource(imgs[position]);
        Picasso.get().load(imgs[position]).into(iv);
        ll.addView(iv);
        return(ll);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
