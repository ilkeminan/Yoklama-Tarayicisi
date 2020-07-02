package com.example.yoklamataraticisi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ActivityLogin extends AppCompatActivity {
    EditText txtInputUserName, txtInputPassword;
    static Instructor logged_in_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtInputUserName = (EditText) this.findViewById(R.id.editUserName);
        txtInputPassword = (EditText) this.findViewById(R.id.editPassword);
        Button buttonLogin = (Button) this.findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection con;
                try{
                    DatabaseConnector connector = new DatabaseConnector();
                    con = connector.Baglanti();
                    if(con==null){
                        Toast.makeText(getApplicationContext(),"İnternet bağlantınızı kontrol edin.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String query = "select * from Instructor where user_name= '" + txtInputUserName.getText().toString() + "' and password= '" + txtInputPassword.getText().toString() + "'";
                        Statement durum = con.createStatement();
                        ResultSet rs = durum.executeQuery(query);
                        if(rs.next()){
                            logged_in_user = new Instructor(rs.getString("name"),rs.getString("surname"),rs.getString("user_name"),rs.getString("password"),rs.getString("email"));
                            Intent intentLogin = new Intent(ActivityLogin.this,ActivityChooseCourse.class);
                            startActivity(intentLogin);
                        }
                        else if(txtInputUserName.getText().toString().equals("") && !txtInputPassword.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),"Kullanıcı adınızı giriniz.",Toast.LENGTH_LONG).show();
                        }
                        else if(!txtInputUserName.getText().toString().equals("") && txtInputPassword.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),"Şifrenizi giriniz.",Toast.LENGTH_LONG).show();
                        }
                        else if(txtInputUserName.getText().toString().equals("") && txtInputPassword.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),"Kullanıcı adınızı ve şifrenizi giriniz.",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Kullanıcı adı ya da şifre hatalı.",Toast.LENGTH_LONG).show();
                        }
                    }
                    con.close();
                }
                catch(Exception e){

                }
            }
        });
        Button buttonSignup = (Button) this.findViewById(R.id.buttonSignup);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignup = new Intent(ActivityLogin.this,ActivitySignup.class);
                startActivity(intentSignup);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Close application
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
