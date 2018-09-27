package com.example.gabriel.aberturadechamados;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendentesAdmFragment extends Fragment {

    ListView list_view_chamados_adm;
    ChamadoAdapter adapterAdm;
    String nomeUsuario, API_URL;
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
        list_view_chamados_adm = view.findViewById(R.id.list_view_chamados_adm);

//        setando o adapter  e o click do item da lista
        adapterAdm = new ChamadoAdapter(getActivity());
        list_view_chamados_adm.setAdapter(adapterAdm);
//        list_view_chamados.setOnItemClickListener();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

//        pegando as informações do usuario gravados no celular
        nomeUsuario = preferencesConfig.readUsuarioNome();
        idUsuario = Integer.parseInt(preferencesConfig.readUsuarioId());

//        limpando o adapter da lista, para não duplicar
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
