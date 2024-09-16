package com.huypo.tase.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huypo.tase.Adapter.ItemProjectPartner;
import com.huypo.tase.Model.PersonalTable;
import com.huypo.tase.Model.Project_Partner;
import com.huypo.tase.R;
import com.huypo.tase.Retrofit.IMyService;
import com.huypo.tase.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class project_partner extends AppCompatActivity {
    ListView l;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    String token="";
    Date date1 = null;
    ArrayList<Project_Partner> project_partnerss;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_partner);
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        project_partnerss= new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        l =(ListView) findViewById(R.id.listProjectPartner);



        try {
            date1=new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Project_Partner p = project_partnerss.get(i);


                final AlertDialog.Builder alert = new AlertDialog.Builder(project_partner.this);
                View viewtable = getLayoutInflater().inflate(R.layout.project_partner_dialog,null);
                alert.setView(viewtable);
                TextView txtTitle = (TextView) viewtable.findViewById(R.id.txtTitle);
                Button button= (Button) viewtable.findViewById(R.id.btnAddPartner);

                button.setBackgroundColor(Color.TRANSPARENT);
                Button btnDeletePr = (Button)viewtable.findViewById(R.id.btnDeletePr);
                Button btnShowProject = (Button)viewtable.findViewById(R.id.btnShowProject);

                btnShowProject.setOnClickListener(new View.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        Date d= new Date();
                        PersonalTable personalTable = new PersonalTable("","",d,token,p.getIdProject(),"",false);
                        Intent intent = new Intent(project_partner.this, DashBoard.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("Info", personalTable);
                        intent.putExtra("Project", bundle);
                        startActivity(intent);

//                        String txtTableName, String txtTableDescription, Date txtTableDeadline, String token, String idProject, String title, Boolean done
//                        Toast.makeText(project_partner.this, p.get_id(), Toast.LENGTH_SHORT).show();

                    }

                });


                txtTitle.setText(p.getTitle());
                btnDeletePr.setOnClickListener(new View.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(project_partner.this, p.get_id(), Toast.LENGTH_SHORT).show();

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        compositeDisposable.add( iMyService.deleteProjectPartner(token,p.get_id())
                                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                                        new Consumer<String>() {
                                                            @Override
                                                            public void accept(String reponse) throws Exception
                                                            {


                                                            }
                                                        }
                                                ));
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(project_partner.this);
                        builder.setMessage("Bạn có muốn xoá dự án "+p.getTitle()).setPositiveButton("Đồng ý", dialogClickListener)
                                .setNegativeButton("Không", dialogClickListener).show();
                    }

                });





                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);

                alert.show();


            }
        });


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null)
        {
             token =(String) b.get("token");

        }



//        project_partners.add(new Project_Partner("aaa","aaaa","aaaaa","aaa","sekf",true, date1 ,true));



        String finalToken = token;
        compositeDisposable.add( iMyService.getProjectPartner(token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(String reponse) throws Exception
                            {
                                JSONObject jsonObject= new JSONObject(reponse);
                                JSONArray jsonArray = jsonObject.getJSONArray("detail");
                                if (jsonArray.length()==0)
                                {
                                    TextView empty= (TextView) findViewById(R.id.empty);
                                    empty.setText("Bạn chưa có project nào");

                                }
                                else

                                {
                                    for (int i=0 ; i<jsonArray.length();i++)
                                    {
                                        JSONObject itemProject= new JSONObject(jsonArray.get(i).toString());
                                        Boolean deleted = new Boolean(itemProject.getString("deleted"));
                                        if (deleted==true)
                                        {

                                        }
                                        else
                                        {
                                            String title=itemProject.getString("title");
                                            String idProject= itemProject.getString("id");
                                            String _id= itemProject.getString("_id");

                                            String author = itemProject.getString("author");
                                            String description=itemProject.getString("description");
                                            Boolean done = new Boolean(itemProject.getString("done"));
                                            Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(itemProject.getString("deadline"));
                                            Project_Partner ppp=new Project_Partner(token,_id,author,title,description,done, deadline ,deleted,idProject);
                                            project_partnerss.add(ppp);
                                            Toast.makeText(project_partner.this, ppp.get_id(), Toast.LENGTH_SHORT).show();

                                        }


                                    }
                                    ItemProjectPartner adapter= new ItemProjectPartner(
                                            project_partner.this,

                                            project_partnerss
                                    );
                                    l.setAdapter(adapter);

                                }

                            }
                        }
                ));
//
//        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(project_partner.this,"Chào mừng ", Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }

    public void backPage(View view){
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isValidFormat(String format, String value, Locale locale) {
        LocalDateTime ldt = null;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, fomatter);
            String result = ldt.format(fomatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, fomatter);
                String result = ld.format(fomatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, fomatter);
                    String result = lt.format(fomatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    //e2.printStackTrace();
                }
            }
        }

        return false;
    }


}
