package com.example.gabriel.aberturadechamados;


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
public class ResolvidosAdmFragment extends Fragment {

//    declarando os elementos visuais, variaveis...
    ListView list_resolvidos_adm;
    ChamadoAdapterAdm adapter;
    String API_URL;
    int idUsuario;
    private SharedPreferencesConfig preferencesConfig;

    public ResolvidosAdmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        inflando o layout do fragment
        View view = inflater.inflate(R.layout.fragment_resolvidos_adm, container, false);
//        instanciando o SharedPreferencesConfig, para permitir resgatar infformações gravadas no celular
        preferencesConfig = new SharedPreferencesConfig(getActivity());
//        resgatando o caminho padrao da url
        API_URL = getString(R.string.api_key);

//        finds dos elementos
        list_resolvidos_adm = view.findViewById(R.id.list_resolvidos_adm);

//        criando o adapter, setando na lista
//        setando o click de item da lista
        adapter = new ChamadoAdapterAdm(getActivity());
        list_resolvidos_adm.setAdapter(adapter);
        list_resolvidos_adm.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AbrirVisualizar(position);
            }
        });

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
                final String url = API_URL + "selecionarChamadosAdm.php?status=1";
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
                            ch.setNomeEmpresa(chamadoJson.getString("razaoSocial"));
                            ch.setNomeUsuario(chamadoJson.getString("nome"));

                            lstChamados.add(ch);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    adicionando toda a lista no adapter
                    adapter.addAll(lstChamados);
                }
            }
        }.execute();
    }

//    método que abre a tela de visualização dos detalhes do chamado
    public void AbrirVisualizar(int i) {
        Chamado item = adapter.getItem(i);
        Intent intencao = new Intent(getActivity()
                , VisualizarAdmActivity.class);
        intencao.putExtra("idChamado", item.getId());
        startActivity(intencao);
    }
}
