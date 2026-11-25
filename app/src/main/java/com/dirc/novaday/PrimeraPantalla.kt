package com.dirc.novaday

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirc.novaday.domain.model.Habito
import com.dirc.novaday.ui.theme.EstadoAutenticacionUi
import com.dirc.novaday.ui.theme.Habitoviewmodel
import com.dirc.novaday.ui.theme.HabitoConEstadoHoy
import com.dirc.novaday.ui.theme.NovaDayTheme
import com.dirc.novaday.ui.theme.UsuarioViewModel

@Composable
fun PrimeraPantalla(
    modifier: Modifier = Modifier,
    viewModel: Habitoviewmodel = hiltViewModel(),
    usuarioViewModel: UsuarioViewModel = hiltViewModel()
) {
    val itemsHoy by viewModel.habitosHoy.collectAsStateWithLifecycle()
    val estadoAuth by usuarioViewModel.estadoUi.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        BodyPrimeraPantalla(
            modifier = Modifier.padding(innerPadding),
            itemsHoy = itemsHoy,
            estadoAuth = estadoAuth,
            onToggleCompletado = { item -> viewModel.alternarCompletadoHoy(item) },
            onGuardarHabito = { nombre, meta, frecuencia ->
                viewModel.agregarHabitoDesdeFormulario(
                    nombre = nombre,
                    meta = meta,
                    frecuencia = frecuencia
                )
            },
            onRegistrarUsuario = { nombre, correo, contrasena ->
                usuarioViewModel.registrarUsuario(nombre, correo, contrasena)
            },
            onIniciarSesion = { correo, contrasena ->
                usuarioViewModel.iniciarSesion(correo, contrasena)
            },
            onCerrarSesion = {
                usuarioViewModel.cerrarSesion()
            },
            onLimpiarMensajeAuth = {
                usuarioViewModel.limpiarMensaje()
            }
        )
    }
}

