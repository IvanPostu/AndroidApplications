package com.ipostu.mybasicsample.model

import java.util.Date

interface Comment {
    fun getId(): Int
    fun getProductId(): Int
    fun getText(): String?
    fun getPostedAt(): Date?
}
