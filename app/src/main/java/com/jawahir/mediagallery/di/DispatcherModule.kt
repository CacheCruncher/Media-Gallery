package com.jawahir.mediagallery.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
class DispatcherModule {
    @Provides
    fun provideIODispatcher() = Dispatchers.IO
}