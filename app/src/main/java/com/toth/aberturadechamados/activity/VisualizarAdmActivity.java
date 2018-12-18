package com.toth.aberturadechamados.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.toth.aberturadechamados.R;
import com.toth.aberturadechamados.adapter.ObservacaoAdapter;
import com.toth.aberturadechamados.api.InserirObservacaoApi;
import com.toth.aberturadechamados.model.Chamado;
import com.toth.aberturadechamados.model.HttpConnection;
import com.toth.aberturadechamados.model.SharedPreferencesConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class VisualizarAdmActivity extends AppCompatActivity {

//    declarando os elementos visuais, variaveis...
    TextView lbl_visualizar_titulo_chamado, lbl_visualizar_mensagem, lbl_visualizar_data_chamado;
    TextView lbl_visualizar_status_chamado, lbl_solicitante, lbl_empresa, lbl_cnpj, lbl_local_visualizar_adm;
    String titulo, mensagem, data, nivelUsuario, observacao, solicitante, empresa, cnpj, API_URL, local;
    SharedPreferencesConfig preferencesConfig;
    LayoutInflater layoutInflater;
    ObservacaoAdapter adapter;
    Integer idChamado, status;
    FloatingActionButton fab;
    EditText txt_observacao;
    LinearLayout linear_obs, linear_img;
    ListView list_view_obs;
    ScrollView scrollView;
    Boolean statusChamado;
    Switch sw_status;
    View rootview;
    ArrayList<String> arrayImagens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_adm);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        pegando o caminho padrao da api
        API_URL = getString(R.string.api_key);

//        instaniando a preferencesConfig, onde está gravado algumas informações
        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());

//        pegando o intent
        Intent intent = getIntent();

//        finds dos elementos
        fab = findViewById(R.id.fab);
        lbl_visualizar_titulo_chamado = findViewById(R.id.lbl_titulo_chamado);
        lbl_visualizar_mensagem = findViewById(R.id.lbl_visualzar_mensagem);
        lbl_visualizar_data_chamado = findViewById(R.id.lbl_visualizar_data_chamado);
        lbl_visualizar_status_chamado = findViewById(R.id.lbl_visualizar_status_chamado);
        list_view_obs = findViewById(R.id.list_view_obs);
        linear_obs = findViewById(R.id.linear_obs);
        lbl_solicitante = findViewById(R.id.lbl_solicitante);
        lbl_empresa = findViewById(R.id.lbl_empresa);
        lbl_cnpj = findViewById(R.id.lbl_cnpj);
        linear_img = findViewById(R.id.linear_img);
        lbl_local_visualizar_adm = findViewById(R.id.lbl_local_visualizar_adm);

//        criando e setando o adapter na lista
        adapter = new ObservacaoAdapter(this);
        list_view_obs.setAdapter(adapter);

//        inflando o layout que vai aparecer no alert
        layoutInflater = LayoutInflater.from(VisualizarAdmActivity.this);
        rootview = layoutInflater.inflate(R.layout.content_dialog_atualizar, null, false);
//        finds dos elementos do alert
        txt_observacao = rootview.findViewById(R.id.txt_observacao);
        sw_status = rootview.findViewById(R.id.sw_status);

//        resgatando os parametros passados pelo intent
        idChamado = intent.getIntExtra("idChamado", 0);

//        ação do botão float
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(VisualizarAdmActivity.this);
                builder.setTitle("AtualIzar chamado");
                builder.setView(rootview);
                builder.setCancelable(false)
//                        ação do botão de enviar do alert
                        .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
//                                caso campos estejam preenhidos ele salva, caso contrário não
                                if (ValidarCampos()){
//                                    ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                    SalvarObs();
                                    txt_observacao.setError(null);
                                } else {
                                    ((ViewGroup)rootview.getParent()).removeView(rootview);
                                    Toast.makeText(getApplicationContext(), "Resposta não enviada. Favor preencher os campos obrigatórios.", Toast.LENGTH_SHORT).show();
                                }

                            }
                }) //ação do botão cancelar do alertDialog
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                zerarAlert();
                                txt_observacao.setError(null);
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

//        limpando o adapter para não duplicar os registros
        adapter.clear();

//        resgatando informações gravadas no celular
        nivelUsuario = preferencesConfig.readNivelusuario();

