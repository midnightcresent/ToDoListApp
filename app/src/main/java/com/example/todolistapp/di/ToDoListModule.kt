package com.example.todolistapp.di

import android.content.Context
import androidx.room.Room
import com.example.todolistapp.data.ToDoListApi
import com.example.todolistapp.data.ToDoListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ToDoListModule {

    @Provides
    @Singleton
    fun provideToDoListApi(@ApplicationContext context: Context): ToDoListApi {
        val db by lazy {
            Room.databaseBuilder(
                context,
                ToDoListDatabase::class.java,
                "to_do_list.db"
            ).build()
        }
        return db.toDoListApi
    }
}