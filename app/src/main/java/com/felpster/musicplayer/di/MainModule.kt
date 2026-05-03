package com.felpster.musicplayer.di

import com.felpster.musicplayer.data.remote.ItunesApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {
    companion object {

        @Provides
        fun provideUserApi(): ItunesApi {
            val moshi =
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

            return Retrofit.Builder()
                .baseUrl("http://itunes.apple.com")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ItunesApi::class.java)
        }

    }
}