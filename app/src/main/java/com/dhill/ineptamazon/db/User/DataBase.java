package com.dhill.ineptamazon.db.User;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItemDatabase;

@Database(entities = {User.class},version = 1,exportSchema = false )
public abstract class DataBase extends RoomDatabase {
    public static final String USER_TABLE = "USER_TABLE";
    private static DataBase userDatabase;
    private static DataBase instance;


    public abstract IneptDAO getIneptDAO();

    public static synchronized DataBase getUserDatabase(Context context) {
        if (userDatabase == null) {
            userDatabase = Room.databaseBuilder(context.getApplicationContext(), DataBase.class, "user_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return userDatabase;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(userDatabase).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private IneptDAO userDAO;

        private PopulateDbAsyncTask(DataBase db) {
            userDAO = db.getIneptDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDAO.insert(new User("testuser1","testuser1"));
            userDAO.insert(new User("admin2","admin2","yes"));

            return null;
        }
    }
}





