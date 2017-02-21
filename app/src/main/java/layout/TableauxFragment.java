package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import LogicaNegocio.Administradora;
import fedelizondo.basededatos.MainActivity;
import fedelizondo.basededatos.R;


public class TableauxFragment extends Fragment {

    private Administradora administradora;

    private View view;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tableaux, container, false);
        initView(view);
        return view;
    }


    public void initView(View view)
    {

    }

    public void update()
    {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser)
            update();
        super.setUserVisibleHint(isVisibleToUser);
    }
}
