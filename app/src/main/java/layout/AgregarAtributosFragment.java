package layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import fedelizondo.basededatos.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AgregarAtributosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AgregarAtributosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgregarAtributosFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "listaAtributosAgregar";

    private Button btnAgregar ;
    private Button btnCancelar;
    private EditText atributoPorAgregar;


    private ArrayList<String> listadoAtributo;
    private ArrayList<String> atributos;

    private OnFragmentInteractionListener mListener;

    public AgregarAtributosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment AgregarAtributosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgregarAtributosFragment newInstance(String param1) {
        AgregarAtributosFragment fragment = new AgregarAtributosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            listadoAtributo = getArguments().getStringArrayList(ARG_PARAM1);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_agregar_atributos, container, false);

        atributoPorAgregar = (EditText) v.findViewById(R.id.etAtributoPorAgregar);


        btnAgregar = (Button) v.findViewById(R.id.btnAgregarAtributosAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (atributoPorAgregar.getTextSize() > 0) {
                    String atributo = atributoPorAgregar.getText().toString();
                    String arrayAtributo[] = atributo.split(",");
                    if (!listadoAtributo.isEmpty()) {
                        for (String string : arrayAtributo)
                        {
                            if( !listadoAtributo.contains(string) )
                                atributos.add(string);
                            else
                            {
                                //TODO MENSAJE DE ERROR INFORMANDO QUE HABIA ELEMENTOS REPETIDOS Y NO SE PUDIERON INGRESAR
                            }
                        }

                    }
                    else
                    {
                        for (String s : arrayAtributo) {
                            atributos.add(s);
                            Log.d("DATO EN AGREGAR ELSE",s);
                        }
                        getActivity().finish();
                    }
                }
                else
                {
                    //TODO MOSTRAR MENSAJE DE ERROR QUE EL TEXTO ESTA VACIO
                }
            }
        });

        btnCancelar = (Button) v.findViewById(R.id.btnAgregarAtributosCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });




        return v;
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

    public ArrayList<String> darAtributosPorAgregar()
    {
        return null;
    }


}
