package com.dirc.novaday.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirc.novaday.domain.model.Usuario
import com.dirc.novaday.domain.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EstadoAutenticacionUi(
    val usuarioActual: Usuario? = null,
    val mensajeEstado: String? = null
)

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _estadoUi = MutableStateFlow(EstadoAutenticacionUi())
    val estadoUi: StateFlow<EstadoAutenticacionUi> = _estadoUi.asStateFlow()

    val usuariosRegistrados: StateFlow<List<Usuario>> = usuarioRepository
        .obtenerUsuarios()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        if (nombre.isBlank() || correo.isBlank() || contrasena.isBlank()) {
            _estadoUi.value = _estadoUi.value.copy(
                mensajeEstado = "Completa nombre, correo y contraseña."
            )
            return
        }

        viewModelScope.launch {
            val resultado = usuarioRepository.registrarUsuario(
                Usuario(
                    nombre = nombre.trim(),
                    correo = correo.trim(),
                    contrasena = contrasena
                )
            )
            resultado.fold(
                onSuccess = {
                    val usuarioCreado = usuarioRepository.iniciarSesion(correo.trim(), contrasena)
                    _estadoUi.value = EstadoAutenticacionUi(
                        usuarioActual = usuarioCreado,
                        mensajeEstado = "Cuenta registrada y sesión iniciada."
                    )
                },
                onFailure = { error ->
                    _estadoUi.value = _estadoUi.value.copy(
                        mensajeEstado = error.message ?: "No se pudo registrar el usuario."
                    )
                }
            )
        }
    }

    fun iniciarSesion(correo: String, contrasena: String) {
        if (correo.isBlank() || contrasena.isBlank()) {
            _estadoUi.value = _estadoUi.value.copy(
                mensajeEstado = "Ingresa tu correo y contraseña."
            )
            return
        }

        viewModelScope.launch {
            val usuario = usuarioRepository.iniciarSesion(correo.trim(), contrasena)
            if (usuario != null) {
                _estadoUi.value = EstadoAutenticacionUi(
                    usuarioActual = usuario,
                    mensajeEstado = "Bienvenida, ${usuario.nombre}."
                )
            } else {
                _estadoUi.value = _estadoUi.value.copy(
                    mensajeEstado = "Correo o contraseña incorrectos."
                )
            }
        }
    }

    fun cerrarSesion() {
        _estadoUi.value = EstadoAutenticacionUi(
            usuarioActual = null,
            mensajeEstado = "Sesión cerrada."
        )
    }

    fun limpiarMensaje() {
        _estadoUi.value = _estadoUi.value.copy(mensajeEstado = null)
    }
}