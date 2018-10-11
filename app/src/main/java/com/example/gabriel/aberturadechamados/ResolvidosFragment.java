package com.example.gabriel.aberturadechamados;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResolvidosFragment extends Fragment {

//    declarando elementos visuais, variaveis...
    ListView list_view_resolvidos;
    ChamadoAdapter adapter;
    String API_URL;
    int idUsuario;
    private SharedPreferencesConfig preferencesConfig;


    public ResolvidosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        inflando o layout do fragment
        View view = inflater.inflate(R.layout.fragment_resolvidos, container, false);
//        instanciando o SharedPreferencesConfig, para permitir resgatar infformações gravadas no celular
        preferencesConfig = new SharedPreferencesConfig(getActivity());
//        resgatando o caminho padrao da url
        API_URL = getString(R.string.api_key);

//        finds dos elementos
        list_view_resolvidos = view.findViewById(R.id.list_view_resolvidos);

//        criando o adapter, setando na lista
//        setando o click de item da lista
        adapter = new ChamadoAdapter(getActivity());
        list_view_resolvidos.setAdapter(adapter);
        list_view_resolvidos.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AbrirVisualizar(position);
            }
        });

//        retornando a tela
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

//        pegando informações gravadas no celular
        idUsuario = Integer.parseInt(preferencesConfig.readUsuarioId());

//        limpando o apadter para não duplicar
        adapter.clear();

        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {

                String json = "";
//                URL da API
                final String url = API_URL + "selecionarResolvidos.php?idUsuario="+idUsuario;
                json = HttpConnection.get(url);
                return json;
            }

            @Override
            protected void onPostExecute(String json) {
                super.onPostExecute(json);

//                verifica se o json retornou nulo
                if (json == null) json = "Sem Dados";
                Log.d("onPostExecute", json);

//                cria a lista de chamados, resgata o array de json
//                percorre o array de json, pegando todos os objetos,
//                adiciona à lista de chamados, e a lista ao adapter
                ArrayList<Chamado> lstChamados = new ArrayList<>();
                if (json != null){
                    try {
                        JSONArray arrayChamados = new JSONArray(json);
                        for (int i = 0; i < arrayChamados.length(); i++){
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.addAll(lstChamados);
                }
            }
        }.execute();
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

    //    método que seta o click de um item da lista
//    chama a tela de visualização, passando o id do item
    public void AbrirVisualizar(int i) {
        Chamado item = adapter.getItem(i);
        Intent intencao = new Intent(getActivity(), VisualizarChamadoActivity.class);
        intencao.putExtra("idChamado", item.getId());
        startActivity(intencao);
    }
}
