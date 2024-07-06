package com.ipostu.mybasicsample.db.entity;

import androidx.room.Entity;
import androidx.room.Fts4;

@Entity(tableName = "productsFts")
@Fts4(contentEntity = ProductEntity.class)
public class ProductFullTextSearchEntity {
    private String name;
    private String description;

    public ProductFullTextSearchEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
