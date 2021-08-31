package com.dhill.ineptamazon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dhill.ineptamazon.db.User.DataBase;
import com.dhill.ineptamazon.db.User.IneptDAO;
import com.dhill.ineptamazon.db.User.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=findViewById(R.id.button2);
        register=findViewById(R.id.button);
        getDatabase();
        checkForUser();
        addUserToPreference(mUserId);
        loginUser(mUserId);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=ineptLogin.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=Register.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }
    private static final String USER_ID_KEY ="com.dhill.Ineptamazon.userIdKey" ;
    private static final String PREFERENCES_KEY ="com.dhill.Ineptamazon.PREFERENCES_KEY" ;
    private int mUserId = -1;
    private SharedPreferences mPreferences=null;
    private User mUser;
    IneptDAO ineptDAO;




    private void loginUser (int userId) {
        mUser= ineptDAO.getUserByUserId(userId);
        invalidateOptionsMenu();
    }

    private void addUserToPreference(int userId) {
        if(mPreferences==null){
            getPrefs();
        }
        SharedPreferences.Editor editor= mPreferences.edit();
        editor.putInt(USER_ID_KEY,userId);
    }
    private void getPrefs() {
        mPreferences= this.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE);
    }

    private void checkForUser() {
        //do we have user in intent?
        mUserId=getIntent().getIntExtra(USER_ID_KEY,-1);


        if (mUserId!=-1){
            return;
        }
        //do we have a user in the preferences?

        if(mPreferences==null){
            getPrefs();
        }
        mUserId=mPreferences.getInt(USER_ID_KEY,-1);

        if (mUserId!=-1){
            return;
        }

        //do we have any users at all?
        List<User> users = ineptDAO.ineptGetAllUsers();
        if(users.size()<=1){
            User defaultUser=new User("testuser1","testuser1");
            User altUser=new User("admin2","admin2","yes");
            ineptDAO.insert(defaultUser,altUser);
            //mGymLogDAO.insert(altUser);
        }


    }
    private void getDatabase() {
        ineptDAO = Room.databaseBuilder(this, DataBase.class, DataBase.USER_TABLE)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getIneptDAO();
    }

    public static Intent intentFactory(Context context,int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }


}
