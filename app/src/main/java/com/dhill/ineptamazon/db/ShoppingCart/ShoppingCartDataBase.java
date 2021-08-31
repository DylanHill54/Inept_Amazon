package com.dhill.ineptamazon.db.ShoppingCart;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;



import com.dhill.ineptamazon.db.User.User;

@Database(entities = {ShoppingCart.class, User.class},version = 1,exportSchema = false )
public abstract class ShoppingCartDataBase extends RoomDatabase {
    private static ShoppingCartDataBase instance;
    public abstract ShoppingCartDAO shoppingCartDAO();

    public static synchronized ShoppingCartDataBase getInstance (Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, ShoppingCartDataBase.class, "Shoppingcart_table")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();

        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new ShoppingCartDataBase.PopulateDbAsyncTask(instance).execute();
        }
    };


    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ShoppingCartDAO shoppingCartDAO;
        private PopulateDbAsyncTask(ShoppingCartDataBase db) {
            shoppingCartDAO = db.shoppingCartDAO();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }


}
