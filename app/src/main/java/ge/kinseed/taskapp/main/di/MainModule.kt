package ge.kinseed.taskapp.main.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ge.kinseed.taskapp.main.data.repository.EmployeeRepositoryImpl
import ge.kinseed.taskapp.main.domain.repository.EmployeeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {
    @Binds
    @Singleton
    abstract fun bindEmployeeRepository(repo: EmployeeRepositoryImpl): EmployeeRepository
}