package com.example.gabriel.aberturadechamados.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Enviado!");
                    builder.setMessage("Seu chamado foi enviado com sucesso!\nEm breve entraremos em contato.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    });
                    builder.create().show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Erro!");
                    builder.setMessage("Erro ao realizar o cadastro. Tente Novamente mais tarde.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    });
                    builder.create().show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
