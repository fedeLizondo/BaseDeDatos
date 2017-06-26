package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import LogicaNegocio.Administradora;
import LogicaNegocio.DependenciaFuncional;
import fedelizondo.basededatos.MainActivity;
import fedelizondo.basededatos.R;


public class CalculoEficienteFragment extends Fragment {

    private Administradora administradora;
    private TextView tvContenido;

    private ArrayList<ArrayList<DependenciaFuncional>> lSubEsquemas;
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
        initView(view);
        return view;
    }

    public void initView(View view)
    {
        if(view != null)
        {
            tvContenido = (TextView) view.findViewById(R.id.contenido);
            update();
        }
    }

    public void update()
    {
        if( tvContenido != null && administradora != null)
        {
            ArrayList<ArrayList<DependenciaFuncional>> aux = administradora.calcularDescomposicion3FN();
            if(lSubEsquemas == null || !lSubEsquemas.equals(aux))
            {
                lSubEsquemas = aux;
                String contenido = aux.toString();
                contenido = contenido.substring(1,contenido.lastIndexOf("]"));
                //TODO MODIFICAR EL TEXTO PARA QUE QUEDE MAS ACORDE A LOS SUB ESQUEMAS
                tvContenido.setText(contenido);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser)
            update();
        super.setUserVisibleHint(isVisibleToUser);
    }
}
