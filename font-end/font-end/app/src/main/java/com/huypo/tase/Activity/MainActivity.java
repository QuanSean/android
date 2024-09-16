package com.huypo.tase.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.huypo.tase.Model.Constants;
import com.huypo.tase.Model.Project;
import com.huypo.tase.Model.Respone;
import com.huypo.tase.Model.User;
import com.huypo.tase.R;
import com.huypo.tase.Retrofit.IMyService;
import com.huypo.tase.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.HttpException;
import retrofit2.Retrofit;

import static com.huypo.tase.Model.Validation.validateEmail;
import static com.huypo.tase.Model.Validation.validateFields;

//import io.socket.client.IO;
//import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imgRegister;
    private TextView txtLogin;
    private EditText edt_login_email, edt_login_password;
    private Button btn_login;
    private SharedPreferences mSharedPreferences;
    ArrayAdapter arrayAdapter;
    ListView list;
    ArrayList<Project> projectArrayList = new ArrayList<>();

//    private Socket mSocket;
//    {
//        try {
//            mSocket = IO.socket("http://192.168.1.106:2409/");
//        } catch (URISyntaxException e) {}
//    }
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;


    private Socket socket;

    {
        try {
            socket = IO.socket("http://172.16.8.4:2409/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        socket.connect();
//        socket.on ("hello",onRetrieveData);

        socket.on("n", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject obj = (JSONObject)args[0];
                        try {
                            String n= obj.getString("noidung");
                            Toast.makeText(MainActivity.this,n, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this,"Err", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }
                });



            }
        });

        Button btnChangePassword= (Button) findViewById(R.id.changePass) ;
        btnChangePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                View viewtable = getLayoutInflater().inflate(R.layout.changepass_dialog,null);

                Button btnChangePassword = (Button)viewtable.findViewById(R.id.btnChangePassWord);
                Button btnEmail = (Button)viewtable.findViewById(R.id.btnEmail);

                final EditText editEmail = (EditText) viewtable.findViewById(R.id.editEmail);
                final EditText editKey = (EditText) viewtable.findViewById(R.id.editKey);
                final EditText editPassword = (EditText) viewtable.findViewById(R.id.editPassword);


                btnEmail.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        compositeDisposable.add( iMyService.sendEmailChangePassword(editEmail.getText().toString())
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                        new Consumer<String>() {
                                            @Override
                                            public void accept(String reponse) throws Exception
                                            {
                                                JSONObject jsonObject= new JSONObject(reponse);
                                                Boolean result  = new Boolean(jsonObject.getString("result"));

                                                if (result==false)
                                                {
                                                    Toast.makeText(MainActivity.this,"Vui lòng nhập đúng email để thay đổi mật khẩu", Toast.LENGTH_SHORT).show();

                                                }
                                                else
                                                {
                                                    Toast.makeText(MainActivity.this,"Mã xác nhận đã được gửi vào email của bạn", Toast.LENGTH_SHORT).show();
                                                    btnEmail.setOnClickListener(null);
                                                    btnEmail.setTextColor(Color.GRAY);
//                                                    D81B60
                                                    btnChangePassword.setVisibility(View.VISIBLE);
                                                    editKey.setVisibility(View.VISIBLE);
                                                    editPassword.setVisibility(View.VISIBLE);
                                                    btnChangePassword.setOnClickListener(new View.OnClickListener() {
                                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                                        @Override
                                                        public void onClick(View v) {

                                                            if (editKey.getText().toString().matches("")||editPassword.getText().toString().matches(""))
                                                            {
                                                                Toast.makeText(MainActivity.this,"Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();

                                                            }
                                                            else
                                                            {
                                                                compositeDisposable.add( iMyService.changePassword(editEmail.getText().toString(),editKey.getText().toString(),editPassword.getText().toString())
                                                                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                                                                new Consumer<String>() {
                                                                                    @Override
                                                                                    public void accept(String reponse) throws Exception
                                                                                    {
                                                                                        JSONObject jsonObject= new JSONObject(reponse);
                                                                                        Boolean result  = new Boolean(jsonObject.getString("result"));
                                                                                        if (result==false)
                                                                                        {
                                                                                            Toast.makeText(MainActivity.this,"Vui lòng nhập đúng thông tin", Toast.LENGTH_SHORT).show();

                                                                                        }
                                                                                        else
                                                                                        {
                                                                                            Toast.makeText(MainActivity.this,"Bạn đã thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                                                                            Intent intent = getIntent();
                                                                                            finish();
                                                                                            startActivity(intent);

                                                                                        }



                                                                                    }
                                                                                }
                                                                        ));
                                                            }

                                                        }
                                                    });







                                                }

//


                                            }
                                        }
                                ));


