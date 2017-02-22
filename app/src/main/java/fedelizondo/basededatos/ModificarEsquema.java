package fedelizondo.basededatos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import LogicaNegocio.Esquemas;

public class ModificarEsquema extends AppCompatActivity {

    public static final String ESQUEMA_A_MODIFICAR = "ESQUEMA_A_MODIFICAR";
    public static final String ATRIBUTOS ="ATRIBUTOS";
    public static final String ESQUEMA = "ESQUEMA";
    public static final String ESQUEMA_RESULTADO = "ESQUEMARESULTADO";
    public static final String INDEX = "INDEX";

    private ListView listView;
    private TextView tv_AgregarEsquema;
    private FloatingActionButton fab;
    private ArrayList<Integer> indexSeleccionado;

    private ArrayList<String> esquema;
    private ArrayList<String> atributos;
    private ArrayList<ArrayList<String>> lEsquemas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_esquema);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.tituloAgregarEsquema);
        setSupportActionBar(toolbar);


        esquema = new ArrayList<>();
        indexSeleccionado = new ArrayList<>();
        if(getIntent().hasExtra(ESQUEMA_A_MODIFICAR) )
            esquema = getIntent().getStringArrayListExtra(ESQUEMA_A_MODIFICAR);

        atributos = new ArrayList<>();
        if(getIntent().hasExtra(ATRIBUTOS))
            atributos = getIntent().getStringArrayListExtra(ATRIBUTOS);

        if(!atributos.isEmpty() && !esquema.isEmpty())
        {
            for (String elemento: esquema )
            {
                if(atributos.contains(elemento))
                    indexSeleccionado.add(atributos.indexOf(elemento));
            }
        }


        Esquemas aux = (Esquemas) getIntent().getSerializableExtra(ESQUEMA);
        lEsquemas = aux.getEsquemas();


        tv_AgregarEsquema = (TextView) findViewById(R.id.tv_ModificarEsquema);

        listView = (ListView) findViewById(R.id.lv_Esquema);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_checked,atributos){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView)view.findViewById(android.R.id.text1)).setTextColor(Color.BLACK);
                return view;
            }
        };
        listView.setAdapter(adapter);


        for(Integer index:indexSeleccionado)
        {
            listView.setItemChecked(index,true);
        }
        if(esquema.isEmpty())
            tv_AgregarEsquema.setText(R.string.ErrorEsquemasVacio);
        else
            tv_AgregarEsquema.setText(esquema.toString().replace('[','{').replace(']','}')+"*");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer integer = position;

                if(indexSeleccionado.contains(integer))
                {
                    esquema.remove(atributos.get(position));
                    indexSeleccionado.remove(integer);
                }
                else
                {
                    indexSeleccionado.add(integer);
                    esquema.add(atributos.get(position));
                }

                tv_AgregarEsquema.setText(esquema.toString().replace('[','{').replace(']','}')+"*");
                if(esquema.isEmpty())
                    tv_AgregarEsquema.setText(R.string.ErrorEsquemasVacio);
            }
        });


        fab = (FloatingActionButton) findViewById(R.id.fabModificarEsquema);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (esquema.isEmpty()) {
                    Snackbar.make(view, R.string.ErrorEsquemasVacio, Snackbar.LENGTH_LONG).show();
                } else {
                    if (lEsquemas.contains(esquema)) {
                        Snackbar.make(view, R.string.ErrorEsquemasRepetido, Snackbar.LENGTH_LONG).show();
                    } else {
                        Intent i = getIntent().putExtra(ESQUEMA_RESULTADO, esquema);
                        setResult(2, i);
                        finish();
                    }
                }
            }
        });

    }

}
