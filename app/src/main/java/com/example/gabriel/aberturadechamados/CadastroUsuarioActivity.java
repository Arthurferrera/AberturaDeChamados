package com.example.gabriel.aberturadechamados;

import android.graphics.MaskFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroUsuarioActivity extends AppCompatActivity {

    EditText txt_cnpj, txt_razao_social, txt_nome, txt_usuario, txt_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_cnpj = findViewById(R.id.txt_cnpj);
        txt_razao_social = findViewById(R.id.txt_razao_social);
        txt_nome = findViewById(R.id.txt_nome);
        txt_usuario = findViewById(R.id.txt_usuario);
        txt_senha = findViewById(R.id.txt_senha);

        txt_cnpj.addTextChangedListener(MaskUtil.insert(txt_cnpj, MaskType.CNPJ));
    }

    //    método que valida se os campos estão vazios
    private boolean ValidarCampos(){
        EditText campoComFoco = null;
        boolean isValid = true;

        if(txt_cnpj.getText().toString().length() == 0){
            campoComFoco = txt_cnpj;
            txt_cnpj.setError("CNPJ obrigatório");
            isValid = false;
        }
        if(txt_razao_social.getText().toString().length() == 0){
            if(campoComFoco == null){
                campoComFoco = txt_razao_social;
            }
            txt_razao_social.setError("Razão social obrigatória");
            isValid = false;
        }
        if(txt_nome.getText().toString().length() == 0){
            if(campoComFoco == null){
                campoComFoco = txt_nome;
            }
            txt_nome.setError("Nome obrigatório");
            isValid = false;
        }
        if(txt_usuario.getText().toString().length() == 0){
            if(campoComFoco == null){
                campoComFoco = txt_usuario;
            }
            txt_usuario.setError("Usuário obrigatório");
            isValid = false;
        }
        if(txt_senha.getText().toString().length() == 0){
            if(campoComFoco == null){
                campoComFoco = txt_senha;
            }
            txt_senha.setError("Sehna obrigatória");
            isValid = false;
        }
        if(campoComFoco != null){
            campoComFoco.requestFocus();
        }
        return isValid;
    }

    public void CadastrarUsuario(View view) {
        if(ValidarCampos()){
            String cnpj = String.valueOf(txt_cnpj.getText());
            Toast.makeText(getApplicationContext(), cnpj, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
