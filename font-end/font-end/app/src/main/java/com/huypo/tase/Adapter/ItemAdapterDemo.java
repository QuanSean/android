package com.huypo.tase.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huypo.tase.Model.Item;
import com.huypo.tase.R;

import java.util.List;

public class ItemAdapterDemo extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<Item> myItems;

    public ItemAdapterDemo (Context context,int  layout,List<Item> items)
    {
        this.myContext=context;
        this.myLayout=layout;
        this.myItems=items;

    }


    @Override
    public int getCount() {
        return myItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(myLayout,null);

        TextView textTitle = (TextView) convertView.findViewById(R.id.txtNameItemTask);

        textTitle.setText(myItems.get(position).getTitle());

        return null;
    }
}
