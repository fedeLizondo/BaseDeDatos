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


public class CalculoSinPerdidaFragment extends Fragment {

    private Administradora administradora;
    private TextView tvContenido;
    private ArrayList<ArrayList<DependenciaFuncional>> lSubEsquemas;
    private View view;
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
        initView(view);
        return view;
    }

    public void initView(View view)
    {
            tvContenido = (TextView) view.findViewById(R.id.contenido);
            String contenido = administradora.calcularDescomposicionFNBC().toString();
            //TODO MODIFICAR EL TEXTO PARA QUE QUEDE MAS ACORDE A LOS SUB ESQUEMAS
            tvContenido.setText(contenido);

    }

    public void update()
    {
        if(tvContenido!=null)
        {
            ArrayList<ArrayList<DependenciaFuncional>> aux = administradora.calcularDescomposicionFNBC();
            if(lSubEsquemas == null || !lSubEsquemas.equals(aux))
            {
                lSubEsquemas = aux;
                String contenido = aux.toString();
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
