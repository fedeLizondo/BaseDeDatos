package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import LogicaNegocio.Administradora;
import fedelizondo.basededatos.CalcularClausura;
import fedelizondo.basededatos.MainActivity;
import fedelizondo.basededatos.R;

public class ClausuraFragment extends Fragment {
    private static final String LISTADOATRIBUTOS = "LISTADOATRIBUTOS";

    private ArrayList<String> ListadoAtributos;

    private View view;
    private Administradora administradora;
    private ListView listView;
    private Button btnCalcular;

    private ArrayList<Integer> indexSeleccionado;
    private ArrayList<String> atributosSeleccionados;

    public ClausuraFragment() {
        // Required empty public constructor
    }

    public static ClausuraFragment newInstance(ArrayList<String> param1) {
        ClausuraFragment fragment = new ClausuraFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(LISTADOATRIBUTOS, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ListadoAtributos = getArguments().getStringArrayList(LISTADOATRIBUTOS);
        }
        atributosSeleccionados = new ArrayList<>();
        indexSeleccionado = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_clausura, container, false);

        if( getContext() instanceof MainActivity)
            administradora = ((MainActivity)getContext()).administradora;
        else
            administradora = Administradora.getInstance();

        initView(view);
        return view;
    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    public void initView(View view)
    {
        btnCalcular = (Button) view.findViewById(R.id.button);
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!atributosSeleccionados.isEmpty()) {
                    ArrayList<String> resultado = administradora.calcularClausura(atributosSeleccionados);
                    Intent i = new Intent(getActivity(),CalcularClausura.class);
                    i.putExtra(CalcularClausura.CLAUSURA,atributosSeleccionados);
                    i.putExtra(CalcularClausura.RESULTADOCLAUSURA,resultado);
                    startActivity(i);
                }
                else
                {
                    if(atributosSeleccionados.isEmpty())
                        Snackbar.make(getView(),R.string.seleccionarClausura,Snackbar.LENGTH_LONG).show();
                }
            }
        });


        listView = (ListView) view.findViewById(R.id.lv_AtributosClausura);

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_checked,ListadoAtributos);
        listView.setAdapter(adapter);
        update();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer integer = position;

                if(indexSeleccionado.contains(integer))
                {
                    atributosSeleccionados.remove(ListadoAtributos.get(position));
                    indexSeleccionado.remove(integer);
                }
                else
                {
                    indexSeleccionado.add(integer);
                    atributosSeleccionados.add(ListadoAtributos.get(position));

                }
                btnCalcular.setText(atributosSeleccionados.toString().replace('[','{').replace(']','}')+"*");
                if(atributosSeleccionados.isEmpty())
                    btnCalcular.setText(R.string.btnCalcularClausura);
            }
        });

    }


    public void update()
    {
        if(btnCalcular!=null && view != null)
        {
            ScaleAnimation animation = new ScaleAnimation(0,1,0,1);
            animation.setFillBefore(true);
            animation.setFillAfter(true);
            animation.setFillEnabled(true);
            animation.setDuration(300);
            animation.setInterpolator(new OvershootInterpolator());
            btnCalcular.setAnimation(animation);

            ArrayList<String> aux = administradora.darListadoAtributos();

            if( ListadoAtributos.size() != aux.size() || !ListadoAtributos.containsAll(aux) ) {
                ListadoAtributos = aux;
                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_checked,ListadoAtributos);
                listView.setAdapter(adapter);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser)
        {
            update();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
