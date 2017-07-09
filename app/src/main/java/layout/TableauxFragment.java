package layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import LogicaNegocio.Administradora;
import LogicaNegocio.Esquemas;
import fedelizondo.basededatos.AgregarEsquemas;
import fedelizondo.basededatos.CalculoTableaux;
import fedelizondo.basededatos.EsquemaAdapter;
import fedelizondo.basededatos.MainActivity;
import fedelizondo.basededatos.ModificarEsquema;
import fedelizondo.basededatos.R;


public class TableauxFragment extends Fragment {

    private Administradora administradora;

    private View view;
    private Button btnCalcular;
    private FloatingActionButton fab;
    private RecyclerView rvEsquemas;

    private EsquemaAdapter dataAdapter;
    private ArrayList<ArrayList<String>> lEsquemas;

    private Paint p = new Paint();
    public TableauxFragment() {
        // Required empty public constructor
    }

    public static TableauxFragment newInstance() {
        TableauxFragment fragment = new TableauxFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getContext() instanceof MainActivity) {
            administradora = ((MainActivity)getContext()).administradora;
        }
        else
            administradora = Administradora.getInstance();

        lEsquemas = administradora.darEsquemas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tableaux, container, false);
        initView(view);
        update();
        return view;
    }

    @Override
    public void onResume() {
        update();
        super.onResume();
    }

    public void initView(View view)
    {
        btnCalcular = (Button) view.findViewById(R.id.bt_CalcularTableaux);

        btnCalcular.setVisibility(lEsquemas.size() > 0?View.VISIBLE:View.INVISIBLE);

        rvEsquemas = (RecyclerView) view.findViewById(R.id.rv_Esquemas);
        fab = (FloatingActionButton) view.findViewById(R.id.fabAgregarEsquema);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lEsquemas != null && !lEsquemas.isEmpty()) {

                    Intent i = new Intent(getActivity(), CalculoTableaux.class);
                    Esquemas esquemaAux = administradora.darEsquema();
                    i.putExtra(CalculoTableaux.ESQUEMAS,esquemaAux);
                    startActivity(i);

                } else {
                    final Snackbar snackbar = Snackbar.make(getView(), R.string.ErrorTableauxEsquemasVacios, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(R.string.entendido, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (administradora.darListadoAtributos().isEmpty()) {
                    final Snackbar snackbar = Snackbar.make(getView(), R.string.ErrorTableauxNoHayAtributos, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(R.string.entendido, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                } else {
                       /* if (administradora.darListadoDependenciasFuncional().isEmpty()) {
                            final Snackbar snackbar = Snackbar.make(getView(), R.string.ErrorTableauxNoHayDF, Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction(R.string.entendido, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        } else {*/
                    Intent i = new Intent(getActivity(), AgregarEsquemas.class);
                    Esquemas esquema = administradora.darEsquema();
                    i.putExtra(AgregarEsquemas.ESQUEMA, esquema);
                    i.putExtra(AgregarEsquemas.ATRIBUTOS, administradora.darListadoAtributos());
                    startActivityForResult(i, 101);

                    //}
                }
                btnCalcular.setVisibility(lEsquemas.size() > 0?View.VISIBLE:View.INVISIBLE);
            }
        });

    }

    public void update()
    {

        if(administradora == null)
            administradora = Administradora.getInstance();
        lEsquemas = administradora.darEsquemas();

        if(rvEsquemas!=null) {
            rvEsquemas.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            rvEsquemas.setLayoutManager(layoutManager);

            dataAdapter = new EsquemaAdapter(lEsquemas);
            rvEsquemas.setAdapter(dataAdapter);
            dataAdapter.notifyDataSetChanged();

            btnCalcular.setVisibility(lEsquemas.size() > 0?View.VISIBLE:View.INVISIBLE);

            initSwipe();
        }
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                ArrayList<String> esquema = lEsquemas.get(position);

                if (direction == ItemTouchHelper.LEFT){
                    btnCalcular.setVisibility(lEsquemas.size() > 0?View.VISIBLE:View.INVISIBLE);
                    dataAdapter.removeItem(position);
                    administradora.eliminarEsquema(esquema);
                    lEsquemas.remove(esquema);
                }
                else
                {
                    ModificarEsquema(esquema,position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;

                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvEsquemas);
    }

    public void ModificarEsquema(ArrayList<String> esquemaAModificar,int index)
    {
        Intent i = new Intent(getActivity(),ModificarEsquema.class);
        Esquemas esquema = administradora.darEsquema();
        i.putExtra(ModificarEsquema.ESQUEMA,esquema);
        i.putExtra(ModificarEsquema.INDEX,index);
        i.putExtra(ModificarEsquema.ATRIBUTOS,administradora.darListadoAtributos());
        i.putExtra(ModificarEsquema.ESQUEMA_A_MODIFICAR,esquemaAModificar);
        startActivityForResult(i,101);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
            update();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1)
        {
            if(data.hasExtra(AgregarEsquemas.ESQUEMA_RESULTADO) && lEsquemas!=null)
            {
                ArrayList<String> esquemaResultado = data.getStringArrayListExtra(AgregarEsquemas.ESQUEMA_RESULTADO);
                lEsquemas.add(esquemaResultado); //TODO LINEA QUE SE PUEDE ELIMINAR
                administradora.agregarEsquema(esquemaResultado);
                dataAdapter.addItem(esquemaResultado);
            }
        }
        if(resultCode == 2)
        {
            if(data.hasExtra(ModificarEsquema.ESQUEMA_RESULTADO) && lEsquemas!=null)
            {
                ArrayList<String> esquemaResultado = data.getStringArrayListExtra(ModificarEsquema.ESQUEMA_RESULTADO);
                int posicion = data.getIntExtra(ModificarEsquema.INDEX,-1);
                if(posicion>=0){
                    lEsquemas.remove(posicion);                //TODO ELIMINAR
                    lEsquemas.add( posicion,esquemaResultado );//TODO ELIMINAR
                    administradora.modificarEsquema(administradora.darEsquemas().get(posicion),esquemaResultado);
                    dataAdapter.updateItem(posicion,esquemaResultado);
                }
            }
        }
        if(lEsquemas!=null) //TODO MODIFICAR
            dataAdapter.updateDataSource(lEsquemas);
    }
}
