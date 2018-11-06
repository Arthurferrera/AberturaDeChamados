package com.toth.aberturadechamados.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.toth.aberturadechamados.R;
import com.toth.aberturadechamados.model.SharedPreferencesConfig;
import com.toth.aberturadechamados.adapter.EmpresaAdapter;
import com.toth.aberturadechamados.model.Chamado;
import com.toth.aberturadechamados.model.HttpConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmpresasFragment extends Fragment {

//    declarando elementos, variaveis...
    ListView list_view_empresas;
    EmpresaAdapter adapterEmpresa;
    String API_URL;
    int idusuario, status;
    private SharedPreferencesConfig preferencesConfig;

    public EmpresasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View empresasView =  inflater.inflate(R.layout.fragment_empresas, container, false);

//        pegando o caminho da api
        API_URL = getString(R.string.api_key);
//        intancia do preferencesConfig
        preferencesConfig = new SharedPreferencesConfig(getActivity());
//        find's dos elementos
        list_view_empresas = empresasView.findViewById(R.id.list_view_empresas);

//        PEGANDO OS ARGUMENTOS PASSADOS PELO BUNDLE
        Bundle bundle = this.getArguments();
        if (bundle != null){
            status = bundle.getInt("status");
        }

//        instancia do adapter, setando-o na lista e setando o click da lista
        adapterEmpresa = new EmpresaAdapter(getActivity());
        list_view_empresas.setAdapter(adapterEmpresa);
        list_view_empresas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AbrirListaChamados(position);
            }
        });

        return empresasView;
    }

    @Override
    public void onResume() {
        super.onResume();

//        steando o titulo da tela
        getActivity().setTitle("Empresas");
//        resgatando o id do usuario
        idusuario = Integer.parseInt(preferencesConfig.readUsuarioId());
//        limpando o adapter para não duplicar
        adapterEmpresa.clear();

        new AsyncTask<Void, Void, String>(){

//            método da chamada da api
            @Override
            protected String doInBackground(Void... voids) {
                String json = "";
                final String url = API_URL + "empresas.php";
                json = HttpConnection.get(url);
                return json;
            }

//            resgate dos dados retornados da api
            @Override
            protected void onPostExecute(String json) {
                super.onPostExecute(json);

//                verifica se o json está nulo
                if (json == null) json = "Sem dados";
                Log.d("onPostExecute", json);

//                cria a lista de chamados, resgata o array de json
//                percorre o array de json, pegando todos os objetos
//                adciona a lista de chamados, e a lista ao adapter
                ArrayList<Chamado> lstEmpresas = new ArrayList<>();
                if (json != null){
                    try {
                        JSONArray arrayEmpresas = new JSONArray(json);
                        for (int i = 0; i < arrayEmpresas.length(); i++){
                            JSONObject empresaJson = arrayEmpresas.getJSONObject(i);

                            Chamado ch = new Chamado();
                            ch.setNomeEmpresa(empresaJson.getString("razaoSocial"));
                            ch.setIdUsuarioChamado(empresaJson.getInt("id"));
                            lstEmpresas.add(ch);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapterEmpresa.addAll(lstEmpresas);
                }
            }
        }.execute();
    }

//    método que abre a tela que lista os chamados
    public void AbrirListaChamados(int idEmpresa){
//        pegando o id do chamado
        Chamado chamado = adapterEmpresa.getItem(idEmpresa);
//        cria o FragmentManager
        FragmentManager fm = getActivity().getSupportFragmentManager();
//        cria o fragment
//        chama outro fragment passando parametros
        Fragment fragment;
//        verifica qual o status solicitado para chamar a tela certa
        if (status == 1){
            fragment = new ResolvidosAdmFragment();
        } else {
            fragment = new PendentesAdmFragment();
        }
//        passando parametros para a tela que será chamada
        Bundle bundle = new Bundle();
        bundle.putInt("id", chamado.getIdUsuarioChamado());
        fragment.setArguments(bundle);
//        substitui o fragment atual pelo solicitado
        fm.beginTransaction().replace(R.id.frame_content_adm, fragment).addToBackStack(null).commit();
//        setando o titulo da tela
        getActivity().setTitle("Chamados de "+ chamado.getNomeEmpresa());
    }

}
