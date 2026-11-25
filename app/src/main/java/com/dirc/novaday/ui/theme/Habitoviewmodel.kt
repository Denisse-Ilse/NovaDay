package com.dirc.novaday.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirc.novaday.domain.model.Habito
import com.dirc.novaday.domain.model.Registro
import com.dirc.novaday.domain.repository.HabitoRepository
import com.dirc.novaday.domain.repository.RegistroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class HabitoConEstadoHoy(
    val habito: Habito,
    val completadoHoy: Boolean,
    val idRegistroHoy: Int = 0
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class Habitoviewmodel @Inject constructor(
    private val habitoRepository: HabitoRepository,
    private val registroRepository: RegistroRepository
) : ViewModel() {

    private val fechaHoy: String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    val habitos: StateFlow<List<Habito>> = _busqueda
        .flatMapLatest { query ->
            if (query.isBlank()) {
                habitoRepository.obtenerHabitos()
            } else {
                habitoRepository.buscarHabitos(query.trim())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val habitosHoy: StateFlow<List<HabitoConEstadoHoy>> = combine(
        habitoRepository.obtenerHabitosActivos(),
        registroRepository.obtenerRegistrosPorFecha(fechaHoy)
    ) { listaHabitos, registrosHoy ->
        val mapaRegistros = registrosHoy.associateBy { it.idHabito }
        listaHabitos.map { habito ->
            val reg = mapaRegistros[habito.idHabito]
            HabitoConEstadoHoy(
                habito = habito,
                completadoHoy = reg?.completado == true,
                idRegistroHoy = reg?.idRegistro ?: 0
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun actualizarBusqueda(texto: String) {
        _busqueda.value = texto
    }

    fun agregarHabito(
        nombre: String,
        metaCantidad: Int = 1,
        metaUnidad: String = "minutos",
        diasSemana: String = "1111111"
    ) {
        val nombreLimpio = nombre.trim()
        if (nombreLimpio.isEmpty()) return
        val cantidadSegura = if (metaCantidad > 0) metaCantidad else 1
        val unidadLimpia = metaUnidad.trim().ifEmpty { "minutos" }

        val entradaDias = diasSemana.trim()
        val diasLimpios = when {
            entradaDias.length == 7 && entradaDias.all { it == '0' || it == '1' } -> entradaDias
            entradaDias.toIntOrNull() != null -> {
                val cantidadDias = entradaDias.toInt().coerceIn(1, 7)
                "1".repeat(cantidadDias).padEnd(7, '0')
            }
            entradaDias.isEmpty() -> "1111111"
            else -> "1111111"
        }

        viewModelScope.launch {
            habitoRepository.insertarHabito(
                Habito(
                    nombre = nombreLimpio,
                    metaCantidad = cantidadSegura,
                    metaUnidad = unidadLimpia,
                    diasSemana = diasLimpios,
                    activo = true,
                    fechaCreacion = fechaHoy
                )
            )
        }
    }

    fun agregarHabitoDesdeFormulario(
        nombre: String,
        meta: String,
        frecuencia: String
    ) {
        val nombreLimpio = nombre.trim()
        if (nombreLimpio.isEmpty()) return

        val cantidadExtraida = Regex("\\d+").find(meta)?.value?.toIntOrNull() ?: 1
        val unidadLimpia = meta.replace(Regex("\\d+"), "").trim().ifEmpty { "veces" }

        agregarHabito(
            nombre = nombreLimpio,
            metaCantidad = cantidadExtraida,
            metaUnidad = unidadLimpia,
            diasSemana = frecuencia
        )
    }

    fun editarHabitoDesdeFormulario(
        original: Habito,
        nombre: String,
        meta: String,
        frecuencia: String
    ) {
        val nombreLimpio = nombre.trim()
        if (nombreLimpio.isEmpty()) return

        val cantidad = Regex("\\d+").find(meta)?.value?.toIntOrNull()?.takeIf { it > 0 } ?: 1
        val unidad = meta.replace(Regex("\\d+"), "").trim().ifEmpty { "veces" }
        val dias = if (frecuencia.length == 7 && frecuencia.all { it == '0' || it == '1' }) {
            frecuencia
        } else {
            original.diasSemana
        }

        actualizarHabito(
            original.copy(
                nombre = nombreLimpio,
                metaCantidad = cantidad,
                metaUnidad = unidad,
                diasSemana = dias
            )
        )
    }

    fun alternarCompletadoHoy(item: HabitoConEstadoHoy) {
        val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val nuevoEstado = !item.completadoHoy

        viewModelScope.launch {
            registroRepository.insertarRegistro(
                Registro(
                    idRegistro = item.idRegistroHoy,
                    idHabito = item.habito.idHabito,
                    fecha = fechaHoy,
                    hora = horaActual,
                    completado = nuevoEstado
                )
            )
        }
    }

    fun actualizarHabito(habito: Habito) {
        if (habito.nombre.isBlank()) return
        viewModelScope.launch {
            habitoRepository.actualizarHabito(
                habito.copy(nombre = habito.nombre.trim())
            )
        }
    }

    fun eliminarHabito(habito: Habito) {
        viewModelScope.launch {
            habitoRepository.eliminarHabito(habito)
        }
    }
}