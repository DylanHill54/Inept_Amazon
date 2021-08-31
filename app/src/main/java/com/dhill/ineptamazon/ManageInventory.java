package com.dhill.ineptamazon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import android.widget.Toast;

import com.dhill.ineptamazon.db.User.DataBase;
import com.dhill.ineptamazon.db.User.IneptDAO;
import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItem;
import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItemAdapter;
import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItemViewModel;
import com.dhill.ineptamazon.db.User.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ManageInventory extends AppCompatActivity {
    public static final int ADD_SHOPPINGITEM_REQUEST = 1;
    public static final int EDIT_SHOPPINGITEM_REQUEST = 2;
    private static final String USER_ID_KEY ="com.dhill.Ineptamazon.userIdKey" ;
    private static final String PREFERENCES_KEY ="com.dhill.Ineptamazon.PREFERENCES_KEY" ;
    private ShoppingItemViewModel shoppingItemViewModel;
    private int mUserId = -1;
    private SharedPreferences mPreferences=null;
    private User mUser;
    IneptDAO ineptDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventory);
        getDatabase();
        checkForUser();
        addUserToPreference(mUserId);
        loginUser(mUserId);
        FloatingActionButton buttonAddNote = findViewById(R.id.add_shopping_item);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageInventory.this, AddEditShoppingItemActivity.class);
                startActivityForResult(intent, ADD_SHOPPINGITEM_REQUEST);
            }
        });
        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ShoppingItemAdapter adapter= new ShoppingItemAdapter();
        recyclerView.setAdapter(adapter);

        shoppingItemViewModel= ViewModelProviders.of(this).get(ShoppingItemViewModel.class);
        shoppingItemViewModel.getAllShoppingItems(  ).observe(this, new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(List<ShoppingItem> shoppingItems) {
                adapter.setShoppingItems(shoppingItems);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                shoppingItemViewModel.delete(adapter.getShoppingItemAt(viewHolder.getAdapterPosition()));
                Toast.makeText(ManageInventory.this, "Item deleted", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(new ShoppingItemAdapter.OnItemCLickListener() {
            @Override
            public void onItemCLick(ShoppingItem shoppingItem) {
                Intent intent= new Intent(ManageInventory.this, AddEditShoppingItemActivity.class);
                intent.putExtra(AddEditShoppingItemActivity.EXTRA_ID, shoppingItem.getId());
                intent.putExtra(AddEditShoppingItemActivity.EXTRA_NAME, shoppingItem.getName());
                intent.putExtra(AddEditShoppingItemActivity.EXTRA_DESCRIPTION, shoppingItem.getDescription());
                intent.putExtra(AddEditShoppingItemActivity.EXTRA_SUPPLY, shoppingItem.getSupply());
                startActivityForResult(intent, EDIT_SHOPPINGITEM_REQUEST);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_SHOPPINGITEM_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddEditShoppingItemActivity.EXTRA_NAME);
            String description = data.getStringExtra(AddEditShoppingItemActivity.EXTRA_DESCRIPTION);
            int supply = data.getIntExtra(AddEditShoppingItemActivity.EXTRA_SUPPLY, 1);
            ShoppingItem shoppingItem = new ShoppingItem(name, description, supply);
            shoppingItemViewModel.insert(shoppingItem);
            Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();
        } else if  (requestCode == EDIT_SHOPPINGITEM_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditShoppingItemActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Item can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = data.getStringExtra(AddEditShoppingItemActivity.EXTRA_NAME);
            String description = data.getStringExtra(AddEditShoppingItemActivity.EXTRA_DESCRIPTION);
            int supply = data.getIntExtra(AddEditShoppingItemActivity.EXTRA_SUPPLY, 1);
            ShoppingItem shoppingItem = new ShoppingItem(name, description, supply);
            shoppingItem.setId(id);
            shoppingItemViewModel.update(shoppingItem);
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Item not saved", Toast.LENGTH_SHORT).show();
        }
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
        mPreferences= this.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE);
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
        Intent intent = new Intent(context, ManageInventory.class);
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