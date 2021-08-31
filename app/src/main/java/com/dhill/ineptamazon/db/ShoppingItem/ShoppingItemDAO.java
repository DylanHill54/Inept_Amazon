package com.dhill.ineptamazon.db.ShoppingItem;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItem;

import java.util.List;
@Dao
public interface ShoppingItemDAO {
    @Insert
    void insert(ShoppingItem...shoppingItems);

     @Update
     void update(ShoppingItem...shoppingItems);

    @Delete
    void delete(ShoppingItem...shoppingItems);

    @Query("DELETE FROM ShoppingItem_table")
    void deleteAllShoppingItems();

    @Query(" SELECT * FROM  ShoppingItem_table")
     LiveData<List<ShoppingItem>> getAllShoppingItems();
}