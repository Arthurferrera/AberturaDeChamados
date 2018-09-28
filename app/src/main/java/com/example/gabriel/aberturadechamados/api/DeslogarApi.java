package com.example.gabriel.aberturadechamados.api;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.gabriel.aberturadechamados.HttpConnection;
import com.example.gabriel.aberturadechamados.LoginActivity;
import com.example.gabriel.aberturadechamados.SharedPreferencesConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class DeslogarApi extends AsyncTask<Void, Void, String> {

    private String url;
    private Activity activity;
    private SharedPreferencesConfig preferencesConfig;

    public DeslogarApi(String url, Activity activity) {
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

        if (s != null){
            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean sair = jsonObject.getBoolean("sair");
                if (sair){
                    preferencesConfig.writeLoginStatus(false);
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                    activity.finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
