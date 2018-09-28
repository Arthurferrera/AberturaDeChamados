package com.example.gabriel.aberturadechamados;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendentesAdmFragment extends Fragment {

    ListView list_view_pendentes_adm;
    ChamadoAdapterAdm adapterAdm;
    String API_URL;
    int idUsuario;
    private SharedPreferencesConfig preferencesConfig;


    public PendentesAdmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pendentes_adm, container, false);


//        Toast.makeText(this, "Bem Vindo !", Toast.LENGTH_SHORT);

        API_URL = getString(R.string.api_key);

//        instanciando o SharedPreferencesConfig
        preferencesConfig = new SharedPreferencesConfig(getActivity());

//        finds dos elementos
        list_view_pendentes_adm = view.findViewById(R.id.list_pendentes_adm);

//        setando o adapter  e o click do item da lista
        adapterAdm = new ChamadoAdapterAdm(getActivity());
        list_view_pendentes_adm.setAdapter(adapterAdm);
        list_view_pendentes_adm.setOnItemClickListener(new ListView.OnItemClickListener() {
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

//        pegando as informações do usuario gravados no celular
        idUsuario = Integer.parseInt(preferencesConfig.readUsuarioId());

//        limpando o adapter da lista, para não duplicar
        adapterAdm.clear();

        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {

                String json = "";
//                URL da API
                final String url = API_URL + "selecionarChamadosAdm.php?status=0";
                json = HttpConnection.get(url);
                return json;
            }


            @Override
            protected void onPostExecute(String json) {
                super.onPostExecute(json);

//                verifica se o json retornou nulo
                if(json == null) json = "Sem dados";
                Log.d("onPostExecute", json);

//                cria a lista de chamados, resgata o array de json
//                percorre o array de json, pegando todos os objetos,
//                adiciona à lista de chamados, e a lista ao adapter
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

    public void AbrirVisualizar(int i) {
        Chamado item = adapterAdm.getItem(i);
        Intent intencao = new Intent(getActivity()
                , VisualizarAdmActivity.class);
        intencao.putExtra("idChamado", item.getId());
        startActivity(intencao);
    }
}
