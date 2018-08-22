package com.example.gabriel.aberturadechamados;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list_view_chamados;
    ChamadoAdapter adapter;
    String titulo, mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Intent intent = getIntent();
//        titulo = intent.getStringExtra("usuario");
//        mensagem = intent.getStringExtra("senha");

//        finds dos elementos
        FloatingActionButton fab = findViewById(R.id.fab);
        list_view_chamados = findViewById(R.id.list_view_chamados);

        adapter = new ChamadoAdapter(this);
        list_view_chamados.setAdapter(adapter);
        list_view_chamados.setOnItemClickListener(this);

//        ação do botão float
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencao = new Intent(getApplicationContext(), AbrirChamadoActivity.class);
                startActivity(intencao);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.clear();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {

                String json = "";

                final String url = "http://192.168.2.121/APIChamados/selecionar.php";
                json = HttpConnection.get(url);

                return json;
            }

            @Override
            protected void onPostExecute(String json) {
                super.onPostExecute(json);

                if (json == null) json = "Sem Dados";
                Log.d("onPostExecute", json);

                ArrayList<Chamado> lstChamados = new ArrayList<>();
                if (json != null) {
                    try {
                        JSONArray arrayChamados = new JSONArray(json);
                        for (int i = 0; i < arrayChamados.length(); i++) {
                            JSONObject chamadoJson = arrayChamados.getJSONObject(i);

                            Chamado ch = new Chamado();
                            ch.setId(chamadoJson.getInt("id"));
                            ch.setTitulo(chamadoJson.getString("titulo"));
                            ch.setMensagem(chamadoJson.getString("mensagem"));
                            ch.setData(chamadoJson.getString("data"));
//                            ch.setStatus(chamadoJson.getBoolean("status"));

                            lstChamados.add(ch);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    adapter.addAll(lstChamados);
                }
            }
        }.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Chamado item = adapter.getItem(i);
        Intent intencao = new Intent(this, VisualizarChamadoActivity.class);
        intencao.putExtra("idChamado", item.getId());
        startActivity(intencao);
    }
}
