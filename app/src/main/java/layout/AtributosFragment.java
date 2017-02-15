package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import LogicaNegocio.Administradora;
import fedelizondo.basededatos.R;



public class AtributosFragment extends Fragment implements AgregarAtributosFragment.OnFragmentInteractionListener {

    public static final String LISTADO_ATRIBUTOS = "listadoAtributos";

    private ArrayList<String> listaAtributos;

    private Administradora administradora;

    private ListView listViewAtributos;



    private OnFragmentInteractionListener mListener;

    public AtributosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param listadoAtributos listado de atributos pasada por Main.
     * @return A new instance of fragment AtributosFragment.
     */

    public static AtributosFragment newInstance(ArrayList<String> listadoAtributos) {
        AtributosFragment fragment = new AtributosFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(LISTADO_ATRIBUTOS, listadoAtributos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listaAtributos = getArguments().getStringArrayList(LISTADO_ATRIBUTOS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_atributos, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onFragmentInteraction(ArrayList<String> listadoAtributo) {
        for (String string : listadoAtributo) {
            administradora.agregarAtributos(string);

        }
        listViewAtributos = (ListView) getActivity().findViewById(R.id.listViewAtributos);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, administradora.darListadoAtributos());
        listViewAtributos.setAdapter(arrayAdapter);
    }


}
