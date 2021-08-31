package com.dhill.ineptamazon.db.ShoppingCart;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "Shoppingcart_table")
public class ShoppingCart {
        @PrimaryKey(autoGenerate = true)
        private int id;
        private String name;
        private String description;
        private int supply;
        private String UserId;

    public ShoppingCart(String name, String description, int supply, String userId) {
        this.name = name;
        this.description = description;
        this.supply = supply;
        UserId = userId;
    }

    public ShoppingCart() {

    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getSupply() {
            return supply;
        }

        public void setSupply(int supply) {
            this.supply = supply;
        }
}
