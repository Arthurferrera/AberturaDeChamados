package com.toth.aberturadechamados.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.widget.Toast;

import com.toth.aberturadechamados.BuildConfig;
import com.toth.aberturadechamados.R;
import com.toth.aberturadechamados.SobreActivity;
import com.toth.aberturadechamados.api.DeslogarApi;
import com.toth.aberturadechamados.fragments.EmpresasFragment;
import com.toth.aberturadechamados.model.SharedPreferencesConfig;

public class MainMenuAdmActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    declarando elementos visuais, variaveis...
    Toolbar toolbar;
    private SharedPreferencesConfig preferencesConfig;
    String API_URL;
    DrawerLayout drawer;
    NavigationView navigationView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_adm);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        setTextColorForMenuItem(navigationView.getMenu().getItem(0), R.color.amarelo);

//        instanciando o SharedPreferencesConfig
        preferencesConfig = new SharedPreferencesConfig(this);
//        pegando o caminho padrao da api
        API_URL = getString(R.string.api_key);

//        adicionando o primeiro fragment
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.frame_content_adm, new EmpresasFragment()).commit();
        }
    }

//    setando a ação do botão voltar do dispositivo
    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setTextColorForMenuItem(MenuItem menuItem, @ColorRes int color) {
        SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spanString.length(), 0);
        menuItem.setTitle(spanString);
//        mudando o icone do item selecionado
//        String item = String.valueOf("Pendentes");
//        SpannableString spanItem = new SpannableString(item.toString());
//        if (spanString.toString().equals("Pendentes")) {
//            menuItem.setIcon(R.drawable.ic_pendentes_check);
//        } else {
//            menuItem.setIcon(R.drawable.ic_resolvidos_check);
//        }
    }

    private void resetAllMenuItemsTextColor(NavigationView navigationView) {
        for (int i = 0; i < navigationView.getMenu().size(); i++)
            setTextColorForMenuItem(navigationView.getMenu().getItem(i), R.color.preto);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        resetAllMenuItemsTextColor(navigationView);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        acções para cada item do menu
        if (id == R.id.nav_pendentesAdm) {
            setTextColorForMenuItem(item, R.color.amarelo);
//            cria o FragmentManager
            FragmentManager fm = this.getSupportFragmentManager();
//            cria o fragment
            Fragment fragment = new EmpresasFragment();
//            chama outro fragment passando parametros
            Bundle bundle = new Bundle();
            bundle.putInt("status", 0);
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.frame_content_adm, fragment).commit();
            toolbar.setTitle("Empresas");
        } else if (id == R.id.nav_resolvidosAdm) {
            setTextColorForMenuItem(item, R.color.verde);
//            cria o FragmentManager
            FragmentManager fm = this.getSupportFragmentManager();
//            cria o fragment
            Fragment fragment = new EmpresasFragment();
//            chama outro fragment passando parametros
            Bundle bundle = new Bundle();
            bundle.putInt("status", 1);
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.frame_content_adm, fragment).commit();
            toolbar.setTitle("Empresas");
        } else if (id == R.id.nav_sobreAdm){
            startActivity(new Intent(this, SobreActivity.class));
        } else if (id == R.id.nav_sairAdm) {
            Integer idUser = Integer.valueOf(preferencesConfig.readUsuarioId());
            String nivel = preferencesConfig.readNivelusuario();
            String url = API_URL + "deslogar.php?id="+idUser+"&nivel="+nivel;
            new DeslogarApi(url,this).execute();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
