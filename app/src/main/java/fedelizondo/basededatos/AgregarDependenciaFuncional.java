package fedelizondo.basededatos;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import java.util.ArrayList;

import LogicaNegocio.Administradora;
import LogicaNegocio.DFCompleja;
import LogicaNegocio.DFDeterminadoComplejo;
import LogicaNegocio.DFDeterminanteComplejo;
import LogicaNegocio.DFSimple;
import LogicaNegocio.DependenciaFuncional;
import layout.DependenciaFuncionalFragment;
import layout.SeleccionarDeterminadoFragment;
import layout.SeleccionarDeterminatesFragment;

public class AgregarDependenciaFuncional extends AppCompatActivity implements
        SeleccionarDeterminatesFragment.OnFragmentInteractionListener,
        SeleccionarDeterminadoFragment.OnFragmentInteractionListener{

    public static String AGREGARDEPENDECIAFUNCIONAL = "AGREGARDF";
    public static String LISTAATRIBUTOS = "LISTADOATRIBUTOSDF";

    private ArrayList<String> Determinante;
    private ArrayList<String> Determinado;
    private ArrayList<String> Atributos;
    private Administradora administradora ;
    private ArrayList<DependenciaFuncional> ListadoDependenciaFuncional;
    private DependenciaFuncional dependenciaFuncional;
    private Toolbar toolbar;


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_dependencia_funcional);
        ListadoDependenciaFuncional = new ArrayList<>();
        Determinante = new ArrayList<>();
        Determinado = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.tituloDependenciaFuncional);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        if(getIntent().hasExtra(LISTAATRIBUTOS) )
            Atributos = this.getIntent().getStringArrayListExtra(LISTAATRIBUTOS);

        if(Atributos == null)
            Atributos = new ArrayList<>();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        administradora = Administradora.getInstance();
    }

    @Override
    public void darDeterminante(ArrayList<String> determinante) {
        Determinante = determinante;
        String titulo = (Determinante.toString()+"->"+Determinado.toString()).replace('[',' ').replace(']',' ');
        if(Determinante.isEmpty() && Determinado.isEmpty())
            titulo = getResources().getString(R.string.tituloAgregarDependenciaFuncional);
        toolbar.setTitle(titulo);



    }

    @Override
    public void darDeterminado(ArrayList<String> determinado) {
        Determinado = determinado;
        String titulo = (Determinante.toString()+"->"+Determinado.toString()).replace('[',' ').replace(']',' ');
        if(Determinante.isEmpty() && Determinado.isEmpty())
            titulo = getResources().getString(R.string.tituloAgregarDependenciaFuncional);
        toolbar.setTitle(titulo);
    }

    @Override
    public boolean estaRepetidaLaDependeciaFuncional() {


        if(administradora != null) {
            ListadoDependenciaFuncional = administradora.darListadoDependenciasFuncional();
            dependenciaFuncional = administradora.crearDependenciaFuncional(Determinante,Determinado);
            boolean estaRepetido = administradora.darListadoDependenciasFuncional().contains(dependenciaFuncional);

            if(estaRepetido)
                dependenciaFuncional = null;

            return estaRepetido;

        }
        else
            return false;
    }

    @Override
    public void agregarDependenciaFuncional() {

        if(dependenciaFuncional != null && administradora != null) {
            administradora.agregarDependenciaFuncional(dependenciaFuncional);
            finish();
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return SeleccionarDeterminatesFragment.newInstance(Atributos);
                case 1:
                    return SeleccionarDeterminadoFragment.newInstance(Atributos);
                default:
                    return SeleccionarDeterminatesFragment.newInstance(Atributos);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.tituloDeterminante);
                case 1:
                    return getResources().getString(R.string.tituloDeterminado);
            }
            return null;
        }
    }

}
