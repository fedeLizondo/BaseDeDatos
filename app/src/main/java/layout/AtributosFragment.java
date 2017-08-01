package layout;

import android.content.Context;
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
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;

import LogicaNegocio.Administradora;
import fedelizondo.basededatos.DataAdapter;
import fedelizondo.basededatos.R;


public class AtributosFragment extends Fragment implements
        AgregarAtributosFragment.OnFragmentInteractionListener,
        ModificarAtributosFragment.OnFragmentInteractionListenerFragmentModificarAtributo {

    public static final String LISTADO_ATRIBUTOS = "listadoAtributos";
    public static String TAG ="ATRIBUTOS";

    private ArrayList<String> listaAtributos;

    //private ListView listViewAtributos;
    private RecyclerView recyclerViewAtributos;
    private DataAdapter dataAdapter;

    private View view;
    private Paint p = new Paint();

    private OnFragmentInteractionListener mListener;
    private ModificarAtributosFragment.OnFragmentInteractionListenerFragmentModificarAtributo listenerFragmentModificarAtributo;



    public AtributosFragment() {
        // Required empty public constructor
    }

    public static AtributosFragment newInstance(ArrayList<String> listadoAtributos) {
        AtributosFragment fragment = new AtributosFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(LISTADO_ATRIBUTOS, listadoAtributos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listaAtributos = getArguments().getStringArrayList(LISTADO_ATRIBUTOS);
        }

        if( recyclerViewAtributos != null ) {
            dataAdapter = new DataAdapter(listaAtributos);
            recyclerViewAtributos.setAdapter(dataAdapter);
            dataAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_atributos, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListenerFragmentModificarAtributo");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        if( recyclerViewAtributos != null ) {
            listaAtributos = Administradora.getInstance().darListadoAtributos();
            dataAdapter = new DataAdapter(listaAtributos);
            recyclerViewAtributos.setAdapter(dataAdapter);
            dataAdapter.notifyDataSetChanged();
        }

        super.onResume();
    }




    public void AgregarAtributos()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        AgregarAtributosFragment dialog = new AgregarAtributosFragment();

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(AgregarAtributosFragment.ARG_PARAM1,listaAtributos);

        dialog.setArguments(bundle);
        dialog.setTargetFragment(AtributosFragment.this,1235);
        dialog.show(fragmentManager,"AgregarAtributo");
    }

    public void ModificarAtributos(String atributo)
    {
        FragmentManager fragmentManager =  this.getChildFragmentManager();  //getActivity().getSupportFragmentManager();
        ModificarAtributosFragment dialog = new ModificarAtributosFragment();

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ModificarAtributosFragment.LISTADO_ATRIBUTOS,listaAtributos);
        bundle.putString(ModificarAtributosFragment.ATRIBUTO_A_MODIFICAR,atributo);

        dialog.setArguments(bundle);
        dialog.setTargetFragment(AtributosFragment.this,1234);
        dialog.show(fragmentManager,"ModificarAtributo");
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteractionAgregarAtributos(ArrayList<String> listadoAtributo);
        void onFragmentInteractionModificarAtributos(String AtributoAnterior,String AtributoNuevo);
        void onFragmentInteractionEliminarAtributos(String AtributoAEliminar);
    }


    @Override
    public void OnFragmentInteractionListenerFragmentModificarAtributo(String AtributoAnterior, String AtributoModificado) {
        int index = listaAtributos.indexOf(AtributoAnterior);
        listaAtributos.remove(index);
        listaAtributos.add(index,AtributoModificado);

        dataAdapter.updateItem(index,AtributoModificado);

        if(mListener != null)
        {
            mListener.onFragmentInteractionModificarAtributos(AtributoAnterior,AtributoModificado);
        }
    }

    @Override
    public void OnFragmentInteractionListenerCancelModificarAtributo() {
        dataAdapter.updateDataSource(listaAtributos);
    }

    @Override
    public void onFragmentInteraction(ArrayList<String> listadoAtributo) {

        if(mListener !=null)
            mListener.onFragmentInteractionAgregarAtributos(listadoAtributo);

        for (String string : listadoAtributo) {
                dataAdapter.addItem(string);
            }
    }

    private void initViews(View view)
    {
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAgregarAtributo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarAtributos();
            }
        });

        ScaleAnimation animation = new ScaleAnimation(0,1,0,1);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        animation.setDuration(500);
        animation.setInterpolator(new OvershootInterpolator());
        fab.startAnimation(animation);

        recyclerViewAtributos = (RecyclerView) view.findViewById(R.id.rv_Atributos);
        recyclerViewAtributos.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity().getApplicationContext());
        recyclerViewAtributos.setLayoutManager(layoutManager);
        dataAdapter = new DataAdapter(listaAtributos);
        recyclerViewAtributos.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        initSwipe();
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
                String atributo = listaAtributos.get(position);

                if (direction == ItemTouchHelper.LEFT){
                    dataAdapter.removeItem(position);
                    listaAtributos.remove(atributo);

                    final Snackbar snack = Snackbar.make(getView(), "My Placeholder Text", Snackbar.LENGTH_SHORT);

                    snack.setAction("Deshacer", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listaAtributos = Administradora.getInstance().darListadoAtributos();
                            dataAdapter = new DataAdapter(listaAtributos);
                            recyclerViewAtributos.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                        }
                    });

                    snack.addCallback(new Snackbar.Callback() {

                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                // Snackbar closed on its own
                                if (mListener != null) {
                                    ArrayList<String> listaEliminar = Administradora.getInstance().darListadoAtributos();
                                    listaEliminar.removeAll(listaAtributos);
                                    for (String Atributo : listaEliminar)
                                        mListener.onFragmentInteractionEliminarAtributos( Atributo );
                                }
                            }
                        }

                        @Override
                        public void onShown(Snackbar snackbar) {
                            ArrayList<String> listaEliminar = Administradora.getInstance().darListadoAtributos();
                            listaEliminar.removeAll(listaAtributos);

                            String mensaje = (listaEliminar.size()>1)?
                                    String.format(getContext().getString(R.string.informarAtributosEliminar),listaEliminar.size()):
                                    getContext().getString(R.string.informarAtributoEliminar);


                            snack.setText( mensaje );
                        }
                    });
                    snack.show();


                }
                else
                {
                    ModificarAtributos(atributo);
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
        itemTouchHelper.attachToRecyclerView(recyclerViewAtributos);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser)
        {
            listaAtributos = Administradora.getInstance().darListadoAtributos();
            if(dataAdapter != null)
            {
                dataAdapter = new DataAdapter(listaAtributos);
                recyclerViewAtributos.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
