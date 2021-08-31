package com.dhill.ineptamazon.db.ShoppingCart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItem;
import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItemRepository;

import java.util.List;

public class ShoppingCartViewModel extends AndroidViewModel {
    private ShoppingCartRepository repository;
    private LiveData<List<ShoppingCart>> allShoppingCarts;
    private LiveData<List<ShoppingCart>> someShoppingCarts;

    public ShoppingCartViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ShoppingCartRepository(application);
        allShoppingCarts = repository.getAllShoppingCarts();
        //someShoppingCarts = repository.getAllShoppingCartsByUserId();
    }
  /*public ShoppingCartViewModel(@NonNull Application application,int userId) {
        super(application);
        this.repository = new ShoppingCartRepository(application, userId);
        someShoppingCarts = repository.getAllShoppingCartsByUserId();
      allShoppingCarts = repository.getAllShoppingCarts();
      someShoppingCarts = repository.getAllShoppingCartsByUserId();

    }*/


    public void insert(ShoppingCart shoppingCart) {
        repository.insert(shoppingCart);
    }

    public void update(ShoppingCart shoppingCart) {
        repository.update(shoppingCart);
    }

    public void delete(ShoppingCart shoppingCart) {
        repository.delete(shoppingCart);
    }

    public void deleteAllNotes() {
        repository.deleteAllShoppingItems();
    }

    public LiveData<List<ShoppingCart>> getAllShoppingCartsByUserId(int userId) {
        return someShoppingCarts;

    }
    public LiveData<List<ShoppingCart>> getAllShoppingCarts() {
        return allShoppingCarts;
    }
}