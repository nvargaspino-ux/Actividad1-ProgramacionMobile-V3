package com.example.miprimeraapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ListaRecordatoriosScreen(
    onCrearClick: () -> Unit,
    onRecordatorioClick: (Long) -> Unit,
    onVolver: () -> Unit,
    viewModel: ListaRecordatoriosViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Recordatorios") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onCrearClick) {
                Icon(Icons.Default.Add, contentDescription = "Crear")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (val estado = uiState) {
                is ListaRecordatoriosUiState.Cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ListaRecordatoriosUiState.Vacia -> {
                    MensajeVacio(onCrearClick = onCrearClick)
                }
                is ListaRecordatoriosUiState.ConDatos -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(estado.recordatorios, key = { it.id }) { recordatorio ->
                            TarjetaRecordatorio(
                                recordatorio = recordatorio,
                                onClick = { onRecordatorioClick(recordatorio.id) }
                            )
                        }
                    }
                }
                is ListaRecordatoriosUiState.Error -> {
                    MensajeError(
                        mensaje = estado.mensaje,
                        onReintentar = { viewModel.cargarRecordatorios() }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaRecordatorio(recordatorio: Recordatorio, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = recordatorio.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Categoría: ${recordatorio.categoria}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Prioridad: ${recordatorio.prioridad}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = if (recordatorio.completado) "Estado: Completado" else "Estado: Pendiente",
                color = if (recordatorio.completado) Color.Green else Color.Red
            )
        }
    }
}

@Composable
fun MensajeVacio(onCrearClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No hay recordatorios por el momento.", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onCrearClick) { Text("Crear mi primer recordatorio") }
    }
}

@Composable
fun MensajeError(mensaje: String, onReintentar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = mensaje, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onReintentar) { Text("Reintentar") }
    }
}