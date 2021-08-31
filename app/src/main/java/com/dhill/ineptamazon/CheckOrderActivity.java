package com.dhill.ineptamazon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.Toast;

import com.dhill.ineptamazon.db.ShoppingCart.ShoppingCart;
import com.dhill.ineptamazon.db.ShoppingCart.ShoppingCartAdapter;
import com.dhill.ineptamazon.db.ShoppingCart.ShoppingCartDAO;
import com.dhill.ineptamazon.db.ShoppingCart.ShoppingCartViewModel;
import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItem;
import com.dhill.ineptamazon.db.User.DataBase;
import com.dhill.ineptamazon.db.User.IneptDAO;
import com.dhill.ineptamazon.db.User.User;

import java.util.List;

public class CheckOrderActivity extends AppCompatActivity {
    private static final String USER_ID_KEY ="com.dhill.Ineptamazon.userIdKey" ;
    private static final String PREFERENCES_KEY ="com.dhill.Ineptamazon.PREFERENCES_KEY" ;
    private ShoppingCartViewModel shoppingCartViewModel;
    private int mUserId = -1;
    private SharedPreferences mPreferences=null;
    private User mUser;
    IneptDAO ineptDAO;
    ShoppingCartDAO shoppingCartDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order);
        getDatabase();
        checkForUser();
        addUserToPreference(mUserId);
        loginUser(mUserId);
        mUserId=mPreferences.getInt(USER_ID_KEY,-1);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        //final List<ShoppingCart>shoppingCartsfilter= (List<ShoppingCart>) shoppingCartDAO.getAllShoppingCartsByUserId(mUserId);

          final ShoppingCartAdapter adapter = new ShoppingCartAdapter();
        recyclerView.setAdapter(adapter);

        shoppingCartViewModel = ViewModelProviders.of(this).get(ShoppingCartViewModel.class);
        shoppingCartViewModel.getAllShoppingCarts().observe(this, new Observer<List<ShoppingCart>>() {
            @Override
            public void onChanged(List<ShoppingCart> shoppingCarts) {
                adapter.setShoppingCarts(shoppingCarts);
                if (shoppingCarts.size()==0){
                    Toast.makeText(CheckOrderActivity.this, "No Current Orders", Toast.LENGTH_SHORT).show();
                }


            }

        });
        adapter.setOnItemClickListener(new ShoppingCartAdapter.OnItemCLickListener() {
            @Override
            public void onItemCLick(ShoppingCart shoppingCart) {
                cancelItem(shoppingCart);

            }
        });

    }
    private void cancelItem( final ShoppingCart shoppingItem) {AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(R.string.cancel_item);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        shoppingCartViewModel.delete(shoppingItem);
                        Toast.makeText(CheckOrderActivity.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
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
        mPreferences= this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
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
    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, CheckOrderActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
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


}