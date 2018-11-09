package com.toth.aberturadechamados.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.toth.aberturadechamados.model.HttpConnection;
import com.toth.aberturadechamados.model.ImageHelper;

import java.util.HashMap;

public class UploadFotoApi extends AsyncTask<Bitmap, Void, String> {

    ProgressDialog progress;
    Context ctx;
    StringBuffer nomeImagem;
    private String url;

    public UploadFotoApi(Context ctx, StringBuffer nomeImage, String url){
        this.ctx = ctx;
        this.url = url;
        this.nomeImagem = nomeImage;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(ctx, "Upload", "Aguarde...", false, false);
    }

    @Override
    protected String doInBackground(Bitmap... bitmaps) {

        if(bitmaps.length > 0){
            Bitmap img  = bitmaps[0];
            byte[] arrBytes = ImageHelper.toByteArray(img);
            String img_str = Base64.encodeToString(arrBytes, Base64.DEFAULT);
            HashMap<String, String> values = new HashMap<>();
            values.put("image", img_str);
            String retorno = HttpConnection.post(url, values);
            return retorno;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progress.dismiss();
        nomeImagem.append(s);

        Log.d("doInBackground", s);
    }
}
