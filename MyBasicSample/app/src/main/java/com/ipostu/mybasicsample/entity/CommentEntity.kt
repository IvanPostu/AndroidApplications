package com.ipostu.mybasicsample.entity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ipostu.mybasicsample.model.Comment
import java.util.Date

@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["productId"])]
)
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var productId: Int = 0,
    var text: String = "",
    var postedAt: Date? = null
) : Comment {

    override fun getId(): Int {
        return id
    }

    override fun getProductId(): Int {
        return productId
    }

    override fun getText(): String {
        return text
    }

    override fun getPostedAt(): Date? {
        return postedAt
    }
}
