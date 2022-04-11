package com.example.mycrypto.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mycrypto.db.dao.TokenDao
import com.example.mycrypto.models.Token


@Database(entities =  [
    Token::class
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao
}