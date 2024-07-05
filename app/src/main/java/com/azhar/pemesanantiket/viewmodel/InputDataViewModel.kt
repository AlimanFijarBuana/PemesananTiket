package com.azhar.pemesanantiket.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.azhar.pemesanantiket.database.DatabaseClient.Companion.getInstance
import com.azhar.pemesanantiket.database.dao.DatabaseDao
import com.azhar.pemesanantiket.model.ModelDatabase
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class InputDataViewModel(application: Application) : AndroidViewModel(application) {
    private var databaseDao: DatabaseDao
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    init {
        databaseDao = getInstance(application).appDatabase.databaseDao()
    }

    fun addDataPemesan(
        nama_penumpang: String, keberangkatan: String,
        tujuan: String, tanggal: String, nomor_telepon: String,
        anak_anak: Int, dewasa: Int, harga_tiket: Int, kelas: String, status: String
    ) {
        Completable.fromAction {
            val modelDatabase = ModelDatabase(
                namaPenumpang = nama_penumpang,
                keberangkatan = keberangkatan,
                tujuan = tujuan,
                tanggal = tanggal,
                nomorTelepon = nomor_telepon,
                anakAnak = anak_anak,
                dewasa = dewasa,
                hargaTiket = harga_tiket,
                kelas = kelas,
                status = status
            )
            // Insert to local Room database
            databaseDao.insertData(modelDatabase)

            // Send to Firebase Realtime Database
            val databaseReference = firebaseDatabase.reference.child("pemesanan")
            val key = databaseReference.push().key ?: ""
            databaseReference.child(key).setValue(modelDatabase)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}
