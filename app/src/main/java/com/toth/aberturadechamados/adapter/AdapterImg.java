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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.toth.aberturadechamados.R;

import java.io.File;
import java.util.ArrayList;

public class AdapterImg extends PagerAdapter {

    private Context context;
    private ArrayList<String> imgs;
//    private int[] imgs = new int[]{R.drawable.logo_apesp, R.drawable.logo, R.drawable.favicon};;

    public AdapterImg(Context context, ArrayList<String> imgs){
        this.context = context;
        this.imgs = imgs;
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
//        imageView.setImageResource(imgs[position]);

        String imagens = imgs.get(position);

//        Picasso.get().load(imgs[position]).resize(650, 950).onlyScaleDown()
//                .error(R.drawable.aaa).into(imageView);

//        REDIMENSIONA A IMAGEM, MAS PERDE A PROPORÇÃO
//        Picasso.get().load(imgs[position]).resize(650, 950).into(imageView);

//        A IMAGEM SÓ SERÁ REDIMENSIONADA SE FOR MAIOR QUE O TAMANHO SELECIONADO
//        Picasso.get().load(imgs[position]).resize(650, 950).onlyScaleDown().into(imageView);

//        PREENCHE TODA A IMAGEVIEW QUE DESEJA, MAS CORTA OS EXCESSOS
//        Picasso.get().load(imgs[position]).resize(650, 950).centerCrop().into(imageView);

//        DIMENSIONA A IMAGEM COM OS TAMANHOS SOLCITADOS OU MENORES QUE O IMAGEVIEW, MANTENDO A PROPORÇÃO DA IMAGEM
        Picasso.get().load(imagens).resize(700, 1000).error(R.drawable.cloud_error).centerInside().into(imageView);

//        DIMENSIONA A IMAGEM COM OS TAMANHOS SOLCITADOS OU MENORES QUE O IMAGEVIEW, MANTENDO A PROPORÇÃO DA IMAGEM
//        Picasso.get().load(imgs[position]).fit().error(R.drawable.favicon).into(imageView);


        container.addView(imageView,0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
