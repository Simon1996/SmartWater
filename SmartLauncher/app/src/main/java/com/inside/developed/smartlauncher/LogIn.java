package com.inside.developed.smartlauncher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class LogIn extends AppCompatActivity {
    EditText edtLogin;
    EditText edtPassword;
    Button btnLogin;
    TextView singUpLink;
    MyCallBack myCallBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtLogin = (EditText)findViewById(R.id.input_email);
        edtPassword = (EditText)findViewById(R.id.input_password);
        btnLogin = (Button)findViewById(R.id.btn_login);
        singUpLink = (TextView)findViewById(R.id.link_signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsynkTask asynkTask = new MyAsynkTask();
                asynkTask.execute(edtLogin.getText().toString(),edtPassword.getText().toString());
            }
        });

        singUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent signup = new Intent(LogIn.this, SignUp.class);
                startActivity(signup);
                finish();
            }
        });
    }
    public class MyAsynkTask extends AsyncTask<String,Void,String> {

        ProgressDialog progressDialog ;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LogIn.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Logging....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String login = strings[0];
            String password = strings[1];
            JSONObject object = Util.getResponse(Strings.GET_TOKEN_LINK+login+"/"+password);
            String token = Util.getToken(object);
            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            if(s!=null){
                Log.wtf("TOKEN",s);
                Intent intent = new Intent(LogIn.this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(LogIn.this.getApplicationContext(),"No such login & password combination",Toast.LENGTH_SHORT).show();
            }


        }
    }
    public interface MyCallBack{

        void openSignUp();

        void openMainFragment(String token);
    }




}
