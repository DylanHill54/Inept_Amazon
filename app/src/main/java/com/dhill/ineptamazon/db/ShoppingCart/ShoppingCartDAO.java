package com.dhill.ineptamazon.db.ShoppingCart;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dhill.ineptamazon.db.ShoppingItem.ShoppingItem;

import java.util.List;

@Dao
public interface ShoppingCartDAO {
    @Insert
    void insert(ShoppingCart...shoppingCarts);

    @Update
    void update(ShoppingCart...shoppingCarts);

    @Delete
    void delete(ShoppingCart...shoppingCarts);

    @Query("DELETE FROM Shoppingcart_table")
    void deleteAllShoppingCarts();

    @Query(" SELECT * FROM  Shoppingcart_table")
    LiveData<List<ShoppingCart>> getAllShoppingCarts();


    @Query(" SELECT * FROM  Shoppingcart_table WHERE UserID= :userId ")
    List<ShoppingCart> getAllShoppingCartsByUserId(String userId);
    //LiveData<List<ShoppingCart>> getAllShoppingCartsByUserId(int userId);
}
