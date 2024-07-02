package com.ipostu.mybasicsample.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ipostu.mybasicsample.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey var id: Int = 0,
    var name: String = "",
    var description: String = "",
    var price: Int = 0
) : Product {

    constructor(product: Product) :
            this(
                id = product.getId(),
                name = product.getName() ?: "",
                description = product.getDescription() ?: "",
                price = product.getPrice()
            )

    override fun getId(): Int {
        return id
    }

    override fun getName(): String {
        return name
    }

    override fun getDescription(): String {
        return description
    }

    override fun getPrice(): Int {
        return price
    }
}
