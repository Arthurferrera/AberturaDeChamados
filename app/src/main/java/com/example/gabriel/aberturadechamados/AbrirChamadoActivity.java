package com.example.gabriel.aberturadechamados;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gabriel.aberturadechamados.api.InserirChamadoApi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

public class AbrirChamadoActivity extends AppCompatActivity {

    EditText txt_titulo, txt_mensagem;
    Button btn_abrir_chamado;

    String titulo,mensagem;
    Boolean status;
    String dataAberturaChamado;
    int idUsuario;

    private SharedPreferencesConfig preferencesConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_chamado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        não permite que o teclado apareça assim que a tela iniciar
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        instanciando o sharedPreferences
        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());

//        finds dos elementos
        txt_titulo = findViewById(R.id.txt_titulo);
        txt_mensagem = findViewById(R.id.txt_mensagem);
        btn_abrir_chamado = findViewById(R.id.btn_abrir_chamado);
    }

//    método que valida se os campos estão vazios
    private boolean ValidarCampos(){
        EditText campoComFoco = null;
        boolean isValid = true;

//        primeiro verfica o valor do campo, se for igual a 0, significa que está vazio
//        depois mostra a mensagem de erro
//        e seta a variavel isValid, como null, assim não é valido o formulario

        if(txt_titulo.getText().toString().length() == 0){
            campoComFoco = txt_titulo;
            txt_titulo.setError("Titulo obrigatório");
            isValid = false;
        }
        if(txt_mensagem.getText().toString().length() == 0){
            if(campoComFoco == null){
                campoComFoco = txt_mensagem;
            }
            txt_mensagem.setError("Mensagem obrigatória");
            isValid = false;
        }
        if(campoComFoco != null){
            campoComFoco.requestFocus();
        }
        return isValid;
    }

//    método que faz o processo de gravar/abrir o chamado
    public void AbrirChamado(View view) throws UnsupportedEncodingException {
        if(ValidarCampos()){
//            resgatando os valores dos inputs
            titulo = txt_titulo.getText().toString();
            mensagem = txt_mensagem.getText().toString();
//            pegando o idUsuario que está gravado em um tipo de 'sessão'
            idUsuario = Integer.parseInt(preferencesConfig.readUsuarioId());

//            deixando o que foi digitado pelo usuario no padrao utf-8, para ser passado na url sem problemas
            mensagem = URLEncoder.encode(mensagem, "UTF-8");
            titulo = URLEncoder.encode(titulo, "UTF-8");

//            chamando metodo que seta a data e hora atual
            setarDataHora();
//            setando o status em false, pois todos cos chamados abertos vão ser pendentes, só muda o status se for resolvido
            status = false;

            String url = "http://192.168.137.1/APIChamados/inserir.php?";
            String parametros = "titulo="+titulo+"&mensagem="+mensagem+"&status=0&idUsuario="+idUsuario;
            url += parametros;
            new InserirChamadoApi(url, this).execute();

        }
    }

//    método pega a data e hora atual
    public void setarDataHora() throws UnsupportedEncodingException {
//        setando a data e hora da consulta
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int hora = c.get(Calendar.HOUR);
        int minutos = c.get(Calendar.MINUTE);
        String dataAtual = String.format("%02d/%02d/%d ás %02d:%02d", dia, mes+1, ano, hora, minutos);
        dataAberturaChamado = dataAtual;
//        tirando os espaços, deixando no formato utf-8
        dataAberturaChamado = URLEncoder.encode(dataAberturaChamado, "UTF-8");
    }
}
