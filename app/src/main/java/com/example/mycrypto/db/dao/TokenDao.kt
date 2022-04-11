package com.example.mycrypto.db.dao

import androidx.room.*
import com.example.mycrypto.models.Token
import kotlinx.coroutines.flow.Flow

@Dao
interface TokenDao {

    //Incase of 2 identical token -> replace the old one with the new one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTokenFavorites(token: Token)

    @Update
    fun updateTokenFavorites(token: Token)

    @Delete
    fun deleteTokenFavorites(token: Token)

    @Query("SELECT * FROM Token ORDER BY cmc_rank")
    fun getAllFavorites(): Flow<List<Token>>

}