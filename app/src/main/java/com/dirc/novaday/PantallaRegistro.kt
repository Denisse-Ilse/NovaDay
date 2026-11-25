package com.dirc.novaday

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dirc.novaday.domain.model.Registro
import com.dirc.novaday.ui.theme.Habitoviewmodel
import com.dirc.novaday.ui.theme.NovaDayTheme

@Composable
fun PantallaRegistro(
    modifier: Modifier = Modifier,
    viewModel: Habitoviewmodel = hiltViewModel()
) {
    val itemsHoy by viewModel.habitosHoy.collectAsStateWithLifecycle()
    val registrosSimulados = itemsHoy.map { item ->
        Registro(
            idRegistro = item.idRegistroHoy,
            idHabito = item.habito.idHabito,
            fecha = "",
            hora = "",
            completado = item.completadoHoy
        )
    }

    BodyPantallaRegistro(
        modifier = modifier,
        registros = registrosSimulados,
        onEliminarRegistro = { registro ->
            // Aquí podrás conectar una acción en Habitoviewmodel cuando quieras
        }
    )
}

@Composable
fun BodyPantallaRegistro(
    modifier: Modifier = Modifier,
    registros: List<Registro>,
    onEliminarRegistro: (Registro) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Registro",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(16.dp))

        if (registros.isEmpty()) {
            Text(
                text = "Aún no hay registros guardados.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        } else {
            registros.forEach { reg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Hábito #${reg.idHabito}",
                                fontWeight = FontWeight.Bold
                            )
                            val detalle = listOf(reg.fecha, reg.hora)
                                .filter { it.isNotBlank() }
                                .joinToString(" · ")
                            if (detalle.isNotEmpty()) {
                                Text(
                                    text = detalle,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Text(
                            text = if (reg.completado) "Completado" else "Pendiente",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaRegistroPreview() {
    NovaDayTheme {
        BodyPantallaRegistro(
            registros = listOf(
                Registro(
                    idRegistro = 1,
                    idHabito = 1,
                    fecha = "2026-11-19",
                    hora = "08:30",
                    completado = true
                ),
                Registro(
                    idRegistro = 2,
                    idHabito = 2,
                    fecha = "2026-11-19",
                    hora = "09:15",
                    completado = false
                )
            )
        )
    }
}