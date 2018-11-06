package com.toth.aberturadechamados.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.toth.aberturadechamados.model.HttpConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class InserirObservacaoApi extends AsyncTask<Void, Void, String> {

//    declarando atributos e elementos
    private String url;
    private Activity activity;

//    construtor
    public InserirObservacaoApi(String url,  Activity activity) {
        this.url  = url;
        this.activity = activity;
    }

//    método que chama a api
    @Override
    protected String doInBackground(Void... voids) {
        return HttpConnection.get(url);
    }

//    método que pega o retorno da api
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s != null){
            try {
//                resgata o retorno da api
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
