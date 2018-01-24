package com.example.lourdes.gestormensajes;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ParaFragmentos extends ListaMensajes {

    Bundle datos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para_fragmentos);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        Bundle datos = getIntent().getExtras();

        String fragmento = datos.getString("fragmento");

        Fragment fragment = null;

        if(fragmento.equals("curso")){
            fragment= new PorCursosFragment();

        }
        else if(fragmento.equals("hijos")){
            fragment= new PorHijosFragment();
        }
        else if(fragmento.equals("sin_leer")){
            fragment = new SinLeerFragment();
        }
        else if(fragmento.equals("leidos")){
            fragment = new TodosLeidosFragment();
        }
        else if(fragmento.equals("categ")){
            fragment = new CategorizadosFragment();
        }
        else if(fragmento.equals("gen")){
            fragment = new GeneralFragment();
        }

        if(fragment!=null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area, fragment);
            ft.commit();
        }

    }



}