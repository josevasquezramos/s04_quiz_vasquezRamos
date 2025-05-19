package com.uns.s04_quiz_vasquezramos.domain.model

data class Question(
    val id: Long,
    val question: String,
    val explanation: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)
