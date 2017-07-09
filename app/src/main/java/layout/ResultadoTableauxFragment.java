package layout;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
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


public class ResultadoTableauxFragment extends Fragment {

    private Administradora administradora;
    private String[][] ultimoTableaux;
    private View view;
    private TextView tv_Contenido;
    private TableLayout tableLayout;
    private Esquemas esquemas;
    private Tableaux tableaux;
    private boolean hayPerdidaDeInformacion;

    public ResultadoTableauxFragment() {
        // Required empty public constructor
    }

    public static ResultadoTableauxFragment newInstance() {
        ResultadoTableauxFragment fragment = new ResultadoTableauxFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hayPerdidaDeInformacion = false;

        if (getContext() instanceof MainActivity) {
            administradora = ((MainActivity)getContext()).administradora;
        }
        else
            administradora = Administradora.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_resultado_tableaux, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {
        tv_Contenido = (TextView) view.findViewById(R.id.tv_cuerpoTableux);

        tableaux = administradora.calcularTableaux();

        hayPerdidaDeInformacion = administradora.hayPerdidaDeInformacion();
        tv_Contenido.setText(hayPerdidaDeInformacion?R.string.subTituloTableuxTienePerdida:R.string.subTituloTableuxNoTienePerdida);

        PasoTableaux pasoTableaux = tableaux.ultimoPaso();

        tableLayout = (TableLayout) view.findViewById(R.id.tl_Tableaux);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
        fillTable(tableaux.darFilas()+1,tableaux.darColumnas()+1,pasoTableaux.imprimirEsquema(administradora.darListadoAtributos(),administradora.darEsquema()),tableLayout);

    }

    private void fillTable(final int fila, final int columna,final String[][] matrix, TableLayout table) {
        table.removeAllViews();

        TableRow row = new TableRow(getActivity());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        ArrayList<Integer> filaCompleta = new ArrayList<>();

        if(!hayPerdidaDeInformacion && tableaux != null)
        {
                filaCompleta.add(tableaux.darFilaCompleta()+1);
        }

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
                else
                {
                    if (filaCompleta.contains((Integer) i)) {
                        edit.setTextColor(Color.RED);
                        edit.setBackgroundColor(Color.argb(127, 255, 194, 73));
                    }
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