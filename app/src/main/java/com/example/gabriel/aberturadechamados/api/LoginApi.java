package com.example.gabriel.aberturadechamados.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.gabriel.aberturadechamados.HttpConnection;
import com.example.gabriel.aberturadechamados.MainActivity;
import com.example.gabriel.aberturadechamados.MainAdmActivity;
import com.example.gabriel.aberturadechamados.MainMenuActivity;
import com.example.gabriel.aberturadechamados.MainMenuAdmActivity;
import com.example.gabriel.aberturadechamados.SharedPreferencesConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginApi extends AsyncTask<Void, Void, String> {

    private String url;
    private Activity activity;
    private SharedPreferencesConfig preferencesConfig;
    private  AlertDialog alerta;

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

//        instanciando as preferencias salvas
        preferencesConfig = new SharedPreferencesConfig(activity.getApplicationContext());

        if(s != null){
            try {
//                resgatando o retorno da api de login
                JSONObject jsonObject = new JSONObject(s);
                boolean login = jsonObject.getBoolean("login");
//                verificando se o login está valido
                if (login) {
//                    resgatando o objeto do usuario
                    JSONObject usuarioJson = jsonObject.getJSONObject("usuario");
                    String nomeUsuario = usuarioJson.getString("nome");
                    String nivelUsuario = usuarioJson.getString("nivel");
                    nivelUsuario = nivelUsuario.trim();
                    int idUsuario = usuarioJson.getInt("id");

//                    gravando as informações de login e usuario
                    preferencesConfig.writeUsuarioId(idUsuario);
                    preferencesConfig.writeUsuarioNome(nomeUsuario);
                    preferencesConfig.writeNivelUsuario(nivelUsuario);
                    preferencesConfig.writeLoginStatus(true);



//                    redirecionando para a tela principal do app
//                    passando algumas informações do usuario
                    if (nivelUsuario.equals("Administrador")){
                        Intent intencao = new Intent(activity, MainMenuAdmActivity.class);
                        activity.startActivity(intencao);
                        activity.finish();
                    } else if(nivelUsuario.equals("Cliente")){
                        Intent intencao = new Intent(activity, MainMenuActivity.class);
                        activity.startActivity(intencao);
                        activity.finish();
                    } else {
                        Toast.makeText(activity, "erro", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    caso o login não for valido, mostra uma mensagem
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Erro!");
                    builder.setMessage("Usuário ou Senha incorreta.");
                    builder.setPositiveButton("OK", null);
                    alerta = builder.create();
                    alerta.show();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
