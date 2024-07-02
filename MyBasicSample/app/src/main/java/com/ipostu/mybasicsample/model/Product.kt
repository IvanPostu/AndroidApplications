package com.ipostu.mybasicsample.model

interface Product {
    fun getId(): Int
    fun getName(): String?
    fun getDescription(): String?
    fun getPrice(): Int
}
