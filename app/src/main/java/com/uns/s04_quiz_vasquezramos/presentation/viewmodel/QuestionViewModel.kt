package com.uns.s04_quiz_vasquezramos.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uns.s04_quiz_vasquezramos.data.model.QuestionEntity
import com.uns.s04_quiz_vasquezramos.data.repository.QuestionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuestionViewModel(private val repository: QuestionRepository) : ViewModel() {

    private val _questions: StateFlow<List<QuestionEntity>> by lazy {
        repository.getAllQuestions()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    val questions: StateFlow<List<QuestionEntity>>
        get() = _questions

    init {
        viewModelScope.launch {
            repository.insertDefaultQuestionsIfEmpty()
        }
    }
}
