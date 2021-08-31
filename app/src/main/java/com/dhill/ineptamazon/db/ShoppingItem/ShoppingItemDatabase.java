package com.dhill.ineptamazon.db.ShoppingItem;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dhill.ineptamazon.db.User.User;

@Database(entities = {ShoppingItem.class, User.class},version = 1,exportSchema = false )
public abstract class ShoppingItemDatabase extends RoomDatabase {

    private static ShoppingItemDatabase instance;
    public abstract ShoppingItemDAO shoppingItemDAO();

    public static synchronized ShoppingItemDatabase getInstance (Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, ShoppingItemDatabase.class, "ShoppingItem_table")
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
            new ShoppingItemDatabase.PopulateDbAsyncTask(instance).execute();
        }
    };


    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ShoppingItemDAO shoppingItemDAO;
        private PopulateDbAsyncTask(ShoppingItemDatabase db) {
            shoppingItemDAO = db.shoppingItemDAO();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            shoppingItemDAO.insert(new ShoppingItem("The Ring", "What do I have in my pocket", 1));
            shoppingItemDAO.insert(new ShoppingItem("Samurai Album", "Wake Up Samurai we have a city to burn", 2));
            shoppingItemDAO.insert(new ShoppingItem("Aldecados Jacket", "Always for the family", 3));
            shoppingItemDAO.insert(new ShoppingItem("42", "The answer", 1));
            shoppingItemDAO.insert(new ShoppingItem("RTX 3090", "I want to run everything at once", 0));
            shoppingItemDAO.insert(new ShoppingItem("Raspberry", "Good for jamming radar in space", 1));
            shoppingItemDAO.insert(new ShoppingItem("Infinity Stones", "Only thing in history invented to ensure snapping of fingers", 6));
            shoppingItemDAO.insert(new ShoppingItem("Tailed Beasts", "Apparently you can order Jinchūriki",9));
            shoppingItemDAO.insert(new ShoppingItem("Spice", "See into the future, the past, and whatever else you could want to see", 1));
            shoppingItemDAO.insert(new ShoppingItem("TEEF", "ONLY WAY TO PAY AN ORK OTHER THAN HEADBUTT", 10));
            shoppingItemDAO.insert(new ShoppingItem("Just and F", "Use these to pay respect", 7));
            shoppingItemDAO.insert(new ShoppingItem("Gnome", "Carry this asshole through a game I dare you", 1));
            shoppingItemDAO.insert(new ShoppingItem("SLEEP", "It's 1:37 am and i am finally finished with this Thing", 20));
            shoppingItemDAO.insert(new ShoppingItem("A way to sort the damn Recycler View", "None of the ways i tried worked", 1));
            shoppingItemDAO.insert(new ShoppingItem("Coding in FLow ", "Genuinely Good helpful Youtube Tutorials, he just doesn't have enough of them", 5));

            return null;
        }
    }

}

