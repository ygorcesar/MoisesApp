package com.example.moiseschallenge.player.di

import com.example.moiseschallenge.domain.repository.PlayerRepository
import com.example.moiseschallenge.player.MoisesPlayer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlayerModule {

    @Binds
    @Singleton
    abstract fun bindPlayerRepository(
        moisesPlayer: MoisesPlayer
    ): PlayerRepository
}
