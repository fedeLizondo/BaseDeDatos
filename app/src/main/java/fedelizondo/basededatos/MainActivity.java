package fedelizondo.basededatos;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import Interfaces.ListadoDeStringListener;
import LogicaNegocio.Administradora;
import layout.AgregarAtributosFragment;
import layout.AgregarAtributosFragment.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {


    private Toolbar toolbar;
    private ListView listViewAtributos;
    private Administradora administradora = Administradora.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegador);//content_navegador);//activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

    }



    public void agregarAtributos(View view)
    {
        Toast.makeText(MainActivity.this.getBaseContext(), "This is my Toast message!",
                Toast.LENGTH_LONG).show();
        //Dialog dialog = new Dialog(MainActivity.this);
        //dialog.setTitle(R.string.tituloFragmentAgregarAtributo);
        //dialog.setContentView(R.layout.fragment_agregar_atributos);

        FragmentManager fragmentManager = getSupportFragmentManager();
        AgregarAtributosFragment dialog = new AgregarAtributosFragment();

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(AgregarAtributosFragment.ARG_PARAM1,administradora.darListadoAtributos());
        dialog.setArguments(bundle);

        dialog.show(fragmentManager,"Fragment");
    }

    @Override
    public void onFragmentInteraction(ArrayList<String> listadoAtributo) {
        for (String string:listadoAtributo) {
            administradora.agregarAtributos(string);

        }
        listViewAtributos = (ListView) findViewById(R.id.listViewAtributos);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,administradora.darListadoAtributos());
        listViewAtributos.setAdapter(arrayAdapter);


    }

    public void modificarAtributo(View view)
    {

    }

}
