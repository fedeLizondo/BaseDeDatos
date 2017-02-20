package layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import LogicaNegocio.DependenciaFuncional;
import fedelizondo.basededatos.AdapterDF;

import fedelizondo.basededatos.AgregarDependenciaFuncional;
import fedelizondo.basededatos.MainActivity;
import fedelizondo.basededatos.ModificarDependeciaFuncional;
import fedelizondo.basededatos.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DependenciaFuncionalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DependenciaFuncionalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DependenciaFuncionalFragment extends Fragment {

    public static String TAG ="ATRIBUTOS";

    private RecyclerView recyclerViewAtributos;
    private AdapterDF dataAdapter;

    private View view;
    private Paint p = new Paint();

    private ArrayList<DependenciaFuncional> lDependenciasFuncional;
    private ArrayList<String> lAtributos;

    private OnFragmentInteractionListener mListener;
    private Administradora administradora;

    public DependenciaFuncionalFragment() {
        // Required empty public constructor
    }

    public static DependenciaFuncionalFragment newInstance() {
        DependenciaFuncionalFragment fragment = new DependenciaFuncionalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            lDependenciasFuncional = new ArrayList<>();
            lAtributos = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if( getActivity() instanceof MainActivity)
        {
            administradora = ((MainActivity) getActivity()).administradora;
            lDependenciasFuncional = administradora.darListadoDependenciasFuncional();
            lAtributos = administradora.darListadoAtributos();
        }
        view = inflater.inflate(R.layout.fragment_dependencia_funcional, container, false);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteractionDFAgregar( DependenciaFuncional dfAgregada );
        void onFragmentInteractionDFModificar(  DependenciaFuncional dfVieja , DependenciaFuncional dfNueva );
        void onFragmentInteractionDFEliminar( DependenciaFuncional dfEliminada );
    }


    public void AgregarDF()
    {
        if(administradora != null){ //!lAtributos.isEmpty()) {
            Intent intent = new Intent(getActivity(), AgregarDependenciaFuncional.class);
            intent.putStringArrayListExtra(AgregarDependenciaFuncional.LISTAATRIBUTOS, administradora.darListadoAtributos());
            startActivityForResult(intent,1);
        }
        else
        {
            String mensaje = getResources().getString(R.string.ErrorDependenciaFuncionalNoHayAtributos);
            Snackbar.make(view,mensaje,Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void ModificarDF(int index)
    {
        if(administradora != null){ //!lAtributos.isEmpty()) {
            Intent intent = new Intent(getActivity(), ModificarDependeciaFuncional.class);
            intent.putExtra(ModificarDependeciaFuncional.INDEXAMODIFICAR, index);
            startActivityForResult(intent,1);
        }
    }


    private void initViews(View view)
    {
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAgregarDependeciaFuncional);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarDF();
            }
        });

        ScaleAnimation animation = new ScaleAnimation(0,1,0,1);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        animation.setDuration(500);
        animation.setInterpolator(new OvershootInterpolator());
        fab.startAnimation(animation);

        recyclerViewAtributos = (RecyclerView) view.findViewById(R.id.rv_DependenciaFuncional);
        recyclerViewAtributos.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity().getApplicationContext());
        recyclerViewAtributos.setLayoutManager(layoutManager);
        dataAdapter = new AdapterDF( lDependenciasFuncional );
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
                lDependenciasFuncional = administradora.darListadoDependenciasFuncional();
                DependenciaFuncional df = lDependenciasFuncional.get(position);

                if (direction == ItemTouchHelper.LEFT){
                    dataAdapter.removeItem(position);
                    if (mListener != null) {
                        mListener.onFragmentInteractionDFEliminar( df );
                    }
                }
                else
                {
                    ModificarDF(position);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dataAdapter.updateDataSource(administradora.darListadoDependenciasFuncional());
    }
}
