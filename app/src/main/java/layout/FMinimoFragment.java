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
import fedelizondo.basededatos.MainActivity;
import fedelizondo.basededatos.R;


public class FMinimoFragment extends Fragment {

    private Administradora administradora;
    private TextView contenido;

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



        String dependencias = "{ "+administradora.calcularFmin().toString()+" }";

        contenido.setText(dependencias);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
