package com.example.mycrypto.remote

import com.example.mycrypto.models.Token
import com.example.mycrypto.models.TokenResponse
import retrofit2.Response
import retrofit2.http.*


interface TokenEndPoints {

    /** getAllTokens returns the top 100 cryptocurrencies */
    @GET("listings/latest") // need to add
    suspend fun getAllTokens(
        @Query("CMC_PRO_API_KEY") apiKey: String
    ): Response<TokenResponse>
}