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

public class ObservacaoAdapter extends ArrayAdapter<Chamado>{

//    declarando elementos visuais, variaveis....
    ObservacaoAdapter adapter;

//    construtor
    public ObservacaoAdapter(Context ctx) {
        super(ctx, 0, new ArrayList<Chamado>());
        adapter = this;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item_obs, null);

            Chamado chamado = getItem(position);

            TextView lbl_observacao = v.findViewById(R.id.lbl_observacao_item);
            TextView lbl_dt_observacao = v.findViewById(R.id.lbl_dt_observacao_item);

            lbl_observacao.setText(chamado.getObservacao());
            lbl_dt_observacao.setText(chamado.getDataObservacao());

        }

        return v;
    }
}
