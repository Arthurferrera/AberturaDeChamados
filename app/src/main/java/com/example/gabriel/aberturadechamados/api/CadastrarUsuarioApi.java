package com.example.gabriel.aberturadechamados.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.gabriel.aberturadechamados.HttpConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class CadastrarUsuarioApi extends AsyncTask<Void, Void, String> {

    private String url;
    private Activity activity;

    public CadastrarUsuarioApi(String url, Activity activity){
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
            try{
                //resgata o objeto retornado do cnpj
                JSONObject cnpjObject = new JSONObject(s);
                boolean cnpjValidado = cnpjObject.getBoolean("Validado");
//                verifica se o cnpj foi validado
                if (!cnpjValidado) {
                    Toast.makeText(activity, "CNPJ inválido", Toast.LENGTH_SHORT).show();
                } else {
//                    resgata o objeto retornado da verifiação do usuario
                    JSONObject usuarioExisteJson = new JSONObject(s);
                    boolean usuarioExiste = usuarioExisteJson.getBoolean("usuarioExiste");
//                    verifica se o usuario existe
                    if (usuarioExiste){
                        Toast.makeText(activity, "Usuário existe, tente outro!", Toast.LENGTH_SHORT).show();
                    } else {
//                        resgata o objeto retornado do salvar usuario
                        JSONObject jsonObject = new JSONObject(s);
                        boolean sucesso = jsonObject.getBoolean("Sucesso");
//                    Toast.makeText(activity, "jhj"+sucesso, Toast.LENGTH_SHORT).show();
//                        verifica se o usuario foi cadastrado
                        if (sucesso) {
                            Toast.makeText(activity, "Cadastro efetuado!", Toast.LENGTH_SHORT).show();
                            activity.finish();
                        } else {
                            Toast.makeText(activity, "Erro ao realizar o cadastro. Tente Novamente mais tarde.", Toast.LENGTH_SHORT).show();
                            activity.finish();
                        }
                    }
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
