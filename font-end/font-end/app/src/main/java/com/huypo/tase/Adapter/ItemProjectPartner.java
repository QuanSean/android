package com.huypo.tase.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huypo.tase.Model.Project_Partner;
import com.huypo.tase.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class ItemProjectPartner extends BaseAdapter {

    Context context;
    List<Project_Partner> project_partners;

    public ItemProjectPartner(Context context, List<Project_Partner> project_partners) {
        this.context = context;
        this.project_partners = project_partners;
    }

    @Override
    public int getCount() {
        return project_partners.size();
    }

    @Override
    public Object getItem(int position) {
        return project_partners.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context , R.layout.list_project_partner, null);
        }

//        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        convertView=inflater.inflate(layout,null);

        TextView titleItemProject =(TextView) convertView.findViewById(R.id.titleItemProject);
        TextView descriptionItemProject =(TextView) convertView.findViewById(R.id.descriptionItemProject);
        TextView deadlineItemProject =(TextView) convertView.findViewById(R.id.deadlineItemProject);
        TextView personName =(TextView) convertView.findViewById(R.id.personName);

        titleItemProject.setText(project_partners.get(position).getTitle());
        descriptionItemProject.setText(project_partners.get(position).getDescription());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyy");
        String format = formatter.format(project_partners.get(position).getDeadline());
        deadlineItemProject.setText(format);

        personName.setText("Author: "+project_partners.get(position).get_idUser());
        return convertView;
    }
}
