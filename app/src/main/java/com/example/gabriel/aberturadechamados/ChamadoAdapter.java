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

public class ChamadoAdapter extends ArrayAdapter<Chamado> {

    public ChamadoAdapter(Context ctx, ArrayList<Chamado> lstChamados) {
        super(ctx, 0, lstChamados);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if(v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, null);
        }

        // Pegando o contato daquela posição
        Chamado chamado = getItem(position);

        // Acessando os elementos visuais
        TextView lbl_titulo_chamado = v.findViewById(R.id.lbl_titulo_chamado);
        TextView lbl_mensagem_chamado = v.findViewById(R.id.lbl_mensagem_chamado);

//        verificar se falta algo no adapter

        return v;
    }

}
