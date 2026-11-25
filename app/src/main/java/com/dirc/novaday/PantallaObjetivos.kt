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
import com.dirc.novaday.domain.model.Objetivo
import com.dirc.novaday.ui.theme.NovaDayTheme
import com.dirc.novaday.ui.theme.ObjetivoConHabito
import com.dirc.novaday.ui.theme.ObjetivoViewModel

@Composable
fun PantallaObjetivos(
    modifier: Modifier = Modifier,
    viewModel: ObjetivoViewModel = hiltViewModel()
) {
    val objetivos by viewModel.objetivos.collectAsStateWithLifecycle()
    val habitos by viewModel.habitos.collectAsStateWithLifecycle()

    BodyPantallaObjetivos(
        modifier = modifier,
        objetivos = objetivos,
        habitos = habitos,
        onGuardar = { viewModel.guardarObjetivo(it) },
        onSumarAvance = { viewModel.sumarAvance(it) },
        onEliminar = { viewModel.eliminarObjetivo(it) }
    )
}

@Composable
fun BodyPantallaObjetivos(
    modifier: Modifier = Modifier,
    objetivos: List<ObjetivoConHabito>,
    habitos: List<Habito>,
    onGuardar: (Objetivo) -> Unit = {},
    onSumarAvance: (Objetivo) -> Unit = {},
    onEliminar: (Objetivo) -> Unit = {}
) {
    var mostrarFormulario by remember { mutableStateOf(false) }
    var objetivoEnEdicion by remember { mutableStateOf<Objetivo?>(null) }
    var objetivoDetalle by remember { mutableStateOf<ObjetivoConHabito?>(null) }
    var objetivoPorEliminar by remember { mutableStateOf<Objetivo?>(null) }

    val colores = MaterialTheme.colorScheme
    val enCurso = objetivos.count { it.objetivo.porcentaje < 100 }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            stringResource(R.string.objetivos_titulo),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(16.dp))

        FilledTonalButton(
            onClick = { mostrarFormulario = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.objetivos_nuevo))
        }
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.objetivos_cada_paso), fontWeight = FontWeight.Bold)
            Text(
                stringResource(R.string.objetivos_en_curso, enCurso),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.height(8.dp))

        if (objetivos.isEmpty()) {
            Text(
                stringResource(R.string.objetivos_vacio),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }

        objetivos.forEach { item ->
            val o = item.objetivo
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = colores.surface)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        o.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${o.avanceActual} de ${o.metaTotal} ${o.unidad}".trim())
                        Text(
                            "${o.porcentaje}%",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { o.porcentaje / 100f },
                        modifier = Modifier.fillMaxWidth(),
                        color = colores.tertiary,
                        trackColor = colores.primary
                    )
                    Spacer(Modifier.height(12.dp))

                    Text(
                        if (item.nombreHabito != null)
                            stringResource(R.string.objetivos_habito_asociado, item.nombreHabito)
                        else stringResource(R.string.objetivos_sin_habito),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        if (o.fechaLimite != null)
                            stringResource(R.string.objetivos_fecha, formatearFecha(o.fechaLimite))
                        else stringResource(R.string.objetivos_sin_fecha),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { objetivoDetalle = item }) {
                            Text(stringResource(R.string.objetivos_ver_detalle))
                        }
                        OutlinedButton(onClick = { objetivoEnEdicion = o }) {
                            Text(stringResource(R.string.objetivos_editar))
                        }
                    }
                }
            }
        }
    }

    if (mostrarFormulario) {
        FormularioObjetivo(
            habitos = habitos,
            onGuardar = {
                onGuardar(it)
                mostrarFormulario = false
            },
            onCancelar = { mostrarFormulario = false }
        )
    }

    objetivoEnEdicion?.let { original ->
        FormularioObjetivo(
            objetivoInicial = original,
            habitos = habitos,
            onGuardar = {
                onGuardar(it)
                objetivoEnEdicion = null
            },
            onCancelar = { objetivoEnEdicion = null }
        )
    }

    objetivoDetalle?.let { item ->
        val o = item.objetivo
        AlertDialog(
            onDismissRequest = { objetivoDetalle = null },
            title = { Text(o.titulo) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("${o.avanceActual} de ${o.metaTotal} ${o.unidad}".trim() + " · ${o.porcentaje}%")
                    Text(
                        if (item.nombreHabito != null)
                            stringResource(R.string.objetivos_habito_asociado, item.nombreHabito)
                        else stringResource(R.string.objetivos_sin_habito)
                    )
                    Text(
                        if (o.fechaLimite != null)
                            stringResource(R.string.objetivos_fecha, formatearFecha(o.fechaLimite))
                        else stringResource(R.string.objetivos_sin_fecha)
                    )
                    TextButton(onClick = {
                        objetivoPorEliminar = o
                        objetivoDetalle = null
                    }) {
                        Text(
                            stringResource(R.string.habitos_eliminar),
                            color = colores.error
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = o.avanceActual < o.metaTotal,
                    onClick = {
                        onSumarAvance(o)
                        objetivoDetalle = null
                    }
                ) {
                    Text(stringResource(R.string.objetivos_sumar))
                }
            },
            dismissButton = {
                TextButton(onClick = { objetivoDetalle = null }) {
                    Text(stringResource(R.string.objetivos_cerrar))
                }
            }
        )
    }

    objetivoPorEliminar?.let { o ->
        AlertDialog(
            onDismissRequest = { objetivoPorEliminar = null },
            title = { Text(stringResource(R.string.objetivos_eliminar_titulo)) },
            text = { Text(stringResource(R.string.objetivos_eliminar_mensaje, o.titulo)) },
            confirmButton = {
                TextButton(onClick = {
                    onEliminar(o)
                    objetivoPorEliminar = null
                }) {
                    Text(stringResource(R.string.habitos_eliminar))
                }
            },
            dismissButton = {
                TextButton(onClick = { objetivoPorEliminar = null }) {
                    Text(stringResource(R.string.form_cancelar))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaObjetivosPreview() {
    NovaDayTheme {
        BodyPantallaObjetivos(
            objetivos = listOf(
                ObjetivoConHabito(
                    Objetivo(
                        idObjetivo = 1,
                        titulo = "Leer más",
                        metaTotal = 10,
                        avanceActual = 8,
                        unidad = "libros",
                        fechaLimite = "2026-12-31",
                        idHabito = 1
                    ),
                    nombreHabito = "Leer 20 minutos"
                )
            ),
            habitos = emptyList()
        )
    }
}