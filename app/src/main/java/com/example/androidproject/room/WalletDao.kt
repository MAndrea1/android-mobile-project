package com.example.androidproject.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WalletDao {
    @Insert
    suspend fun insert(wallet: Wallet)

    @Query("SELECT * FROM Wallet")
    suspend fun getAllWallets(): List<Wallet>
}
