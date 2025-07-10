import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.josephcamilo_ap2_p2.presentation.viajes.UiEvent
import edu.ucne.josephcamilo_ap2_p2.presentation.viajes.ViajeEvent
import edu.ucne.josephcamilo_ap2_p2.presentation.viajes.ViajeUiState
import edu.ucne.josephcamilo_ap2_p2.presentation.viajes.ViajeViewModel
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun ViajeScreen(
    viajeId: Int? = null,
    viewModel: ViajeViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viajeId) {
        viajeId?.let {
            if (it > 0) {
                viewModel.onEvent(ViajeEvent.GetViaje(it))
            }
        }
    }

    UsuarioBodyScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViajeBodyScreen(
    uiState: ViajeUiState,
    onEvent: (ViajeEvent) -> Unit,
    goBack: () -> Unit,
    viewModel: ViajeViewModel
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event){
                is UiEvent.NavigateUp -> goBack()
                is UiEvent.ShowSnackbar -> TODO()
            }
        }
    }

    // Mostrar Snackbar cuando el estado cambie
    LaunchedEffect(uiState.isSuccess || !uiState.errorMessage.isNullOrBlank()) {
        if (uiState.isSuccess && !uiState.successMessage.isNullOrBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = uiState.successMessage,
                    duration = SnackbarDuration.Short
                )
                onEvent(ViajeEvent.ResetSuccessMessage)
            }
        } else if (!uiState.errorMessage.isNullOrBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = uiState.errorMessage,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = if (uiState.isSuccess) Color.Green.copy(alpha = 0.8f) else Color.Red.copy(alpha = 0.8f)
            )
        }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.viajeId != null && uiState.viajeId > 0) "Editar viaje" else "Nuevo viaje",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF272D4D)
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.fecha?.toString() ?: "",
                onValueChange = {
                    val date = try {
                        Date(it)
                    } catch (e: Exception) {
                        null
                    }
                    onEvent(ViajeEvent.FechaChange(date))
                },
                label = { Text("Fecha") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.millas.toString(),
                onValueChange = {
                    onEvent(ViajeEvent.MillasChange(it.toDoubleOrNull() ?: 0.0))
                },
                label = { Text("Millas") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.tasaDolar.toString(),
                onValueChange = {
                    onEvent(ViajeEvent.TasaDolarChange(it.toDoubleOrNull() ?: 0.0))
                },
                label = { Text("Tasa") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.monto.toString(),
                onValueChange = {
                    onEvent(ViajeEvent.MontoChange(it.toDoubleOrNull() ?: 0.0))
                },
                label = { Text("Monto") },
                modifier = Modifier.fillMaxWidth(),
                )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.observaciones?: "",
                onValueChange = {
                    onEvent(ViajeEvent.ObservacionesChange(it))
                },
                label = { Text("Observaciones") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onEvent(ViajeEvent.Nuevo) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                    Text("Limpiar")
                }

                OutlinedButton(
                    onClick = { onEvent(ViajeEvent.PostViaje) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Guardar")
                    Text("Guardar")
                }
            }
        }
    }
}