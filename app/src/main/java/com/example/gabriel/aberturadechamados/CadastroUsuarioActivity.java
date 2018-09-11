package com.example.gabriel.aberturadechamados;

import android.graphics.MaskFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gabriel.aberturadechamados.api.CadastrarUsuarioApi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CadastroUsuarioActivity extends AppCompatActivity {

    EditText txt_cnpj, txt_razao_social, txt_nome, txt_usuario, txt_senha, txt_confirma_senha;
    String cnpj, razaoSocial, nome, usuario, senha, confirmaSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        não permite que o teclado apareça assim que a tela iniciar
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        finds dos elementos
        txt_cnpj = findViewById(R.id.txt_cnpj);
        txt_razao_social = findViewById(R.id.txt_razao_social);
        txt_nome = findViewById(R.id.txt_nome);
        txt_usuario = findViewById(R.id.txt_usuario);
        txt_senha = findViewById(R.id.txt_senha);
        txt_confirma_senha = findViewById(R.id.txt_confirma_senha);
//        setando mascara para o campo
        txt_cnpj.addTextChangedListener(MaskUtil.insert(txt_cnpj, MaskType.CNPJ));
    }

    //    método que valida se os campos estão vazios
    private boolean ValidarCampos(){
        EditText campoComFoco = null;
        boolean isValid = true;

        if(txt_cnpj.getText().toString().length() < 18){
            campoComFoco = txt_cnpj;
            txt_cnpj.setError("CNPJ inválido");
            isValid = false;
        }

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
        if(txt_senha.getText().toString().length() < 6){
            if(campoComFoco == null){
                campoComFoco = txt_senha;
            }
            txt_senha.setError("Minímo de 6 caracteres");
            isValid = false;
        }
        if(txt_senha.getText().toString().length() == 0){
            if(campoComFoco == null){
                campoComFoco = txt_senha;
            }
            txt_senha.setError("Sehna obrigatória");
            isValid = false;
        }
        if(txt_confirma_senha.getText().toString().length() == 0){
            if(campoComFoco == null){
                campoComFoco = txt_confirma_senha;
            }
            txt_confirma_senha.setError("Confirmação obrigatória");
            isValid = false;
        }
        if(campoComFoco != null){
            campoComFoco.requestFocus();
        }
        return isValid;
    }

    public void CadastrarUsuario(View view) throws UnsupportedEncodingException {

//        verifica se os campos estão vazios
        if(ValidarCampos()){

//            pegando os valores dos campos
            cnpj = txt_cnpj.getText().toString();
            razaoSocial = txt_razao_social.getText().toString();
            nome = txt_nome.getText().toString();
            usuario = txt_usuario.getText().toString().toLowerCase();
            senha = txt_senha.getText().toString();
            confirmaSenha = txt_confirma_senha.getText().toString();

//            verificando se as senhas digitadas são iguais
            if (confirmaSenha.equals(senha)){
//                deixando os textos no padrao UTF-8, para poder ser enviado na url
                cnpj = URLEncoder.encode(cnpj, "UTF-8");
                razaoSocial = URLEncoder.encode(razaoSocial, "UTF-8");
                nome = URLEncoder.encode(nome, "UTF-8");
                usuario = URLEncoder.encode(usuario, "UTF-8");
                senha = URLEncoder.encode(senha, "UTF-8");

//                setando url, os parametros e instanciando a api
                String url = "http://192.168.137.1/APIChamados/cadastroUsuario.php?";
                String parametros = "cnpj="+cnpj+"&razaoSocial="+razaoSocial+"&nome="+nome+"&usuario="+usuario+"&senha="+senha;
                url += parametros;
                new CadastrarUsuarioApi(url, this).execute();
            }else{
//                caso as senhas não sejam iguais, mostra ma mensagem de erro
                Toast.makeText(this, "Senhas não conferem", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
