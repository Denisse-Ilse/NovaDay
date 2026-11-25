package com.dirc.novaday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dirc.novaday.ui.theme.NovaDayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NovaDayTheme {
                NovoDayApp()
            }
        }
    }
}

@Composable
fun NovoDayApp() {
    var seccion by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            BarraInferior(
                seleccionado = seccion,
                onSeleccion = { nuevaSeccion -> seccion = nuevaSeccion }
            )
        }
    ) { padding ->
        when (seccion) {
            0 -> PrimeraPantalla(modifier = Modifier.padding(padding))
            1 -> PantallaHabitos(modifier = Modifier.padding(padding))
            2 -> PantallaObjetivos(modifier = Modifier.padding(padding))
            else -> Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.proximamente))
            }
        }
    }
}

@Composable
fun BarraInferior(seleccionado: Int, onSeleccion: (Int) -> Unit) {
    val items = listOf(
        R.string.nav_inicio to Icons.Default.Home,
        R.string.nav_habitos to Icons.Default.DateRange,
        R.string.nav_objetivos to Icons.Default.Star,
        R.string.nav_progreso to Icons.Default.Check
    )
    NavigationBar {
        items.forEachIndexed { i, (nombreId, icono) ->
            val nombre = stringResource(nombreId)
            NavigationBarItem(
                selected = seleccionado == i,
                onClick = { onSeleccion(i) },
                icon = { Icon(icono, contentDescription = nombre) },
                label = { Text(nombre) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NovoDayAppPreview() {
    NovaDayTheme {
        NovoDayApp()
    }
}