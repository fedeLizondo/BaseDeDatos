package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import LogicaNegocio.Administradora;
import LogicaNegocio.FormaNormal;
import fedelizondo.basededatos.AdapterCardView;
import fedelizondo.basededatos.MainActivity;
import fedelizondo.basededatos.PasosCardView;
import fedelizondo.basededatos.R;


public class FormaNormalFragment extends Fragment {


    private Administradora administradora;
    private TextView contenido;

    private RecyclerView recyclerViewCalculoSinPerdida;
    private AdapterCardView adapter;

    public FormaNormalFragment() {
        // Required empty public constructor
    }

    public static FormaNormalFragment newInstance() {
        FormaNormalFragment fragment = new FormaNormalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        if(getContext() instanceof MainActivity)
            administradora = ((MainActivity)getContext()).administradora;
        else
            administradora = Administradora.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forma_normal, container, false);
        //contenido = (TextView) view.findViewById(R.id.tv_cuerpoFNormal);
        recyclerViewCalculoSinPerdida = (RecyclerView) view.findViewById(R.id.rv_Fnormal);
        recyclerViewCalculoSinPerdida.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity().getApplicationContext());
        recyclerViewCalculoSinPerdida.setLayoutManager(layoutManager);
        update();
        return view;
    }

    @Override
    public void onResume() {
        update();
        super.onResume();
    }

    public void update()
    {
        if(administradora == null)
               administradora = Administradora.getInstance();

        FormaNormal fn = administradora.calcularFormaNormal();

        /*if(contenido != null)
        {
            if(fn != null)
                contenido.setText(fn.JustificaMiFN().toString());

            if(administradora.darListadoAtributos().size() == 0)
                contenido.setText(getResources().getString(R.string.ErrorNoHayAtributos) );
        }*/

        if( recyclerViewCalculoSinPerdida != null && administradora != null)
        {
            ArrayList<PasosCardView> pasos = new ArrayList<>();
            String resultado="";
            String descripcion="";

            if( administradora.darListadoAtributos().isEmpty())
            {
                    descripcion = getContext().getString(R.string.ErrorNoHayAtributos);
                    pasos.add(new PasosCardView("",descripcion,""));
            }
            else
            {
                String claveCandidata = administradora.darClaveCandidataSeleccionada().toString();
                claveCandidata = claveCandidata.substring(1,claveCandidata.length()-1);
                String determinante ="";
                String determinado = "";

                if(!administradora.darListadoDependenciasFuncional().isEmpty()) {
                    determinante = fn.obtenerDependenciaFuncional().getDeterminante().toString();
                    determinante = determinante.substring(1,determinante.length()-1);
                    determinado = fn.obtenerDependenciaFuncional().getDeterminado().toString();
                    determinado = determinado.substring(1,determinado.length()-1);
                }

                pasos.add(new PasosCardView("",fn.JustificaMiFN(),""));

               if(fn.soyFNBC())
                {
                    if(administradora.darListadoDependenciasFuncional().isEmpty())
                        resultado = String.format(getContext().getString(R.string.JustificacionFormaNormalBoyceCoddSinDependencias),claveCandidata);
                    else
                        resultado = String.format(getContext().getString(R.string.JustificacionFormaNormalBoyceCodd),determinante,claveCandidata);

                    descripcion = getContext().getString(R.string.tituloJustificacionFormaNormalBoyceCodd);

                    pasos.add(new PasosCardView("",descripcion,resultado));
                }
                else
                {
                    resultado = String.format(getContext().getString(R.string.JustificacionPorQueNoFormaNormalBoyceCodd),determinante,claveCandidata);
                    descripcion = getContext().getString(R.string.tituloJustificacionNoFormaNormalBoyceCodd);
                    pasos.add(new PasosCardView("1",descripcion,resultado));

                    if(fn.soyTerceraFN())
                    {
                        resultado=String.format(getContext().getString(R.string.JustificacionTerceraFormaNormal),determinado,claveCandidata);
                        descripcion= getContext().getString(R.string.tituloJustificacionTerceraFormaNormal) ;
                        pasos.add(new PasosCardView("2",descripcion,resultado));
                    }
                    else
                    {
                        resultado = String.format(getContext().getString(R.string.JustificacionPorQueNoTerceraFormaNormal),determinado,claveCandidata);
                        descripcion = getContext().getString(R.string.tituloJustificacionNoTerceraFormaNormal);
                        pasos.add(new PasosCardView("2",descripcion,resultado));

                        if( fn.soySegundaFN())
                        {
                            resultado = String.format(getContext().getString(R.string.JustificacionSegundaFormaNormal),determinado,claveCandidata);
                            descripcion = getContext().getString(R.string.tituloJustificacionSegundaFormaNormal);
                            pasos.add(new PasosCardView("3",descripcion,resultado));
                        }
                        else
                        {
                            resultado = String.format(getContext().getString(R.string.JustificacionPorQueNoSegundaFormaNormal),determinante,claveCandidata);
                            descripcion = getContext().getString(R.string.tituloJustificacionNoSegundaFormaNormal);
                            pasos.add(new PasosCardView("3",descripcion,resultado));

                            resultado = getContext().getString(R.string.JustificacionPrimeraFormaNormal);
                            descripcion = getContext().getString(R.string.tituloJustificacionPrimeraFormaNormal);
                            pasos.add(new PasosCardView("4",descripcion,resultado));
                        }
                    }
                }
            }

            adapter = new AdapterCardView(pasos);
            recyclerViewCalculoSinPerdida.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser)
            update();
        super.setUserVisibleHint(isVisibleToUser);
    }
}
