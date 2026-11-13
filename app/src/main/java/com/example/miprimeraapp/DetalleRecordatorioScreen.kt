package com.example.miprimeraapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRecordatorioScreen(
    recordatorioId: Long,
    onVolver: () -> Unit
) {
    // Buscamos el recordatorio en el repositorio usando el ID
    var recordatorio by remember { mutableStateOf<Recordatorio?>(null) }

    LaunchedEffect(recordatorioId) {
        recordatorio = RepositorioGlobal.instancia.obtenerPorId(recordatorioId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        val item = recordatorio
        if (item != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = item.titulo, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(text = "Categoría: ${item.categoria}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Prioridad: ${item.prioridad}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Descripción:", fontWeight = FontWeight.Bold)
                Text(text = item.descripcion.ifBlank { "Sin descripción" }, style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            Text("Cargando detalles...", modifier = Modifier.padding(innerPadding))
        }
    }
}