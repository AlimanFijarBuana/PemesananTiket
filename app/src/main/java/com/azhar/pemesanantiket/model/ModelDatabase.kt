package com.azhar.pemesanantiket.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_travel")
data class ModelDatabase(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    var uid: Int = 0,

    @ColumnInfo(name = "nama_penumpang")
    var namaPenumpang: String,

    @ColumnInfo(name = "keberangkatan")
    var keberangkatan: String,

    @ColumnInfo(name = "tujuan")
    var tujuan: String,

    @ColumnInfo(name = "tanggal")
    var tanggal: String,

    @ColumnInfo(name = "harga_tiket")
    var hargaTiket: Int = 0,

    @ColumnInfo(name = "anak_anak")
    var anakAnak: Int = 0,

    @ColumnInfo(name = "dewasa")
    var dewasa: Int = 0,

    @ColumnInfo(name = "nomor_telepon")
    var nomorTelepon: String,

    @ColumnInfo(name = "kelas")
    var kelas: String,

    @ColumnInfo(name = "status")
    var status: String




) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uid)
        parcel.writeString(namaPenumpang)
        parcel.writeString(keberangkatan)
        parcel.writeString(tujuan)
        parcel.writeString(tanggal)
        parcel.writeInt(hargaTiket)
        parcel.writeInt(anakAnak)
        parcel.writeInt(dewasa)
        parcel.writeString(nomorTelepon)
        parcel.writeString(kelas)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelDatabase> {
        override fun createFromParcel(parcel: Parcel): ModelDatabase {
            return ModelDatabase(parcel)
        }

        override fun newArray(size: Int): Array<ModelDatabase?> {
            return arrayOfNulls(size)
        }
    }
}
