package com.uns.s04_quiz_vasquezramos.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uns.s04_quiz_vasquezramos.data.dao.QuestionDao
import com.uns.s04_quiz_vasquezramos.data.model.QuestionEntity

@Database(entities = [QuestionEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
}