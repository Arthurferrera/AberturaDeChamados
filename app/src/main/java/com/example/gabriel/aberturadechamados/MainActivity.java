package com.example.gabriel.aberturadechamados;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list_view_chamados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        finds dos elementos
        fab = findViewById(R.id.fab);
        list_view_chamados = findViewById(R.id.list_view_chamados);

//        ação do botão float
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencao = new Intent(getApplicationContext(), AbrirChamadoActivity.class);
                startActivity(intencao);
            }
        });
    }

    public void VisualizarChamado(View view) {
        Intent intencao = new Intent(getApplicationContext(), VisualizarChamadoActivity.class);
        startActivity(intencao);
    }
}
