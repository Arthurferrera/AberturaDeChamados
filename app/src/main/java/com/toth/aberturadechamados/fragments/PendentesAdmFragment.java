package com.toth.aberturadechamados.fragments;

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

import com.toth.aberturadechamados.R;
import com.toth.aberturadechamados.model.SharedPreferencesConfig;
import com.toth.aberturadechamados.activity.VisualizarAdmActivity;
import com.toth.aberturadechamados.adapter.ChamadoAdapterAdm;
import com.toth.aberturadechamados.model.Chamado;
import com.toth.aberturadechamados.model.HttpConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendentesAdmFragment extends Fragment {

//    declarando elementos visuais, variaveis...
    ListView list_view_pendentes_adm;
    ChamadoAdapterAdm adapterAdm;
    String API_URL;
    int idUsuario, idUsuarioChamado;
    private SharedPreferencesConfig preferencesConfig;

    public PendentesAdmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        inflando o layout do fragment
        View view = inflater.inflate(R.layout.fragment_pendentes_adm, container, false);

//        resgatando o caminho padrao da url
        API_URL = getString(R.string.api_key);

//        instanciando o SharedPreferencesConfig
        preferencesConfig = new SharedPreferencesConfig(getActivity());

//        finds dos elementos
        list_view_pendentes_adm = view.findViewById(R.id.list_pendentes_adm);

//        pegando os argumrntos passados pelo bundle
        Bundle bundle = this.getArguments();
        if (bundle != null){
            idUsuarioChamado = bundle.getInt("id");
        }

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
//            método que chama a api
            @Override
            protected String doInBackground(Void... voids) {
                String json = "";
//                URL da API
                final String url = API_URL + "selecionarChamadosAdm.php?status=0&idUsuario="+idUsuarioChamado;
                json = HttpConnection.get(url);
                return json;
            }

//            método que pega o retorno da api
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
                            JSONObject dataJson = chamadoJson.getJSONObject("data");
                            String dataAbertura  = dataJson.getString("date");
                            dataAbertura = converterData(dataAbertura);
                            ch.setData(dataAbertura);
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
//        String dataCerta = dataConvertida+" "+horaConvertida;

        return dataConvertida;
    }

//    método que abre a tela de visualização do chamado
    public void AbrirVisualizar(int i) {
        Chamado item = adapterAdm.getItem(i);
        Intent intencao = new Intent(getActivity()
                , VisualizarAdmActivity.class);
        intencao.putExtra("idChamado", item.getId());
        startActivity(intencao);
    }
}
