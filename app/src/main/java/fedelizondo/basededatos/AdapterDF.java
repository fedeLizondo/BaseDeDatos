package fedelizondo.basededatos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import LogicaNegocio.DependenciaFuncional;

/**
 * Created by federicolizondo on 19/02/17.
 */

public class AdapterDF extends RecyclerView.Adapter<AdapterDF.ViewHolder> {
    private ArrayList<DependenciaFuncional> ListaDatos;

    public AdapterDF( ArrayList<DependenciaFuncional> listaDF ) {
        if(listaDF != null)
            this.ListaDatos = listaDF;
        else
            this.ListaDatos = new ArrayList<>();
    }

    @Override
    public AdapterDF.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new AdapterDF.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterDF.ViewHolder viewHolder, int i) {

        viewHolder.tv_itemData.setText((CharSequence) ListaDatos.get(i).toString().replace('[',' ').replace(']',' '));
    }

    @Override
    public int getItemCount() {
        return ListaDatos.size();
    }

    public void addItem(DependenciaFuncional dependenciaFuncional) {
        ListaDatos.add(dependenciaFuncional);
        notifyItemInserted(ListaDatos.size());
    }

    public void removeItem(int position) {

        ListaDatos.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, ListaDatos.size());
    }

    public void updateItem( int positionDF ,DependenciaFuncional nuevo)
    {
        ListaDatos.remove(positionDF);
        ListaDatos.add(positionDF,nuevo);
        notifyItemChanged(positionDF);
    }

    public void updateDataSource(ArrayList<DependenciaFuncional> nuevoListadoDeDF)
    {
        ListaDatos = nuevoListadoDeDF;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_itemData;
        public ViewHolder(View view) {
            super(view);
            tv_itemData = (TextView)view.findViewById(R.id.tv_itemStringData);
        }
    }

}

