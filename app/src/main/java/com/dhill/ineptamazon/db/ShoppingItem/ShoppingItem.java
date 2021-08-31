package com.dhill.ineptamazon.db.ShoppingItem;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ShoppingItem_table")
public class ShoppingItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private int supply;

    public ShoppingItem(String name, String description, int supply) {
        this.name = name;
        this.description = description;
        this.supply = supply;
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
