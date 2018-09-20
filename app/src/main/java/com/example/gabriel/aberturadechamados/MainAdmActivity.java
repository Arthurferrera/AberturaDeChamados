package com.example.gabriel.aberturadechamados;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainAdmActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list_view_chamados_adm;
    ChamadoAdapterAdm adapterAdm;
    String nomeUsuario, API_URL;
    int idUsuario;
    private SharedPreferencesConfig preferencesConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_adm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        API_URL = getString(R.string.api_key);

        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());

        list_view_chamados_adm = findViewById(R.id.list_view_chamados_adm);

        adapterAdm = new ChamadoAdapterAdm(this);
        list_view_chamados_adm.setAdapter(adapterAdm);
        list_view_chamados_adm.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        nomeUsuario = preferencesConfig.readUsuarioNome();
        idUsuario = Integer.parseInt(preferencesConfig.readUsuarioId());

        adapterAdm.clear();

        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {

                String json = "";
                final String url = API_URL + "selecionarChamadosAdm.php";
                json = HttpConnection.get(url);
                return json;
            }


            @Override
            protected void onPostExecute(String json) {
                super.onPostExecute(json);

                if(json == null) json = "Sem dados";
                Log.d("onPostExecute", json);


                ArrayList<Chamado> lstChamado = new ArrayList<>();
                if (json != null){
                    try {
                        JSONArray arrayChamados = new JSONArray(json);
                        for (int i = 0; i < arrayChamados.length(); i++){
                            JSONObject chamadoJson = arrayChamados.getJSONObject(i);

                            Chamado ch = new Chamado();
                            ch.setId(chamadoJson.getInt("id"));
                            ch.setTitulo(chamadoJson.getString("titulo"));
                            ch.setMensagem(chamadoJson.getString("mensagem"));
                            ch.setNomeEmpresa(chamadoJson.getString("razaoSocial"));
                            ch.setNomeUsuario(chamadoJson.getString("nome"));

                            lstChamado.add(ch);
                        }
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                    adapterAdm.addAll(lstChamado);
                }
            }
        }.execute();
    }

//    método que seta o click de um item da lista
//    chama a tela de visualização, passando o id do item
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Chamado item = adapterAdm.getItem(i);
        Intent intencao = new Intent(this, VisualizarAdmActivity.class);
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
