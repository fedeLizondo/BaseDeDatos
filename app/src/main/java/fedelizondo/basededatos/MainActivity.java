package fedelizondo.basededatos;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import Adapters.OnSwipeTouchListener;
import Adapters.StringAdapter;
import Interfaces.ListadoDeStringListener;
import LogicaNegocio.Administradora;
import layout.AgregarAtributosFragment;
import layout.AgregarAtributosFragment.OnFragmentInteractionListener;
import layout.ModificarAtributosFragment;

public class MainActivity extends AppCompatActivity implements AgregarAtributosFragment.OnFragmentInteractionListener,ModificarAtributosFragment.OnFragmentInteractionListener {


    private Toolbar toolbar;
    private ListView listViewAtributos;
    private Administradora administradora = Administradora.getInstance();

    //PARA EL ELIMINAR CON SWIPE
    private boolean mSwiping = false;
    private boolean mItemPressed = false;
    private static int SWIPE_DURATION = 250;
    private static int MOVE_DURATION = 150;
    HashMap<Long,Integer> mItemIdTopMap = new HashMap<Long,Integer>();

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
        //StringAdapter arrayAdapter = new StringAdapter(MainActivity.this,administradora.darListadoAtributos(),mTouchListener);

        listViewAtributos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                ModificarAtributosFragment dialog = new ModificarAtributosFragment();

                Bundle bundle = new Bundle();
                String atributoAModificar = administradora.darListadoAtributos().get(position);
                bundle.putString(ModificarAtributosFragment.ATRIBUTO_A_MODIFICAR,atributoAModificar);
                bundle.putStringArrayList(ModificarAtributosFragment.LISTADO_ATRIBUTOS,administradora.darListadoAtributos());
                dialog.setArguments(bundle);

                dialog.show(fragmentManager,"Fragment");
            }
        });

        listViewAtributos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DATA ID",id+"");

                String atributoABorrar = administradora.darListadoAtributos().get(position);
                administradora.eliminarAtributo(atributoABorrar);

                    listViewAtributos = (ListView) findViewById(R.id.listViewAtributos);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,administradora.darListadoAtributos());
                    //StringAdapter arrayAdapter = new StringAdapter(MainActivity.this,administradora.darListadoAtributos(),mTouchListener);
                    listViewAtributos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            ModificarAtributosFragment dialog = new ModificarAtributosFragment();

                            Bundle bundle = new Bundle();
                            String atributoAModificar = administradora.darListadoAtributos().get(position);
                            bundle.putString(ModificarAtributosFragment.ATRIBUTO_A_MODIFICAR,atributoAModificar);
                            bundle.putStringArrayList(ModificarAtributosFragment.LISTADO_ATRIBUTOS,administradora.darListadoAtributos());
                            dialog.setArguments(bundle);

                            dialog.show(fragmentManager,"Fragment");
                        }
                    });

                    listViewAtributos.setAdapter(arrayAdapter);



                return  false;
            }
        });

        listViewAtributos.setAdapter(arrayAdapter);
    }

    public void modificarAtributo(View view)
    {

    }


    @Override
    public void onFragmentInteraction(String AtributoAnterior, String AtributoModificado) {

        administradora.eliminarAtributo(AtributoAnterior);
        administradora.agregarAtributos(AtributoModificado);

        listViewAtributos = (ListView) findViewById(R.id.listViewAtributos);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,administradora.darListadoAtributos());
        //StringAdapter arrayAdapter = new StringAdapter(MainActivity.this,administradora.darListadoAtributos(),mTouchListener);
        listViewAtributos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                ModificarAtributosFragment dialog = new ModificarAtributosFragment();

                Bundle bundle = new Bundle();
                String atributoAModificar = administradora.darListadoAtributos().get(position);
                bundle.putString(ModificarAtributosFragment.ATRIBUTO_A_MODIFICAR,atributoAModificar);
                bundle.putStringArrayList(ModificarAtributosFragment.LISTADO_ATRIBUTOS,administradora.darListadoAtributos());
                dialog.setArguments(bundle);

                dialog.show(fragmentManager,"Fragment");
            }
        });

        listViewAtributos.setAdapter(arrayAdapter);
    }





}
