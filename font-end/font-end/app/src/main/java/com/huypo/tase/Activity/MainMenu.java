package com.huypo.tase.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huypo.tase.Adapter.TableAdapter;
import com.huypo.tase.Model.Item;
import com.huypo.tase.Model.ListDetails;
import com.huypo.tase.Model.PersonalTable;
import com.huypo.tase.Model.Project;
import com.huypo.tase.Model.Task;
import com.huypo.tase.Model.User;
import com.huypo.tase.R;
import com.huypo.tase.Retrofit.IMyService;
import com.huypo.tase.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
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
    IMyService iMyService;
    ArrayAdapter arrayAdapter;
    ListView list;
    ArrayList<Project> projectArrayList = new ArrayList<>();
    private String token="";
    private String idPr="";
    int click=0;
    User u;
    String emailUser="";
    String nameUser="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        listView = findViewById(R.id.listPersonalTable);
        personalTables = ListDetails.getlist();
        Date date = new Date();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


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
        u=user;
        nameUser=user.getName();
        emailUser=user.getEmail();
        token=user.getToken();
        Toast.makeText(MainMenu.this,nameUser + " " + emailUser, Toast.LENGTH_SHORT).show();



        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                click=click+1;
                PersonalTable p=personalTables1.get(i);
                Handler handler= new Handler();
                handler.postDelayed(new Runnable() {
                    @Override

                    public void run() {
                        if (click==1)
                        {


                            final AlertDialog.Builder alert = new AlertDialog.Builder(MainMenu.this);
                            View viewtable = getLayoutInflater().inflate(R.layout.project_dialog,null);
                            alert.setView(viewtable);


                            final AlertDialog alertDialog = alert.create();
                            alertDialog.setCanceledOnTouchOutside(false);


                             TextView txtTitle = (TextView) viewtable.findViewById(R.id.txtTitle);
                            Button btnShowProject = (Button)viewtable.findViewById(R.id.btnShowProject);
                            Button btnUpdateStatusDone = (Button)viewtable.findViewById(R.id.btnUpdateStatusDone);
                            Button btnDeletePr = (Button)viewtable.findViewById(R.id.btnDeletePr);
                            Button btnAddPartner = (Button)viewtable.findViewById(R.id.btnAddPartner);

                            btnShowProject.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(MainMenu.this, DashBoard.class);
                                    Bundle bundle= new Bundle();
                                    bundle.putSerializable("Info", p);
                                    intent.putExtra("Project", bundle);
                                    startActivity(intent);

                                }


                            });
                            btnAddPartner.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainMenu.this);
                                    View viewtable = getLayoutInflater().inflate(R.layout.email_partner_dialog,null);


                                    Button btn_create = (Button)viewtable.findViewById(R.id.btnEmailPartner);
                                    final EditText editEmailPartner = (EditText) viewtable.findViewById(R.id.editEmailPartner);
                                    alert.setView(viewtable);

                                    final AlertDialog alertDialog = alert.create();
                                    alertDialog.setCanceledOnTouchOutside(false);

                                    btn_create.setOnClickListener(new View.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void onClick(View v) {

                                            if (editEmailPartner.getText().toString().matches(""))
                                            {
                                                Toast.makeText(MainMenu.this,"Vui lòng nhập email", Toast.LENGTH_SHORT).show();

                                            }
                                            else
                                            {
                                                    compositeDisposable.add( iMyService.addEmailPartner(token,p.getIdProject(),editEmailPartner.getText().toString())
                                                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                                                    new Consumer<String>() {
                                                                        @Override
                                                                        public void accept(String reponse) throws Exception
                                                                        {

                                                                            JSONObject jsonObject= new JSONObject(reponse);
                                                                            try {
                                                                                JSONObject jsonObject1= new JSONObject(jsonObject.getString("detail"));
                                                                                Toast.makeText(MainMenu.this,"Đã thêm thành công", Toast.LENGTH_SHORT).show();

                                                                                Intent intent = getIntent();
                                                                                finish();
                                                                                startActivity(intent);
                                                                                compositeDisposable.add( iMyService.sendEmailProjectPartner(editEmailPartner.getText().toString(),p.getTitle(),nameUser
                                                                                )
                                                                                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                                                                                new Consumer<String>() {
                                                                                                    @Override
                                                                                                    public void accept(String reponse) throws Exception
                                                                                                    {


                                                                                                    }
                                                                                                }
                                                                                        ));





                                                                            }
                                                                            catch (Exception e)
                                                                            {
                                                                                Toast.makeText(MainMenu.this,"Email không tồn tại", Toast.LENGTH_SHORT).show();


                                                                            }

                                                                        }
                                                                    }
                                                            ));

                                            }



                                        }
                                    });
                                    alert.show();

                                }

                            });
                            btnDeletePr.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onClick(View v) {

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
                                }

                            });
                            btnUpdateStatusDone.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onClick(View v) {

                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which){
                                                case DialogInterface.BUTTON_POSITIVE:

                                                    compositeDisposable.add( iMyService.changeStatusDoneProject(token,p.getIdProject())
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
                                    builder.setMessage("Bạn đã hoàn thành dự án "+p.getTitle()).setPositiveButton("Đồng ý", dialogClickListener)
                                            .setNegativeButton("Không", dialogClickListener).show();

                                }


                            });


                            txtTitle.setText(p.getTitle());

                            alert.show();



                        }
                        if (click==2)
                        {

                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:

                                            compositeDisposable.add( iMyService.changeStatusDoneProject(token,p.getIdProject())
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
                            builder.setMessage("Bạn đã hoàn thành dự án "+p.getTitle()).setPositiveButton("Đồng ý", dialogClickListener)
                                    .setNegativeButton("Không", dialogClickListener).show();

                        }
                        click=0;

                    }
                }, 500);
            }
            public void onItemDoubleClick(AdapterView<?> adapterView, View view, int i, long l) {


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
                                    a.setText("Chào mừng bạn đến với T. Hãy bắt đầu trải nghiệm cùng với TASE bằng việc tạo project đầu tiên");
                                }
                                else
                                {
                                    int count=0;
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
                                                count++;
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
                                                            Integer _idTask = taskObject.getInt("_id");
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
                                        } catch (JSONException e) {

                                            Toast.makeText(MainMenu.this,"Err", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    if (count==0)
                                    {
                                        TextView a=(TextView) findViewById(R.id.tb);
                                        a.setText("Bạn đã không còn project nào, hãy lên kế hoạch bằng việc tạo project mới nhé !");
                                    }
                                    for (Project p:projectArrayList)
                                    {
                                        personalTables1.add(new PersonalTable( p.getTitle(),p.getDescription(),p.getDeadline(),user.getToken(),p.get_id(),p.getTitle(),p.getDone()));
                                    }
                                    TableAdapter tableAdapter;

                                    listView.setAdapter( new TableAdapter(MainMenu.this, personalTables1){
                                        @Override
                                        public View getView(int position, View convertView, ViewGroup parent) {
                                            View row = super.getView(position, convertView, parent);

                                            Calendar  cal = Calendar.getInstance();
                                            int yearNow= cal.get(Calendar.YEAR);
                                            int monthNow= cal.get(Calendar.MONTH);
                                            int dayNow= cal.get(Calendar.DAY_OF_MONTH);

                                            PersonalTable personalTable2= personalTables1.get(position);
                                            Date deadline=personalTable2.getTxtTableDeadline();
                                            Calendar cal1= Calendar.getInstance();
                                            cal1.setTime(deadline);
                                            int year= cal1.get(Calendar.YEAR);
                                            int month= cal1.get(Calendar.MONTH);
                                            int day= cal1.get(Calendar.DAY_OF_MONTH);


                                            if (personalTable2.getDone()==true)
                                            {
                                                row.setBackgroundColor (0x989898);
                                                TextView textView=(TextView) row.findViewById(R.id.txtTableName);
                                                TextView textView1=(TextView) row.findViewById(R.id.txtTableDescription);
                                                TextView textView2=(TextView) row.findViewById(R.id.txtTableDeadline);
                                                textView.setTextColor(Color.BLACK);
                                                textView1.setTextColor(Color.WHITE);
                                                textView2.setTextColor(Color.WHITE);
                                            }
                                            else
                                            {
                                                if (year==yearNow&&month==monthNow&&day-1<=dayNow)
                                                {
                                                    Toast.makeText(MainMenu.this,"true", Toast.LENGTH_SHORT).show();
                                                    row.setBackgroundColor (Color.rgb(204,0,102));
                                                    TextView textView=(TextView) row.findViewById(R.id.txtTableName);
                                                    TextView textView1=(TextView) row.findViewById(R.id.txtTableDescription);
                                                    TextView textView2=(TextView) row.findViewById(R.id.txtTableDeadline);
                                                    textView.setTextColor(Color.BLACK);
                                                    textView1.setTextColor(Color.WHITE);
                                                    textView2.setTextColor(Color.WHITE);
                                                }
                                                else
                                                {

                                                    row.setBackgroundColor (Color.rgb(237, 237, 237)); // default coloe

                                                }
                                            }

                                            return row;
                                        }
                                    });
//
                                }


                            }
                        }
                ));

    }
    public void btnDeleteProject(View view){

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_partner:
                    Intent intent= new Intent(MainMenu.this, project_partner.class);

                    intent.putExtra("token", token);
                    startActivity(intent);
                    return true;

                case R.id.navigation_notification:
                    Intent intent1= new Intent(MainMenu.this, MainMenu.class);
                    Bundle bundle= new Bundle();

                    bundle.putSerializable("Info",  u);
                    intent1.putExtra("User", bundle);
                    startActivity(intent1);
                    finish();

            }
            return false;
        }
    };
    public void chooseDeadline (EditText editText)
    {
        final Calendar calendar= Calendar.getInstance();
        int d = calendar.get(Calendar.DATE);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                editText.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },y,m,d);
        datePickerDialog.show();

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

        editProjectDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDeadline(editProjectDeadline);
            }
        });




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
                        String deadl="";
                        try {
                            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(editProjectDeadline.getText().toString());
                            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                            deadl=dateFormat.format(date1);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        compositeDisposable.add( iMyService.createProject(token,editProjectName.getText().toString(),editTableDescription.getText().toString(),deadl)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_navigation, menu);

        return true;
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//
//        if (item.getItemId()==R.id.navigation_home){
//            Toast.makeText(this, "Action Item", Toast.LENGTH_SHORT).show();
//        }
//
//        if (item.getItemId()==R.id.navigation_notification){
//            Toast.makeText(this, "Hnstructions Item", Toast.LENGTH_SHORT).show();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
