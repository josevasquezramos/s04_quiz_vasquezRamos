package com.uns.s04_quiz_vasquezramos.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.uns.s04_quiz_vasquezramos.data.database.Converters

@Entity(tableName = "questions")
@TypeConverters(Converters::class)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val question: String,
    val explanation: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)
