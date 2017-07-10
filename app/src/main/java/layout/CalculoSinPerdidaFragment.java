package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import LogicaNegocio.Administradora;
import LogicaNegocio.CalculoEficiente;
import LogicaNegocio.CalculoSinPerdidaDeInfo;
import LogicaNegocio.ComponenteEsquemaDF;
import LogicaNegocio.DependenciaFuncional;
import LogicaNegocio.FormaNormal;
import fedelizondo.basededatos.AdapterCardView;
import fedelizondo.basededatos.MainActivity;
import fedelizondo.basededatos.PasosCardView;
import fedelizondo.basededatos.R;


public class CalculoSinPerdidaFragment extends Fragment {

    private Administradora administradora;
    private ArrayList<ArrayList<DependenciaFuncional>> lSubEsquemas;
    private View view;

    private RecyclerView recyclerViewCalculoSinPerdida;
    private AdapterCardView adapter;

    public CalculoSinPerdidaFragment() {
        // Required empty public constructor
    }

    public static CalculoSinPerdidaFragment newInstance() {
        CalculoSinPerdidaFragment fragment = new CalculoSinPerdidaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        view = inflater.inflate(R.layout.fragment_calculo_sin_perdida, container, false);
//        initView(view);

        recyclerViewCalculoSinPerdida = (RecyclerView) view.findViewById(R.id.rv_CalculoSinPerdida);
        recyclerViewCalculoSinPerdida.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity().getApplicationContext());
        recyclerViewCalculoSinPerdida.setLayoutManager(layoutManager);
        update();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    public void initView(View view)
    {

    }

    public void update()
    {
        if( recyclerViewCalculoSinPerdida != null && administradora != null)
        {
            FormaNormal fn = administradora.calcularFormaNormal();
            ArrayList<PasosCardView> pasos = new ArrayList<>();
            String resultado="";
            String descripcion="";
            if(fn.soyFNBC()  || administradora.darListadoAtributos().isEmpty())
            {

                if( administradora.darListadoAtributos().isEmpty())
                {
                    descripcion = getContext().getString(R.string.ErrorNoHayAtributos);
                    pasos.add(new PasosCardView("",descripcion,""));
                }
                else {

                    descripcion = getContext().getString(R.string.EsquemasEnFNBC);
                    ComponenteEsquemaDF cdf = new ComponenteEsquemaDF(administradora.darListadoAtributos(), administradora.darListadoDependenciasFuncional());
                    resultado = cdf.toString();
                    resultado = resultado.substring(0, resultado.length() );
                    pasos.add(new PasosCardView("", descripcion, resultado));
                }
            }
            else
            {

                CalculoSinPerdidaDeInfo ce = administradora.calculoSinPerdidaDeInfoFNBC();

                resultado = "";
                for (ComponenteEsquemaDF compEsq : ce.getPaso1()) {
                    resultado += compEsq.toString().substring(0, compEsq.toString().length());
                    resultado += "\n";
                }
                descripcion = getContext().getString(R.string.CalculoSinPerdidaPrimerPaso);
                pasos.add(new PasosCardView("1", descripcion, resultado));

                resultado = "";
                descripcion = getContext().getString(R.string.CalculoSinPerdidaSegundoPaso);
                for (ComponenteEsquemaDF compEsq : ce.getPaso2()) {
                    resultado += compEsq.toString().substring(0, compEsq.toString().length());
                    resultado += "\n";
                }
                pasos.add(new PasosCardView("2", descripcion, resultado));

                resultado = "";
                descripcion = getContext().getString(R.string.CalculoSinPerdidaTercerPaso);
                for (ComponenteEsquemaDF compEsq : ce.getPaso3()) {
                    resultado += compEsq.toString().substring(0, compEsq.toString().length());
                    resultado += "\n";
                }
                pasos.add(new PasosCardView("3", descripcion, resultado));

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
