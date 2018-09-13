package com.example.gabriel.aberturadechamados.api;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.gabriel.aberturadechamados.HttpConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class InserirObservacaoApi extends AsyncTask<Void, Void, String> {

    private String url;
    private Activity activity;

    public InserirObservacaoApi(String url,  Activity activity) {
        this.url  = url;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return HttpConnection.get(url);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s != null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean sucesso = false;
                sucesso = jsonObject.getBoolean("Sucesso");
                if (sucesso){
                    Toast.makeText(activity, "Observação adicionada!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Erro ao tentar adicionar a observacao!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
