package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.security.PrivateKey;
import java.util.ArrayList;

import fedelizondo.basededatos.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SeleccionarDeterminatesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SeleccionarDeterminatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeleccionarDeterminatesFragment extends Fragment {

    public static String LISTADOATRIBUTOS ="LISTADO ATRIBUTOS";
    public static String LISTADOINDEX = "LISTADO INDEX DETERMINATES";

    private ArrayList<String> determinante;//ES UNA LISTA DE ATRIBUTOS SELECCIONADOS

    private ArrayList<Integer> indexSeleccionados;
    private ArrayList<String> ListadoAtributos ;

    private View view;

    private ListView listView;
    private TextView textView;

    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SeleccionarDeterminatesFragment() {
        // Required empty public constructor
    }

    public static SeleccionarDeterminatesFragment newInstance(ArrayList<String> listadoAtributos) {
        SeleccionarDeterminatesFragment fragment = new SeleccionarDeterminatesFragment();
        Bundle args = new Bundle();
        if(listadoAtributos == null)
            listadoAtributos = new ArrayList<>();
        args.putStringArrayList(LISTADOATRIBUTOS, listadoAtributos);
        args.putIntegerArrayList(LISTADOINDEX,new ArrayList<Integer>());
        fragment.setArguments(args);
        return fragment;
    }


    public static SeleccionarDeterminatesFragment newInstance(ArrayList<String> listadoAtributos,ArrayList<String> determinante)
    {
        SeleccionarDeterminatesFragment fragment = new SeleccionarDeterminatesFragment();
        Bundle args = new Bundle();
        if(listadoAtributos == null)
        {   listadoAtributos = new ArrayList<>();}

        args.putStringArrayList(LISTADOATRIBUTOS, listadoAtributos);

        if(determinante == null)
        {   determinante = new ArrayList<>();}

        ArrayList<Integer> listaIndex = new ArrayList<>();

        if(determinante != null)
        {
            for (String componenteDeterminante: determinante )
            {
                if(listadoAtributos.contains(componenteDeterminante))
                    listaIndex.add(listadoAtributos.indexOf(componenteDeterminante));
            }
        }
        args.putIntegerArrayList(LISTADOINDEX,listaIndex);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ListadoAtributos = getArguments().getStringArrayList(LISTADOATRIBUTOS);
            indexSeleccionados = getArguments().getIntegerArrayList(LISTADOINDEX);
            determinante = new ArrayList<>();
            if( !indexSeleccionados.isEmpty()  )
            {   for(Integer index:indexSeleccionados)
                determinante.add(ListadoAtributos.get(index));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_agregar_determinates, container, false);
        initView(view);
        return view;
    }

    public void initView(View view)
    {
        textView = (TextView) view.findViewById(R.id.tv_AgregarDeterminantes);
        listView = (ListView) view.findViewById(R.id.lv_ListadoDeterminantes);
        ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_checked,ListadoAtributos);//
        listView.setAdapter(adapter);

        for (Integer posicion: indexSeleccionados) {
            listView.setItemChecked(posicion,true);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer integer = position;

                if(indexSeleccionados.contains(integer))
                {
                    determinante.remove(ListadoAtributos.get(position));
                    indexSeleccionados.remove(integer);
                }
                else
                {
                    indexSeleccionados.add(integer);
                    determinante.add(ListadoAtributos.get(position));

                }
                textView.setText(determinante.toString().replace('[',' ').replace(']',' ')+"->");
                if(determinante.isEmpty())
                    textView.setText(R.string.ErrorDFNoHayDeterminanteSeleccionados);

                if(mListener != null)
                    mListener.darDeterminante(determinante);
            }
        });

        if(determinante.isEmpty())
            textView.setText(R.string.ErrorDFNoHayDeterminanteSeleccionados);
        else
            textView.setText(determinante.toString().replace('[',' ').replace(']',' ')+" ->");
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
        void darDeterminante(ArrayList<String> determinante);
    }
}
