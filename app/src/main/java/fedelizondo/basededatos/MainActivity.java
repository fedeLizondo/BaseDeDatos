package fedelizondo.basededatos;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import layout.AgregarAtributosFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
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
        DialogFragment dialog = new AgregarAtributosFragment();


        //dialog.setTitle(R.string.tituloFragmentAgregarAtributo);
        //dialog.setContentView(R.layout.fragment_agregar_atributos);
        //dialog.show();

    }

}
