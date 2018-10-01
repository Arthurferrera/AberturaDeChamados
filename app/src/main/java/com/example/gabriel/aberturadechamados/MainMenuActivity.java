package com.example.gabriel.aberturadechamados;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.gabriel.aberturadechamados.api.DeslogarApi;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    private SharedPreferencesConfig preferencesConfig;
    String API_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        preferencesConfig = new SharedPreferencesConfig(this);
        API_URL = getString(R.string.api_key);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.frame_content, new PendentesFragment()).commit();
        }
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
        getMenuInflater().inflate(R.menu.main_menu, menu);
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

        if (id == R.id.nav_pendentes) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new PendentesFragment()).commit();
            toolbar.setTitle(getString(R.string.activity_pendentes));
        } else if (id == R.id.nav_resolvidos) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new ResolvidosFragment()).commit();
            toolbar.setTitle(getString(R.string.activity_resolvidos));
        } else if (id == R.id.nav_abrirChamado) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new AbrirChamadoFragment()).commit();
        } else if (id == R.id.nav_sair) {
            Integer idUser = Integer.valueOf(preferencesConfig.readUsuarioId());
            String nivel = preferencesConfig.readNivelusuario();
            String url = API_URL + "deslogar.php?id="+idUser+"&nivel="+nivel;
            new DeslogarApi(url, this);
//            TODO: continuar daqui, atualizar o status para deslogar e deslogar
        }

//        setSupportActionBar();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
