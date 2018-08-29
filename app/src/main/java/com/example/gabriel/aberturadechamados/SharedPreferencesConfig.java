package com.example.gabriel.aberturadechamados;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesConfig {

    private SharedPreferences sharedPreferences;
    private Context context;

//    cria o sharedPreferences
    public SharedPreferencesConfig (Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preferences), Context.MODE_PRIVATE);
    }

//    método que ecreve, muda o valor do login
    public void writeLoginStatus(boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.login_status_preferences), status);
        editor.commit();
    }

//    método que lê a informação do login
    public boolean readLoginStatus (){
        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_preferences), false);
        return status;
    }

//    método que escreve, muda o valor do nome
    public void writeUsuarioNome(String nome){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.usuario_nome), nome);
        editor.commit();
    }

//    método que lê as informações do nome
    public String readUsuarioNome(){
        String nome = "";
        nome = sharedPreferences.getString(context.getResources().getString(R.string.usuario_nome), "");
        return nome;
    }

//    método que escreve, muda o valor do ID
    public void writeUsuarioId(int id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(String.valueOf(context.getResources().getString(R.string.usuario_id)), String.valueOf(id));
        editor.commit();
    }

//    método que lê as informações do ID
    public String readUsuarioId(){
        String id = "";
        id = sharedPreferences.getString(context.getResources().getString(R.string.usuario_id), "");
        return id;
    }
}
