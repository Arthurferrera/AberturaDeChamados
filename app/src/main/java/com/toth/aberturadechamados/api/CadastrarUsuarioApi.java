package com.toth.aberturadechamados.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.toth.aberturadechamados.model.HttpConnection;
import com.toth.aberturadechamados.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CadastrarUsuarioApi extends AsyncTask<Void, Void, String> {

//    declarando atributos e elementos
    private String url, API_URL;
    private Activity activity;
    private AlertDialog alerta;

//    construtor
    public CadastrarUsuarioApi(String url, Activity activity){
        this.url = url;
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

        if(s != null){
            try{
//                resgata o objeto retornado do cnpj
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
//                        resgata o objeto retornado da verificação do cnpj
                        JSONObject cnpjExisteJson = new JSONObject(s);
                        boolean cnpjExiste = cnpjExisteJson.getBoolean("cnpjExiste");
//                        verifica se o cnpj ja está cadastrado
                        if (cnpjExiste){
                            Toast.makeText(activity, "CNPJ já está cadastrado, tente outro!", Toast.LENGTH_SHORT).show();
                        } else {
                            JSONObject senhaJson = new JSONObject(s);
                            boolean senhaBoa = senhaJson.getBoolean("senhaValidada");
                            if (senhaBoa){
//                              resgata o objeto retornado do salvar usuario
                                JSONObject jsonObject = new JSONObject(s);
                                boolean sucesso = jsonObject.getBoolean("Sucesso");
//                              verifica se o usuario foi cadastrado
                                if (sucesso) {
                                    String usuario = jsonObject.getString("usuario");
                                    String senha = jsonObject.getString("senha");
                                    API_URL = activity.getString(R.string.api_key);
                                    try {
                                        AutenticacaoCadastro(usuario, senha);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(activity, "Erro ao realizar o cadastro. Tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                                    activity.finish();
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                builder.setTitle("Aviso!");
                                builder.setMessage("A senha deve conter letras e números.");
                                builder.setPositiveButton("Entendi", null);
                                alerta = builder.create();
                                alerta.show();
                            }
                        }
                    }
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

//    método que faz a autenticação do usuario
    public void AutenticacaoCadastro(String usuario, String senha) throws UnsupportedEncodingException {

//        preparando a variavel  para ser passada pela url
        usuario = URLEncoder.encode(usuario, "UTF-8");
        senha = URLEncoder.encode(senha, "UTF-8");

//        setando parametros e url da API e instanciando a API
        String url = API_URL + "login.php?";
        String parametros = "usuario="+usuario+"&senha="+senha;
        url += parametros;
        new LoginApi(url, activity).execute();
    }
}
