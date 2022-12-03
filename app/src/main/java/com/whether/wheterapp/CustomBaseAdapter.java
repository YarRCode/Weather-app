package com.whether.wheterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    String listStr[];
    int listTemp[];
    String listWhether[];
    LayoutInflater inflater;

    public CustomBaseAdapter(Context cnx,String[] str, int[] temp, String[] whether){
        this.context = cnx;
        this.listStr = str;
        this.listTemp = temp;
        this.listWhether = whether;
        inflater = LayoutInflater.from(cnx);
    }
    @Override
    public int getCount() {
        return listStr.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_custom_list_view, null);

        TextView textView = (TextView) view.findViewById(R.id.linearText);
        TextView tempView = (TextView) view.findViewById(R.id.tempLinearText);
        TextView whetherView = (TextView) view.findViewById(R.id.whetherLinearText);

        String s = Integer.toString(listTemp[i]);
        s += "°C";

        textView.setText(listStr[i]);
        tempView.setText(s);
        whetherView.setText(listWhether[i]);

        return view;
    }
}
