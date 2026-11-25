package com.dirc.novaday.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirc.novaday.domain.model.Habito
import com.dirc.novaday.domain.model.Objetivo
import com.dirc.novaday.domain.repository.HabitoRepository
import com.dirc.novaday.domain.repository.ObjetivoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ObjetivoConHabito(
    val objetivo: Objetivo,
    val nombreHabito: String?
)

@HiltViewModel
class ObjetivoViewModel @Inject constructor(
    private val objetivoRepository: ObjetivoRepository,
    private val habitoRepository: HabitoRepository
) : ViewModel() {

    val habitos: StateFlow<List<Habito>> = habitoRepository.obtenerHabitos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val objetivos: StateFlow<List<ObjetivoConHabito>> = combine(
        objetivoRepository.obtenerObjetivos(),
        habitoRepository.obtenerHabitos()
    ) { listaObjetivos, listaHabitos ->
        val nombres = listaHabitos.associate { it.idHabito to it.nombre }
        listaObjetivos.map { o ->
            ObjetivoConHabito(o, o.idHabito?.let { id -> nombres[id] })
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun guardarObjetivo(objetivo: Objetivo) {
        val titulo = objetivo.titulo.trim().take(40)
        if (titulo.isEmpty()) return
        val meta = if (objetivo.metaTotal > 0) objetivo.metaTotal else 1
        val avance = objetivo.avanceActual.coerceIn(0, meta)

        viewModelScope.launch {
            objetivoRepository.insertarObjetivo(
                objetivo.copy(
                    titulo = titulo,
                    metaTotal = meta,
                    avanceActual = avance,
                    unidad = objetivo.unidad.trim()
                )
            )
        }
    }

    fun sumarAvance(objetivo: Objetivo) {
        viewModelScope.launch {
            objetivoRepository.insertarObjetivo(
                objetivo.copy(
                    avanceActual = (objetivo.avanceActual + 1).coerceAtMost(objetivo.metaTotal)
                )
            )
        }
    }

    fun eliminarObjetivo(objetivo: Objetivo) {
        viewModelScope.launch {
            objetivoRepository.eliminarObjetivo(objetivo)
        }
    }
}