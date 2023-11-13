package com.example.androidproject.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Wallet::class], version = 1, exportSchema = false)
abstract class WalletDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao

    // Companion object for obtaining the database instance
    companion object {
        @Volatile
        private var INSTANCE: WalletDatabase? = null

        // Function to get the database instance
        fun getDatabase(context: Context): WalletDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): WalletDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                WalletDatabase::class.java,
                "wallet_database"
            ).build()
        }
    }
}
