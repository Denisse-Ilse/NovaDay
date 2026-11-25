package com.dirc.novaday

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dirc.novaday.domain.model.Habito

@Composable
fun FormularioHabito(
    habitoInicial: Habito? = null,
    onGuardar: (nombre: String, meta: String, frecuencia: String) -> Unit,
    onCancelar: () -> Unit
) {
    val esEdicion = habitoInicial != null
    var nombre by remember { mutableStateOf(habitoInicial?.nombre ?: "") }
    var meta by remember {
        mutableStateOf(
            habitoInicial?.let { "${it.metaCantidad} ${it.metaUnidad}".trim() } ?: ""
        )
    }
    var errorNombre by remember { mutableStateOf(false) }
    val dias = remember {
        val m = habitoInicial?.diasSemana.orEmpty()
        val valida = m.length == 7 && m.all { it == '0' || it == '1' }
        mutableStateListOf(*Array(7) { i -> if (valida) m[i] == '1' else true })
    }
    val hayDias = dias.any { it }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(if (esEdicion) "Editar hábito" else "Nuevo hábito") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        if (it.length <= 40) nombre = it
                        errorNombre = false
                    },
                    label = { Text("Nombre") },
                    singleLine = true,
                    isError = errorNombre,
                    supportingText = {
                        if (errorNombre) Text("Escribe un nombre para el hábito")
                        else Text("${nombre.length}/40")
                    }
                )
                OutlinedTextField(
                    value = meta,
                    onValueChange = { meta = it },
                    label = { Text("Meta (ej. 30 min)") },
                    singleLine = true
                )
                Text("Días", style = MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    LETRAS_DIAS.forEachIndexed { i, letra ->
                        DiaToggle(
                            letra = letra,
                            activo = dias[i],
                            modifier = Modifier.weight(1f),
                            onClick = { dias[i] = !dias[i] }
                        )
                    }
                }
                if (!hayDias) {
                    Text(
                        "Elige al menos un día",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = hayDias,
                onClick = {
                    if (nombre.isBlank()) {
                        errorNombre = true
                    } else {
                        val mascara = dias.joinToString("") { if (it) "1" else "0" }
                        onGuardar(nombre.trim(), meta.trim(), mascara)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun DiaToggle(
    letra: String,
    activo: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (activo) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (activo) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.aspectRatio(1f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(letra, style = MaterialTheme.typography.labelLarge)
        }
    }
}