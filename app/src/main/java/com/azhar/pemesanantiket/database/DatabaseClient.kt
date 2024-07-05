package com.azhar.pemesanantiket.database

import android.content.Context
import androidx.room.Room

/**
 * Created by Azhar Rivaldi on 19-11-2021
 * Youtube Channel : https://bit.ly/2PJMowZ
 * Github : https://github.com/AzharRivaldi
 * Twitter : https://twitter.com/azharrvldi_
 * Instagram : https://www.instagram.com/azhardvls_
 * LinkedIn : https://www.linkedin.com/in/azhar-rivaldi
 */

class DatabaseClient private constructor(context: Context) {
    var appDatabase: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "travel_db"
    )
        .fallbackToDestructiveMigration()
        .build()

    companion object {
        @Volatile
        private var mInstance: DatabaseClient? = null

        @Synchronized
        fun getInstance(context: Context): DatabaseClient {
            return mInstance ?: synchronized(this) {
                val instance = DatabaseClient(context)
                mInstance = instance
                instance
            }
        }
    }
}
