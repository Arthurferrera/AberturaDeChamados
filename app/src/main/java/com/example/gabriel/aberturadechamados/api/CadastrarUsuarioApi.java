package com.example.gabriel.aberturadechamados.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.gabriel.aberturadechamados.HttpConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class CadastrarUsuarioApi extends AsyncTask<Void, Void, String> {

    private String url;
    private Activity activity;

    public CadastrarUsuarioApi(String url, Activity activity){
        this.url = url;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return HttpConnection.get(url);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(s != null){
            try{
                JSONObject jsonObject = new JSONObject(s);
                boolean sucesso = jsonObject.getBoolean("Sucesso");
                if (sucesso){
                    Toast.makeText(activity, "Cadastro efetuado!", Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
