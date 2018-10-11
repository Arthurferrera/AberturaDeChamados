package com.example.gabriel.aberturadechamados;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChamadoAdapterAdm extends ArrayAdapter<Chamado> {

//    declarando elementos visuais, variaveis
    ChamadoAdapterAdm adapterAdm;

//    construtor
    public ChamadoAdapterAdm(@NonNull Context ctx) {
        super(ctx, 0, new ArrayList<Chamado>());
        adapterAdm = this;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item_adm, null);
        }

        Chamado chamado = getItem(position);

//        finds dos elementos
        TextView lbl_nome_empresa = v.findViewById(R.id.lbl_nome_empresa);
        TextView lbl_nome_usuario = v.findViewById(R.id.lbl_nome_usuario);
        TextView lbl_titulo_chamado_adm = v.findViewById(R.id.lbl_titulo_chamado_adm);
        TextView lbl_mensagem_chamado_adm = v.findViewById(R.id.lbl_mensagem_chamado_adm);

//        setando o valor dos textsViews
        lbl_nome_empresa.setText(chamado.getNomeEmpresa());
        lbl_nome_usuario.setText(chamado.getNomeUsuario());
        lbl_titulo_chamado_adm.setText(chamado.getTitulo());
        lbl_mensagem_chamado_adm.setText(chamado.getMensagem());

        return v;
    }
}
