package com.dhill.ineptamazon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dhill.ineptamazon.db.User.DataBase;
import com.dhill.ineptamazon.db.User.IneptDAO;
import com.dhill.ineptamazon.db.User.User;

public class Register extends AppCompatActivity {

    private EditText username, password;
    private Button register;
    private String userNameString, passwordString;
    private IneptDAO mIneptDAO;
    private User userCheck;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        wireUpDisplay();
        getDatabase();


    }
    private void getDatabase(){
        mIneptDAO = Room.databaseBuilder(this, DataBase.class, DataBase.USER_TABLE)
                .allowMainThreadQueries()
                .build()
                .getIneptDAO();

    }

    private void wireUpDisplay() {

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        login=findViewById(R.id.login);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromDisplay();
                if(!checkForUserInDataBase()){
                    Toast.makeText(getApplicationContext(),"Inavlid username",Toast.LENGTH_SHORT).show();
                }else{
                    User user = new User();
                    user.setUserName(userNameString);
                    user.setPassword(passwordString);
                    mIneptDAO.insert(user);
                    Toast.makeText(getApplicationContext(),"User registered",Toast.LENGTH_SHORT).show();
                }



            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=ineptLogin.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

    }

    private boolean checkForUserInDataBase() {
        userCheck= mIneptDAO.getUserByUsername(userNameString);
        if(userCheck!=null){
            Toast.makeText(this, " Username Already in use",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getValuesFromDisplay() {
        userNameString=username.getText().toString();
        passwordString=password.getText().toString();

    }
    public static Intent intentFactory(Context context){
        Intent intent =new Intent(context, Register.class);
        return intent;

    }
}