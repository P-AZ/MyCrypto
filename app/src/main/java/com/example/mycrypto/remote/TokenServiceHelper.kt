package com.example.mycrypto.remote

import com.example.mycrypto.models.TokenResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface TokenServiceHelper {

    /** getAllTokens returns the top 100 cryptocurrencies */
    @GET("listings/latest")
    suspend fun getAllTokens(
        @Query("CMC_PRO_API_KEY") apiKey: String
    ): Response<TokenResponse>
}

class TokenServiceHelperImpl @Inject constructor (private val tokenService: TokenEndPoints) : TokenServiceHelper {
    override suspend fun getAllTokens(apiKey: String): Response<TokenResponse> =
        tokenService.getAllTokens(apiKey)
}