package layout;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import fedelizondo.basededatos.R;


public class SeleccionarDeterminadoFragment extends Fragment {

    private static final String LISTADOATRIBUTOS = "LISTADOATRIBUTOSDETERMINANTE";
    private static final String LISTADOINDEX = "LISTADOINDEX";

    private ArrayList<String> listadoAtributos;
    private ArrayList<Integer> indexSeleccionado;
    private ArrayList<String> determinado;
    private View view;
    private ListView listView;
    private TextView textView;

    private OnFragmentInteractionListener mListener;

    public SeleccionarDeterminadoFragment() {
    }

    public static SeleccionarDeterminadoFragment newInstance(ArrayList<String> atributos) {
        SeleccionarDeterminadoFragment fragment = new SeleccionarDeterminadoFragment();
        Bundle args = new Bundle();
        if(atributos == null)
            atributos = new ArrayList<>();

        args.putStringArrayList(LISTADOATRIBUTOS, atributos);
        args.putIntegerArrayList(LISTADOINDEX,new ArrayList<Integer>());
        fragment.setArguments(args);
        return fragment;
    }

    public static SeleccionarDeterminadoFragment newInstance(ArrayList<String> atributos, ArrayList<String> determinado) {
        SeleccionarDeterminadoFragment fragment = new SeleccionarDeterminadoFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(LISTADOATRIBUTOS, atributos);

        ArrayList<Integer> posiciones = new ArrayList<>();
        for (String componente: determinado ) {
            if(atributos.contains(componente))
                posiciones.add(atributos.indexOf(componente));
        }

        args.putIntegerArrayList(LISTADOINDEX, posiciones);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listadoAtributos = new ArrayList<>();
        indexSeleccionado = new ArrayList<>();
        determinado = new ArrayList<>();
        if (getArguments() != null) {
            listadoAtributos = getArguments().getStringArrayList(LISTADOATRIBUTOS);
            indexSeleccionado = getArguments().getIntegerArrayList(LISTADOINDEX);
            for(Integer posicion: indexSeleccionado)
            {
                determinado.add(listadoAtributos.get(posicion));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_agregar_determinado, container, false);
        initView(view);
        return view;
    }

    public void initView(View view)
    {

        textView = (TextView) view.findViewById(R.id.tv_AgregarDeterminado);
        listView = (ListView) view.findViewById(R.id.lv_ListadoDeterminados);
        ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_checked,listadoAtributos);
        listView.setAdapter(adapter);


        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAgregarDeterminado);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AgregarAtributos();
                if(mListener != null)
                {
                    if( mListener.estaRepetidaLaDependeciaFuncional())
                        Snackbar.make(getView(),R.string.ErrorDependenciaFuncionalRepetida,Snackbar.LENGTH_LONG).show();
                    else
                        mListener.agregarDependenciaFuncional();
                }
            }
        });

        ScaleAnimation animation = new ScaleAnimation(0,1,0,1);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        animation.setDuration(500);
        animation.setInterpolator(new OvershootInterpolator());
        fab.startAnimation(animation);

        for (Integer posicion: indexSeleccionado) {
            listView.setItemChecked(posicion,true);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer integer = position;

                if(indexSeleccionado.contains(integer))
                {
                    determinado.remove(listadoAtributos.get(position));
                    indexSeleccionado.remove(integer);
                }
                else
                {
                    indexSeleccionado.add(integer);
                    determinado.add(listadoAtributos.get(position));

                }
                textView.setText("->"+determinado.toString().replace('[',' ').replace(']',' '));
                if(determinado.isEmpty())
                    textView.setText(R.string.ErrorDFNoHayDeterminanteSeleccionados);

                if(mListener != null)
                    mListener.darDeterminado(determinado);
            }
        });

        if(determinado.isEmpty())
            textView.setText(R.string.ErrorDFNoHayDeterminadoSeleccionados);
        else
            textView.setText("->"+determinado.toString().replace('[',' ').replace(']',' '));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                   + " must implement OnFragmentInteractionListenerFragmentModificarAtributo");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void darDeterminado(ArrayList<String> determinado);
        boolean estaRepetidaLaDependeciaFuncional();
        void agregarDependenciaFuncional();
    }
}
