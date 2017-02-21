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


public class FMinimoFragment extends Fragment {

    private Administradora administradora;
    private TextView contenido;
    private ArrayList<DependenciaFuncional> dependenciasFuncionales;

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

        contenido = (TextView) view.findViewById(R.id.tv_cuerpoFMIN);

        update();
        return view;
    }

    public void update()
    {
        ArrayList<DependenciaFuncional> aux = administradora.calcularFmin();
        if(dependenciasFuncionales == null || dependenciasFuncionales.equals(aux))
        dependenciasFuncionales = aux;
        String dependencias = "{ "+dependenciasFuncionales.toString().replace('[',' ').replace(']',' ')+" }";
        if(contenido != null)
        contenido.setText(dependencias);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser)
            update();
        super.setUserVisibleHint(isVisibleToUser);
    }
}
