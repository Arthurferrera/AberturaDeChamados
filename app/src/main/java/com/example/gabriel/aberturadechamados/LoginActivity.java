package com.example.gabriel.aberturadechamados;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gabriel.aberturadechamados.api.LoginApi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    EditText txt_usuario, txt_senha;
    TextView lbl_cadastro_usuario;
    Button btn_entrar;
    String usuario, senha;
    private SharedPreferencesConfig preferencesConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());

//        finds dos elementos
        txt_usuario = findViewById(R.id.txt_usuario);
        txt_senha = findViewById(R.id.txt_senha);
        btn_entrar = findViewById(R.id.btn_entrar);
        lbl_cadastro_usuario = findViewById(R.id.lbl_cadastro_usuario);

        if (preferencesConfig.readLoginStatus()){
                Intent intencao = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intencao);
                finish();
        }
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
    public void Autencicacao(View view) throws UnsupportedEncodingException {
        if(ValidarCampos()){
            usuario = txt_usuario.getText().toString();
            senha = txt_senha.getText().toString();

            usuario = URLEncoder.encode(usuario, "UTF-8");
            senha = URLEncoder.encode(senha, "UTF-8");

            String url = "http://192.168.2.121/APIChamados/login.php?";
            String parametros = "usuario="+usuario+"&senha="+senha;
            url += parametros;
            new LoginApi(url, this).execute();
        }
    }

//    ação do link que redireciona para a tela de cadastro
    public void AbrirCadastroUsuario(View view) {
        Intent intencao = new Intent(getApplicationContext(), CadastroUsuarioActivity.class);
        startActivity(intencao);
    }
}