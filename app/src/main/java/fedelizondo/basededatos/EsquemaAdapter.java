package fedelizondo.basededatos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by federicolizondo on 21/02/17.
 */

public class EsquemaAdapter extends RecyclerView.Adapter<EsquemaAdapter.ViewHolder> {
    private ArrayList<ArrayList<String>> ListaDatos;

    public EsquemaAdapter(ArrayList<ArrayList<String>> ListadoDeStrings) {
        this.ListaDatos = ListadoDeStrings;
    }

    @Override
    public EsquemaAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new EsquemaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EsquemaAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_itemData.setText(ListaDatos.get(i).toString().replace('[',' ').replace(']',' '));
    }

    @Override
    public int getItemCount() {
        return ListaDatos.size();
    }

    public void addItem(ArrayList<String> itemString) {
        if(!ListaDatos.contains(itemString))
        {
            ListaDatos.add(itemString);
            notifyItemInserted(ListaDatos.size());
        }
    }

    public void removeItem(int position) {

        ListaDatos.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, ListaDatos.size());
    }

    public void updateItem( int positionAtributoViejo ,ArrayList<String> nuevo)
    {
        ListaDatos.remove(positionAtributoViejo);
        ListaDatos.add(positionAtributoViejo,nuevo);
        notifyItemChanged(positionAtributoViejo);
    }

    public void updateDataSource(ArrayList<ArrayList<String>> nuevoListadoDeStrings)
    {
        ListaDatos = nuevoListadoDeStrings;
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
