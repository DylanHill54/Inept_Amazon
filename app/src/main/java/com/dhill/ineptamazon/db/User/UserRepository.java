package com.dhill.ineptamazon.db.User;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItem;
import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItemDAO;
import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItemDatabase;

import java.util.List;

public class UserRepository {

    private IneptDAO userDAO;
    private LiveData<List<User>> allUsers;
    public UserRepository(Application application) {
        DataBase dataBase = DataBase.getUserDatabase(application);
        userDAO=dataBase.getIneptDAO();
        allUsers = userDAO.getAllUsers();
    }
    public LiveData<List<User>> getAllUsers(){
        return allUsers;
    }
}
