package com.toth.aberturadechamados.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.toth.aberturadechamados.R;

import java.io.File;

public class AdapterImg extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
//    private int[] imgs;
    private String imgs;
    private String API_URL;

    public AdapterImg(Context context, String imgs){
        this.context = context;
        this.imgs = imgs;
    }

    @Override
    public int getCount() {
        return Integer.parseInt(imgs);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        API_URL = context.getString(R.string.api_key);
        String image_url = "http://resoltecksolucoes.com.br/wp-content/uploads/2018/02/cropped-logomarca-01-teste.png";

                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.viewpager_layout, null);
        ImageView imageView = view.findViewById(R.id.imageViewPager);

        String url = "http://resoltecksolucoes.com.br/wp-content/uploads/2018/02/cropped-logomarca-01-teste.png";

        Picasso.get().load(url).resize(70, 70).centerCrop().into(imageView);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

//        LinearLayout ll = new LinearLayout(context);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        ll.setOrientation(LinearLayout.VERTICAL);
//        ll.setLayoutParams(lp);
//        container.addView(ll);
//
//        ImageView iv = new ImageView(context);
////        iv.setImageResource(imgs[position]);
//
////        Obter as dimensões do componente na tela
//        File file = new File(API_URL+imgs[position]);
//        int targetW = iv.getWidth();
//        int targetH = iv.getHeight();
//        Log.d("Imagens", file+"");
//
////        Obter as dimensões do bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//
//        bmOptions.inJustDecodeBounds = true;
//
//        BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
//
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
////        Determinar o quanto é necessario diminuir a imagem
//        int scaleFactor = 1;
//        if ((targetW > 0) || (targetH > 0)) {
//            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//        }
//
////        Decodifica o arquivo de imagem em um Bitmap dimensionando para preencher o
////        ImagemView
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
//
//        Picasso.get().load(API_URL+imgs[position]).error(R.drawable.favicon).resize(10,10).centerCrop().into(iv);
////        iv.setImageBitmap();
//        ll.addView(iv);
//
//        return(ll);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
