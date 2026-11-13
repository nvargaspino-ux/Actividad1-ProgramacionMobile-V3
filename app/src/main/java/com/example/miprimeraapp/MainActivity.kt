package com.example.miprimeraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable data object ListaRoute
@Serializable data object CrearRoute
@Serializable data class DetalleRoute(val recordatorioId: Long)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavegacion()
            }
        }
    }
}

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ListaRoute) {

        composable<ListaRoute> {
            ListaRecordatoriosScreen(
                onCrearClick = { navController.navigate(CrearRoute) },
                onRecordatorioClick = { id -> navController.navigate(DetalleRoute(id)) },
                onVolver = { }
            )
        }

        composable<CrearRoute> {
            CrearRecordatorioScreen(
                onVolver = { navController.popBackStack() },
                onGuardadoExitoso = { navController.popBackStack() }
            )
        }

        composable<DetalleRoute> { backStackEntry ->
            val argumento = backStackEntry.toRoute<DetalleRoute>()
            DetalleRecordatorioScreen(
                recordatorioId = argumento.recordatorioId,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}