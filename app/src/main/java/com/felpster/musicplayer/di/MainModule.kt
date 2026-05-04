package com.felpster.musicplayer.di

import com.felpster.musicplayer.data.SongRepositoryImpl
import com.felpster.musicplayer.data.remote.AlbumResponseAdapter
import com.felpster.musicplayer.data.remote.ItunesApi
import com.felpster.musicplayer.domain.SongRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {

    @Binds
    abstract fun bindsSongRepository(it: SongRepositoryImpl): SongRepository

    companion object {

        @Provides
        fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        fun provideUserApi(): ItunesApi {
            val moshi =
                Moshi.Builder()
                    .add(AlbumResponseAdapter())
                    .add(KotlinJsonAdapterFactory())
                    .build()

            return Retrofit.Builder()
                .baseUrl("https://itunes.apple.com")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ItunesApi::class.java)
        }

    }
}