//        setando a url da api
        final String url = API_URL + "selecionarumChamado.php?id="+idChamado;

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
                    titulo = titulo.trim(); //TIIRA OS ESPAÇOS NO COMEÇO E NO FINAL DA STRING
                    mensagem = chamadoJson.optString("mensagem");
                    JSONObject dataJson = chamadoJson.getJSONObject("data");
                    data  = dataJson.getString("date");
                    status = chamadoJson.getInt("status");
                    solicitante = chamadoJson.getString("nome");
                    empresa = chamadoJson.getString("razaoSocial");
                    cnpj = chamadoJson.getString("cnpj");
                    local = chamadoJson.getString("local");

                    JSONArray observacaoJson = objeto.getJSONArray("obs");
                    if (observacaoJson.length() == 0 || observacaoJson.equals(null)){
                        linear_obs.setVisibility(View.GONE);
                    } else {
                        for (int i =0; i < observacaoJson.length(); i++){
//                            resgata todas as obsercações que um chamado  possui
                            JSONObject obsJson = observacaoJson.getJSONObject(i);
                            Chamado ch = new Chamado();
                            ch.setObservacao(obsJson.getString("observacao"));
//                            resgatando a data e formatando-a
                            JSONObject dataObsJson = obsJson.getJSONObject("dataHora");
                            String dataTotal = dataObsJson.getString("date");
                            dataTotal = converterData(dataTotal);
                            ch.setDataObservacao(dataTotal);
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
//                            arrayImagens = new String[]{fotoJson.getString("caminhoFoto")};
                            arrayImagens.add(fotoJson.getString("caminhoFoto"));
                            Log.d("Imagens", arrayImagens.get(0));

                        }
                    }

//                    setando os editText
                    lbl_visualizar_titulo_chamado.setText(titulo);
                    lbl_visualizar_mensagem.setText(mensagem);
                    data = converterData(data);
                    lbl_visualizar_data_chamado.setText(data);
                    lbl_solicitante.setText(solicitante);
                    lbl_empresa.setText(empresa);
                    lbl_cnpj.setText(cnpj);
                    lbl_local_visualizar_adm.setText(local);

//                    verificando qual o status do chamado,
//                    1 foi resolvido e 0 não
                    if (status == 1){
                        lbl_visualizar_status_chamado.setText("Resolvido");
                        lbl_visualizar_status_chamado.setTextColor(getResources().getColor(R.color.verde));
                        fab.setVisibility(View.GONE);
                    } else if (status == 0){
                        lbl_visualizar_status_chamado.setText("Pendente");
                        lbl_visualizar_status_chamado.setTextColor(getResources().getColor(R.color.vermelho));
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
//                adicionando toda a lista no adapter
                adapter.addAll(listObsChamado);
            }
        }.execute();
    }

//    função que valida se os campos estão vazios
    private boolean ValidarCampos(){
        EditText campoComFoco = null;
        boolean isValid = true;

        if (txt_observacao.getText().toString().length() == 0){
            campoComFoco = txt_observacao;
            txt_observacao.setError("Mensagem obrigatória");
            isValid = false;
        }
        if(campoComFoco != null){
            campoComFoco.requestFocus();
        }
        return isValid;
    }

//    funcção que salva uma observação de um chamado
    private void SalvarObs(){
//        resgata a informação
        observacao = txt_observacao.getText().toString();
        statusChamado = sw_status.isChecked();

        try {
//            encode para ficar no padrão de url
            observacao = URLEncoder.encode(observacao, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//            chamando a api e o arquivo da api
        String url = API_URL + "inserirObservacao.php?";
        String parametros = "observacao="+observacao+"&statusChamado="+statusChamado+"&idChamado="+idChamado;
        url += parametros;
        new InserirObservacaoApi(url, this).execute();
        onResume();
        zerarAlert();
    }

//    função que converte a data para o padrão brasileiro
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

//    função que zera os campos de um alert
    public void zerarAlert(){
        ((ViewGroup)rootview.getParent()).removeView(rootview);
        txt_observacao.setText("");
        sw_status.setChecked(false);
    }
    public void AbrirFotos(View view) {
        Intent intent = new Intent(this, FotosActivity.class);
        intent.putStringArrayListExtra("imgs", arrayImagens);
        startActivity(intent);
    }

}
