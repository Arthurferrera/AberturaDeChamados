package com.toth.aberturadechamados.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.toth.aberturadechamados.R;
import com.toth.aberturadechamados.model.Chamado;

import java.util.ArrayList;

public class EmpresaAdapter extends ArrayAdapter<Chamado> {

    public EmpresaAdapter(@NonNull Context context) {
        super(context, 0, new ArrayList<Chamado>());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item_empresa, null);
        }

        Chamado chamado = getItem(position);

//        finds dos elementos
        TextView lbl_empresa = v.findViewById(R.id.lbl_empresa);

//        setando os valores
        lbl_empresa.setText(chamado.getNomeEmpresa());

        return v;
    }
}
