package layout;

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
import LogicaNegocio.CalculoEficiente;
import LogicaNegocio.ComponenteEsquemaDF;
import LogicaNegocio.DependenciaFuncional;
import LogicaNegocio.FormaNormal;
import fedelizondo.basededatos.AdapterCardView;
import fedelizondo.basededatos.DataAdapter;
import fedelizondo.basededatos.MainActivity;
import fedelizondo.basededatos.PasosCardView;
import fedelizondo.basededatos.R;


public class CalculoEficienteFragment extends Fragment {

    private Administradora administradora;

    private RecyclerView recyclerViewCalculoEficiente;

    private AdapterCardView adapter;

    private View view;

    public CalculoEficienteFragment() {
        // Required empty public constructor
    }

    public static CalculoEficienteFragment newInstance() {
        CalculoEficienteFragment fragment = new CalculoEficienteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() instanceof MainActivity) {
            administradora = ((MainActivity)getContext()).administradora;
        }
        else
            administradora = Administradora.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_calculo_eficiente, container, false);
        recyclerViewCalculoEficiente = (RecyclerView) view.findViewById(R.id.rv_CalculoEficiente);
        recyclerViewCalculoEficiente.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity().getApplicationContext());
        recyclerViewCalculoEficiente.setLayoutManager(layoutManager);
        update();
        return view;
    }



    public void update()
    {
        if( recyclerViewCalculoEficiente != null && administradora != null)
        {
            FormaNormal fn = administradora.calcularFormaNormal();
            ArrayList<PasosCardView> pasos = new ArrayList<>();
            String resultado="";
            String descripcion="";
            if(fn.soyFNBC() || fn.soyTerceraFN() || administradora.darListadoAtributos().isEmpty())
            {

                if( administradora.darListadoAtributos().isEmpty())
                {
                    descripcion = getContext().getString(R.string.ErrorNoHayAtributos);
                    pasos.add(new PasosCardView("",descripcion,""));
                }
                else {
                        if (fn.soyFNBC())
                            descripcion = getContext().getString(R.string.EsquemasEnFNBC);
                        else
                            descripcion = getContext().getString(R.string.EsquemasEn3FN);


                        ComponenteEsquemaDF cdf = new ComponenteEsquemaDF(administradora.darListadoAtributos(), administradora.darListadoDependenciasFuncional());
                        resultado = cdf.toString();
                        resultado = resultado.substring(0, resultado.length() );
                        pasos.add(new PasosCardView("", descripcion, resultado));
                    }
            }
            else
            {

                CalculoEficiente ce = administradora.calculoEficiente3FN();
                resultado = "";
                //resultado = ce.getPaso1().toString();
                for (ComponenteEsquemaDF compEsq : ce.getPaso1()) {
                    resultado += compEsq.toString().substring(0, compEsq.toString().length());
                    resultado += "\n";
                }
                descripcion = getContext().getString(R.string.CalculoEficientePrimerPaso);
                pasos.add(new PasosCardView("1", descripcion, resultado));

                resultado = "";
                descripcion = getContext().getString(R.string.CalculoEficienteSegundoPaso);
                for (ComponenteEsquemaDF compEsq : ce.getPaso2()) {
                    resultado += compEsq.toString().substring(0, compEsq.toString().length());
                    resultado += "\n";
                }
                pasos.add(new PasosCardView("2", descripcion, resultado));

                resultado = "";
                descripcion = getContext().getString(R.string.CalculoEficienteTercerPaso);
                for (ComponenteEsquemaDF compEsq : ce.getPaso3()) {
                    resultado += compEsq.toString().substring(0, compEsq.toString().length());
                    resultado += "\n";
                }
                pasos.add(new PasosCardView("3", descripcion, resultado));

                resultado = "";
                descripcion = getContext().getString(R.string.CalculoEficienteCuartoPaso);
                for (ComponenteEsquemaDF compEsq : ce.getPaso4()) {
                    resultado += compEsq.toString().substring(0, compEsq.toString().length());
                    resultado += "\n";
                }
                pasos.add(new PasosCardView("4", descripcion, resultado));
            }

            adapter = new AdapterCardView(pasos);
            recyclerViewCalculoEficiente.setAdapter(adapter);
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
