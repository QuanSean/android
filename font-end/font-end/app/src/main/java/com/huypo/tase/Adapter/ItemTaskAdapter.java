package com.huypo.tase.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huypo.tase.Model.Item;
import com.huypo.tase.R;
import java.util.ArrayList;

public class ItemTaskAdapter extends BaseAdapter {
     Context context;
     ArrayList<Item> itemTask;
    int layout;
    public ItemTaskAdapter(Context context,int layout, ArrayList<Item> itemTask) {
        this.context = context;
        this.layout=layout;
        this.itemTask = itemTask;
    }
    @Override
    public int getCount() {
        return itemTask.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Item> getTitle() {
        return itemTask;
    }

    public void setTitle(ArrayList<Item> title) {
        this.itemTask = title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if(convertView == null){
//            convertView = View.inflate(context , R.layout.item_tasks, null);
//        }
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =inflater.inflate( R.layout.item_tasks,null);

        TextView txtNameItemTask = convertView.findViewById(R.id.txtNameItemTask);

//        images.setImageResource(personalTable.getImageTable());
        txtNameItemTask.setText(itemTask.get(position).getTitle());

        return convertView;

    }
}
