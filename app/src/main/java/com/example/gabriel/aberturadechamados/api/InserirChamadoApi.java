package com.example.gabriel.aberturadechamados.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.gabriel.aberturadechamados.HttpConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class InserirChamadoApi extends AsyncTask<Void, Void, String> {

    private String url;
    private Activity activity;

    public InserirChamadoApi(String url, Activity activity){
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
            try {
//                resgata o objeto que mostra se o chamado foi cadastrad
                JSONObject jsonObjeto = new JSONObject(s);
                boolean sucesso = jsonObjeto.getBoolean("Sucesso");
//                verificando
                if(sucesso){
                    Toast.makeText(activity, "Chamado cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    activity.finish();
                } else {
                    Toast.makeText(activity, "Erro ao realizar o cadastro. Tente Novamente mais tarde.", Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
