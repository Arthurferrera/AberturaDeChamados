package com.example.gabriel.aberturadechamados.api;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.gabriel.aberturadechamados.HttpConnection;
import com.example.gabriel.aberturadechamados.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginApi extends AsyncTask<Void, Void, String> {

    private String url;
    private Activity activity;

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

        if(s != null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean login = jsonObject.getBoolean("login");
                if (login) {
                    JSONObject usuarioJson = jsonObject.getJSONObject("usuario");
                    String nomeUsuario = usuarioJson.getString("nome");
                    int idUsuario = usuarioJson.getInt("id");
                    Intent intencao = new Intent(activity, MainActivity.class);
                    intencao.putExtra("idUsuario", idUsuario);
                    intencao.putExtra("nomeUsuario", nomeUsuario);
                    activity.startActivity(intencao);
                } else {
                    Toast.makeText(activity, "Login inválido!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
