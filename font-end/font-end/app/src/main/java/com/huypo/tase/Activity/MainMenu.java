package com.huypo.tase.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huypo.tase.Adapter.TableAdapter;
import com.huypo.tase.Model.Item;
import com.huypo.tase.Model.ListDetails;
import com.huypo.tase.Model.PersonalTable;
import com.huypo.tase.Model.Project;
import com.huypo.tase.Model.Respone;
import com.huypo.tase.Model.Task;
import com.huypo.tase.Model.User;
import com.huypo.tase.R;
import com.huypo.tase.Retrofit.IMyService;
import com.huypo.tase.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainMenu extends AppCompatActivity {

     ListView listView;
     ArrayList<PersonalTable> personalTables;



     TextView TableName;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    CompositeDisposable compositeDisposable1 = new CompositeDisposable();

    IMyService iMyService;
    ArrayAdapter arrayAdapter;
    ListView list;
    ArrayList<Project> projectArrayList = new ArrayList<>();
    private String token="";
    private String idPr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        listView = findViewById(R.id.listPersonalTable);
        personalTables = ListDetails.getlist();
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        PersonalTable personalTable= new PersonalTable("Demo"," The technical term for this physical arrangement is codex (in the plural, codices)", calendar.getTime());
        PersonalTable personalTable1= new PersonalTable( "Demo1"," The technical term for this physical arrangement is codex (in the plural, codices)", calendar.getTime());

//        int imageTable, String txtTableName, String txtTableDescription, Date txtTableDeadline
        ArrayList<PersonalTable> personalTables1 = new ArrayList<>();

//        personalTables1.add(personalTable);
//        personalTables1.add(personalTable1);

        TableName = findViewById(R.id.txtTableName);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("User");
        User user = (User) bundle.getSerializable("Info");
        token=user.getToken();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PersonalTable p=personalTables1.get(i);
                Toast.makeText(MainMenu.this,"Chào mừng "+ p.getIdProject(), Toast.LENGTH_SHORT).show();
//                compositeDisposable1.add( iMyService.deleteProject(token, p.getIdProject())
//                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
//                                new Consumer<String>() {
//                                    @Override
//                                    public void accept(String reponse) throws Exception
//                                    {
//
//                                        try{
//
//
//
//                                        }
//                                        catch (Exception e){
//
//                                        }
//                                    }
//                                }
//                        ));


            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int i, long id) {
                PersonalTable p=personalTables1.get(i);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:



                                compositeDisposable.add( iMyService.deleteProject(token,p.getIdProject()
                                )
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
                builder.setMessage("Bạn có muốn xoá dự án "+p.getTitle()).setPositiveButton("Đồng ý", dialogClickListener)
                        .setNegativeButton("Không", dialogClickListener).show();

                return true;
            }
        });



        compositeDisposable.add(iMyService.getProject(user.getToken())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(String reponse) throws Exception {

                                JSONObject jsonObject = new JSONObject(reponse);

                                JSONArray jsonArray = jsonObject.getJSONArray("detail");
                                Project project = new Project();
                                JSONObject demo = new JSONObject();
                                ArrayList<Task> tasks = new ArrayList<>();
                                ArrayList<Item> arrayItem= new ArrayList<>();

                                int aa=0;
                                if (jsonArray.length()==0)
                                {
                                    TextView a=(TextView) findViewById(R.id.tb);
                                    a.setText("Hãy bắt đầu trải nghiệm cùng với TASE, bằng việc tạo project đầu tiên");
                                }
                                else
                                {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        try {
                                            JSONObject oneObject = jsonArray.getJSONObject(i);
                                            demo = jsonArray.getJSONObject(0);
//
//                                            // Pulling items from the array
                                            Boolean deleted = new Boolean(oneObject.getString("deleted"));
                                            if (deleted==true)
                                            {

                                            }
                                            else
                                            {
                                                String _id = oneObject.getString("_id");
                                                String _idUser = oneObject.getString("_idUser");
                                                String title = oneObject.getString("title");
                                                String description = oneObject.getString("description");

//
                                                Boolean done = new Boolean(oneObject.getString("done"));
                                                Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(oneObject.getString("deadline"));

                                                JSONArray arrayTask=new JSONArray();

                                                arrayTask = oneObject.getJSONArray("task");
//
                                                if (arrayTask.length()==0)
                                                {

                                                }
                                                else
                                                {
                                                    for (int j=0;j<arrayTask.length();j++)
                                                    {

//                                                demo = arrayTask.getJSONObject(1);

                                                        JSONObject taskObject = arrayTask.getJSONObject(j);
                                                        Boolean deletedTask = new Boolean(taskObject.getString("delete"));

                                                        if (deletedTask==false)
                                                        {
                                                            String _idTask = taskObject.getString("_id");
                                                            String titleTask = taskObject.getString("title");
                                                            Date deadlineTask = new SimpleDateFormat("yyyy-MM-dd").parse(taskObject.getString("deadline"));
//                                                    Date deadlineTask = new SimpleDateFormat("yyyy-MM-dd").parse("2019-01-01");

                                                            Boolean doneTask = new Boolean(taskObject.getString("done"));
                                                            Item item= new Item(1,"A",true,false);
                                                            arrayItem.add(item);

                                                            Task task= new Task(_idTask,titleTask,deadlineTask,doneTask,false,arrayItem);
                                                            tasks.add(task);
                                                        }
                                                        else {

                                                        }


                                                    }
                                                }
                                                Project project1 = new Project(_id, _idUser, title, description, done, deadline, deleted, tasks);
                                                projectArrayList.add(project1);

                                            }


//






                                        } catch (JSONException e) {

                                            Toast.makeText(MainMenu.this,"Err", Toast.LENGTH_SHORT).show();


                                        }
                                    }



                                    for (Project p:projectArrayList)
                                    {
                                        personalTables1.add(new PersonalTable( p.getTitle(),p.getDescription(),p.getDeadline(),user.getToken(),p.get_id(),p.getTitle()));
                                    }
                                    TableAdapter tableAdapter;


                                    tableAdapter = new TableAdapter(MainMenu.this, personalTables1);

                                    listView.setAdapter(tableAdapter);
//
                                }


                            }
                        }
                ));
    }

    public void btnNewTable(View view){
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainMenu.this);
        View viewtable = getLayoutInflater().inflate(R.layout.table_dialog,null);

