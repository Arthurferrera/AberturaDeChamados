package com.example.gabriel.aberturadechamados;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list_view_chamados;
    ChamadoAdapter adaptador;
    String titulo, mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        titulo = intent.getStringExtra("usuario");
        mensagem = intent.getStringExtra("senha");

//        finds dos elementos
        fab = findViewById(R.id.fab);
        list_view_chamados = findViewById(R.id.list_view_chamados);

        list_view_chamados.setOnItemClickListener(this);

        ArrayList<Chamado> listaChamados = new ArrayList<>();
        listaChamados.add(new Chamado("PC", "PC quebrado"));
        listaChamados.add(new Chamado("PC", "PC quebrado"));
        listaChamados.add(new Chamado("PC", "PC quebrado"));
        listaChamados.add(new Chamado("PC", "PC quebrado"));
        listaChamados.add(new Chamado("PC", "PC quebrado"));
        listaChamados.add(new Chamado("PC", "PC quebrado"));

        adaptador = new ChamadoAdapter(this, listaChamados);
        list_view_chamados.setAdapter(adaptador);

//        ação do botão float
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencao = new Intent(getApplicationContext(), AbrirChamadoActivity.class);
                startActivity(intencao);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Chamado item = adaptador.getItem(i);
        Intent intencao = new Intent(this, VisualizarChamadoActivity.class);
        intencao.putExtra("titulo", item.getTitulo());
        intencao.putExtra("mensagem", item.getMensagem());
        startActivity(intencao);
    }
}
