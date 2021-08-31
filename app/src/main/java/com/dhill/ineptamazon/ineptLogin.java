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

public class ineptLogin extends AppCompatActivity {
    private EditText mUsername;
    private android.widget.EditText mPassword;

    private Button mButton;

    private IneptDAO ineptDAO;

    private String mUsernameString;
    private String mPasswordString;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inept_login);

        wireUpDisplay();

        getDatabase();
    }

    private void wireUpDisplay(){
        mUsername=findViewById(R.id.editTextTextLoginUserName);
        mPassword=findViewById(R.id.editTextTextLoginPassword);

        mButton=findViewById(R.id.buttonLogin);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromDisplay();
                if (checkForUserInDataBase()) {
                    if (!validatePassword()) {
                        Toast.makeText(ineptLogin.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent=LandingPAge.intentFactory(getApplicationContext(),mUser.getUserId());
                        startActivity(intent);

                    }
                }
            }
        });
    }
    private boolean checkForUserInDataBase() {
        mUser =ineptDAO.getUserByUsername(mUsernameString);
        if(mUser==null){
            Toast.makeText(this, "no user" +mUsernameString+" found",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validatePassword(){
        return mUser.getPassword().equals(mPasswordString);
    }

    private void getValuesFromDisplay() {
        mUsernameString=mUsername.getText().toString();
        mPasswordString=mPassword.getText().toString();
    }

    private void getDatabase() {
        ineptDAO = Room.databaseBuilder(this, DataBase.class, DataBase.USER_TABLE)
                        .allowMainThreadQueries()
                        .build()
                        .getIneptDAO();

    }
    public static Intent intentFactory(Context context){
        Intent intent =new Intent(context, ineptLogin.class);
        return intent;

    }

}