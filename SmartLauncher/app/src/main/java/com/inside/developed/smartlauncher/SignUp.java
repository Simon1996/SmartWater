package com.inside.developed.smartlauncher;

import android.app.ProgressDialog;
import android.content.Context;
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

public class SignUp extends AppCompatActivity {

    EditText edtLogin;
    EditText edtPassword;
    EditText edtConfirm;
    Button btnSignUp;
    TextView txtLogInLink;
    SignUpCallback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtLogin = (EditText)findViewById(R.id.input_login);
        edtPassword = (EditText)findViewById(R.id.input_pasword_reg);
        edtConfirm = (EditText)findViewById(R.id.input_password_confirm);
        btnSignUp = (Button)findViewById(R.id.btn_signup);
        txtLogInLink = (TextView)findViewById(R.id.link_login);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPasswords()){
                    MyAsynkTask asynkTask = new MyAsynkTask();
                    asynkTask.execute(edtLogin.getText().toString(),edtPassword.getText().toString());
                }
            }
        });
        txtLogInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(SignUp.this, LogIn.class);
                startActivity(login);
                finish();

            }
        });
    }
    private boolean checkPasswords() {
        if(edtPassword.getText().toString().equals(edtConfirm.getText().toString())){
            return true;
        }else{
            Toast.makeText(SignUp.this.getApplicationContext(),"Passwords doesn't match",Toast.LENGTH_SHORT).show();
            return false;
        }


    }
    public interface SignUpCallback{

        void  onLoginLinkListner();
        void   onOpenMainFragment(String token);
    }




    class MyAsynkTask extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog ;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SignUp.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Logging....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String login = strings[0];
            String password = strings[1];
            JSONObject object = new JSONObject();
            String token = null;
            try {
                object.put("login", login);
                object.put("password",password);
                JSONObject response = Util.getResponse(Strings.SING_UP_LINK,object);
                token = Util.getToken(response);
            }catch (Exception e){

            }

            return token;

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            if(s!=null){
                Log.wtf("TOKEN",s);
              //  callback.onOpenMainFragment(s);
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(SignUp.this.getApplicationContext(),"This login is already used",Toast.LENGTH_SHORT).show();
            }
        }
    }


}