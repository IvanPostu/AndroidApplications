package com.ipostu.mybasicsample.entity

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "productsFts")
@Fts4(contentEntity = ProductEntity::class)
data class ProductFtsEntity(
    @PrimaryKey(autoGenerate = true) val rowid: Int = 0, // FTS requires a rowid column
    val name: String,
    val description: String
)