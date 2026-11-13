package com.example.miprimeraapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListaRecordatoriosViewModel : ViewModel() {

    // Usamos la instancia global
    private val repository = RepositorioGlobal.instancia

    private val _uiState = MutableStateFlow<ListaRecordatoriosUiState>(ListaRecordatoriosUiState.Cargando)
    val uiState: StateFlow<ListaRecordatoriosUiState> = _uiState.asStateFlow()

    init {
        cargarRecordatorios()
    }

    fun cargarRecordatorios() {
        viewModelScope.launch {
            _uiState.value = ListaRecordatoriosUiState.Cargando
            delay(1500)
            try {
                repository.observarRecordatorios().collect { lista ->
                    if (lista.isEmpty()) {
                        _uiState.value = ListaRecordatoriosUiState.Vacia
                    } else {
                        _uiState.value = ListaRecordatoriosUiState.ConDatos(lista)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ListaRecordatoriosUiState.Error("Error al cargar los datos")
            }
        }
    }

    fun simularError() {
        _uiState.value = ListaRecordatoriosUiState.Error("No se pudo conectar con el servidor. Revisa tu internet.")
    }
}