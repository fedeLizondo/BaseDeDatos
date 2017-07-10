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
import LogicaNegocio.DependenciaFuncional;
import fedelizondo.basededatos.AdapterCardView;
import fedelizondo.basededatos.MainActivity;
import fedelizondo.basededatos.PasosCardView;
import fedelizondo.basededatos.R;


public class FMinimoFragment extends Fragment {

    private Administradora administradora;
    private ArrayList<DependenciaFuncional> dependenciasFuncionales;

    private RecyclerView recyclerViewFM;

    private AdapterCardView adapter;



    public FMinimoFragment() {
        // Required empty public constructor
    }

    public static FMinimoFragment newInstance() {
        FMinimoFragment fragment = new FMinimoFragment();
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
        {
            administradora = ((MainActivity)getContext()).administradora;
        }
        else
            administradora = Administradora.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fminimo, container, false);

        recyclerViewFM = (RecyclerView) view.findViewById(R.id.rv_Fmin);
        recyclerViewFM.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity().getApplicationContext());
        recyclerViewFM.setLayoutManager(layoutManager);

        update();
        return view;
    }

    public void update()
    {
        if(administradora== null)
            administradora = Administradora.getInstance();

        ArrayList<DependenciaFuncional> ultimoPaso =  administradora.calcularFmin();

        if(recyclerViewFM!=null) {
            ArrayList<PasosCardView> pasos = new ArrayList<>();
            String resultado = "";
            String descripcion = "";
            if (administradora.darListadoDependenciasFuncional().isEmpty() || administradora.darListadoAtributos().isEmpty()) {

                if (administradora.darListadoAtributos().isEmpty()) {
                    descripcion = getContext().getString(R.string.ErrorNoHayAtributos);
                    pasos.add(new PasosCardView("", descripcion, ""));
                } else {

                    descripcion = getContext().getString(R.string.subTituloFMin);
                    pasos.add(new PasosCardView("", descripcion, "F* = {}"));
                }
            } else {


                resultado = "F* = { " + administradora.getPaso1().toString().replace('[', ' ').replace(']', ' ') + " }";

                descripcion = getContext().getString(R.string.CalculoFMPrimerPaso);
                pasos.add(new PasosCardView("1", descripcion, resultado));


                resultado = "F* = { " +  administradora.getPaso2().toString().replace('[', ' ').replace(']', ' ') + " }";
                descripcion = getContext().getString(R.string.CalculoFMSegundoPaso);
                pasos.add(new PasosCardView("2", descripcion, resultado));


                resultado = "F* = { " + administradora.calcularFmin().toString().replace('[', ' ').replace(']', ' ') + " }";
                descripcion = getContext().getString(R.string.CalculoFMTercerPaso);
                pasos.add(new PasosCardView("3", descripcion, resultado));

            }

            adapter = new AdapterCardView(pasos);
            recyclerViewFM.setAdapter(adapter);
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