//        final EditText editTextTable = (EditText)viewtable.findViewById(R.id.editTableName);
//        Button btn_cancel = (Button)viewtable.findViewById(R.id.btnCancel);
        Button btn_create = (Button)viewtable.findViewById(R.id.btnNewTable);
        final EditText editProjectName = (EditText) viewtable.findViewById(R.id.editProjectName);
        final EditText editTableDescription = (EditText) viewtable.findViewById(R.id.editTableDescription);
        final EditText editProjectDeadline = (EditText) viewtable.findViewById(R.id.editProjectDeadline);




        alert.setView(viewtable);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);



        btn_create.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if (editProjectName.getText().toString().matches("")||editTableDescription.getText().toString().matches("")||editProjectDeadline.getText().toString().matches(""))
                {
                    Toast.makeText(MainMenu.this,"Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    if (isValidFormat("dd/MM/yyyy", editProjectDeadline.getText().toString(), Locale.ENGLISH)==false)
                    {
                        Toast.makeText(MainMenu.this,"Vui lòng nhập deadline đúng định dạng dd/MM/yyyy", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        compositeDisposable.add( iMyService.createProject(token,editProjectName.getText().toString(),editTableDescription.getText().toString(),editProjectDeadline.getText().toString())
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

                    }
                }
//                Toast.makeText(MainMenu.this,editProjectName.getText().toString(), Toast.LENGTH_SHORT).show();


//                isValidFormat("dd/MM/yyyy", "20130925", Locale.ENGLISH);
//                Toast.makeText(MainMenu.this,""+isValidFormat("dd/MM/yyyy", "01/01/2019", Locale.ENGLISH), Toast.LENGTH_SHORT).show();


            }
        });
        alert.show();
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
