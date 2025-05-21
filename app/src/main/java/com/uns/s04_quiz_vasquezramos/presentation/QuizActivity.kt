package com.uns.s04_quiz_vasquezramos.presentation

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.os.Vibrator
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.uns.s04_quiz_vasquezramos.R
import com.uns.s04_quiz_vasquezramos.data.database.AppDatabase
import com.uns.s04_quiz_vasquezramos.data.model.QuestionEntity
import com.uns.s04_quiz_vasquezramos.data.repository.QuestionRepository
import com.uns.s04_quiz_vasquezramos.presentation.viewmodel.QuestionViewModel
import kotlinx.coroutines.launch

class QuizActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var resultText: TextView
    private lateinit var correctAnswerText: TextView
    private lateinit var explanationText: TextView
    private lateinit var btnAction: Button
    private lateinit var btnSkip: TextView
    private lateinit var btnExit: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var imgQuestion: ImageView

    private lateinit var questions: List<QuestionEntity>
    private var currentIndex = 0
    private var score = 0
    private var startTime = 0L
    private lateinit var progressText: TextView

    private var selectedOptionIndex: Int? = null
    private var answered = false

    private lateinit var vibrator: Vibrator
    private lateinit var mpCorrect: MediaPlayer
    private lateinit var mpWrong: MediaPlayer

    private lateinit var viewModel: QuestionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        mpCorrect = MediaPlayer.create(this, R.raw.right)
        mpWrong = MediaPlayer.create(this, R.raw.wrong)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "quiz_db").build()
        val repo = QuestionRepository(db.questionDao())
        viewModel = QuestionViewModel(repo)

        bindViews()

        lifecycleScope.launch {
            viewModel.questions.collect {
                questions = it
                if (questions.isNotEmpty()) {
                    startTime = SystemClock.elapsedRealtime()
                    showQuestion()
                }
            }
        }
    }

    private fun bindViews() {
        questionText = findViewById(R.id.txtQuestion)
        optionsGroup = findViewById(R.id.radioGroup)
        resultText = findViewById(R.id.txtResult)
        correctAnswerText = findViewById(R.id.txtCorrectAnswer)
        explanationText = findViewById(R.id.txtExplanation)
        btnAction = findViewById(R.id.btnAction)
        btnSkip = findViewById(R.id.btnSkip)
        btnExit = findViewById(R.id.btnExit)
        progressText = findViewById(R.id.txtProgress)
        progressBar = findViewById(R.id.progressBar)
        imgQuestion = findViewById(R.id.imgQuestion)

        optionsGroup.setOnCheckedChangeListener { _, _ ->
            if (!answered) {
                btnAction.isEnabled = true
                btnAction.isClickable = true
            }
        }

        btnAction.setOnClickListener {
            if (!answered) {
                checkAnswer()
            } else {
                nextQuestion()
            }
        }

        btnSkip.setOnClickListener {
            showResult(false)
        }

        btnExit.setOnClickListener {
            showExitConfirmationDialog()
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Salir del Quiz")
            .setMessage("¿Estás seguro de que quieres salir? Se perderá tu progreso actual.")
            .setPositiveButton("Sí") { _, _ ->
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showQuestion() {
        progressText.text = "Pregunta ${currentIndex + 1}/${questions.size}"
        progressBar.max = questions.size
        progressBar.progress = currentIndex + 1

        if (currentIndex >= questions.size) {
            goToResults()
            return
        }

        val q = questions[currentIndex]
        questionText.text = q.question

        // Cargar la imagen si existe
        val imageName = "quiz_${currentIndex + 1}" // quiz_1, quiz_2, etc.
        val resId = resources.getIdentifier(imageName, "drawable", packageName)

        if (resId != 0) {
            imgQuestion.setImageResource(resId)
            imgQuestion.visibility = View.VISIBLE
        } else {
            imgQuestion.visibility = View.GONE
        }

        optionsGroup.removeAllViews()
        optionsGroup.clearCheck()

        q.options.forEachIndexed { i, option ->
            val rb = RadioButton(this).apply {
                text = option
                id = i

                // Tamaño de texto
                textSize = 16f

                // Espaciado entre el círculo y el texto
                setPadding(
                    (24 * resources.displayMetrics.density).toInt(),
                    (10 * resources.displayMetrics.density).toInt(),
                    (24 * resources.displayMetrics.density).toInt(),
                    (10 * resources.displayMetrics.density).toInt()
                )

                // Margen entre opciones
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    if (i > 0) {
                        topMargin = (8 * resources.displayMetrics.density).toInt()
                    }

                    // Asegurar que el texto no quede muy pegado al borde derecho
                    marginEnd = (16 * resources.displayMetrics.density).toInt()
                }

                // Alinear el texto correctamente respecto al círculo
                setLineSpacing(0f, 1.2f)
            }
            optionsGroup.addView(rb)
        }

        resultText.visibility = View.GONE
        correctAnswerText.visibility = View.GONE
        explanationText.visibility = View.GONE
        btnAction.text = "Comprobar"
        btnAction.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary)
        btnAction.isEnabled = false
        btnAction.isClickable = false
        btnSkip.isEnabled = true
        answered = false
        selectedOptionIndex = null

        optionsGroup.isEnabled = true
        for (i in 0 until optionsGroup.childCount) {
            optionsGroup.getChildAt(i).isEnabled = true
        }
    }

    private fun checkAnswer() {
        val selectedId = optionsGroup.checkedRadioButtonId
        if (selectedId == -1) return

        selectedOptionIndex = selectedId
        val correct = selectedId == questions[currentIndex].correctAnswerIndex
        showResult(correct)
    }

    private fun showResult(correct: Boolean) {
        answered = true

        if (correct) {
            score++
            resultText.text = "¡Correcto!"
            mpCorrect.start()
            btnAction.backgroundTintList = ContextCompat.getColorStateList(this, R.color.green)
        } else {
            resultText.text = "Incorrecto"
            correctAnswerText.text = "Respuesta correcta: ${questions[currentIndex].options[questions[currentIndex].correctAnswerIndex]}"
            correctAnswerText.visibility = View.VISIBLE
            mpWrong.start()
            btnAction.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red)
        }

        vibrator.vibrate(200)
        explanationText.text = questions[currentIndex].explanation
        resultText.visibility = View.VISIBLE
        explanationText.visibility = View.VISIBLE
        btnAction.text = "Siguiente"
        btnAction.isEnabled = true
        btnAction.isClickable = true

        btnSkip.isEnabled = false
        optionsGroup.isEnabled = false

        for (i in 0 until optionsGroup.childCount) {
            val rb = optionsGroup.getChildAt(i) as RadioButton
            rb.isEnabled = false
        }
    }


    private fun nextQuestion() {
        currentIndex++
        showQuestion()
    }

    private fun goToResults() {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("score", score)
            putExtra("total", questions.size)
            putExtra("time", SystemClock.elapsedRealtime() - startTime)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        mpCorrect.release()
        mpWrong.release()
        super.onDestroy()
    }
}