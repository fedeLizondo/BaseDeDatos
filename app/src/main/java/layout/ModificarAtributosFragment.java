package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fedelizondo.basededatos.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ModificarAtributosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ModificarAtributosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModificarAtributosFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ATRIBUTO_A_MODIFICAR = "atributoAnterior";
    public static final String LISTADO_ATRIBUTOS = "listadoAtributo";

    // TODO: Rename and change types of parameters
    private String atributoAnterior;
    private ArrayList<String> listaAtributos;

    private Button btnAceptar;
    private Button btnCancelar;
    private EditText etAtributo;
    private TextView etAtributoAnterior;

    private OnFragmentInteractionListener mListener;

    public ModificarAtributosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param AtributoAnterior Parameter 1.
     * @param ListadoAtributos Parameter 2.
     * @return A new instance of fragment ModificarAtributosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModificarAtributosFragment newInstance(String AtributoAnterior, ArrayList<String> ListadoAtributos) {
        ModificarAtributosFragment fragment = new ModificarAtributosFragment();
        Bundle args = new Bundle();
        args.putString(ATRIBUTO_A_MODIFICAR, AtributoAnterior);
        args.putStringArrayList(LISTADO_ATRIBUTOS, ListadoAtributos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            atributoAnterior = getArguments().getString(ATRIBUTO_A_MODIFICAR);
            listaAtributos = getArguments().getStringArrayList(LISTADO_ATRIBUTOS);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_modificar_atributos, container, false);

        etAtributo = (EditText) rootView.findViewById(R.id.etAtributoIngresadoModificar);

        etAtributoAnterior = (TextView) rootView.findViewById(R.id.etATributoModificar);
        etAtributoAnterior.setText(atributoAnterior);

        btnAceptar = (Button) rootView.findViewById(R.id.btnModificarAtributosAceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( etAtributo.getTextSize() > 0 && !etAtributo.getText().equals("") && !etAtributo.getText().equals(atributoAnterior))
                {

                    String atributo = etAtributo.getText().toString();
                    if(!listaAtributos.contains(atributo))
                    {
                        if(mListener!= null)
                            mListener.onFragmentInteraction(atributoAnterior,atributo);
                        dismiss();
                    }
                    else
                    {
                        Toast.makeText(getContext(),
                                getResources().getString(R.string.errorModificarAtributoYaIngresado),
                                Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    if(etAtributo.getText().equals(""))
                    {
                        Toast.makeText(getContext(),
                                getResources().getString(R.string.errorModificarAtributoVacio),
                                Toast.LENGTH_LONG).show();
                    }

                    if(etAtributo.getText().equals(atributoAnterior))
                    {
                        Toast.makeText(getContext(),
                                getResources().getString(R.string.errorModificarAtributoYaIngresado),
                                Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

        btnCancelar = (Button) rootView.findViewById(R.id.btnModificarAtributosCancelar) ;
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getDialog().setTitle(R.string.tituloFragmentModificarAtributo);
        return rootView;
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
        void onFragmentInteraction(String AtributoAnterior,String AtributoModificado);
    }
}
