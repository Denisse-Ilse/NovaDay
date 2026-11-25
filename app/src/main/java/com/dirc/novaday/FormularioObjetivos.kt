package com.dirc.novaday

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dirc.novaday.domain.model.Habito
import com.dirc.novaday.domain.model.Objetivo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun fechaDeMillis(millis: Long): String {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    formato.timeZone = TimeZone.getTimeZone("UTC")
    return formato.format(Date(millis))
}

fun formatearFecha(guardada: String): String {
    return try {
        val entrada = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        entrada.timeZone = TimeZone.getTimeZone("UTC")
        val fecha = entrada.parse(guardada) ?: return guardada
        val salida = SimpleDateFormat("d MMM yyyy", Locale("es", "MX"))
        salida.timeZone = TimeZone.getTimeZone("UTC")
        salida.format(fecha)
    } catch (e: Exception) {
        guardada
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioObjetivo(
    objetivoInicial: Objetivo? = null,
    habitos: List<Habito>,
    onGuardar: (Objetivo) -> Unit,
    onCancelar: () -> Unit
) {
    val esEdicion = objetivoInicial != null
    var titulo by remember { mutableStateOf(objetivoInicial?.titulo ?: "") }
    var metaTexto by remember { mutableStateOf(objetivoInicial?.metaTotal?.toString() ?: "") }
    var unidad by remember { mutableStateOf(objetivoInicial?.unidad ?: "") }
    var avanceTexto by remember { mutableStateOf(objetivoInicial?.avanceActual?.toString() ?: "0") }
    var idHabito by remember { mutableStateOf(objetivoInicial?.idHabito) }
    var fecha by remember { mutableStateOf(objetivoInicial?.fechaLimite) }
    var menuHabito by remember { mutableStateOf(false) }
    var mostrarCalendario by remember { mutableStateOf(false) }

    val metaNum = metaTexto.toIntOrNull() ?: 0
    val avanceNum = avanceTexto.toIntOrNull() ?: 0
    val valido = titulo.isNotBlank() && metaNum > 0
    val nombreHabito = habitos.firstOrNull { it.idHabito == idHabito }?.nombre

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(if (esEdicion) "Editar objetivo" else "Nuevo objetivo") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { if (it.length <= 40) titulo = it },
                    label = { Text("Nombre del objetivo") },
                    singleLine = true,
                    supportingText = { Text("${titulo.length}/40") }
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = metaTexto,
                        onValueChange = { if (it.all(Char::isDigit) && it.length <= 6) metaTexto = it },
                        label = { Text("Meta") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = unidad,
                        onValueChange = { unidad = it },
                        label = { Text("Unidad (libros)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = avanceTexto,
                    onValueChange = { if (it.all(Char::isDigit) && it.length <= 6) avanceTexto = it },
                    label = { Text("Avance actual") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Text("Hábito asociado", style = MaterialTheme.typography.labelLarge)
                Box {
                    OutlinedButton(onClick = { menuHabito = true }) {
                        Text(nombreHabito ?: "Sin hábito asociado")
                    }
                    DropdownMenu(
                        expanded = menuHabito,
                        onDismissRequest = { menuHabito = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sin hábito asociado") },
                            onClick = {
                                idHabito = null
                                menuHabito = false
                            }
                        )
                        habitos.forEach { h ->
                            DropdownMenuItem(
                                text = { Text(h.nombre) },
                                onClick = {
                                    idHabito = h.idHabito
                                    menuHabito = false
                                }
                            )
                        }
                    }
                }

                Text("Fecha límite", style = MaterialTheme.typography.labelLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(onClick = { mostrarCalendario = true }) {
                        Text(fecha?.let { formatearFecha(it) } ?: "Elegir fecha")
                    }
                    if (fecha != null) {
                        TextButton(onClick = { fecha = null }) { Text("Quitar") }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                enabled = valido,
                onClick = {
                    onGuardar(
                        Objetivo(
                            idObjetivo = objetivoInicial?.idObjetivo ?: 0,
                            titulo = titulo.trim(),
                            metaTotal = metaNum,
                            avanceActual = avanceNum,
                            unidad = unidad.trim(),
                            fechaLimite = fecha,
                            idHabito = idHabito
                        )
                    )
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )

    if (mostrarCalendario) {
        val estadoFecha = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    estadoFecha.selectedDateMillis?.let { fecha = fechaDeMillis(it) }
                    mostrarCalendario = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = estadoFecha)
        }
    }
}