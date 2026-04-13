package com.example.moiseschallenge.data.di

import android.content.Context
import androidx.room.Room
import com.example.moiseschallenge.data.local.MoisesDatabase
import com.example.moiseschallenge.data.local.dao.TrackDao
import com.example.moiseschallenge.data.repository.MusicRepositoryImpl
import com.example.moiseschallenge.domain.repository.MusicRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MoisesDatabase {
        return Room.databaseBuilder(
            context,
            MoisesDatabase::class.java,
            MoisesDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideTrackDao(database: MoisesDatabase): TrackDao {
        return database.trackDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindingModule {

    @Binds
    @Singleton
    abstract fun bindMusicRepository(
        musicRepositoryImpl: MusicRepositoryImpl
    ): MusicRepository
}
