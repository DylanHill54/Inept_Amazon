package com.dhill.ineptamazon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import android.content.Intent;

import android.os.Bundle;

import com.dhill.ineptamazon.db.User.User;
import com.dhill.ineptamazon.db.User.UserAdapter;
import com.dhill.ineptamazon.db.User.UserViewModel;

import java.util.List;

public class ViewUsersActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final UserAdapter adapter=new UserAdapter();
        recyclerView.setAdapter(adapter);

        userViewModel= ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.setUsers(users);
            }
        });



    }
    public static Intent intentFactory(Context context){
        Intent intent =new Intent(context, ViewUsersActivity.class);
        return intent;

    }

}