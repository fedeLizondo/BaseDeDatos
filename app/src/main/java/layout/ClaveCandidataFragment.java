package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import LogicaNegocio.Administradora;
import fedelizondo.basededatos.MainActivity;
import fedelizondo.basededatos.R;


public class ClaveCandidataFragment extends Fragment {

    private View view;
    private ListView listaCC;
    private ListView listaSuperClaves;
    private TextView tvCCSeleccionada;
    private ArrayList<ArrayList<String>> ListadoClavesCandidatas;
    private ArrayList<ArrayList<String>> ListadoSuperClaves;

    private ArrayList<String> Clavecandidata;
    private Administradora administradora;

    public ClaveCandidataFragment() {
        // Required empty public constructor
    }


    public static ClaveCandidataFragment newInstance() {
        ClaveCandidataFragment fragment = new ClaveCandidataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getContext() instanceof MainActivity)
            administradora = ((MainActivity) getContext()).administradora;
        else
            administradora = Administradora.getInstance();

        Clavecandidata = new ArrayList<>();
        ListadoSuperClaves = new ArrayList<>();
        ListadoClavesCandidatas = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_clave_candidata, container, false);

        initView(view);
        return view;
    }


    public void initView(View view)
    {
        tvCCSeleccionada = (TextView) view.findViewById(R.id.tv_ccSeleccionada);
        listaCC = (ListView) view.findViewById(R.id.lv_ClaveCandidata);
        listaSuperClaves = (ListView) view.findViewById(R.id.lv_SuperClave);
        update();
    }

    private ArrayList<String> convertirAArrayList(ArrayList<ArrayList<String>> clave)
    {
        ArrayList<String> resultado = new ArrayList();

        if(clave!=null)
        for (ArrayList<String> item :clave)
        {
            String itemAuxiliar =  item.toString().replace('[',' ').replace(']',' ');
            resultado.add(itemAuxiliar);
        }
        return resultado;
    }

    private void update()
    {
        if( ListadoClavesCandidatas == null || !ListadoClavesCandidatas.equals( administradora.calcularClavesCandidatas() )) {

            ListadoClavesCandidatas = administradora.calcularClavesCandidatas();
            if (ListadoClavesCandidatas != null && !ListadoClavesCandidatas.isEmpty())
                Clavecandidata = ListadoClavesCandidatas.get(0);
            ArrayAdapter ccAdaper = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, convertirAArrayList(ListadoClavesCandidatas));
            listaCC.setAdapter(ccAdaper);

            listaCC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Clavecandidata = ListadoClavesCandidatas.get(position);
                    tvCCSeleccionada.setText(Clavecandidata.toString().replace('[',' ').replace(']',' '));
                    administradora.cambiarClaveCandidata(Clavecandidata);//TODO MODIFICAR NO HACE CAMBIOS
                }
            });
        }

        if( ListadoSuperClaves == null || !ListadoSuperClaves.equals(administradora.darSuperClaves()))
        {
            ListadoSuperClaves = administradora.darSuperClaves();
            ArrayAdapter skAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,convertirAArrayList(ListadoSuperClaves));
            listaSuperClaves.setAdapter(skAdapter);
        }

        if(!Clavecandidata.isEmpty())
            tvCCSeleccionada.setText(Clavecandidata.toString().replace('[',' ').replace(']',' '));

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser)
        {
            update();
        }
        super.setUserVisibleHint(isVisibleToUser);

    }
}
