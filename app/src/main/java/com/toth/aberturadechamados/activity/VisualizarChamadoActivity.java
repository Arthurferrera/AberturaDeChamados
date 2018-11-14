package com.toth.aberturadechamados.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.toth.aberturadechamados.R;
import com.toth.aberturadechamados.adapter.AdapterImg;
import com.toth.aberturadechamados.adapter.ObservacaoAdapter;
import com.toth.aberturadechamados.adapter.ViewPagerAdapter;
import com.toth.aberturadechamados.model.Chamado;
import com.toth.aberturadechamados.model.HttpConnection;
import com.toth.aberturadechamados.model.SharedPreferencesConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VisualizarChamadoActivity extends AppCompatActivity {

    private int[] imagensFotos = {R.drawable.logo_apesp, R.drawable.aaa, R.drawable.logo, R.drawable.favicon};

    //    declarando os elementos
    TextView lbl_visualizar_titulo_chamado, lbl_visualizar_mensagem, lbl_visualizar_data_chamado, lbl_visualizar_status_chamado, lbl_visualizar_local;
    Integer idChamado;
    String titulo, mensagem, data, nivelUsuario, API_URL, local;
    Integer status;
    SharedPreferencesConfig preferencesConfig;
    ObservacaoAdapter adapter;
    ListView list_view_obs;
    LinearLayout linear_obs, linear_img;
    ScrollView scroll;
    ViewPager viewPager;
    View rootview;
    EditText txt_observacao;
    Switch sw_status;
    LayoutInflater layoutInflater;
    AdapterImg viewPagerAdapter;
    String[] arrayImagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_chamado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewPager);
        LinearLayout.LayoutParams lp =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(lp);
        viewPager.setAdapter(new AdapterImg(this, imagensFotos));

//        pegando o endereço padrão da api
        API_URL = getString(R.string.api_key);

//        instancia do SharedPreferences
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
        linear_img = findViewById(R.id.linear_img);
        lbl_visualizar_local = findViewById(R.id.lbl_visualizar_local);

//        criando e setando o adapter na lista
        adapter = new ObservacaoAdapter(this);
        list_view_obs.setAdapter(adapter);

//        inflando o layout que está dentro do alertDialog
        layoutInflater = LayoutInflater.from(VisualizarChamadoActivity.this);
        rootview = layoutInflater.inflate(R.layout.content_dialog_atualizar, null, false);
//        finds dos elementos que estão no layout do alertDialog
        txt_observacao = rootview.findViewById(R.id.txt_observacao);
        sw_status = rootview.findViewById(R.id.sw_status);

//        resgatando os parametros passados pelo intent
        idChamado = intent.getIntExtra("idChamado", 0);

//        viewPager = new ViewPager(this);
//        viewPagerAdapter = new ViewPagerAdapter(this, imagensFotos);
//        viewPager.setAdapter(new ViewPagerAdapter(this, imagensFotos));

    }

    @Override
    protected void onResume() {
        super.onResume();
//        limpando o adapter para não gerar duplicidade
        adapter.clear();

//        resgatando o nivel do usuario
        nivelUsuario = preferencesConfig.readNivelusuario();

//        setando a url da api
        final String url = API_URL + "selecionarUmChamado.php?id="+idChamado;

        new AsyncTask<Void, Void, Void>(){
            String retorno = "";

//            método que chama a api
            @Override
            protected Void doInBackground(Void... voids) {
                retorno = HttpConnection.get(url);
                return null;
            }

//            método que pega o retorno da api
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
                    status = chamadoJson.getInt("status");
                    local = chamadoJson.getString("local");
                    JSONObject dataJson = chamadoJson.getJSONObject("data");
                    data  = dataJson.getString("date");

//                    pegando as observações de um chamado
                    JSONArray observacaoJson = objeto.getJSONArray("obs");
                    if (observacaoJson.length() == 0 || observacaoJson.equals(null)){
                        linear_obs.setVisibility(View.GONE);
                    } else {
                        for (int i =0; i < observacaoJson.length(); i++){
                            JSONObject obsJson = observacaoJson.getJSONObject(i);
                            Chamado ch = new Chamado();
                            ch.setObservacao(obsJson.getString("observacao"));

                            JSONObject dataObsJson = obsJson.getJSONObject("dataHora");
                            String dataObs = dataObsJson.getString("date");
                            dataObs = converterData(dataObs);
                            ch.setDataObservacao(dataObs);
                            listObsChamado.add(ch);
                        }
                    }

//                    pegando as fotos de um chamado
                    JSONArray fotosArray = objeto.getJSONArray("fotos");
                    if (fotosArray.length() == 0 || fotosArray.equals(null)){
                        linear_img.setVisibility(View.GONE);
                    } else {
                        for (int i =0; i < fotosArray.length(); i++){
                            JSONObject fotoJson = fotosArray.getJSONObject(i);
                            Chamado ch = new Chamado();
                            ch.setImagem(fotoJson.getString("caminhoFoto"));
                            arrayImagens = new String[]{fotoJson.getString("caminhoFoto")};

                        }
                    }

//                    setando os TextView
                    lbl_visualizar_titulo_chamado.setText(titulo);
                    lbl_visualizar_mensagem.setText(mensagem);
                    data = converterData(data);
                    lbl_visualizar_data_chamado.setText(data);
                    lbl_visualizar_local.setText(local);

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
//                adicionando toda a lista no adapter
                adapter.addAll(listObsChamado);
//                viewPagerAdapter = new AdapterImg(VisualizarChamadoActivity.this, arrayImagens);
            }
        }.execute();

    }

//    método que converte a data para o padrão brasileiro
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