package com.example.lourdes.gestormensajes;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Lourdes on 23/01/2018.
 */

public class CategorizadosFragment extends android.support.v4.app.Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String categoria = getArguments().getString("categoria");
        //Toast.makeText(getActivity(),"categoria = "+categoria,Toast.LENGTH_LONG).show();
        //Para conectar con la BBDD
        BDDHelper miHelper = new BDDHelper(getActivity());


        //Comprobar si hay mensajes del tipo seleccionado

        SQLiteDatabase db3 = miHelper.getReadableDatabase();
        String RAW_QUERY3 = "SELECT name FROM sqlite_master WHERE type='table' AND name NOT IN('android_metadata','tokens','categorias')";
        Cursor cursor_check = db3.rawQuery(RAW_QUERY3, null);
        cursor_check.moveToFirst();
        ArrayList<String> checker = new ArrayList<String>();

        //comprobar si hay mensajes de esa categoría en alguna tabla
        for (int i = 0; i < cursor_check.getCount(); i++) {
            String check_tabla = cursor_check.getString(0);
            String CHECK_QUERY = "SELECT autor FROM '" + check_tabla + "' WHERE categoria ='"+categoria+"'";
            Cursor cursor_autor = db3.rawQuery(CHECK_QUERY, null);


            if (cursor_autor.getCount() != 0) {
                cursor_autor.moveToFirst();
                checker.add(cursor_autor.getString(0));
            }
            Log.d("size_array ", "" + checker.size());
            cursor_check.moveToNext();
        }


        if(checker.size()>0) {

            //Definir layout principal, , y definir parámetros del layout
            final LinearLayout layout_principal = (LinearLayout) view.findViewById(R.id.linearLayout);
            layout_principal.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.fondo));
            //Definir parámetros del layout principal
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            //Para leer datos de la BBDD
            SQLiteDatabase db = miHelper.getReadableDatabase();
            //Recoger los datos del intento que inició esta actividad
            // Bundle datos = getIntent().getExtras();
            //Recoger el nombre de la tabla que contiene los mensajes que se van a mostrar
            //final String nombre_tabla = datos.getString("nombre_tabla");


            //Definir la consulta

            String RAW_QUERY = "SELECT name FROM sqlite_master WHERE type='table' and name not in ('android_metadata','tokens','categorias')";
            //Ejecutar consulta
            Cursor cursor = db.rawQuery(RAW_QUERY, null);

            cursor.moveToFirst();


            //para cada tabla
            for (int i = 0; i < cursor.getCount(); i++) {
                //Coger los mensajes de la primera tabla
                final String nombre_tabla = cursor.getString(0);

                String RAW_QUERY_2 = "SELECT id,autor,fecha,titulo,mensaje,leido FROM '" + nombre_tabla + "' WHERE categoria = '"+categoria+"'";

                final Cursor cursor2 = db.rawQuery(RAW_QUERY_2, null);
                cursor2.moveToFirst();

                //Log.d("milog",""+cursor2.getString(6));

                //Creamos elementos dinámicamente, uno para cada mensaje que haya en la tabla correspondiente
                for (int j = 0; j < cursor2.getCount(); j++) {


                    // Crear LinearLayout layout_fila que albergará los elementos
                    LinearLayout layout_fila = new LinearLayout(getActivity());
                    //parámetros del layout fila
                    LinearLayout.LayoutParams param_layout_fila = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    param_layout_fila.height = 230;
                    layout_fila.setOrientation(LinearLayout.HORIZONTAL);


                    //CREAMOS EL BOTÓN
                    final Button boton = new Button(getActivity());
                    //le damos parámetros comunes a todos los botones, independientes del tipo de mensaje
                    LinearLayout.LayoutParams param_layout_boton = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    param_layout_boton.weight = 4;
                    param_layout_boton.width = convertDpToPixel(300, getActivity());
                    boton.setLayoutParams(param_layout_boton);
                    boton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    boton.setGravity(0);
                    boton.setPadding(20, 20, 0, 20);
                    boton.setTypeface(null, Typeface.ITALIC);
                    boton.setId(cursor2.getInt(0));

                    //lo configuramos dependiendo de leído o no
                    if (cursor2.getInt(5) == 0) {
                        //Si el mensaje no se ha leído aún, el texto se pone en negrita
                        boton.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextoTituloNoLeido));
                        boton.setTypeface(null, Typeface.BOLD_ITALIC);
                    }

                    //CREAMOS EL IMAGE VIEW
                    final ImageView imagen = new ImageView(getActivity());
                    //creamos parámetros comunes a cada image view imagen
                    LinearLayout.LayoutParams param_layout_imagen = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    param_layout_imagen.weight = 1;
                    imagen.setLayoutParams(param_layout_imagen);
                    imagen.setPadding(0, 0, 0, 0);

                    //CREAMOS EL BOTÓN PAPELERA
                    final ImageButton boton_borrar = new ImageButton(getActivity());
                    LinearLayout.LayoutParams param_layout_boton_borrar = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    param_layout_boton_borrar.weight = 1;
                    boton_borrar.setLayoutParams(param_layout_boton_borrar);
                    boton_borrar.setId(cursor2.getInt(0));


                    //configuramos parámetros dependientes del tipo de mensaje

                    String tipo_tabla = nombre_tabla.substring(0, 3);
                    String fecha = cursor2.getString(2);
                    fecha = fecha.substring(0, 10);


                    layout_fila.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.fondo_layout_fila_curso));

                    // String curso = nombre_tabla.substring(nombre_tabla.indexOf("-") + 1, nombre_tabla.indexOf("!"));
                    // curso = curso.replace("_", " ");
                    boton.setText(cursor2.getString(3)
                            + "\n- " + fecha
                            + "\n- " + "Categoría: " + categoria);
                    boton.setBackgroundColor(getResources().getColor(R.color.fondo_layout_fila_curso));
                    boton_borrar.setImageResource(R.mipmap.ic_borra_categorizados);
                    imagen.setImageResource(R.mipmap.ic_categorizados);


                    // Se pone el boton principal a la escucha de ser pulsado
                    boton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            String titulo = boton.getText().toString();
                            titulo = titulo.substring(0, titulo.indexOf("\n"));
                            //Crear intento para iniciar una nueva actividad
                            Intent intent = new Intent(getActivity(), MuestraMensaje.class);
                            //Añadir datos al intento para que los use la actividad que se va a iniciar
                            intent.putExtra("titulo", titulo);
                            intent.putExtra("nombre_tabla", "'" + nombre_tabla + "'");
                            intent.putExtra("id_mensaje", boton.getId());
                            intent.putExtra("fragmento", "categ");
                            intent.putExtra("categoria",categoria);
                            intent.putExtra("desde_categorizados",true);
                            //Comenzamos la nueva actividad
                            startActivity(intent);
                            //Finalizamos la actividad actual
                            getActivity().finish();

                        }
                    });


                    //se pone el boton borrar a la escucha de ser pulsado

                    boton_borrar.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            String titulo = boton.getText().toString();
                            titulo = titulo.substring(0, titulo.indexOf("\n"));
                            //Crear intento para iniciar una nueva actividad
                            Intent intent = new Intent(getActivity(), ConfirmarBorradoMensaje.class);
                            //Añadir datos al intento para que los use la actividad que se va a iniciar
                            intent.putExtra("titulo", titulo);
                            intent.putExtra("nombre_tabla", "'" + nombre_tabla + "'");
                            intent.putExtra("id_mensaje", boton_borrar.getId());
                            intent.putExtra("fragmento", "categ");
                            intent.putExtra("categoria",categoria);
                            //Comenzamos la nueva actividad
                            startActivity(intent);
                            //Finalizamos la actividad actual
                            getActivity().finish();

                        }
                    });


                    //Añadimos a los layout
                    //elementos al layout_fila
                    layout_fila.addView(boton);
                    layout_fila.addView(imagen);
                    layout_fila.addView(boton_borrar);

                    //y la fila al layout principal
                    layout_principal.addView(layout_fila);

                    cursor2.moveToNext();
                }//END FOR MENSAJE

                cursor.moveToNext();
            }//END FOR TABLA


            //Cerrar conexión con la BBDD
            db.close();

        }else{
            TextView no_mensajes = (TextView)getActivity().findViewById(R.id.texto_no_mensajes);
            no_mensajes.setText("No tiene mensajes en la categoria "+categoria);
            no_mensajes.setTextSize(20);
            no_mensajes.setPadding(20,20,0,0);
        }
    }

    /*@Override
    public void onDetach() {
        super.onDetach();


    }*/



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.activity_categorizados_fragment,null);
    }



    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        int pxx = Math.round(px);
        return pxx;
    }

}//end of class
