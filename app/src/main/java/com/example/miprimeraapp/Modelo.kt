package com.example.miprimeraapp

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

// 1. El Enum de Prioridad (igual que antes)
enum class Prioridad { ALTA, MEDIA, BAJA }

// 2. La Data Class actualizada (le agregamos descripción)
data class Recordatorio(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val prioridad: Prioridad,
    val completado: Boolean = false
)

// 3. La Interfaz del Repositorio (Las reglas de qué se puede hacer)
interface RecordatoriosRepository {
    fun observarRecordatorios(): Flow<List<Recordatorio>>
    suspend fun obtenerPorId(id: Long): Recordatorio?
    suspend fun guardar(recordatorio: Recordatorio)
}

// 4. La Implementación en Memoria (La "base de datos" falsa)
class RecordatoriosEnMemoriaRepository : RecordatoriosRepository {
    // MutableStateFlow es como una caja que avisa cuando cambia su contenido
    private val recordatorios = MutableStateFlow<List<Recordatorio>>(emptyList())

    override fun observarRecordatorios(): Flow<List<Recordatorio>> = recordatorios

    override suspend fun obtenerPorId(id: Long): Recordatorio? {
        return recordatorios.value.firstOrNull { it.id == id }
    }

    override suspend fun guardar(recordatorio: Recordatorio) {
        recordatorios.update { listaActual ->
            // Si el ID ya existe, lo actualiza. Si no, lo agrega.
            if (listaActual.any { it.id == recordatorio.id }) {
                listaActual.map { if (it.id == recordatorio.id) recordatorio else it }
            } else {
                listaActual + recordatorio
            }
        }
    }
}

// 5. Estados de la Interfaz (Sealed Interface)
// Esto sirve para manejar los estados de la pantalla de listado
sealed interface ListaRecordatoriosUiState {
    data object Cargando : ListaRecordatoriosUiState
    data object Vacia : ListaRecordatoriosUiState
    data class ConDatos(val recordatorios: List<Recordatorio>) : ListaRecordatoriosUiState
    data class Error(val mensaje: String) : ListaRecordatoriosUiState
}

// 6. Estado para la pantalla de Creación
data class CrearRecordatorioUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String = "Trabajo", // Categoría por defecto
    val prioridad: Prioridad = Prioridad.MEDIA,
    val errorTitulo: String? = null,
    val errorDescripcion: String? = null,
    val enviando: Boolean = false,
    val guardadoExitoso: Boolean = false,
    val errorGeneral: String? = null
) {
    // Calculamos si el formulario es válido en base a las reglas
    val formularioValido: Boolean
        get() = titulo.trim().length >= 4 && descripcion.length <= 200
}
object RepositorioGlobal {
    val instancia: RecordatoriosRepository = RecordatoriosEnMemoriaRepository()
}