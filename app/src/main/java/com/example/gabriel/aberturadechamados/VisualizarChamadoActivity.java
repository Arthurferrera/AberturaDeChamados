package com.example.gabriel.aberturadechamados;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class VisualizarChamadoActivity extends AppCompatActivity {

    TextView lbl_visualizar_titulo_chamado, lbl_visualizar_mensagem, lbl_visualizar_data_chamado, lbl_visualizar_status_chamado, lbl_visualizar_observacao;
    Integer idChamado;
    String titulo, mensagem, data, statusChamado;
    Boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_chamado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        lbl_visualizar_titulo_chamado = findViewById(R.id.lbl_titulo_chamado);
        lbl_visualizar_mensagem = findViewById(R.id.lbl_visualzar_mensagem);
        lbl_visualizar_data_chamado = findViewById(R.id.lbl_visualizar_data_chamado);
        lbl_visualizar_status_chamado = findViewById(R.id.lbl_visualizar_status_chamado);
        lbl_visualizar_observacao = findViewById(R.id.lbl_visualizar_observacao);

//        lbl_visualizar_titulo_chamado.setText();
//        lbl_visualizar_mensagem.setText();

        idChamado = intent.getIntExtra("idChamado", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final String url = "http://192.168.2.121/APIChamados/selecionarumChamado.php?id="+idChamado;

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
                    JSONObject objeto = new JSONObject(retorno);
                    Log.d("onPostExecute", "onPostExecute: "+retorno);

//                    lbl_visualizar_titulo_chamado.setText(objeto.optString("titulo"));
                    titulo = objeto.optString("titulo");
//                    titulo = titulo.trim();
                    mensagem = objeto.optString("mensagem");
                    Log.d("onPostExecute", titulo+""+mensagem);
//                    lbl_visualizar_titulo_chamado.setText(titulo);
                    lbl_visualizar_mensagem.setText(objeto.optString("mensagem"));
                    lbl_visualizar_data_chamado.setText(objeto.optString("data"));
                    status = objeto.optBoolean("status");
                    if (!status){
                        lbl_visualizar_status_chamado.setText("Pendente");
                        lbl_visualizar_status_chamado.setTextColor(getResources().getColor(R.color.vermelho));
                    } else {
                        lbl_visualizar_status_chamado.setText("Resolvido");
                        lbl_visualizar_status_chamado.setTextColor(getResources().getColor(R.color.verde));
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}