package com.toth.aberturadechamados.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.toth.aberturadechamados.model.Chamado;
import com.toth.aberturadechamados.R;

import java.util.ArrayList;

public class ChamadoAdapter extends ArrayAdapter<Chamado> {

//    declarando elementos visuais, variaveis...
    ChamadoAdapter adapter;

//    construtor
    public ChamadoAdapter(Context ctx) {
        super(ctx, 0,  new ArrayList<Chamado>());
        adapter = this;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if(v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, null);
        }

        // Pegando o chamado daquela posição
        Chamado chamado = getItem(position);

        // Acessando os elementos visuais
        TextView lbl_titulo_chamado = v.findViewById(R.id.lbl_titulo_chamado);
        TextView lbl_mensagem_chamado = v.findViewById(R.id.lbl_mensagem_chamado);
        TextView lbl_data_chamado = v.findViewById(R.id.lbl_data_chamado);

//        verificar se falta algo no adapter
        lbl_titulo_chamado.setText(chamado.getTitulo());
        lbl_mensagem_chamado.setText(chamado.getMensagem());
        lbl_data_chamado.setText(chamado.getData());

        return v;
    }
}
