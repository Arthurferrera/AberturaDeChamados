package com.example.gabriel.aberturadechamados;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AbrirChamadoActivity extends AppCompatActivity {

    EditText txt_titulo, txt_mensagem;
    Button btn_abrir_chamado;

    String titulo,mensagem;
    Boolean status;
    String dataAberturaChamado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_chamado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        finds dos elementos
        txt_titulo = findViewById(R.id.txt_titulo);
        txt_mensagem = findViewById(R.id.txt_mensagem);
        btn_abrir_chamado = findViewById(R.id.btn_abrir_chamado);
    }

//    método que valida se os campos estão vazios
    private boolean ValidarCampos(){
        EditText campoComFoco = null;
        boolean isValid = true;

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
    public void AbrirChamado(View view) {
        if(ValidarCampos()){
            titulo = txt_titulo.getText().toString();
            mensagem = txt_mensagem.getText().toString();
            setarDataHora();
            status = false;

            Chamado c = new Chamado();
            c.setTitulo(titulo);
            c.setMensagem(mensagem);
            c.setData(dataAberturaChamado);
            c.setStatus(status);

            finish();

            Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

//    método pega a data e hora atual
    public void setarDataHora(){
//        setando a data e hora da consulta
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int hora = c.get(Calendar.HOUR);
        int minutos = c.get(Calendar.MINUTE);
        String dataAtual = String.format("%02d/%02d/%d ás %02d:%02d", dia, mes+1, ano, hora, minutos);
        dataAberturaChamado = dataAtual;
    }
}
