package fedelizondo.basededatos;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;

import LogicaNegocio.Administradora;
import LogicaNegocio.DependenciaFuncional;
import layout.SeleccionarDeterminadoFragment;
import layout.SeleccionarDeterminatesFragment;

public class ModificarDependeciaFuncional extends AppCompatActivity  implements
        SeleccionarDeterminatesFragment.OnFragmentInteractionListener,
        SeleccionarDeterminadoFragment.OnFragmentInteractionListener{


    public static String INDEXAMODIFICAR = "INDEXAMODIFICAR";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private Administradora administradora ;
    private ArrayList<String> Determinante;
    private ArrayList<String> Determinado;
    private DependenciaFuncional dependenciaFuncional;
    private DependenciaFuncional dfINICIAL;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_dependecia_funcional);
        administradora = Administradora.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.tituloFragmentModificarAtributo);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


            if(getIntent().hasExtra(INDEXAMODIFICAR) ) {
                int index = this.getIntent().getIntExtra(INDEXAMODIFICAR,-1);
                dfINICIAL = administradora.darListadoDependenciasFuncional().get(index);
                dependenciaFuncional = dfINICIAL;
                Determinado = dfINICIAL.getDeterminado();
                Determinante = dfINICIAL.getDeterminante();
            }


    }

    @Override
    public void darDeterminado(ArrayList<String> determinado) {
        Determinado = determinado;
        String titulo = (Determinante.toString()+"->"+Determinado.toString()).replace('[',' ').replace(']',' ');
        if(Determinante.isEmpty() && Determinado.isEmpty())
            titulo = getResources().getString(R.string.tituloModificarDependenciaFuncional);
        toolbar.setTitle(titulo);
    }

    @Override
    public boolean estaRepetidaLaDependeciaFuncional() {

        if(administradora != null && !Determinante.isEmpty() && !Determinado.isEmpty() ) {
            ArrayList<DependenciaFuncional>ListadoDependenciaFuncional = administradora.darListadoDependenciasFuncional();
            dependenciaFuncional = administradora.crearDependenciaFuncional(Determinante,Determinado);
            boolean estaRepetido = administradora.darListadoDependenciasFuncional().contains(dependenciaFuncional);

            if(estaRepetido)
                dependenciaFuncional = null;
            return estaRepetido;
        }
        else
        {
            if(Determinante.isEmpty() || Determinado.isEmpty())
                return true;//TODO INFORMAR QUE LA DETERMINATE O DETERMINADO ES NULL
            else
                return false;
        }
    }

    @Override
    public void agregarDependenciaFuncional() {

        if(dependenciaFuncional!= null) {
            administradora.modificarDependenciaFuncional(dfINICIAL, dependenciaFuncional);
            finish();
        }
    }

    @Override
    public void darDeterminante(ArrayList<String> determinante) {
        Determinante = determinante;
        String titulo = (Determinante.toString()+"->"+Determinado.toString()).replace('[',' ').replace(']',' ');
        if(Determinante.isEmpty() && Determinado.isEmpty())
            titulo = getResources().getString(R.string.tituloModificarDependenciaFuncional);
        toolbar.setTitle(titulo);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                default:
                case 0:
                    return SeleccionarDeterminatesFragment.newInstance(administradora.darListadoAtributos(), dfINICIAL.getDeterminante());
                case 1:
                    return SeleccionarDeterminadoFragment.newInstance(administradora.darListadoAtributos(), dfINICIAL.getDeterminado());
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
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
