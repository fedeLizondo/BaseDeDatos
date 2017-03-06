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
import LogicaNegocio.Esquemas;
import layout.PasosTableauxFragment;
import layout.ResultadoTableauxFragment;

public class CalculoTableaux extends AppCompatActivity {

    public static final String ESQUEMAS = "ESQUEMAS";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private Administradora administradora;


    private int cantidadTotalPasos;
    private Esquemas esquemas;

    public ArrayList<String[][]> tableaux;



    private int filas ;
    private int columnas;

    public int getCantidadTotalPasos() {
        return cantidadTotalPasos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_tableaux);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.tituloTableaux);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (getIntent().hasExtra(ESQUEMAS)) {
            esquemas = (Esquemas) getIntent().getSerializableExtra(ESQUEMAS);
        } else {
            esquemas = new Esquemas(new ArrayList<ArrayList<String>>());
        }
        administradora  = Administradora.getInstance();

        tableaux = administradora.calcularTableaux(esquemas.getEsquemas());
        filas = esquemas.getEsquemas().size();
        columnas = administradora.darListadoAtributos().size();
        cantidadTotalPasos = tableaux.size();

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
                case 0: return ResultadoTableauxFragment.newInstance();
                case 1: return PasosTableauxFragment.newInstance();
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
                    return  getResources().getString(R.string.tituloTableauxResultado);
                case 1:
                    return getResources().getString(R.string.tituloTableauxPasos);
            }
            return null;
        }
    }


    public String[][] ConversorATextoMatrixCompleta(int indexPaso)
    {

        if(esquemas != null && filas > 0 && columnas >0)
        {
            String[][] pasoTableaux = tableaux.get(indexPaso);
            String[][] resultado = new String[filas+1][columnas+1];
            resultado[0][0] = "  ";
            //RELLENO LA PRIMERA FILA CON LOS TITULOS
            ArrayList<String> atributos = administradora.darListadoAtributos();
            for(int i = 0;i < (columnas);i++)
            {
                resultado[0][i+1] = atributos.get(i);
            }
            //RELLENO LA PRIMERA COLUMNA CON LOS ESQUEMAS
            ArrayList<ArrayList<String>> esq = esquemas.getEsquemas();
            for(int i = 0;i < filas ;i++)
            {
                resultado[i+1][0] = esq.get(i).toString().replace('[',' ').replace(']',' ');
            }

            for( int i = 0 ; i < filas ; i++ )
            {
                for(int j= 0; j < columnas ; j++)
                {
                       resultado [i+1][j+1] = pasoTableaux[i][j] ;
                }
            }
            return resultado;
        }
        else
            return null;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }
    public Esquemas getEsquemas() {
        return esquemas;
    }
}