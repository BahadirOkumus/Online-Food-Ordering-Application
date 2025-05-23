package com.example.yemekuygulamasi.di

import com.example.yemekuygulamasi.data.datasource.YemeklerDataSource
import com.example.yemekuygulamasi.data.repo.YemeklerRepository
import com.example.yemekuygulamasi.retrofit.ApiUtils
import com.example.yemekuygulamasi.retrofit.YemeklerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
@Singleton
fun provideYemeklerRepository(yemeklerDataSource: YemeklerDataSource) : YemeklerRepository{
    return YemeklerRepository(yemeklerDataSource)
}

    @Provides
    @Singleton
    fun provideYemeklerDataSource(YemeklerDao: YemeklerDao) : YemeklerDataSource{
        return YemeklerDataSource(YemeklerDao)
    }
    @Provides
    @Singleton
    fun provideYemeklerDao() : YemeklerDao {
        return ApiUtils.getYemeklerDao()
    }
}
