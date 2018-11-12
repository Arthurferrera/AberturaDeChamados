package com.toth.aberturadechamados.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.toth.aberturadechamados.api.UploadFotoApi;
import com.toth.aberturadechamados.model.ImageFilePath;
import com.toth.aberturadechamados.R;
import com.toth.aberturadechamados.model.SharedPreferencesConfig;
import com.toth.aberturadechamados.api.InserirChamadoApi;

import java.io.File;
import java.io.InputStream;
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

    final int REQUEST_PERMISSION = 101;
    final int SELECT_PICTURE = 1;

    Bitmap foto1, foto2, foto3;
    StringBuffer nomeImagem = new StringBuffer();
    ImageView img_1, img_2, img_3;
    ImageView[] vetorImg;
    String pathFoto1, getPathFoto2, getPathFoto3;
    String[] listaPaths;
    int posicaoImg = 0;

    View.OnClickListener clickImageView = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (verificarPermissoes()){
                posicaoImg = Integer.parseInt(v.getTag().toString());
                capturarImagem();
            } else {
                solicitarPermissoes();
            }
        }
    };

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
        img_1 = view.findViewById(R.id.img_1);
        img_2 = view.findViewById(R.id.img_2);
        img_3 = view.findViewById(R.id.img_3);

        img_1.setOnClickListener(clickImageView);
        img_2.setOnClickListener(clickImageView);
        img_3.setOnClickListener(clickImageView);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                capturarImagem();
            } else {
//                permissão negada
            }
        }
    }

    void solicitarPermissoes(){
        String[] permissoes = new String[1];
        permissoes[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
        ActivityCompat.requestPermissions(getActivity(), permissoes, REQUEST_PERMISSION);
    }

    boolean verificarPermissoes(){
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    private void capturarImagem(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PICTURE);
//        Intent pickGaleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        String pickTitle = "Selecione uma imagem";
//
//        Intent chooserIntent = Intent.createChooser(pickGaleryIntent, pickTitle);
//
//        if (chooserIntent.resolveActivity(getActivity().getPackageManager()) != null){
//            startActivityForResult(chooserIntent, SELECT_PICTURE);
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == SELECT_PICTURE ){

            if(resultCode == getActivity().RESULT_OK){
                //selecionou alguma coisa

                try {
//                    pegando a img em binario
                    InputStream inp = getActivity().getContentResolver()
                            .openInputStream(data.getData());

                    switch (posicaoImg){
                        case 0:
                            foto1 = BitmapFactory.decodeStream(inp);
                            img_1.setImageBitmap(foto1);
                            break;
                        case 1:
                            foto2 = BitmapFactory.decodeStream(inp);
                            img_2.setImageBitmap(foto2);
                            break;
                        case 2:
                            foto3 = BitmapFactory.decodeStream(inp);
                            img_3.setImageBitmap(foto3);
                            break;
                        default:
//                        default
                            break;
                    }
                    String url = API_URL+"upload_imagem.php";
                    nomeImagem.setLength(0); //limpando o atringBuffer
                    new UploadFotoApi(getActivity(), nomeImagem, url).execute(foto1);
                    pathFoto1 = API_URL+"img/";
                    Toast.makeText(getActivity(), pathFoto1, Toast.LENGTH_SHORT).show();
                    listaPaths = new String[]{pathFoto1, getPathFoto2, getPathFoto3};
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }

//        if (resultCode ==  getActivity().RESULT_OK){
//            if (requestCode == SELECT_PICTURE){
//                Uri imgUri = data.getData();
//                String realPath = ImageFilePath.getPath(getActivity(), data.getData());
//                Picasso.get().load(new File(realPath)).into(vetorImg[posicaoImg]);
//                switch (posicaoImg){
//                    case 0:
//                        pathFoto1 = realPath;
//                        break;
//                    case 1:
//                        getPathFoto2 = realPath;
//                        break;
//                    case 2:
//                        getPathFoto3 = realPath;
//                        break;
//                    default:
////                        default
//                        break;
//                }
//                listaPaths = new String[]{pathFoto1, getPathFoto2, getPathFoto3};
//            }
//        }
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
            String caminhoFoto = "img/"+nomeImagem.toString();

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
            String parametros = "titulo="+titulo+"&mensagem="+mensagem+"&local="+local+"&status=0&idUsuario="+idUsuario+"&imagem="+caminhoFoto;
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
