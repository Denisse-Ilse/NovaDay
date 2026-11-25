package com.dirc.novaday

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirc.novaday.domain.model.Habito
import com.dirc.novaday.ui.theme.Habitoviewmodel
import com.dirc.novaday.ui.theme.NovaDayTheme

@Composable
fun PantallaHabitos(
    modifier: Modifier = Modifier,
    viewModel: Habitoviewmodel = hiltViewModel()
) {
    val habitos by viewModel.habitos.collectAsStateWithLifecycle()
    val busqueda by viewModel.busqueda.collectAsStateWithLifecycle()

    BodyPantallaHabitos(
        modifier = modifier,
        habitos = habitos,
        busqueda = busqueda,
        onBuscarChange = { viewModel.actualizarBusqueda(it) },
        onGuardarHabito = { nombre, meta, frecuencia ->
            viewModel.agregarHabitoDesdeFormulario(nombre, meta, frecuencia)
        },
        onEditarHabito = { original, nombre, meta, frecuencia ->
            viewModel.editarHabitoDesdeFormulario(original, nombre, meta, frecuencia)
        },
        onCambiarActivo = { habito ->
            viewModel.actualizarHabito(habito.copy(activo = !habito.activo))
        },
        onEliminarHabito = { habito -> viewModel.eliminarHabito(habito) }
    )
}

@Composable
fun BodyPantallaHabitos(
    modifier: Modifier = Modifier,
    habitos: List<Habito>,
    busqueda: String,
    onBuscarChange: (String) -> Unit,
    onGuardarHabito: (nombre: String, meta: String, frecuencia: String) -> Unit,
    onEditarHabito: (Habito, String, String, String) -> Unit = { _, _, _, _ -> },
    onCambiarActivo: (Habito) -> Unit = {},
    onEliminarHabito: (Habito) -> Unit = {}
) {
    var mostrarFormulario by remember { mutableStateOf(false) }
    var habitoEnEdicion by remember { mutableStateOf<Habito?>(null) }
    var habitoMenuAbierto by remember { mutableStateOf<Habito?>(null) }
    var habitoPorEliminar by remember { mutableStateOf<Habito?>(null) }

    val colores = MaterialTheme.colorScheme
    val activosCount = habitos.count { it.activo }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.habitos_titulo),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = onBuscarChange,
            label = { Text(stringResource(R.string.habitos_buscar)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { mostrarFormulario = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.habitos_crear))
        }
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.habitos_tus_habitos),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.habitos_activos, activosCount),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.height(8.dp))

        if (habitos.isEmpty()) {
            Text(
                text = stringResource(R.string.habitos_vacio),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        } else {
            habitos.forEach { h ->
                val metaTexto = "${h.metaCantidad} ${h.metaUnidad}".trim()
                val detalle = listOf(metaTexto, formatearDias(h.diasSemana))
                    .filter { it.isNotBlank() }
                    .joinToString(" · ")

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = h.nombre, fontWeight = FontWeight.Bold)
                            Text(
                                text = stringResource(
                                    if (h.activo) R.string.habitos_activo
                                    else R.string.habitos_inactivo
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                color = if (h.activo) colores.onSurface else colores.secondary
                            )
                        }
                        Text(text = detalle, style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(onClick = { habitoEnEdicion = h }) {
                                Text(stringResource(R.string.habitos_editar))
                            }
                            Box {
                                OutlinedButton(onClick = { habitoMenuAbierto = h }) {
                                    Text(stringResource(R.string.habitos_mas_opciones))
                                }
                                DropdownMenu(
                                    expanded = habitoMenuAbierto?.idHabito == h.idHabito,
                                    onDismissRequest = { habitoMenuAbierto = null }
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                stringResource(
                                                    if (h.activo) R.string.habitos_desactivar
                                                    else R.string.habitos_activar
                                                )
                                            )
                                        },
                                        onClick = {
                                            onCambiarActivo(h)
                                            habitoMenuAbierto = null
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.habitos_eliminar)) },
                                        onClick = {
                                            habitoPorEliminar = h
                                            habitoMenuAbierto = null
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
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

    habitoEnEdicion?.let { original ->
        FormularioHabito(
            habitoInicial = original,
            onGuardar = { nombre, meta, frecuencia ->
                onEditarHabito(original, nombre, meta, frecuencia)
                habitoEnEdicion = null
            },
            onCancelar = { habitoEnEdicion = null }
        )
    }

    habitoPorEliminar?.let { h ->
        AlertDialog(
            onDismissRequest = { habitoPorEliminar = null },
            title = { Text(stringResource(R.string.eliminar_titulo)) },
            text = { Text(stringResource(R.string.eliminar_mensaje, h.nombre)) },
            confirmButton = {
                TextButton(onClick = {
                    onEliminarHabito(h)
                    habitoPorEliminar = null
                }) {
                    Text(stringResource(R.string.habitos_eliminar))
                }
            },
            dismissButton = {
                TextButton(onClick = { habitoPorEliminar = null }) {
                    Text(stringResource(R.string.form_cancelar))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaHabitosPreview() {
    NovaDayTheme {
        BodyPantallaHabitos(
            habitos = listOf(
                Habito(
                    idHabito = 1,
                    nombre = "Leer 20 minutos",
                    metaCantidad = 20,
                    metaUnidad = "minutos",
                    diasSemana = "1111111",
                    activo = true
                )
            ),
            busqueda = "",
            onBuscarChange = {},
            onGuardarHabito = { _, _, _ -> }
        )
    }
}