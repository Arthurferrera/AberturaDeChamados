package com.toth.aberturadechamados.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.toth.aberturadechamados.R;
import com.toth.aberturadechamados.adapter.AdapterImg;

import java.util.ArrayList;

public class FotosActivity extends AppCompatActivity {

    private int[] imagensFotos = {R.drawable.logo_apesp, R.drawable.logo, R.drawable.favicon};
    ViewPager viewPager;
    AdapterImg viewPagerAdapter;
    ArrayList<String> imagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        imagens = intent.getStringArrayListExtra("imgs");

        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new AdapterImg(this, imagens);
        viewPager.setAdapter(viewPagerAdapter);
//        LinearLayout.LayoutParams lp =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        viewPager.setLayoutParams(lp);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        viewPager.setSaveEnabled(false);
        finish();
    }
}
