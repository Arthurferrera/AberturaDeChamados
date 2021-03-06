package com.toth.aberturadechamados.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.toth.aberturadechamados.R;
import com.toth.aberturadechamados.api.LoginApi;
import com.toth.aberturadechamados.model.SharedPreferencesConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

//    declarando elementos visuais, variaveis
    EditText txt_usuario, txt_senha;
    TextView lbl_cadastro_usuario;
    Button btn_entrar;
    String usuario, senha, API_URL;
    private SharedPreferencesConfig preferencesConfig;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        não permite que o teclado apareça assim que a tela iniciar
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        caminho padrao da api
        API_URL = getString(R.string.api_key);

//        instanciando o SharedPreferencesConfig
        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());

//        finds dos elementos
        txt_usuario = findViewById(R.id.txt_usuario);
        txt_senha = findViewById(R.id.txt_senha);
        btn_entrar = findViewById(R.id.btn_entrar);
        lbl_cadastro_usuario = findViewById(R.id.lbl_cadastro_usuario);

//        lendo o status do login, caso valido,
//        redireciona para a pagina principal do app
//        e finaliza a tela de login
        if (preferencesConfig.readLoginStatus()){
            if (preferencesConfig.readNivelusuario().equals("Administrador")){
                Intent intencao = new Intent(getApplicationContext(), MainMenuAdmActivity.class);
                startActivity(intencao);
                finish();
            } else if(preferencesConfig.readNivelusuario().equals("Cliente")){
                Intent intencao = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intencao);
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        limpando os inputs
        txt_senha.setText("");
    }

    //    método que valida os campos, para não ficarem vazios
    private boolean ValidarCampos(){
        EditText campoComFoco = null;
        boolean isValid = true;

        if(txt_usuario.getText().toString().length() == 0){
            campoComFoco = txt_usuario;
            txt_usuario.setError("Usuário obrigatório");
            isValid = false;
        }
        if(txt_senha.getText().toString().length() == 0){
            if(campoComFoco == null){
                campoComFoco = txt_senha;
            }
            txt_senha.setError("Senha obrigatória");
            isValid = false;
        }
        if(campoComFoco != null){
            campoComFoco.requestFocus();
        }
        return isValid;
    }

//    método que faz a autenticação do usuario
    public void Autenticacao(View view) throws UnsupportedEncodingException {

//        verificando se os campos estão vazios
        if(ValidarCampos()){
//            resgatando os valores digitados
            usuario = txt_usuario.getText().toString().toLowerCase();
            senha = txt_senha.getText().toString();
//            setando no padrao UTF-8, para a url
            usuario = URLEncoder.encode(usuario, "UTF-8");
            senha = URLEncoder.encode(senha, "UTF-8");

//            setando parametros e url da API e instanciando a API
            String url = API_URL + "login.php?";
            String parametros = "usuario="+usuario+"&senha="+senha;
            url += parametros;
            new LoginApi(url, this).execute();
            onResume();
        }
    }

//    ação do link que redireciona para a tela de cadastro
    public void AbrirCadastroUsuario(View view) {
        Intent intencao = new Intent(getApplicationContext(), CadastroUsuarioActivity.class);
        startActivity(intencao);
    }
}