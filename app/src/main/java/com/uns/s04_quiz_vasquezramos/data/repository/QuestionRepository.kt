package com.uns.s04_quiz_vasquezramos.data.repository

import com.uns.s04_quiz_vasquezramos.data.dao.QuestionDao
import com.uns.s04_quiz_vasquezramos.data.model.QuestionEntity
import kotlinx.coroutines.flow.Flow

class QuestionRepository(private val dao: QuestionDao) {
    fun getAllQuestions(): Flow<List<QuestionEntity>> = dao.getAllQuestions()

    suspend fun insertDefaultQuestionsIfEmpty() {
        if (dao.getCount() == 0) {
            dao.insertAll(defaultQuestions())
        }
    }

    private fun defaultQuestions(): List<QuestionEntity> = listOf(
        QuestionEntity(
            question = "Componente que representa una interfaz de usuario",
            explanation = "Un Activity representa una sola pantalla con una interfaz de usuario.",
            options = listOf("Activity", "Service", "BroadcastReceiver", "ContentProvider"),
            correctAnswerIndex = 0
        ),
        QuestionEntity(
            question = "Componente que se ejecuta en segundo plano",
            explanation = "Un Service es un componente que se ejecuta en segundo plano sin interfaz de usuario.",
            options = listOf("Activity", "Service", "BroadcastReceiver", "ContentProvider"),
            correctAnswerIndex = 1
        ),
        QuestionEntity(
            question = "¿Qué científico y matemático ayudó a descifrar el código enigma?",
            explanation = "Alan Turing fue clave para descifrar el código Enigma durante la Segunda Guerra Mundial.",
            options = listOf("Alan Turing", "Whiston Churchil", "Joseph Stalin", "Benito Mussolini"),
            correctAnswerIndex = 0
        ),
        QuestionEntity(
            question = "¿Qué generación de red móvil se usa en la película Enterrado (2010)?",
            explanation = "En la película se hace uso de red 3G, permitiendo enviar videos y usar funciones básicas de internet.",
            options = listOf("2G", "3G", "4G", "5G"),
            correctAnswerIndex = 1
        ),
        QuestionEntity(
            question = "¿Qué generación de red móvil se usa en la película Celular (2004)?",
            explanation = "La película se basa en el uso de tecnología 2G, centrado en llamadas largas, sin funciones inteligentes o acceso a internet.",
            options = listOf("2G", "3G", "4G", "5G"),
            correctAnswerIndex = 0
        ),
        QuestionEntity(
            question = "Clase para navegar entre activities",
            explanation = "La clase Intent se usa para iniciar nuevas Activities o comunicar componentes.",
            options = listOf("View", "Intent", "LayoutInflater", "Handler"),
            correctAnswerIndex = 1
        ),
        QuestionEntity(
            question = "Layout que organiza elementos de forma vertical u horizontal",
            explanation = "LinearLayout organiza elementos en una única dirección: vertical u horizontal.",
            options = listOf("RelativeLayout", "ConstraintLayout", "LinearLayout", "FrameLayout"),
            correctAnswerIndex = 2
        ),
        QuestionEntity(
            question = "¿Qué método de la clase Activity se llama al iniciar la actividad por primera vez?",
            explanation = "El método onCreate() se ejecuta al crear la actividad por primera vez.",
            options = listOf("onStart()", "onResume()", "onRestart()", "onCreate()"),
            correctAnswerIndex = 3
        ),
        QuestionEntity(
            question = "¿Cuál clase permite mostrar un mensaje breve y no interactivo al usuario?",
            explanation = "Toast permite mostrar mensajes cortos que desaparecen automáticamente.",
            options = listOf("Snackbar", "Dialog", "Toast", "AlertDialog"),
            correctAnswerIndex = 2
        ),
        QuestionEntity(
            question = "¿Cuál clase se recomienda para manejar listas dinámicas con eficiencia?",
            explanation = "RecyclerView es más eficiente y flexible que ListView para listas grandes.",
            options = listOf("ListView", "ScrollView", "ArrayAdapter", "RecyclerView"),
            correctAnswerIndex = 3
        ),
        QuestionEntity(
            question = "No es una carpeta clave en el entorno de Android Studio",
            explanation = "Adapter no es una carpeta clave del proyecto; Manifest, Java y Res sí lo son.",
            options = listOf("Manifest", "Java", "Adapter", "Res"),
            correctAnswerIndex = 2
        ),
        QuestionEntity(
            question = "No es un componente Nativo (Widget)",
            explanation = "MainActivity es una clase, no un componente visual como los widgets.",
            options = listOf("MainActivity", "TextView", "Button", "EditText"),
            correctAnswerIndex = 0
        ),
        QuestionEntity(
            question = "¿Qué método de View se utiliza para asignar un listener de clic?",
            explanation = "setOnClickListener() es el método usado para detectar clics en vistas.",
            options = listOf("setClickListener()", "addClickListener()", "setOnClickListener()", "registerClick()"),
            correctAnswerIndex = 2
        ),
        QuestionEntity(
            question = "¿En qué carpeta deben colocarse los íconos de la aplicación?",
            explanation = "Los íconos y otros recursos gráficos deben colocarse en res/drawable.",
            options = listOf("res/raw", "res/anim", "res/drawable", "res/layout"),
            correctAnswerIndex = 2
        ),
        QuestionEntity(
            question = "¿En qué carpeta se definen los archivos XML que contienen las interfaces de usuario?",
            explanation = "Los archivos XML de interfaz gráfica se colocan en res/layout.",
            options = listOf("res/drawable", "res/layout", "res/xml", "res/values"),
            correctAnswerIndex = 1
        )
    )
}