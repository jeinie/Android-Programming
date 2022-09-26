package com.example.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Student::class], exportSchema = false, version = 1)
abstract class MyDatabase: RoomDatabase() {
    abstract fun getMyDao(): MyDao

    companion object {
        fun getDatabase(context: Context): MyDatabase {
            val builder = Room.databaseBuilder(context, MyDatabase::class.java, "school_database")
            val db = builder.build()
            return db
        }
    }
}