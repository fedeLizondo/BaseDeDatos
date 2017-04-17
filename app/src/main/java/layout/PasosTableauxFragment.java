package layout;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;

import LogicaNegocio.Administradora;
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
        view = inflater.inflate(R.layout.fragment_resultado_tableaux, container, false);
        initView(view);
        return inflater.inflate(R.layout.fragment_pasos_tableaux, container, false);

    }

    public void initView(View view) {

       /* tableaux = administradora.calcularTableaux();

        spinner = (Spinner) view.findViewById(R.id.spinner);

        ArrayList<Integer> arrayListPasos = new ArrayList<>();

        for(int i = 0 ;i< tableaux.cantidadDePasos();i++)
        {
            arrayListPasos.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(view.getContext(), android.R.layout.simple_spinner_item,arrayListPasos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PasoTableaux pasoTableaux = tableaux.getPaso(position);

                tableLayout = (TableLayout) view.findViewById(R.id.tl_Tableaux);
                TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
                fillTable(tableaux.darFilas()+1,tableaux.darColumnas()+1,pasoTableaux.imprimirEsquema(administradora.darListadoAtributos(),administradora.darEsquema()),tableLayout);
            }
        });

        PasoTableaux pasoTableaux = tableaux.getPaso(0);

        tableLayout = (TableLayout) view.findViewById(R.id.tl_Tableaux);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
        //fillTable(tableaux.darFilas()+1,tableaux.darColumnas()+1,pasoTableaux.imprimirEsquema(administradora.darListadoAtributos(),administradora.darEsquema()),tableLayout);
*/
    }

    private void fillTable(final int fila, final int columna,final String[][] matrix, TableLayout table) {
        table.removeAllViews();

        TableRow row = new TableRow(getActivity());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < fila; i++) {
            row = new TableRow(getActivity());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < columna; j++) {
                TextView edit = new TextView(getContext());
                edit.setInputType(InputType.TYPE_CLASS_TEXT);
                edit.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                edit.setTextSize(20);
                if(i == 0 || j==0)
                    edit.setTextColor(Color.BLUE);
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
