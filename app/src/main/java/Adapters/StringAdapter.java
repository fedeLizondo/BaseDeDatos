package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fedelizondo.basededatos.R;

/**
 * Created by federicolizondo on 15/02/17.
 */

public class StringAdapter extends ArrayAdapter<String> {

    View.OnTouchListener mTouchListener;

    public StringAdapter(Context context, ArrayList<String> resource, View.OnTouchListener listener) {
        super(context, android.R.layout.simple_list_item_1  , resource);
        mTouchListener = listener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(android.R.layout.simple_list_item_1,parent,false);
        TextView b = (TextView) v.findViewById( android.R.id.text1 );
        b.setText(getItem(position));
        v.setOnTouchListener(mTouchListener);
        return v;//super.getView(position, convertView, parent);
    }
}
