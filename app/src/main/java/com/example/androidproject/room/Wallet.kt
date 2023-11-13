package com.example.androidproject.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Wallet(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val walletName: String,
    val walletAmount: Float,
    val walletCurrency: String

    //TODO: Add the currency type and commit before moving on
)