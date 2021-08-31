package com.dhill.ineptamazon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.dhill.ineptamazon.db.ShoppingCart.ShoppingCart;
import com.dhill.ineptamazon.db.ShoppingCart.ShoppingCartAdapter;
import com.dhill.ineptamazon.db.ShoppingCart.ShoppingCartDAO;
import com.dhill.ineptamazon.db.ShoppingCart.ShoppingCartViewModel;
import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItem;
import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItemAdapter;
import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItemViewModel;
import com.dhill.ineptamazon.db.User.DataBase;
import com.dhill.ineptamazon.db.User.IneptDAO;
import com.dhill.ineptamazon.db.User.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BuyItems extends AppCompatActivity {
    public static final int ADD_SHOPPINGITEM_REQUEST = 1;
    public static final int EDIT_SHOPPINGITEM_REQUEST = 2;
    private static final String USER_ID_KEY ="com.dhill.Ineptamazon.userIdKey" ;
    private static final String PREFERENCES_KEY ="com.dhill.Ineptamazon.PREFERENCES_KEY" ;
    private ShoppingItemViewModel shoppingItemViewModel;
    private int mUserId = -1;
    private SharedPreferences mPreferences=null;
    private User mUser;
    IneptDAO ineptDAO;
    private ShoppingCartViewModel shoppingCartViewModel;
    MenuItem searchItem;
    private Object SearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_items);
        getDatabase();
        checkForUser();
        addUserToPreference(mUserId);
        loginUser(mUserId);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ShoppingItemAdapter adapter = new ShoppingItemAdapter();
        recyclerView.setAdapter(adapter);
        shoppingCartViewModel = ViewModelProviders.of(this).get(ShoppingCartViewModel.class);
        shoppingItemViewModel = ViewModelProviders.of(this).get(ShoppingItemViewModel.class);
        shoppingItemViewModel.getAllShoppingItems().observe(this, new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(List<ShoppingItem> shoppingItems) {
                adapter.setShoppingItems(shoppingItems);


            }
        });
        adapter.setOnItemClickListener(new ShoppingItemAdapter.OnItemCLickListener() {
            @Override
            public void onItemCLick(ShoppingItem shoppingItem) {
                if(shoppingItem.getSupply()>0) {
                    buyItem(shoppingItem);
                }else if( shoppingItem.getSupply()==0){
                    Toast.makeText(BuyItems.this, "Item out of stock", Toast.LENGTH_SHORT).show();
                }
                }

        });




    }

    private void buyItem( final ShoppingItem shoppingItem) {AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(R.string.buy_item);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            String name =shoppingItem.getName();
            String description = shoppingItem.getDescription();
            int supply = 1;
            int supplyUpdate=shoppingItem.getSupply()-1;

            ShoppingItem updateItem=new ShoppingItem(name,description,supplyUpdate);
            ShoppingCart shoppingCart = new ShoppingCart(name, description, supply, mUser.getUserName());
            updateItem.setId(shoppingItem.getId());
            shoppingItemViewModel.update(updateItem);
            shoppingCartViewModel.insert(shoppingCart);
            Toast.makeText(BuyItems.this, "Item ordered", Toast.LENGTH_SHORT).show();
        }
    });
        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            Toast.makeText(BuyItems.this, "Item not ordered", Toast.LENGTH_SHORT).show();


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

    }
    private void getDatabase() {
        ineptDAO = Room.databaseBuilder(this, DataBase.class, DataBase.USER_TABLE)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getIneptDAO();
    }
    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, BuyItems.class);
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