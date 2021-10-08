package project.android.footstamp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stamps")
data class Stamp(
    @PrimaryKey val id: String,

    var area: String,
    var district: String,
    var date: String,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray?,

    var memo: String? = null

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Stamp

        if (id != other.id) return false
        if (area != other.area) return false
        if (date != other.date) return false
        if (!image.contentEquals(other.image)) return false
        if (memo != other.memo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + area.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + (memo?.hashCode() ?: 0)
        return result
    }


}
