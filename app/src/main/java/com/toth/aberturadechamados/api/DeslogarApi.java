package com.toth.aberturadechamados.api;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.toth.aberturadechamados.model.HttpConnection;
import com.toth.aberturadechamados.activity.LoginActivity;
import com.toth.aberturadechamados.model.SharedPreferencesConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class DeslogarApi extends AsyncTask<Void, Void, String> {

//    declarando atributos e elementos
    private String url;
    private Activity activity ;
    private SharedPreferencesConfig preferencesConfig;

//    construtor
    public DeslogarApi(String url, Activity activity) {
        this.url = url;
        this.activity = activity;
    }

//    método que chama a api
    @Override
    public String doInBackground(Void... voids) {
        return HttpConnection.get(url) ;
    }

//    método que pega o retorno da api
    @Override
    public void onPostExecute(String s) {
        super.onPostExecute(s);

//        instanciando as preferencias salvas
        preferencesConfig = new SharedPreferencesConfig(activity.getApplicationContext());

        if (s != null){
            try {
//                instancia e resgata o retorno da api
                JSONObject jsonObject = new JSONObject(s);
                boolean sair = jsonObject.getBoolean("sair");
//                verifica se executou corretamente
                if (sair){
//                   limpando as informações gravadas no celular
                    preferencesConfig.writeUsuarioId(0);
                    preferencesConfig.writeUsuarioNome("");
                    preferencesConfig.writeNivelUsuario("");
                    preferencesConfig.writeLoginStatus(false);

//                   finalizando a tela e redirecionando para a login
                    Intent intencao = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intencao);
                    activity.finish();
                } else {
                    Toast.makeText(activity, "Erro ao tentar sair, tente novamente mais tarde", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
