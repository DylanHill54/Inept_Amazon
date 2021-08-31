package com.dhill.ineptamazon.db.ShoppingItem;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ShoppingItemViewModel extends AndroidViewModel {
        private ShoppingItemRepository repository;
        private LiveData<List<ShoppingItem>> allShoppingItems;

    public ShoppingItemViewModel(@NonNull Application application) {
            super(application);
            this.repository = new ShoppingItemRepository(application);
            allShoppingItems = repository.getAllShoppingItems();
        }
        public void insert(ShoppingItem shoppingItem) {
            repository.insert(shoppingItem);
        }
        public void update(ShoppingItem shoppingItem) {
            repository.update(shoppingItem);
        }
        public void delete(ShoppingItem shoppingItem) {
            repository.delete(shoppingItem);
        }
        public void deleteAllNotes() {
            repository.deleteAllShoppingItems();
        }
        public LiveData<List<ShoppingItem>> getAllShoppingItems() {
            return allShoppingItems;
        }
}

