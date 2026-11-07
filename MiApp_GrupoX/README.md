# MiApp_GrupoX — Control sanitario de bebederos

Aplicación móvil Android (Kotlin + Jetpack Compose) para consultar la temperatura del agua en las líneas de bebederos de una granja avícola, identificar alertas y registrar los flushing realizados.

- **Asignatura:** DSY1105 · Desarrollo de Aplicaciones Móviles
- **Evaluación:** Parcial 2 (encargo en parejas)
- **Caso:** Temperatura de agua en bebederos (empresa de servicios avícola y ganadero)
- **Alcance:** MVP académico con datos ficticios, sin conexión a sistemas productivos reales

## Integrantes

| Nombre | Usuario de GitHub |
|---|---|
| solange valladares | solange1988 |
| ángel villegas | Pitikleiiinn |

**Equipo N°:7

## Descripción del problema

Los operarios y supervisores controlan la temperatura del agua y ejecutan flushing (purga de las líneas) cuando detectan condiciones inadecuadas, pero la información no está centralizada en una aplicación móvil. La app permite consultar el estado de las líneas desde terreno, revisar alertas, registrar mediciones y dejar evidencia de cada flushing.

## Perfiles de usuario

- **Operario:** registra temperaturas, registra flushing con foto de evidencia y revisa alertas.
- **Supervisor:** ve un panel resumen (líneas críticas, en advertencia y normales) y consulta temperaturas, alertas e historial de flushing.

## Funcionalidades implementadas

- **Inicio de sesión** con validación por campo y mensajes visuales.
- **Selección de perfil** (Operario o Supervisor).
- **Temperaturas:** registro por granja, galpón, línea y **rango de temperatura** mediante combo box; tarjetas por línea con color según estado (Normal, Advertencia, Crítico) y botón "Ver detalle".
- **Alertas:** listado de líneas en advertencia o estado crítico, con métricas de resumen.
- **Flushing:** formulario validado con combo box, responsable, motivo, observaciones y **foto de evidencia**; historial de registros con estado animado.
- **Panel del supervisor** con métricas calculadas automáticamente.
- **Mensajes al pie de la pantalla (Snackbar)** para confirmaciones y errores.

### Reglas de estado

| Temperatura | Estado |
|---|---|
| Menos de 27 °C | Normal |
| De 27 °C a 29,9 °C | Advertencia |
| 30 °C o más | Crítico |

## Arquitectura (MVVM)

```
app/src/main/java/com/example/miapp_grupox/
├── model/        Entidades de Room, DAOs, base de datos, validadores y opciones de formularios
├── repository/   Acceso a los datos (BebederoRepository, MainRepository)
├── viewmodel/    BebederoViewModel y MainViewModel (estado con StateFlow)
└── ui/theme/     Pantallas (Compose), componentes reutilizables y tema visual
```

- **Validaciones desacopladas de la interfaz:** `LoginValidator`, `ValidadorTemperatura` y `ValidadorFlushing` (en `model/`). Las pantallas solo muestran los errores que reciben.
- **Persistencia local:** Room guarda las mediciones de temperatura y los registros de flushing; los datos se mantienen al cerrar la app.
- **Estado reactivo:** los ViewModels exponen `StateFlow` y las pantallas se actualizan solas.

## Recursos nativos del dispositivo

1. **Cámara:** botón "Tomar foto de evidencia" en el registro de flushing (`ActivityResultContracts.TakePicturePreview`), con solicitud del permiso `CAMERA` en tiempo de ejecución.
2. **Vibración:** confirmación táctil al guardar una temperatura o un flushing (permiso `VIBRATE`).

## Animaciones

- `animateColorAsState`: el color de las tarjetas cambia según el estado de la línea.
- `animateDpAsState` y `animateContentSize`: elevación y tamaño de la tarjeta al abrir el detalle.
- `AnimatedVisibility`: detalle de la tarjeta y vista previa de la foto.
- `LaunchedEffect`: simulación del cambio de estado de un flushing (En proceso → Completado).

## Cómo ejecutar el proyecto

**Requisitos:** Android Studio actualizado, JDK configurado y un emulador (o celular) con Android 7.0 (API 24) o superior.

1. Clonar el repositorio:
   ```
   git clone https://github.com/solange1988/MiApp_GrupoX.git
   ```
2. Abrir la carpeta `MiApp_GrupoX` en Android Studio (**File → Open**) y esperar a que termine el *Gradle sync*.
3. Crear o seleccionar un emulador en **Device Manager** (por ejemplo Medium Phone).
4. Presionar **Run ▶** (o `Shift + F10`).

**Usuarios de prueba (ficticios):**

| Usuario | Contraseña |
|---|---|
| operario | 1234 |
| supervisor | 1234 |

Después de iniciar sesión se elige el perfil con el que se quiere ingresar.

### Generar el APK

**Build → Generate App Bundles or APKs → Generate APKs**. Para instalar una versión nueva en el celular, desinstalar antes la anterior.

## Organización del trabajo

- **Control de versiones:** GitHub, con commits descriptivos por cambio.
- **Planificación:** tablero Trello con las listas Por hacer, En proceso y Hecho.

