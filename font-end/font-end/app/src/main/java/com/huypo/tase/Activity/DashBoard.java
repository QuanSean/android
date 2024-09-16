package com.huypo.tase.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huypo.tase.Adapter.CardItemString;
import com.huypo.tase.Adapter.CardPagerAdapterS;
import com.huypo.tase.Adapter.ItemTaskAdapter;
import com.huypo.tase.Model.Item;
import com.huypo.tase.Model.PersonalTable;
import com.huypo.tase.Model.Task;
import com.huypo.tase.R;
import com.huypo.tase.Retrofit.IMyService;
import com.huypo.tase.Retrofit.RetrofitClient;
import com.huypo.tase.Utils.ShadowTranformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Retrofit;


public class DashBoard extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    private ViewPager mViewPager;
    private CardPagerAdapterS mCardAdapter;
    private ShadowTranformer mCardShadowTransformer;
    String idProject="";
    String token="";
    String titleProject="";

    ItemTaskAdapter itemTaskAdapter;

    ArrayList<String> arrayListItemTask;


    ArrayList<CardItemString> arrayList;

    String name;

    private Socket socket;
    {
        try {
            socket = IO.socket("https://tase-nodejs.herokuapp.com/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Retrofit retrofitClient = RetrofitClient.getInstance();

        iMyService = retrofitClient.create(IMyService.class);
        arrayList= new ArrayList<>();
        //get activity for context
        DashBoard.context = getApplicationContext() ;

        socket.connect();
        socket.on("n", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject obj = (JSONObject)args[0];
                        try {
                            String n= obj.getString("noidung");
                            Intent intent1 = getIntent();
                            finish();
                            startActivity(intent1);
                        } catch (JSONException e) {
                            Toast.makeText(DashBoard.this,"Err", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });



            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Project");
        PersonalTable personalTable = (PersonalTable) bundle.getSerializable("Info");
        arrayListItemTask= new ArrayList<>();
        idProject=personalTable.getIdProject();
         titleProject=personalTable.getTitle();
        token=personalTable.getToken();
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mCardAdapter = new CardPagerAdapterS();

        FloatingActionButton btnNewTask = (FloatingActionButton) findViewById(R.id.btnNewTask) ;
        if (titleProject.equals(""))
        {
            btnNewTask.setAlpha(0f);
//            Toast.makeText(DashBoard.this,"true", Toast.LENGTH_SHORT).show();

        }
        else {}

        if (titleProject.equals(""))
        {
            compositeDisposable.add( iMyService.getNameUser(token)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                            new Consumer<String>() {
                                @Override
                                public void accept(String reponse) throws Exception
                                {
                                    JSONObject jsonObject = new JSONObject(reponse);
                                    name = jsonObject.getString("name");
                                }
                            }
                    ));

        }


        else
        {
            name="master";

        }

//        compositeDisposable.add(iMyService.showListTask(personalTable.getToken(), personalTable.getIdProject())
                compositeDisposable.add(iMyService.showListTask(personalTable.getIdProject())

                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(String reponse) throws Exception {

                                JSONObject jsonObject = new JSONObject(reponse);
                                JSONArray  detail= jsonObject.getJSONArray("detail");
                                ArrayList<Task> arrayListTask = new ArrayList<>();
                                ArrayList<Item> arrayListItem = new ArrayList<>();
                                    for (int i=0;i<detail.length();i++)
                                    {
                                        JSONObject objectTask = detail.getJSONObject(i);

                                        JSONArray taskArray = objectTask.getJSONArray("task");
                                        if (taskArray.length()==0){
                                            TextView a=(TextView) findViewById(R.id.tvTask);
                                            a.setText("Quản lí dự án tốt hơn bằng cách hãy tạo công việc mới");
                                        }
                                        else
                                        {
                                            for (int j=0;j<taskArray.length();j++)
                                            {
                                                JSONObject taskObject = taskArray.getJSONObject(j);
                                                String title=taskObject.getString("title");
                                                String description="";
                                                Boolean done = new Boolean(taskObject.getString("done"));
                                                Boolean delete = new Boolean(taskObject.getString("delete"));
                                                Integer _id=taskObject.getInt("_id");
                                                Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(taskObject.getString("deadline"));
                                                JSONArray  objectJSONArrayItem= taskObject.getJSONArray("item");
                                                ArrayList<Item> items= new ArrayList<>();
                                                ArrayList<String> strings= new ArrayList<>();

                                                if (objectJSONArrayItem.length()==0)
                                                {
                                                }
                                                else
                                                {
                                                    for (int f=0;f<objectJSONArrayItem.length();f++)
                                                    {
                                                        JSONObject itemObject = objectJSONArrayItem.getJSONObject(f);
                                                        Boolean deletedItem = new Boolean(itemObject.getString("deleted"));

                                                        if (deletedItem==false)
                                                        {
                                                            Integer idItem= itemObject.getInt("_id");
                                                            String titleItem=itemObject.getString("title");
                                                            Boolean doneItem = new Boolean(itemObject.getString("done"));
                                                            Item item = new Item(idItem,titleItem,doneItem,deletedItem);
                                                            strings.add(item.getTitle());
                                                            items.add(item);
                                                            arrayListItemTask.add(item.getTitle());

                                                        }
                                                    }
                                                }

                                                Task t = new Task(_id,title,deadline,delete,done,items,strings);
                                                arrayListTask.add(t);
                                            }
//                                            Toast.makeText(DashBoard.this,taskArray.get(0).toString(), Toast.LENGTH_SHORT).show();

                                        }
                                    }


                                    for (Task t: arrayListTask)
                                    {
                                            CardItemString cardItemString= new CardItemString( t.getTitle(), "",t.getItem(),t.get_id(),token,idProject);
                                            arrayList.add(cardItemString);
                                            mCardAdapter.addCardItemS(cardItemString);
                                    }

//                                Toast.makeText(DashBoard.this,String.valueOf(mViewPager.getCurrentItem()), Toast.LENGTH_SHORT).show();
//
//                                    View v= mViewPager.getChildAt(mViewPager.getCurrentItem());
//
//                                    Button b= (Button) v.findViewById(R.id.demo);


                                mCardShadowTransformer = new ShadowTranformer(mViewPager, mCardAdapter);
                                mViewPager.setAdapter(mCardAdapter);
                                mViewPager.setPageTransformer(false, mCardShadowTransformer);
                                mViewPager.setOffscreenPageLimit(3);


                                mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int i, float v, int i1) {
                                    }

                                    @Override
                                    public void onPageSelected(int i) {
                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int i) {
                                    }
                                });

                            }
                        }
                ));

    }
    public static Context getAppContext() {
        return DashBoard.context;
    }
    public void btnCreateItem(View view)
    {

        final AlertDialog.Builder alert = new AlertDialog.Builder(DashBoard.this);
        View viewtable = getLayoutInflater().inflate(R.layout.create_item_task,null);



        final EditText editCreateTask = (EditText) viewtable.findViewById(R.id.editCreateItemTask);

        Button btn_createItem = (Button)viewtable.findViewById(R.id.btnCreateItemTask);





        int p=mViewPager.getCurrentItem();

        CardItemString demo = arrayList.get(p);


        alert.setView(viewtable);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btn_createItem.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {



                if (editCreateTask.getText().toString().matches(""))
                {
                    Toast.makeText(DashBoard.this,"Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    socket.emit("reset","reset");

                    String i=editCreateTask.getText().toString()+ " ("+name+")";
                    compositeDisposable.add( iMyService.addItemTask(i,idProject,demo.getIdTask())
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                    new Consumer<String>() {
                                        @Override
                                        public void accept(String reponse) throws Exception
                                        {
                                        }
                                    }
                            ));
//                    sendEmail();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });
        alert.show();
    }



    public void btnCreateTask(View view){
        final AlertDialog.Builder alert = new AlertDialog.Builder(DashBoard.this);
        View viewtable = getLayoutInflater().inflate(R.layout.create_task,null);

//        final EditText editTextTable = (EditText)viewtable.findViewById(R.id.editTableName);
//        Button btn_cancel = (Button)viewtable.findViewById(R.id.btnCancel);
        Button btn_create = (Button)viewtable.findViewById(R.id.btnCreateTask);
        final EditText editCreateTask = (EditText) viewtable.findViewById(R.id.editCreateTask);
        alert.setView(viewtable);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btn_create.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if (editCreateTask.getText().toString().matches(""))
                {
                    Toast.makeText(DashBoard.this,"Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    Toast.makeText(DashBoard.this,token, Toast.LENGTH_SHORT).show();


                    compositeDisposable.add( iMyService.addTask(token,idProject,editCreateTask.getText().toString(),"2018/01/01")
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
        });
        alert.show();
    }


}

