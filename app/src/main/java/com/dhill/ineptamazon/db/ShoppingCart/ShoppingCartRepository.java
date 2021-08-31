package com.dhill.ineptamazon.db.ShoppingCart;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.List;

public class ShoppingCartRepository {
    private ShoppingCartDAO shoppingCartDAO;
    private LiveData<List<ShoppingCart>> allShoppingCarts;
    private LiveData<List<ShoppingCart>> someShoppingCarts;
   public ShoppingCartRepository(Application application) {
        ShoppingCartDataBase shoppingCartDataBase = ShoppingCartDataBase.getInstance(application);
        shoppingCartDAO =shoppingCartDataBase.shoppingCartDAO();
        allShoppingCarts = shoppingCartDAO.getAllShoppingCarts();
        //allShoppingCarts = shoppingCartDAO.getAllShoppingCartsByUserId(UserId);
    }
    /*public ShoppingCartRepository(Application application,int userId) {
        ShoppingCartDataBase shoppingCartDataBase = ShoppingCartDataBase.getInstance(application);
        shoppingCartDAO =shoppingCartDataBase.shoppingCartDAO();
        allShoppingCarts = shoppingCartDAO.getAllShoppingCarts();
        someShoppingCarts = shoppingCartDAO.getAllShoppingCartsByUserId(userId);
        }*/

    public void insert(ShoppingCart shoppingCart) {
        new InsertShoppingCartAsyncTask(shoppingCartDAO).execute(shoppingCart);
    }
    public void update(ShoppingCart shoppingCart) {
        new ShoppingCartRepository.UpdateShoppingItemAsyncTask(shoppingCartDAO).execute(shoppingCart);
    }
    public void delete(ShoppingCart shoppingCart) {
        new ShoppingCartRepository.DeleteShoppingItemAsyncTask(shoppingCartDAO).execute(shoppingCart);
    }
    public void deleteAllShoppingItems() {
        new ShoppingCartRepository.DeleteShoppingItemAsyncTask(shoppingCartDAO).execute();
    }
    public LiveData<List<ShoppingCart>> getAllShoppingCarts() {
        return allShoppingCarts;
    }
    public LiveData<List<ShoppingCart>> getAllShoppingCartsByUserId() {
        return someShoppingCarts;
    }



    private static class InsertShoppingCartAsyncTask extends AsyncTask<ShoppingCart, Void, Void> {
        private ShoppingCartDAO shoppingCartDAO;
        private InsertShoppingCartAsyncTask(ShoppingCartDAO shoppingCartDAO) {
            this.shoppingCartDAO = shoppingCartDAO;
        }
        @Override
        protected Void doInBackground(ShoppingCart... shoppingCart) {
            shoppingCartDAO.insert(shoppingCart[0]);
            return null;
        }
    }
    private static class UpdateShoppingItemAsyncTask extends AsyncTask<ShoppingCart, Void, Void> {
        private ShoppingCartDAO shoppingCartDAO;
        private UpdateShoppingItemAsyncTask(ShoppingCartDAO shoppingCartDAO) {
            this.shoppingCartDAO = shoppingCartDAO;
        }
        @Override
        protected Void doInBackground(ShoppingCart... shoppingCart) {
            shoppingCartDAO.update(shoppingCart[0]);
            return null;
        }
    }
    private static class DeleteShoppingItemAsyncTask extends AsyncTask<ShoppingCart, Void, Void> {
        private ShoppingCartDAO shoppingCartDAO;
        private DeleteShoppingItemAsyncTask(ShoppingCartDAO shoppingCartDAO) {
            this.shoppingCartDAO = shoppingCartDAO;
        }
        @Override
        protected Void doInBackground(ShoppingCart... shoppingCart) {
            shoppingCartDAO.delete(shoppingCart[0]);
            return null;
        }
    }
    private static class DeleteAllShoppingItemAsyncTask extends AsyncTask<Void, Void, Void> {
        private ShoppingCartDAO shoppingCartDAO;
        private DeleteAllShoppingItemAsyncTask(ShoppingCartDAO shoppingCartDAO) {
            this.shoppingCartDAO = shoppingCartDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            shoppingCartDAO.deleteAllShoppingCarts();
            return null;
        }
    }
}
