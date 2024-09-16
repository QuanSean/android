package com.huypo.tase.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huypo.tase.Model.PersonalTable;
import com.huypo.tase.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TableAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PersonalTable> personalTables;

    public TableAdapter(Context context, ArrayList<PersonalTable> personalTables) {
        this.context = context;
        this.personalTables = personalTables;
    }

    @Override
    public int getCount() {
        return personalTables.size();
    }

    @Override
    public Object getItem(int position) {
        return personalTables.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context , R.layout.list_items, null);
        }

        ImageView images = convertView.findViewById(R.id.imageTable);
        TextView tablename = convertView.findViewById(R.id.txtTableName);
        TextView description = convertView.findViewById(R.id.txtTableDescription);
        TextView deadline = convertView.findViewById(R.id.txtTableDeadline);

        PersonalTable personalTable = personalTables.get(position);
//        images.setImageResource(personalTable.getImageTable());
        tablename.setText(personalTable.getTxtTableName());
        description.setText(personalTable.getTxtTableDescription());
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dl = formatter.format(personalTable.getTxtTableDeadline());


        deadline.setText("Deadline: "+ dl);

        return convertView;
    }
}
