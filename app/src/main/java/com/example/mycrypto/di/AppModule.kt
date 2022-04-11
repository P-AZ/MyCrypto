package com.example.mycrypto.di

import android.content.Context
import androidx.room.Room
import com.example.mycrypto.db.AppDatabase
import com.example.mycrypto.db.dao.TokenDao
import com.example.mycrypto.remote.TokenEndPoints
import com.example.mycrypto.remote.TokenServiceHelper
import com.example.mycrypto.remote.TokenServiceHelperImpl
import com.example.mycrypto.repos.TokenRepo
import com.example.mycrypto.utils.Const.BASE_URL
import com.example.mycrypto.utils.Const.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Singleton
    @Provides
    fun provideRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideTokenService(retrofit: Retrofit): TokenEndPoints = retrofit.create(TokenEndPoints::class.java)

    @Singleton
    @Provides
    fun provideTokenServiceHelper(tokenServiceHelper: TokenServiceHelperImpl): TokenServiceHelper = tokenServiceHelper

    @Provides
    fun provideTokenDao(appDatabase: AppDatabase): TokenDao = appDatabase.tokenDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
         Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
             DB_NAME
        ).build()

    @Provides
    @Singleton
    fun provideTokenRepo(tokenServiceHelper: TokenServiceHelper, tokenDao: TokenDao): TokenRepo = TokenRepo(tokenServiceHelper,tokenDao)
}


