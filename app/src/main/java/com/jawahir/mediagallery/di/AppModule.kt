package com.jawahir.mediagallery.di

import android.content.Context
import com.jawahir.multimediagallery.database.repository.MediaRepository
import com.jawahir.multimediagallery.database.repository.MediaRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMediaRepository(@ApplicationContext app: Context):MediaRepository = MediaRepositoryImpl(app)
}