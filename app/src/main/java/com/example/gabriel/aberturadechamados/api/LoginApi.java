package com.example.gabriel.aberturadechamados.api;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.gabriel.aberturadechamados.HttpConnection;
import com.example.gabriel.aberturadechamados.MainActivity;
import com.example.gabriel.aberturadechamados.MenuActivity;
import com.example.gabriel.aberturadechamados.SharedPreferencesConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginApi extends AsyncTask<Void, Void, String> {

    private String url;
    private Activity activity;
    private SharedPreferencesConfig preferencesConfig;

    public LoginApi(String url, Activity activity) {
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

        preferencesConfig = new SharedPreferencesConfig(activity.getApplicationContext());

        if(s != null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean login = jsonObject.getBoolean("login");
                if (login) {
                    JSONObject usuarioJson = jsonObject.getJSONObject("usuario");
                    String nomeUsuario = usuarioJson.getString("nome");
                    int idUsuario = usuarioJson.getInt("id");

                    preferencesConfig.writeUsuarioId(idUsuario);
                    preferencesConfig.writeUsuarioNome(nomeUsuario);
                    preferencesConfig.writeLoginStatus(true);

                    Intent intencao = new Intent(activity, MenuActivity.class);
                    intencao.putExtra("idUsuario", idUsuario);
                    intencao.putExtra("nomeUsuario", nomeUsuario);
                    activity.startActivity(intencao);
                    activity.finish();
                } else {
                    Toast.makeText(activity, "Login inválido!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