## Mejoras futuras

- Notificaciones cuando una línea pase a estado crítico.
- Guardar la foto de evidencia junto con el registro del historial.
- Consumir una API real del sistema de monitoreo de temperatura.
- Inicio de sesión con usuarios y perfiles definidos en el sistema, en lugar de datos de prueba.

## enlace trello
-https://trello.com/b/K0H7qjht/kotlin



---

# Integración con backend (microservicios Spring Boot)

El frontend se mantiene igual (mismas pantallas, validaciones, animaciones, cámara y vibración). Lo que cambió: ahora los datos viven en **3 microservicios** (Java 17 + Spring Boot 3 + JPA) y la app los consume con **Retrofit**. Room queda como copia local para que la app siga mostrando los últimos datos si no hay conexión.

## Arquitectura

```
App Android (Compose + MVVM)
  Pantalla → ViewModel → Repository → Retrofit (remote/)  ──► ms-usuarios      :8084  login y roles
                          └─► Room (copia local)           ──► ms-temperaturas  :8082  líneas, estado, alertas, resumen
                                                           ──► ms-flushing      :8083  CRUD de flushing
                                                           ──► Open-Meteo (API externa) clima exterior
```

Cambios mínimos en la app:

| Archivo | Cambio |
|---|---|
| `remote/` (nuevo) | Retrofit: `ApiConfig`, `RetrofitClient`, APIs, DTOs y manejo de errores |
| `repository/` | `BebederoRepository` y `MainRepository` ahora llaman al servidor y actualizan Room; nuevos `UsuariosRepository` y `ClimaRepository` |
| `viewmodel/` | Sincronizan al iniciar y exponen `mensaje` (errores de red) y `clima` |
| `model/` | DAOs con `insertarTodos/borrarFueraDe`; `LoginValidator.validarCampos`; coordenadas de granjas |
| `LoginScreen` | La contraseña se verifica en ms-usuarios |
| `TemperaturasScreen` | Tarjeta de clima exterior (API externa) |
| `MainActivity` | Muestra los mensajes de red (Toast) |
| `build.gradle.kts` / manifest | Retrofit, MockK, firma de APK, permiso INTERNET |

## Endpoints

| Servicio | Método y ruta | Descripción |
|---|---|---|
| ms-usuarios 8084 | `POST /api/usuarios/login` | Login (`usuario`, `clave`) → devuelve rol |
| | `GET/POST /api/usuarios`, `GET/PUT/DELETE /api/usuarios/{id}` | CRUD usuarios |
| ms-temperaturas 8082 | `GET /api/lineas` | Líneas con última temperatura y estado |
| | `POST /api/lineas/temperatura` | Registrar temperatura (crea/actualiza la línea y guarda historial) |
| | `GET /api/lineas/{id}` · `GET /api/lineas/{id}/lecturas` · `DELETE /api/lineas/{id}` | Detalle, historial, eliminar |
| | `GET /api/alertas` · `GET /api/resumen` | Líneas en advertencia/crítico · métricas del supervisor |
| ms-flushing 8083 | `GET/POST /api/flushing`, `GET/PUT/DELETE /api/flushing/{id}` | CRUD de flushing |
| **API externa** | `GET https://api.open-meteo.com/v1/forecast?latitude=..&longitude=..&current=temperature_2m,relative_humidity_2m` | Clima actual |

Reglas de estado (en el servidor y en la app): < 27 °C Normal · 27 a 29,9 °C Advertencia · ≥ 30 °C Crítico.

## Cómo ejecutar

1. **Backend** (JDK 17 + Maven), una terminal por servicio:
   ```bash
   cd microservicios/ms-usuarios      && mvn spring-boot:run   # 8084
   cd microservicios/ms-temperaturas  && mvn spring-boot:run   # 8082
   cd microservicios/ms-flushing      && mvn spring-boot:run   # 8083
   ```
   Prueba rápida en el navegador: http://localhost:8082/api/lineas
2. **App**: abrir `MiApp_GrupoX` en Android Studio → Sync → Run en el emulador (usa `10.0.2.2`).
   En celular físico cambia `HOST` en `remote/ApiConfig.kt` por la IP de tu PC (misma WiFi) y abre los puertos 8082–8084.
3. Usuarios de prueba: `operario` / `supervisor`, contraseña `1234`.

## Pruebas unitarias
`Run Tests` sobre `app/src/test` (JUnit4 + MockK + coroutines-test). Cubren estados, rangos, validadores, manejo de errores de red y todos los repositorios (con API y DAO simulados). Para medir cobertura: *Run with Coverage*. Backend: `mvn test` en `ms-temperaturas`.

## APK firmado
```bash
cd app
keytool -genkey -v -keystore ariztia-release.jks -alias ariztia -keyalg RSA -keysize 2048 -validity 10000
cd ..
cp keystore.properties.example keystore.properties   # completar contraseñas
./gradlew assembleRelease
```
El APK queda en `app/build/outputs/apk/release/`. Sube al repositorio el `.jks`, `keystore.properties` y el APK, y agrega aquí la captura.
