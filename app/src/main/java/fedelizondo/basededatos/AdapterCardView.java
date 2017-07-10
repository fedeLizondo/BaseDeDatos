package fedelizondo.basededatos;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by federicolizondo on 10/07/17.
 */

public class AdapterCardView extends RecyclerView.Adapter<AdapterCardView.PasosViewHolder> {

    public static class PasosViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nroPaso;
        TextView descripcionPaso;
        TextView resultadoPaso;

        PasosViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            nroPaso = (TextView)itemView.findViewById(R.id.tv_NroPaso);
            descripcionPaso = (TextView)itemView.findViewById(R.id.tv_DescripcionPaso);
            resultadoPaso = (TextView)itemView.findViewById(R.id.tv_ResultadoPaso);
        }
    }

    ArrayList<PasosCardView> pasos;

    public AdapterCardView(ArrayList<PasosCardView> pasos ){
        if(pasos == null)
            pasos = new ArrayList<>();
        this.pasos = pasos;
    }

    @Override
    public int getItemCount() {
        return pasos.size();
    }

    @Override
    public PasosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        PasosViewHolder pvh = new PasosViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PasosViewHolder personViewHolder, int i) {
        personViewHolder.nroPaso.setText(pasos.get(i).nroPaso);
        personViewHolder.descripcionPaso.setText(pasos.get(i).descripcionDelPaso);
        personViewHolder.resultadoPaso.setText(pasos.get(i).resultadoDelPaso);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
