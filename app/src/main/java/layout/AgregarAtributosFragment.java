package layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
public class AgregarAtributosFragment extends DialogFragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM1 = "listaAtributosAgregar";

    private Button btnAgregar ;
    private Button btnCancelar;
    private EditText atributoPorAgregar;


    private ArrayList<String> listadoAtributo;
    private ArrayList<String> atributos;

    private OnFragmentInteractionListener mListener;

    public AgregarAtributosFragment() {

    }

    public static AgregarAtributosFragment newInstance(ArrayList<String> param1) {
        AgregarAtributosFragment fragment = new AgregarAtributosFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
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

        final View rootView = inflater.inflate(R.layout.fragment_agregar_atributos, container, false);
        InitView(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

            if( getTargetFragment() instanceof OnFragmentInteractionListener )
                mListener = (OnFragmentInteractionListener) getTargetFragment();
            else
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
        void onFragmentInteraction(ArrayList<String> listadoAtributo);
    }

    public void InitView(View rootView)
    {
        atributoPorAgregar = (EditText) rootView.findViewById(R.id.etAtributoPorAgregar);
        atributoPorAgregar.setText("");

        btnAgregar = (Button) rootView.findViewById(R.id.btnAgregarAtributosAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                atributos = new ArrayList<String>();
                if (atributoPorAgregar.getTextSize() > 0 && !atributoPorAgregar.getText().equals("")) {

                    String atributo = atributoPorAgregar.getText().toString();
                    String arrayAtributo[] = atributo.split(",");

                    ArrayList<String> listaStringRepetidos = new ArrayList<String>();

                    if (listadoAtributo != null && !listadoAtributo.isEmpty()) {
                        for (String string : arrayAtributo) {
                            if (!listadoAtributo.contains(string))
                                atributos.add(string);
                            else
                                listaStringRepetidos.add(string);
                        }

                        if (!listaStringRepetidos.isEmpty()) {
                            String preTexto = getResources().getString((listaStringRepetidos.size() > 1) ?
                                    R.string.preTextoMuchosFragmentAgregarAtributo :
                                    R.string.preTextoUnicoFragmentAgregarAtributo);

                            String postTexto = getResources().getString((listaStringRepetidos.size() > 1) ?
                                    R.string.postTextoMuchosFragmentAgregarAtributo :
                                    R.string.postTextoUnicoFragmentAgregarAtributo);

                            Toast.makeText(getContext(),
                                    preTexto + " " + listaStringRepetidos.toString() + " " + postTexto,
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        for (String s : arrayAtributo) {
                            atributos.add(s);
                        }
                    }
                    if (mListener != null)
                        mListener.onFragmentInteraction(atributos);
                    dismiss();
                } else {
                    String mensaje = getResources().getString(R.string.errorAgregarAtributoVacio);
                    Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancelar = (Button) rootView.findViewById(R.id.btnAgregarAtributosCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //Agrego el Titulo
        getDialog().setTitle(R.string.tituloFragmentAgregarAtributo);
    }


}
