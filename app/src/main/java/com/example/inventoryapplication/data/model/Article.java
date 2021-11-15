package com.example.inventoryapplication.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "article_table")
public class Article {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private int price;

    private int quantity;

    private String supplier;

    private String image;

    public Article(String name, int price, int quantity, String supplier, String image) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.supplier = supplier;
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getImage() {
        return image;
    }
}
