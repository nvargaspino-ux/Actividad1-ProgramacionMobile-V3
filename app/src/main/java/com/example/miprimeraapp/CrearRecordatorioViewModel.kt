package com.example.miprimeraapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CrearRecordatorioViewModel : ViewModel() {

    private val repository = RepositorioGlobal.instancia

    private val _uiState = MutableStateFlow(CrearRecordatorioUiState())
    val uiState: StateFlow<CrearRecordatorioUiState> = _uiState.asStateFlow()

    fun actualizarTitulo(nuevoTitulo: String) {
        val error = if (nuevoTitulo.isNotEmpty() && nuevoTitulo.trim().length < 4) {
            "El título debe tener al menos 4 letras"
        } else null
        _uiState.value = _uiState.value.copy(titulo = nuevoTitulo, errorTitulo = error)
    }

    fun actualizarDescripcion(nuevaDesc: String) {
        val error = if (nuevaDesc.length > 200) {
            "La descripción no puede superar los 200 caracteres"
        } else null
        _uiState.value = _uiState.value.copy(descripcion = nuevaDesc, errorDescripcion = error)
    }

    fun seleccionarCategoria(categoria: String) {
        _uiState.value = _uiState.value.copy(categoria = categoria)
    }

    fun seleccionarPrioridad(prioridad: Prioridad) {
        _uiState.value = _uiState.value.copy(prioridad = prioridad)
    }

    fun guardarRecordatorio(onGuardadoExitoso: () -> Unit) {
        val estadoActual = _uiState.value
        if (!estadoActual.formularioValido) return

        viewModelScope.launch {
            _uiState.value = estadoActual.copy(enviando = true, errorGeneral = null)
            delay(1000)
            try {
                val nuevoRecordatorio = Recordatorio(
                    id = System.currentTimeMillis(),
                    titulo = estadoActual.titulo.trim(),
                    descripcion = estadoActual.descripcion.trim(),
                    categoria = estadoActual.categoria,
                    prioridad = estadoActual.prioridad
                )
                repository.guardar(nuevoRecordatorio)
                _uiState.value = _uiState.value.copy(enviando = false, guardadoExitoso = true)
                onGuardadoExitoso()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    enviando = false,
                    errorGeneral = "Hubo un problema al guardar. Intenta de nuevo."
                )
            }
        }
    }
}