package com.dhill.ineptamazon.db.ShoppingItem;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ShoppingItemRepository {
    private ShoppingItemDAO shoppingItemDAO;
    private LiveData<List<ShoppingItem>> allShoppingItems;
    public ShoppingItemRepository(Application application) {
        ShoppingItemDatabase shoppingItemDatabase = ShoppingItemDatabase.getInstance(application);
        shoppingItemDAO=shoppingItemDatabase.shoppingItemDAO();
        allShoppingItems = shoppingItemDAO.getAllShoppingItems();
    }
    public void insert(ShoppingItem shoppingItem) {
        new InsertShoppingItemAsyncTask(shoppingItemDAO).execute(shoppingItem);
    }
    public void update(ShoppingItem shoppingItem) {
        new UpdateShoppingItemAsyncTask(shoppingItemDAO).execute(shoppingItem);
    }
    public void delete(ShoppingItem shoppingItem) {
        new DeleteShoppingItemAsyncTask(shoppingItemDAO).execute(shoppingItem);
    }
    public void deleteAllShoppingItems() {
        new DeleteShoppingItemAsyncTask(shoppingItemDAO).execute();
    }
    public LiveData<List<ShoppingItem>> getAllShoppingItems() {
        return allShoppingItems;
    }


    private static class InsertShoppingItemAsyncTask extends AsyncTask<ShoppingItem, Void, Void> {
        private ShoppingItemDAO shoppingItemDAO;
        private InsertShoppingItemAsyncTask(ShoppingItemDAO shoppingItemDAO) {
            this.shoppingItemDAO = shoppingItemDAO;
        }
        @Override
        protected Void doInBackground(ShoppingItem... shoppingItems) {
            shoppingItemDAO.insert(shoppingItems[0]);
            return null;
        }
    }
    private static class UpdateShoppingItemAsyncTask extends AsyncTask<ShoppingItem, Void, Void> {
        private ShoppingItemDAO shoppingItemDAO;
        private UpdateShoppingItemAsyncTask(ShoppingItemDAO shoppingItemDAO) {
            this.shoppingItemDAO = shoppingItemDAO;
        }
        @Override
        protected Void doInBackground(ShoppingItem... shoppingItems) {
            shoppingItemDAO.update(shoppingItems[0]);
            return null;
        }
    }
    private static class DeleteShoppingItemAsyncTask extends AsyncTask<ShoppingItem, Void, Void> {
        private ShoppingItemDAO shoppingItemDAO;
        private DeleteShoppingItemAsyncTask(ShoppingItemDAO shoppingItemDAO) {
            this.shoppingItemDAO = shoppingItemDAO;
        }
        @Override
        protected Void doInBackground(ShoppingItem... shoppingItems) {
            shoppingItemDAO.delete(shoppingItems[0]);
            return null;
        }
    }
    private static class DeleteAllShoppingItemAsyncTask extends AsyncTask<Void, Void, Void> {
        private ShoppingItemDAO shoppingItemDAO;
        private DeleteAllShoppingItemAsyncTask(ShoppingItemDAO shoppingItemDAO) {
            this.shoppingItemDAO = shoppingItemDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            shoppingItemDAO.deleteAllShoppingItems();
            return null;
        }
    }
}

