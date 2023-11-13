package ge.kinseed.taskapp.main.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ge.kinseed.taskapp.main.data.api.ExchangeApi
import ge.kinseed.taskapp.main.data.repository.ExchangeRepositoryImpl
import ge.kinseed.taskapp.main.domain.repository.ExchangeRepository
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {
    @Singleton
    @Binds
    abstract fun bindExchangeRepository(impl: ExchangeRepositoryImpl): ExchangeRepository

    companion object {
        private const val BASE_URL = "https://developers.paysera.com/"

        @Singleton
        @Provides
        fun provideRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        @Provides
        @Singleton
        fun provideExchangeApi(retrofit: Retrofit): ExchangeApi =
            retrofit.create(ExchangeApi::class.java)
    }
}