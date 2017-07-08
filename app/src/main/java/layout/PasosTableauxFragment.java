package layout;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import LogicaNegocio.Administradora;
import LogicaNegocio.Coordenada;
import LogicaNegocio.Esquemas;
import LogicaNegocio.PasoTableaux;
import LogicaNegocio.Tableaux;
import fedelizondo.basededatos.CalculoTableaux;
import fedelizondo.basededatos.MainActivity;
import fedelizondo.basededatos.R;


public class PasosTableauxFragment extends Fragment {


    private Administradora administradora;
    private String[][] ultimoTableaux;
    private View view;
    private TextView tv_Contenido;
    private TextView tv_DependenciaFuncional;
    private TableLayout tableLayout;
    private Esquemas esquemas;
    private Tableaux tableaux;
    private Spinner spinner;
    private PasoTableaux pasoTableaux;
    private int posicion ;
    public PasosTableauxFragment() {
        // Required empty public constructor
    }


    public static PasosTableauxFragment newInstance() {
        PasosTableauxFragment fragment = new PasosTableauxFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* if (getArguments() != null) {
        }

        if (getContext() instanceof MainActivity) {
            administradora = ((MainActivity)getContext()).administradora;
        }
        else
            administradora = Administradora.getInstance();
*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //view = inflater.inflate(R.layout.fragment_resultado_tableaux, container, false);
        view = inflater.inflate(R.layout.fragment_pasos_tableaux, container, false);
        initView(view);
        return view;//inflater.inflate(R.layout.fragment_pasos_tableaux, container, false);

    }

    public void initView(View view) {

        posicion = 0;

       if(administradora == null)
           administradora = Administradora.getInstance();
        tableaux = administradora.calcularTableaux();

        spinner = (Spinner) view.findViewById(R.id.spinner);

        tv_Contenido = (TextView) view.findViewById(R.id.txtDependenciaFuncional);
        tv_Contenido.setText(R.string.subTituloPasosTableauxInicial);



        if(spinner != null)
        {
            ArrayList<String> arrayListPasos = new ArrayList<>();
            arrayListPasos.add("Esquema Inicial");

            for (int i = 1; i < tableaux.cantidadDePasos(); i++) {
                arrayListPasos.add("Paso "+i);
            }



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, arrayListPasos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabSiguientePaso);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(posicion >= 0 && posicion < (tableaux.cantidadDePasos()-1))
                    {
                        posicion++;
                        spinner.setSelection(posicion);
                    }
                    else
                    {
                        if (posicion == tableaux.cantidadDePasos()-1)
                        {   //TODO CAMBIAR SIMBOLO DE + POR FLECHA
                            Toast toast = Toast.makeText(getContext(),getContext().getString(R.string.ErrorNoHayMasPasos), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
            });

            spinner.setAdapter(adapter);
            spinner.setSelection(posicion);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pasoTableaux = tableaux.getPaso(position);
                    posicion = position;

                    if(tableLayout == null) {
                        tableLayout = (TableLayout) view.findViewById(R.id.tl_Tableaux);
                        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    }


                    if(position == (tableaux.cantidadDePasos()-1))
                    {
                        fab.setVisibility(View.INVISIBLE);
                    }
                    else
                        fab.setVisibility(View.VISIBLE);


                    if(position == 0)
                        tv_Contenido.setText(R.string.subTituloPasosTableauxInicial);
                    else {

                        tv_Contenido.setText(String.format(getResources().getString(R.string.subTituloPasosTableaux), pasoTableaux.getDependenciaFuncional().toString()));

                    }
                    fillTable(tableaux.darFilas() + 1, tableaux.darColumnas() + 1, pasoTableaux.imprimirEsquema(administradora.darListadoAtributos(), administradora.darEsquema()), tableLayout);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

           /* spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PasoTableaux pasoTableaux = tableaux.getPaso(position);
                    tableLayout = (TableLayout) view.findViewById(R.id.tl_Tableaux);
                    TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    fillTable(tableaux.darFilas() + 1, tableaux.darColumnas() + 1, pasoTableaux.imprimirEsquema(administradora.darListadoAtributos(), administradora.darEsquema()), tableLayout);
                }
            });*/
        }
        pasoTableaux = tableaux.getPaso(posicion);

        tableLayout = (TableLayout) view.findViewById(R.id.tl_Tableaux);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
        fillTable(tableaux.darFilas()+1,tableaux.darColumnas()+1,pasoTableaux.imprimirEsquema(administradora.darListadoAtributos(),administradora.darEsquema()),tableLayout);






    }

    private void fillTable(final int fila, final int columna,final String[][] matrix, TableLayout table) {
        if(table != null && pasoTableaux != null){
            table.removeAllViews();

            TableRow row = new TableRow(getActivity());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (int i = 0; i < fila; i++) {

                ArrayList<Integer> cambiosFila = new ArrayList<>();
                ArrayList<Integer> cambiosColumna = new ArrayList<>();
                boolean marcarFila = false;
                for(Coordenada c:pasoTableaux.getCambios())
                {
                    if((c.getFila()+1) == i)
                    {
                        cambiosFila.add(c.getColumna()+1);
                        marcarFila = true;
                    }
                    if(!cambiosColumna.contains(c.getColumna()+1) )
                    {
                        cambiosColumna.add(c.getColumna()+1);
                    }
                }

                row = new TableRow(getActivity());
                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                for (int j = 0; j < columna; j++) {
                    TextView edit = new TextView(getContext());
                    edit.setInputType(InputType.TYPE_CLASS_TEXT);
                    edit.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    edit.setTextSize(20);

                    if(i == 0 || j==0)
                        edit.setTextColor(Color.BLUE);
                    else {
                        if (marcarFila)
                            edit.setBackgroundColor(Color.argb(67, 255, 194, 73));
                        if(cambiosColumna.contains(j))
                            edit.setBackgroundColor(Color.argb(67, 255, 194, 73));
                    }

                    if(cambiosFila.contains((Integer)j))
                    {
                        edit.setTextColor(Color.RED);
                        edit.setBackgroundColor(Color.argb(127,255,194,73));
                    }

                    edit.setText(matrix[i][j]);
                    edit.setPadding(8,4,8,4);
                    edit.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    edit.setKeyListener(null);
                    row.addView(edit);

                }
                table.addView(row);
                table.setStretchAllColumns(true);
            }
        }
    }

}
