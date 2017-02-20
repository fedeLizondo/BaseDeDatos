package fedelizondo.basededatos;

import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import LogicaNegocio.Administradora;
import LogicaNegocio.DFSimple;
import LogicaNegocio.DependenciaFuncional;
import layout.AgregarAtributosFragment;
import layout.AtributosFragment;
import layout.ClausuraFragment;
import layout.ClaveCandidataFragment;
import layout.DependenciaFuncionalFragment;
import layout.FMinimoFragment;
import layout.FormaNormalFragment;
import layout.TableauxFragment;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        AtributosFragment.OnFragmentInteractionListener,
        DependenciaFuncionalFragment.OnFragmentInteractionListener
{


    private Toolbar toolbar;
    public Administradora administradora = Administradora.getInstance();

    private static final short ATRIBUTO = 0;
    private static final short DEPENDENCIA_FUNCIONAL = 1;
    private static final int CLAUSURA = 2;
    private static final short CLAVE = 3;
    private static final short FMIN = 4;
    private static final short FORMA_NORMAL = 5;
    private static final short TABLEAUX = 6;
    private static final short CALCULO_EFICIENTE = 7;
    private static final short CALCULO_SIN_PERDIDA = 8;
    private static final short HEAT = 9;

    private static final short TOTAL_FRAGMENTS = 8;


    private short posicionActual = 0;


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private NavigationView navigationView;

    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegador);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        int index = 1;


        switch (id) {
            case R.id.nav_atributo:
                index = ATRIBUTO;
                break;
            case R.id.nav_dependencia:
                index = DEPENDENCIA_FUNCIONAL;
                break;
            case R.id.nav_clausura:
                index = CLAUSURA;
                break;
            case R.id.nav_claveCandidata:
                index = CLAVE;
                break;
            case R.id.nav_fmin:
                index = FMIN;
                break;
            case R.id.nav_formaNormal:
                index = FORMA_NORMAL;
                break;
            case R.id.nav_tableaux:
                index = TABLEAUX;
                break;
            case R.id.nav_calculoEficiente:
                index = CALCULO_EFICIENTE;
                break;
            case R.id.nav_calculoSinPerdida:
                index = CALCULO_SIN_PERDIDA;
                break;
            default: index = posicionActual;
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if(index>=0)
        {
            mViewPager.setCurrentItem(0);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                      Fragment Pager Adapter                                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            boolean hayCambios = true;
            Fragment fragment;
            switch (position) {
                case ATRIBUTO:
                        return AtributosFragment.newInstance(administradora.darListadoAtributos());

                case DEPENDENCIA_FUNCIONAL:
                        return DependenciaFuncionalFragment.newInstance();

                case CLAUSURA:
                       return ClausuraFragment.newInstance(administradora.darListadoAtributos());

                case CLAVE:
                    fragment = ClaveCandidataFragment.newInstance();
                    break;

                case FMIN:
                    fragment = FMinimoFragment.newInstance();
                    break;
                case FORMA_NORMAL:
                    fragment = FormaNormalFragment.newInstance();
                    break;
                /*
                case TABLEAUX:
                    fragment = TableauxFragment.newInstance();
                    break;
                case CALCULO_EFICIENTE:
                    fragment = CalculoEficienteFragment();
                    break;
                case CALCULO_SIN_PERDIDA
                    fragment = CalculoSinPerdidaFragment();
                    break;
                case HEAT:*/
                default:
                    hayCambios = false;
                    //fragment = this.getItem(posicionActual);
                    return AtributosFragment.newInstance(administradora.darListadoAtributos());
                    //break;
            }

            /*if(hayCambios)
                posicionActual = (short) position;*/

            return fragment;
        }

        @Override
        public int getCount() {
           /* int cantidad = administradora.estadoCambiosAdministradora();

            if( cantidad == 0 )
                cantidad = TOTAL_FRAGMENTS+1;

            for(int i = 0; i<cantidad;i++) {
                navigationView.getMenu().getItem(i).setEnabled(true);
            }

            for (int i = cantidad;i<TOTAL_FRAGMENTS;i++)
            {
                navigationView.getMenu().getItem(i).setEnabled(false);
            }*/

            return 6;//cantidad;

        }
    }

    @Override
    public void onFragmentInteractionAgregarAtributos(ArrayList<String> listadoAtributo) {
        for(String string:listadoAtributo)
        {
            administradora.agregarAtributos(string);
        }
    }

    @Override
    public void onFragmentInteractionModificarAtributos(String AtributoAnterior, String AtributoNuevo) {
        administradora.modificarAtributo(AtributoAnterior,AtributoNuevo);
    }

    @Override
    public void onFragmentInteractionEliminarAtributos(String atributoAEliminar) {
        administradora.eliminarAtributo(atributoAEliminar);
    }

    @Override
    public void onFragmentInteractionDFAgregar(DependenciaFuncional dfAgregada) {
        administradora.agregarDependenciaFuncional(dfAgregada);
    }

    @Override
    public void onFragmentInteractionDFModificar(DependenciaFuncional dfVieja, DependenciaFuncional dfNueva ) {
        administradora.modificarDependenciaFuncional(dfVieja,dfNueva);
    }

    @Override
    public void onFragmentInteractionDFEliminar(DependenciaFuncional dfEliminada) {
        administradora.eliminarDependenciaFuncional(dfEliminada);
    }



}
