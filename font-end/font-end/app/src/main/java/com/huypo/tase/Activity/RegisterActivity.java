package com.huypo.tase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huypo.tase.R;
import com.huypo.tase.Retrofit.IMyService;
import com.huypo.tase.Retrofit.RetrofitClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private Animation animation;
    private Button btnRegister;
    EditText edt_register_email;
    EditText edt_register_name;
    EditText edt_register_password;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        relativeLayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        relativeLayout.setAnimation(animation);

        edt_register_email = (EditText)findViewById(R.id.editEmail);
        edt_register_name = (EditText)findViewById(R.id.editName);
        edt_register_password = (EditText)findViewById(R.id.editPass);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (isEmailValid(edt_register_email.getText().toString())==false)
                {
                    Toast.makeText(RegisterActivity.this,"Vui lòng nhập đúng email",Toast.LENGTH_LONG).show();

                }
                else
                {
                    registerUser(edt_register_email.getText().toString(),
                            edt_register_name.getText().toString(),
                            edt_register_password.getText().toString());
                }


            }
        });
//        btnRegister.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                final View activity_register = LayoutInflater.from(MainActivity.this)
//                        .inflate(R.layout.activity_register,null);
//
//                new MaterialStyledDialog.Builder(MainActivity.this)
//                        .setIcon(R.drawable.ic_person)
//                        .setTitle("REGISTRATION")
//                        .setDescription("Please fill all fields")
//                        .setNegativeText("CANCEL")
//                        .onNegative(new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .setPositiveText("REGISTER")
//                        .onPositive(new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                EditText edt_register_email = (EditText)activity_register.findViewById(R.id.editEmail);
//                                EditText edt_register_name = (EditText)activity_register.findViewById(R.id.editName);
//                                EditText edt_register_password = (EditText)activity_register.findViewById(R.id.editPass);
//
//                                if(TextUtils.isEmpty(edt_register_email.getText().toString())){
//                                    Toast.makeText(MainActivity.this,"Email cannot be null",Toast.LENGTH_LONG).show();
//                                    return;
//                                }
//                                if(TextUtils.isEmpty(edt_register_name.getText().toString())){
//                                    Toast.makeText(MainActivity.this,"Name cannot be null",Toast.LENGTH_LONG).show();
//                                    return;
//                                }
//                                if(TextUtils.isEmpty(edt_register_password.getText().toString())){
//                                    Toast.makeText(MainActivity.this,"Password cannot be null",Toast.LENGTH_LONG).show();
//                                    return;
//                                }
//
//                                registerUser(edt_register_email.getText().toString(),
//                                        edt_register_name.getText().toString(),
//                                        edt_register_password.getText().toString());
//                            }
//                        }).show();
//            }
//        });
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    private void registerUser(String email, String name, String password) {
        if(TextUtils.isEmpty(edt_register_email.getText().toString())){
            Toast.makeText(RegisterActivity.this,"Email cannot be null",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(edt_register_name.getText().toString())){
            Toast.makeText(RegisterActivity.this,"Name cannot be null",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(edt_register_password.getText().toString())){
            Toast.makeText(RegisterActivity.this,"Password cannot be null",Toast.LENGTH_LONG).show();
            return;
        }
        compositeDisposable.add( iMyService.registerUser(edt_register_email.getText().toString(),edt_register_password.getText().toString(),edt_register_name.getText().toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(String reponse) throws Exception
                            {
                                Toast.makeText(RegisterActivity.this,"Success",Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);


                            }
                        }
                ));

    }
}
