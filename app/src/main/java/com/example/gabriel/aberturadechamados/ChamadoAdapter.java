package com.example.gabriel.aberturadechamados;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

//        pegando a data e hora atual
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int hora = c.get(Calendar.HOUR);
        int minutos = c.get(Calendar.MINUTE);
//        String dataAtual = String.format("%02d/%02d/%d ás %02d:%02d", dia, mes+1, ano, hora, minutos);

        // Pegando o contato daquela posição
        Chamado chamado = getItem(position);

        // Acessando os elementos visuais
        TextView lbl_titulo_chamado = v.findViewById(R.id.lbl_titulo_chamado);
        TextView lbl_mensagem_chamado = v.findViewById(R.id.lbl_mensagem_chamado);
        TextView lbl_data_chamado = v.findViewById(R.id.lbl_data_chamado);

//        verificar se falta algo no adapter
        lbl_titulo_chamado.setText(chamado.getTitulo());
        lbl_mensagem_chamado.setText(chamado.getMensagem());
        lbl_data_chamado.setText(dia+"/"+mes+"/"+ano+" ás "+hora+":"+minutos);

        return v;
    }
}
