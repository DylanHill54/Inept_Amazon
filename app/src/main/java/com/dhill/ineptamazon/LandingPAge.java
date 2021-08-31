package com.dhill.ineptamazon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dhill.ineptamazon.db.User.DataBase;
import com.dhill.ineptamazon.db.User.IneptDAO;
import com.dhill.ineptamazon.db.User.User;

import java.util.List;

public class LandingPAge extends AppCompatActivity {
    private static final String USER_ID_KEY ="com.dhill.Ineptamazon.userIdKey" ;
    private static final String PREFERENCES_KEY ="com.dhill.Ineptamazon.PREFERENCES_KEY" ;
    private int mUserId = -1;
    private SharedPreferences mPreferences=null;
    private User mUser;
    IneptDAO ineptDAO;
    private Button adminButton;
    private Button buyItems;
    private Button checkOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_p_age);
        getDatabase();
        checkForUser();
        addUserToPreference(mUserId);
        loginUser(mUserId);
        buyItems=findViewById(R.id.button4);
        checkOrders=findViewById(R.id.button5);
        adminButton = findViewById(R.id.button3);
        adminButton.setVisibility(View.VISIBLE);
        if (mUser.isMisAdmin() == false) {
            adminButton.setVisibility(View.INVISIBLE);
        }
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=AdminActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                startActivity(intent);

            }
        });
        buyItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=BuyItems.intentFactory(getApplicationContext(), mUser.getUserId());
                startActivity(intent);


            }
        });
        checkOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=CheckOrderActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                startActivity(intent);
            }
        });
    }
    private void loginUser (int userId) {
        mUser= ineptDAO.getUserByUserId(userId);
    }

    private void addUserToPreference(int userId) {
        if(mPreferences==null){
            getPrefs();
        }
        SharedPreferences.Editor editor= mPreferences.edit();
        editor.putInt(USER_ID_KEY,userId);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mUser!=null){
            MenuItem item=menu.findItem(R.id.item1);
            item.setTitle(mUser.getUserName());
        }
        return super.onPrepareOptionsMenu(menu);
    }
    private void getPrefs() {
        mPreferences= this.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                        mUserId=-1;
                        checkForUser();
                        Intent intent= MainActivity.intentFactory(getApplicationContext(),mUserId);
                        startActivity(intent);
                    }
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {


                    }
                });
        alertBuilder.create().show();
    }

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY,-1);
    }

    private void clearUserFromPref(){
        //Toast.makeText(this,"clear users not yet implemented",Toast.LENGTH_SHORT).show();
        addUserToPreference(-1);

    }
    public static Intent intentFactory(Context context,int userId) {
        Intent intent = new Intent(context, LandingPAge.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}