package com.toth.aberturadechamados.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.toth.aberturadechamados.R;
import com.toth.aberturadechamados.model.SharedPreferencesConfig;
import com.toth.aberturadechamados.api.InserirChamadoApi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class AbrirChamadoFragment extends Fragment {

//    declarando atributos, elemenntos...
    EditText txt_titulo, txt_mensagem, txt_local;
    Button btn_abrir_chamado;
    String titulo,mensagem,local;
    Boolean status;
    String dataAberturaChamado, API_URL;
    int idUsuario;
    private SharedPreferencesConfig preferencesConfig;

    public AbrirChamadoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_abrir_chamado, container, false);

//        pegando o caminho da api
        API_URL = getString(R.string.api_key);

//        instanciando o sharedPreferences
        preferencesConfig = new SharedPreferencesConfig(getActivity());

//        finds dos elementos
        txt_titulo = view.findViewById(R.id.txt_titulo);
        txt_mensagem = view.findViewById(R.id.txt_mensagem);
        txt_local = view.findViewById(R.id.txt_local);
        btn_abrir_chamado = view.findViewById(R.id.btn_abrir_chamado);

//        click do botão
        btn_abrir_chamado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AbrirChamado();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

//    método que valida se os campos estão vazios
    private boolean ValidarCampos(){
        EditText campoComFoco = null;
        boolean isValid = true;

//        primeiro verfica o valor do campo, se for igual a 0, significa que está vazio
//        depois mostra a mensagem de erro
//        e seta a variavel isValid, como null, assim não é valido o formulario

        if(txt_titulo.getText().toString().length() == 0){
            campoComFoco = txt_titulo;
            txt_titulo.setError("Titulo obrigatório");
            isValid = false;
        }
        if(txt_mensagem.getText().toString().length() == 0){
            if(campoComFoco == null){
                campoComFoco = txt_mensagem;
            }
            txt_mensagem.setError("Mensagem obrigatória");
            isValid = false;
        }
        if(txt_local.getText().toString().length() == 0){
            if(campoComFoco == null){
                campoComFoco = txt_local;
            }
            txt_local.setError("Local obrigatório");
            isValid = false;
        }
        if(campoComFoco != null){
            campoComFoco.requestFocus();
        }
        return isValid;
    }

//    método que faz o processo de gravar/abrir o chamado
    public void AbrirChamado() throws UnsupportedEncodingException {
        if(ValidarCampos()){
//            resgatando os valores dos inputs
            titulo = txt_titulo.getText().toString();
            mensagem = txt_mensagem.getText().toString();
            local = txt_local.getText().toString();

//            pegando o idUsuario que está gravado em um tipo de 'sessão'
            idUsuario = Integer.parseInt(preferencesConfig.readUsuarioId());

//            deixando o que foi digitado pelo usuario no padrao utf-8, para ser passado na url sem problemas
            mensagem = URLEncoder.encode(mensagem, "UTF-8");
            titulo = URLEncoder.encode(titulo, "UTF-8");
            local = URLEncoder.encode(local, "UTF-8");

//            chamando metodo que seta a data e hora atual
            setarDataHora();
//            setando o status em false, pois todos cos chamados abertos vão ser pendentes, só muda o status se for resolvido
            status = false;

//            chamadno o arquivo da api, passando a url
            String url = API_URL + "inserir.php?";
            String parametros = "titulo="+titulo+"&mensagem="+mensagem+"&local="+local+"&status=0&idUsuario="+idUsuario;
            url += parametros;
            new InserirChamadoApi(url, getActivity()).execute();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new PendentesFragment()).commit();
        }
    }

//    método pega a data e hora atual
    public void setarDataHora() throws UnsupportedEncodingException {
//        setando a data e hora da consulta
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int hora = c.get(Calendar.HOUR);
        int minutos = c.get(Calendar.MINUTE);
        String dataAtual = String.format("%02d/%02d/%d ás %02d:%02d", dia, mes+1, ano, hora, minutos);
        dataAberturaChamado = dataAtual;
//        tirando os espaços, deixando no formato utf-8
        dataAberturaChamado = URLEncoder.encode(dataAberturaChamado, "UTF-8");
    }

}
