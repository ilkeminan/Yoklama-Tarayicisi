package com.example.yoklamataraticisi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ActivitySignup extends AppCompatActivity {
    EditText editTextName, editTextSurname, editTextUserName, editTextPassword, editTextEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editTextName = (EditText) this.findViewById(R.id.editTextName);
        editTextSurname = (EditText) this.findViewById(R.id.editTextSurname);
        editTextUserName = (EditText) this.findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) this.findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) this.findViewById(R.id.editTextEmail);
        Button buttonSignup = (Button) this.findViewById(R.id.buttonKayit);
        Button buttonLogin = this.findViewById(R.id.button_login_in_signup);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextName.getText().toString().equals("") || editTextSurname.getText().toString().equals("") || editTextUserName.getText().toString().equals("") || editTextPassword.getText().toString().equals("") || editTextEmail.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Lütfen tüm alanları giriniz.", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        DatabaseConnector connector = new DatabaseConnector();
                        Connection con = connector.Baglanti();
                        if (con == null) {
                            Toast.makeText(getApplicationContext(),"İnternet bağlantınızı kontrol edin.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String query = "select * from Instructor where user_name= '" + editTextUserName.getText().toString() + "'";
                            Statement durum = con.createStatement();
                            ResultSet rs = durum.executeQuery(query);
                            if(rs.next()){
                                Toast.makeText(getApplicationContext(), "Başka bir kullanici adi seçiniz.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String insert_instructor = "insert into Instructor values(?,?,?,?,?)";
                                PreparedStatement preparedStatement_forStudent = con.prepareStatement(insert_instructor);
                                preparedStatement_forStudent.setString(1,editTextName.getText().toString());
                                preparedStatement_forStudent.setString(2,editTextSurname.getText().toString());
                                preparedStatement_forStudent.setString(3,editTextUserName.getText().toString());
                                preparedStatement_forStudent.setString(4,editTextPassword.getText().toString());
                                preparedStatement_forStudent.setString(5,editTextEmail.getText().toString());
                                preparedStatement_forStudent.executeUpdate();
                                Toast.makeText(getApplicationContext(),"Kayıt başarılı.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActivitySignup.this,ActivityLogin.class);
                                startActivity(intent);
                            }
                        }
                        con.close();
                    }
                    catch(Exception e){

                    }
                }
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySignup.this,ActivityLogin.class);
                startActivity(intent);
            }
        });
    }
}
