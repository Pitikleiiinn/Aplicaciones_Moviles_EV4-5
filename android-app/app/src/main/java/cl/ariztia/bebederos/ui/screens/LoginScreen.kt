package cl.ariztia.bebederos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.ariztia.bebederos.ui.theme.RojoAriztia
import cl.ariztia.bebederos.ui.theme.RojoOscuro
import cl.ariztia.bebederos.viewmodel.SesionViewModel

/**
 * [PANTALLA] Inicio de sesión. El rol (Operario / Supervisor) lo define el microservicio ms-usuarios.
 * Incluye atajos "demo" para rellenar las credenciales de prueba.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(sesionVm: SesionViewModel, onIngreso: () -> Unit) {
    val usuario by sesionVm.usuario.collectAsStateWithLifecycle()
    val ui by sesionVm.ui.collectAsStateWithLifecycle()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    // Cuando el login es exitoso (usuario != null) se navega al inicio
    LaunchedEffect(usuario) { if (usuario != null) onIngreso() }

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())) {
        // ----- [ENCABEZADO] Banda roja con marca -----
        Box(
            Modifier.fillMaxWidth().background(RojoAriztia).padding(top = 64.dp, bottom = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(72.dp).background(Color.White, RoundedCornerShape(36.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.WaterDrop, contentDescription = null, tint = RojoOscuro, modifier = Modifier.size(40.dp))
                }
                Spacer(Modifier.height(12.dp))
                Text("Ariztia", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.ExtraBold)
                Text("Control de temperatura de bebederos", color = Color.White.copy(alpha = 0.9f))
            }
        }

        // ----- [FORMULARIO] -----
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Iniciar sesión", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = email, onValueChange = { email = it }, label = { Text("Correo") },
                leadingIcon = { Icon(Icons.Filled.Email, null) }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password, onValueChange = { password = it }, label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Filled.Lock, null) }, singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            ui.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Button(
                onClick = { sesionVm.iniciarSesion(email, password) },
                enabled = !ui.cargando,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (ui.cargando) CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                else Text("Ingresar", fontSize = 16.sp)
            }

            Text("Usuarios de prueba", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = { email = "operario@ariztia.cl"; password = "1234" }, label = { Text("Operario") })
                AssistChip(onClick = { email = "supervisor@ariztia.cl"; password = "1234" }, label = { Text("Supervisor") })
            }
        }
    }
}
