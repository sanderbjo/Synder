package com.example.synder.service.module

import com.example.synder.service.AccountService
import com.example.synder.service.ImgStorageService
import com.example.synder.service.StorageService
import com.example.synder.service.impl.AccountServiceImpl
import com.example.synder.service.impl.ImgStorageServiceImpl
import com.example.synder.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideImgStorageService(impl: ImgStorageServiceImpl): ImgStorageService
}