//                Toast.makeText(MainMenu.this,editProjectName.getText().toString(), Toast.LENGTH_SHORT).show();




                    }
                });

                alert.setView(viewtable);

                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);

                alert.show();


            }
        });
//        View viewtable = getLayoutInflater().inflate(R.layout.changepass_dialog,null);



//        mSocket.connect();


        init();
        initSharedPreferences();
        login();
    }



    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }
    public void init(){
        imgRegister = findViewById(R.id.imgRegister);
        txtLogin = findViewById(R.id.txtLogin);
        imgRegister.setOnClickListener(this);
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        //Init view
        edt_login_email = (EditText) findViewById(R.id.editEmail);
        edt_login_password = (EditText) findViewById(R.id.editPass);

        btn_login = (Button) findViewById(R.id.btnLogin);
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                socket.emit("aaa","Hello");

                loginUser(edt_login_email.getText().toString(),
                        edt_login_password.getText().toString());
            }
        });
    }

    private void loginUser (String email, String password)
    {
        compositeDisposable.add( iMyService.loginUser(email,password)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(String reponse) throws Exception
                            {

                                try{
                                    JSONObject jsonObject= new JSONObject(reponse);
                                    JSONObject jsonObject1= new JSONObject(jsonObject.getString("info"));

                                    Boolean result  = new Boolean(jsonObject.getString("result"));

                                    Boolean deleted  = new Boolean( jsonObject1.getString("deleted"));


                                    User user= new User(result,jsonObject.getString("token"),jsonObject1.getString("_id"),jsonObject1.getString("email"),jsonObject1.getString("name"),deleted);
                                    User user1 = new User();




                                    Intent intent= new Intent(MainActivity.this,MainMenu.class);
                                    Bundle bundle= new Bundle();

//                                    intent.putExtra("User",user);
                                    bundle.putSerializable("Info",  user);
                                    intent.putExtra("User", bundle);
                                    startActivity(intent);


                                }
                                catch (Exception e){
                                    Toast.makeText(MainActivity.this,"Ban da sai ten dang nhap hoac mat khau", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                ));
    }
    private void login() {

        setError();

        String email = edt_login_email.getText().toString();
        String password = edt_login_password.getText().toString();


        int err = 0;

        if (!validateEmail(email)) {

            err++;
            edt_login_email.setError("Email should be valid !");
        }

        if (!validateFields(password)) {

            err++;
            edt_login_password.setError("Password should not be empty !");
        }

        if (err == 0) {

            loginUser(email,password);

        } else {
            Toast.makeText(this,"Enter Valid Details !",Toast.LENGTH_LONG).show();
        }
    }

    private void setError() {

        edt_login_email.setError(null);
        edt_login_password.setError(null);
    }
    private void handleResponse(Respone response) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.TOKEN,response.getToken());
//        editor.putString(Constants.EMAIL,response.getMessage());
        editor.apply();

        edt_login_email.setText(null);
        edt_login_password.setText(null);

        Intent intent = new Intent(this, DashBoard.class);
        startActivity(intent);

    }
//888091407597-43mdv7gh4jjaibe301adabdfp05dtspm.apps.googleusercontent.com
    private void handleError(Throwable error) {


        if (error instanceof HttpException) {

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();

                Toast.makeText(this,""+errorBody,Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this,"Network Error !",Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v == imgRegister) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);

            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(txtLogin, "txtLogin");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
            startActivity(intent, activityOptions.toBundle());


        }
    }
}
