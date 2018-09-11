package com.example.gabriel.aberturadechamados;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class VisualizarChamadoActivity extends AppCompatActivity {

    TextView lbl_visualizar_titulo_chamado, lbl_visualizar_mensagem, lbl_visualizar_data_chamado, lbl_visualizar_status_chamado, lbl_visualizar_observacao;
    Integer idChamado;
    String titulo, mensagem, data, nivelUsuario, observacao;
    Integer status;
    SharedPreferencesConfig preferencesConfig;

    TextView txt_observacao;
    Switch sw_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_chamado);
//        setContentView(R.layout.content_dialog_atualizar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());

//        pegando o intent
        Intent intent = getIntent();

//        finds dos elementos
        lbl_visualizar_titulo_chamado = findViewById(R.id.lbl_titulo_chamado);
        lbl_visualizar_mensagem = findViewById(R.id.lbl_visualzar_mensagem);
        lbl_visualizar_data_chamado = findViewById(R.id.lbl_visualizar_data_chamado);
        lbl_visualizar_status_chamado = findViewById(R.id.lbl_visualizar_status_chamado);
        lbl_visualizar_observacao = findViewById(R.id.lbl_visualizar_observacao);

//        finds dos itens do dialog
        txt_observacao =  findViewById(R.id.txt_observacao);
        sw_status = findViewById(R.id.sw_status);

//        resgatando os parametros passados pelo intent
        idChamado = intent.getIntExtra("idChamado", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        nivelUsuario = preferencesConfig.readNivelusuario();

//        setando a url da api
        final String url = "http://192.168.137.1/APIChamados/selecionarumChamado.php?id="+idChamado;

        new AsyncTask<Void, Void, Void>(){
            String retorno = "";

            @Override
            protected Void doInBackground(Void... voids) {
                retorno = HttpConnection.get(url);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try {
//                    resgatando o objeto retornado da api,
//                    e setando as variaveis com os valores obtidos dele
                    JSONObject objeto = new JSONObject(retorno);
                    titulo = objeto.optString("titulo");
                    titulo = titulo.trim(); //TIIRA OS ESPAÇOS INUTEIS
                    mensagem = objeto.optString("mensagem");
                    data = objeto.optString("data");
                    status = objeto.getInt("status");

//                    Log.d("onPostExecute", titulo + " " + mensagem + " " + status);
//                    setando os editText
                    lbl_visualizar_titulo_chamado.setText(titulo);
                    lbl_visualizar_mensagem.setText(mensagem);
                    lbl_visualizar_data_chamado.setText(data);
//                    verificando qual o status do chamado,
//                    1 foi resolvido e 0 não
                    if (status == 1){
                        lbl_visualizar_status_chamado.setText("Resolvido");
                        lbl_visualizar_status_chamado.setTextColor(getResources().getColor(R.color.verde));
                    } else if (status == 0){
                        lbl_visualizar_status_chamado.setText("Pendente");
                        lbl_visualizar_status_chamado.setTextColor(getResources().getColor(R.color.vermelho));
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_visualizar_adm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_atualizar){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("AtualIzar chamado");
            builder.setView(R.layout.content_dialog_atualizar);
            builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    observacao = txt_observacao.getText().toString();
//                    if (sw_status.isChecked()){
//                        status = 1;
//                    } else {
//                        status = 0;
//                    }

                    Toast.makeText(getApplicationContext(), "asdafad", Toast
                    .LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancelar", null);
            builder.create().show();
        }


        return super.onOptionsItemSelected(item);
    }
}