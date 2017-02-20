package fedelizondo.basededatos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CalcularClausura extends AppCompatActivity {

    public static final String CLAUSURA = "CLAUSURA";
    public static final String RESULTADOCLAUSURA = "RESULTADOCLAUSURA";

    private ArrayList<String> clausura;
    private ArrayList<String> resultadoClausura;

    private TextView tvClausura;
    private TextView tvResultado;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_clausura_resultados);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.tituloCalcularClausura);

        if( getIntent().hasExtra(CLAUSURA) && getIntent().hasExtra(RESULTADOCLAUSURA) )
        {
            clausura = getIntent().getStringArrayListExtra(CLAUSURA);
            resultadoClausura = getIntent().getStringArrayListExtra(RESULTADOCLAUSURA);
        }

        tvClausura = (TextView)findViewById(R.id.tv_clausura);
        tvResultado = (TextView) findViewById(R.id.tv_resultado);

        String stringClausura = clausura.toString().replace('[','{').replace(']','}')+"* = ";
        tvClausura.setText(stringClausura);

        String stringResultado = resultadoClausura.toString().replace('[','{').replace(']','}');
        tvResultado.setText(stringResultado);

        btnVolver = (Button) findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
