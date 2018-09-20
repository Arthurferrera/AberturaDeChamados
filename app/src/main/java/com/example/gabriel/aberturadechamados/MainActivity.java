package com.example.gabriel.aberturadechamados;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list_view_chamados;
    ChamadoAdapter adapter;
    String nomeUsuario, API_URL;
    int idUsuario;
    private SharedPreferencesConfig preferencesConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(this, "Bem Vindo !", Toast.LENGTH_SHORT);

        API_URL = getString(R.string.api_key);

//        instanciando o SharedPreferencesConfig
        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());

//        finds dos elementos
        FloatingActionButton fab = findViewById(R.id.fab);
        list_view_chamados = findViewById(R.id.list_view_chamados);

//        setando o adapter  e o click do item da lista
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

//        pegando as informações do usuario gravados no celular
        nomeUsuario = preferencesConfig.readUsuarioNome();
        idUsuario = Integer.parseInt(preferencesConfig.readUsuarioId());

//        limpando o adapter da lista, para não duplicar
        adapter.clear();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {

                String json = "";
//                URL da API
                final String url = API_URL + "selecionarPendencias.php?idUsuario="+idUsuario;
                json = HttpConnection.get(url);
                return json;
            }

            @Override
            protected void onPostExecute(String json) {
                super.onPostExecute(json);

//                verifica se o json está, nulo
                if (json == null) json = "Sem Dados";
                Log.d("onPostExecute", json);

//                cria a lista de chamados, resgata o array de json
//                percorre o array de json, pegando todos os objetos,
//                adiciona à lista de chamados, e a lista ao adapter
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
                            JSONObject dataJson = chamadoJson.getJSONObject("data");
                            String dataAbertura  = dataJson.getString("date");
                            dataAbertura = converterData(dataAbertura);
                            ch.setData(dataAbertura);
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

//    método que seta o click de um item da lista
//    chama a tela de visualização, passando o id do item
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Chamado item = adapter.getItem(i);
        Intent intencao = new Intent(this, VisualizarChamadoActivity.class);
        intencao.putExtra("idChamado", item.getId());
        startActivity(intencao);
    }

//    método que faz o logoff do aplicativo
//    mudando o stats do login, para falso
    public void Sair() {
        preferencesConfig.writeLoginStatus(false);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

//    cria o menu superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    setando as opções do menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_sair){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sair");
            builder.setMessage("Tem certeza que deseja sair do aplicativo?");
            final Activity activity = this;
            builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Sair();
                }
            });
            builder.setNegativeButton("NÃO", null);
            builder.create().show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String converterData(String dataTotal){
//      separa a data quando entre a data e a hora
        String[] data = dataTotal.split(" ");

//      separa a data a partir do '-', e converte para o padrão brasileiro
        String [] dataSeparada = data[0].split("-");
        String dataConvertida = dataSeparada[2]+"/"+dataSeparada[1]+"/"+dataSeparada[0];

//      separa a hora a partir do ':', e deixa somente no padrão HH:MM
        String [] horaSeparada = data[1].split(":");
        String horaConvertida = horaSeparada[0]+":"+horaSeparada[1];

//       concatenando data e hora para ser mostrado
        String dataCerta = dataConvertida+" "+horaConvertida;

        return dataCerta;
    }
}
