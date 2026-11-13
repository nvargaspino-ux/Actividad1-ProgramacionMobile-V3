package com.example.miprimeraapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRecordatorioScreen(
    onVolver: () -> Unit,
    onGuardadoExitoso: () -> Unit,
    viewModel: CrearRecordatorioViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Recordatorio") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo Título
            OutlinedTextField(
                value = uiState.titulo,
                onValueChange = { viewModel.actualizarTitulo(it) },
                label = { Text("Título") },
                isError = uiState.errorTitulo != null,
                supportingText = {
                    if (uiState.errorTitulo != null) {
                        Text(uiState.errorTitulo!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Descripción
            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = { viewModel.actualizarDescripcion(it) },
                label = { Text("Descripción") },
                isError = uiState.errorDescripcion != null,
                supportingText = {
                    if (uiState.errorDescripcion != null) {
                        Text(uiState.errorDescripcion!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Selección de Prioridad
            Text("Prioridad:", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Prioridad.values().forEach { prioridad ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.selectable(
                            selected = uiState.prioridad == prioridad,
                            onClick = { viewModel.seleccionarPrioridad(prioridad) }
                        )
                    ) {
                        RadioButton(
                            selected = uiState.prioridad == prioridad,
                            onClick = { viewModel.seleccionarPrioridad(prioridad) }
                        )
                        Text(text = prioridad.name)
                    }
                }
            }

            // Botón Guardar
            Button(
                onClick = { viewModel.guardarRecordatorio(onGuardadoExitoso) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = uiState.formularioValido && !uiState.enviando
            ) {
                if (uiState.enviando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar Recordatorio")
                }
            }
        }
    }
}