@Composable
fun BodyPrimeraPantalla(
    modifier: Modifier = Modifier,
    itemsHoy: List<HabitoConEstadoHoy>,
    estadoAuth: EstadoAutenticacionUi = EstadoAutenticacionUi(),
    onToggleCompletado: (HabitoConEstadoHoy) -> Unit,
    onGuardarHabito: (nombre: String, meta: String, frecuencia: String) -> Unit,
    onRegistrarUsuario: (nombre: String, correo: String, contrasena: String) -> Unit = { _, _, _ -> },
    onIniciarSesion: (correo: String, contrasena: String) -> Unit = { _, _ -> },
    onCerrarSesion: () -> Unit = {},
    onLimpiarMensajeAuth: () -> Unit = {}
) {
    var mostrarFormulario by remember { mutableStateOf(false) }
    var mostrarMenuCuenta by remember { mutableStateOf(false) }
    var modoCuenta by remember { mutableStateOf<String?>(null) } // "login" o "registro"
    var nombreUsuario by remember { mutableStateOf("") }
    var correoUsuario by remember { mutableStateOf("") }
    var contrasenaUsuario by remember { mutableStateOf("") }

    val completados = itemsHoy.count { it.completadoHoy }
    val progreso = if (itemsHoy.isEmpty()) 0f else completados.toFloat() / itemsHoy.size

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.inicio_titulo),
                    style = MaterialTheme.typography.headlineSmall
                )
                if (estadoAuth.usuarioActual != null) {
                    Text(
                        text = "Sesión: ${estadoAuth.usuarioActual.nombre}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            IconButton(onClick = {
                onLimpiarMensajeAuth()
                mostrarMenuCuenta = true
            }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Cuenta"
                )
            }
        }
        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.inicio_progreso_diario),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progreso },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        itemsHoy.forEach { item ->
            val metaTexto = if (item.habito.metaCantidad > 0 || item.habito.metaUnidad.isNotBlank()) {
                "${item.habito.metaCantidad} ${item.habito.metaUnidad}".trim()
            } else {
                ""
            }
            val detalle = listOf(metaTexto, formatearDias(item.habito.diasSemana))
                .filter { it.isNotBlank() }
                .joinToString(" · ")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = item.completadoHoy,
                    onCheckedChange = { onToggleCompletado(item) }
                )
                Column {
                    Text(text = item.habito.nombre)
                    if (detalle.isNotEmpty()) {
                        Text(text = detalle, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { mostrarFormulario = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.inicio_anadir_habito))
        }
    }

    if (mostrarMenuCuenta) {
        val usuario = estadoAuth.usuarioActual
        AlertDialog(
            onDismissRequest = { mostrarMenuCuenta = false },
            title = { Text(if (usuario != null) "Tu cuenta" else "Cuenta") },
            text = {
                if (usuario != null) {
                    Text("Conectada como ${usuario.nombre} (${usuario.correo})")
                } else {
                    Text("Elige una opción para continuar:")
                }
            },
            confirmButton = {
                if (usuario != null) {
                    TextButton(onClick = {
                        onCerrarSesion()
                        mostrarMenuCuenta = false
                    }) {
                        Text("Cerrar sesión")
                    }
                } else {
                    TextButton(onClick = {
                        mostrarMenuCuenta = false
                        onLimpiarMensajeAuth()
                        correoUsuario = ""
                        contrasenaUsuario = ""
                        modoCuenta = "login"
                    }) {
                        Text("Iniciar sesión")
                    }
                }
            },
            dismissButton = {
                if (usuario == null) {
                    TextButton(onClick = {
                        mostrarMenuCuenta = false
                        onLimpiarMensajeAuth()
                        nombreUsuario = ""
                        correoUsuario = ""
                        contrasenaUsuario = ""
                        modoCuenta = "registro"
                    }) {
                        Text("Registrarse")
                    }
                }
            }
        )
    }

    if (modoCuenta != null) {
        val esLogin = modoCuenta == "login"
        AlertDialog(
            onDismissRequest = { modoCuenta = null },
            title = { Text(if (esLogin) "Iniciar sesión" else "Registrarse") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (!esLogin) {
                        OutlinedTextField(
                            value = nombreUsuario,
                            onValueChange = { nombreUsuario = it },
                            label = { Text("Nombre") },
                            singleLine = true
                        )
                    }
                    OutlinedTextField(
                        value = correoUsuario,
                        onValueChange = { correoUsuario = it },
                        label = { Text("Correo") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = contrasenaUsuario,
                        onValueChange = { contrasenaUsuario = it },
                        label = { Text("Contraseña") },
                        singleLine = true
                    )
                    if (estadoAuth.mensajeEstado != null) {
                        Text(
                            text = estadoAuth.mensajeEstado,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (esLogin) {
                        onIniciarSesion(correoUsuario, contrasenaUsuario)
                    } else {
                        onRegistrarUsuario(nombreUsuario, correoUsuario, contrasenaUsuario)
                    }
                }) {
                    Text(if (esLogin) "Entrar" else "Crear cuenta")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onLimpiarMensajeAuth()
                    modoCuenta = null
                }) {
                    Text("Cerrar")
                }
            }
        )
    }

    if (mostrarFormulario) {
        FormularioHabito(
            onGuardar = { nombre, meta, frecuencia ->
                onGuardarHabito(nombre, meta, frecuencia)
                mostrarFormulario = false
            },
            onCancelar = { mostrarFormulario = false }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PrimeraPantallaPreview() {
    NovaDayTheme {
        BodyPrimeraPantalla(
            itemsHoy = listOf(
                HabitoConEstadoHoy(
                    habito = Habito(
                        idHabito = 1,
                        nombre = "Leer",
                        metaCantidad = 20,
                        metaUnidad = "páginas"
                    ),
                    completadoHoy = true
                ),
                HabitoConEstadoHoy(
                    habito = Habito(
                        idHabito = 2,
                        nombre = "Estudiar",
                        metaCantidad = 1,
                        metaUnidad = "hora"
                    ),
                    completadoHoy = false
                )
            ),
            onToggleCompletado = {},
            onGuardarHabito = { _, _, _ -> }
        )
    }
}