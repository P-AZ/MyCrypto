package com.example.mycrypto.repos

import com.example.mycrypto.db.dao.TokenDao
import com.example.mycrypto.models.Token
import com.example.mycrypto.remote.TokenServiceHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


class TokenRepo @Inject constructor(
    private val tokenServiceHelper: TokenServiceHelper,
    private val tokenDao: TokenDao
) {
    private val API_KEY = "b86bd3a5-f3f2-48db-b7ae-062895370b88"

    //Get token from server
    suspend fun getAllTokens(): List<Token?>? = tokenServiceHelper.getAllTokens(API_KEY).body()?.data

    //Updating the favorites list
    fun updateTokenFavorites(token: Token) = tokenDao.updateTokenFavorites(token)

    //Getting all favorites tokens from DB
    fun getAllFavorites(): Flow<List<Token>> = tokenDao.getAllFavorites()

    //Inserting new token to the favorites list in the DB
    fun insertTokenFavorites(token: Token) = tokenDao.addTokenFavorites(token)

    //Deleteing tokem from favorites list in the DB
    fun deleteTokenFavorites(token: Token) = tokenDao.deleteTokenFavorites(token)
}

