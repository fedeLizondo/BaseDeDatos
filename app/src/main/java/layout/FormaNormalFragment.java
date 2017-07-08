package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import LogicaNegocio.Administradora;
import LogicaNegocio.FormaNormal;
import fedelizondo.basededatos.MainActivity;
import fedelizondo.basededatos.R;


public class FormaNormalFragment extends Fragment {


    private Administradora administradora;
    private TextView contenido;

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
        contenido = (TextView) view.findViewById(R.id.tv_cuerpoFNormal);
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

        if(contenido != null)
        {
            if(fn != null)
                contenido.setText(fn.JustificaMiFN().toString());

            if(administradora.darListadoAtributos().size() == 0)
                contenido.setText(getResources().getString(R.string.ErrorNoHayAtributos) );
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser)
            update();
        super.setUserVisibleHint(isVisibleToUser);
    }
}
