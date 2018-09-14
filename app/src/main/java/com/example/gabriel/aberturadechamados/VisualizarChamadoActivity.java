package com.example.gabriel.aberturadechamados;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.aberturadechamados.api.InserirObservacaoApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.gabriel.aberturadechamados.R.layout.activity_abrir_chamado;
import static com.example.gabriel.aberturadechamados.R.layout.activity_login;
import static com.example.gabriel.aberturadechamados.R.layout.activity_visualizar_chamado;
import static com.example.gabriel.aberturadechamados.R.layout.content_dialog_atualizar;

public class VisualizarChamadoActivity extends AppCompatActivity {

    TextView lbl_visualizar_titulo_chamado, lbl_visualizar_mensagem, lbl_visualizar_data_chamado, lbl_visualizar_status_chamado, lbl_visualizar_observacao;
    Integer idChamado;
    String titulo, mensagem, data, nivelUsuario;
    Integer status;
    SharedPreferencesConfig preferencesConfig;
    ObservacaoAdapter adapter;
    ListView list_view_obs;
    LinearLayout linear_obs;
    ScrollView scroll;

    View rootview;
    EditText txt_observacao;
    Switch sw_status;
    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_chamado);
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
        list_view_obs = findViewById(R.id.list_view_obs);
        linear_obs = findViewById(R.id.linear_obs);
        scroll = findViewById(R.id.scrollChamado);

        adapter = new ObservacaoAdapter(this);
        list_view_obs.setAdapter(adapter);

        layoutInflater = LayoutInflater.from(VisualizarChamadoActivity.this);
        rootview = layoutInflater.inflate(R.layout.content_dialog_atualizar, null, false);

        txt_observacao = rootview.findViewById(R.id.txt_observacao);
        sw_status = rootview.findViewById(R.id.sw_status);

//        resgatando os parametros passados pelo intent
        idChamado = intent.getIntExtra("idChamado", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        TODO: Fazer o scroll ir para o topo sempre que iniciar/voltar a activity
//        scroll.fullScroll(ScrollView.FOCUS_UP);

        adapter.clear();

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
                ArrayList<Chamado> listObsChamado = new ArrayList<>();
                try {
//                    resgatando o objeto retornado da api,
//                    e setando as variaveis com os valores obtidos dele
                    JSONObject objeto = new JSONObject(retorno);
                    JSONObject chamadoJson = objeto.getJSONObject("chamado");
                    titulo = chamadoJson.optString("titulo");
                    titulo = titulo.trim(); //TIIRA OS ESPAÇOS INUTEIS
                    mensagem = chamadoJson.optString("mensagem");
                    JSONObject dataJson = chamadoJson.getJSONObject("data");
                    data  = dataJson.getString("date");
                    status = chamadoJson.getInt("status");

                    JSONArray observacaoJson = objeto.getJSONArray("obs");
                    if (observacaoJson.length() == 0 || observacaoJson.equals(null)){
                        linear_obs.setVisibility(View.GONE);
                    } else {
                        for (int i =0; i < observacaoJson.length(); i++){

                            JSONObject obsJson = observacaoJson.getJSONObject(i);
                            Chamado ch = new Chamado();
                            ch.setObservacao(obsJson.getString("observacao"));

                            JSONObject dataObsJson = obsJson.getJSONObject("dataHora");
                            ch.setDataObservacao(dataObsJson.getString("date"));
                            listObsChamado.add(ch);
                        }
                    }

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
                adapter.addAll(listObsChamado);
            }
        }.execute();
    }
}