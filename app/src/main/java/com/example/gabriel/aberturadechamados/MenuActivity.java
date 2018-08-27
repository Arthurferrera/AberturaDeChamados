package com.example.gabriel.aberturadechamados;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    ListView list_view_chamados;
    ChamadoAdapter adapter;
    String nomeUsuario;
    int idUsuario;
    private SharedPreferencesConfig preferencesConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());

//        finds dos elementos
        FloatingActionButton fab = findViewById(R.id.fab);
        list_view_chamados = findViewById(R.id.list_view_chamados);

        adapter = new ChamadoAdapter(this);
        list_view_chamados.setAdapter(adapter);
        list_view_chamados.setOnItemClickListener(this);

//        ação do botão float
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencao = new Intent(getApplicationContext(), AbrirChamadoActivity.class);
                startActivity(intencao);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        idUsuario = intent.getIntExtra("idUsuario", 0);
        nomeUsuario = intent.getStringExtra("nomeUsuario");

        nomeUsuario = preferencesConfig.readUsuarioNome();
        idUsuario = Integer.parseInt(preferencesConfig.readUsuarioId());

        Toast.makeText(this, "Bem vindo, "+nomeUsuario+" "+idUsuario, Toast.LENGTH_SHORT).show();

        adapter.clear();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {

                String json = "";

                final String url = "http://192.168.2.121/APIChamados/selecionar.php?idUsuario="+idUsuario;
                json = HttpConnection.get(url);

                return json;
            }

            @Override
            protected void onPostExecute(String json) {
                super.onPostExecute(json);

                if (json == null) json = "Sem Dados";
                Log.d("onPostExecute", json);

                ArrayList<Chamado> lstChamados = new ArrayList<>();
                if (json != null) {
                    try {
                        JSONArray arrayChamados = new JSONArray(json);
                        for (int i = 0; i < arrayChamados.length(); i++) {
                            JSONObject chamadoJson = arrayChamados.getJSONObject(i);

                            Chamado ch = new Chamado();
                            ch.setId(chamadoJson.getInt("id"));
                            ch.setTitulo(chamadoJson.getString("titulo"));
                            ch.setMensagem(chamadoJson.getString("mensagem"));
                            ch.setData(chamadoJson.getString("data"));

                            lstChamados.add(ch);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    adapter.addAll(lstChamados);
                }
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(this, "Camera", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, "Galeria", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, "SlideShow", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "Manager", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Sair();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
        Chamado item = adapter.getItem(i);
        Intent intencao = new Intent(this, VisualizarChamadoActivity.class);
        intencao.putExtra("idChamado", item.getId());
        startActivity(intencao);
    }

    public void Sair() {
        preferencesConfig.writeLoginStatus(false);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
