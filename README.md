# Ariztia · Control de Temperatura de Bebederos (DSY1105)

App móvil Android (Kotlin + Jetpack Compose + MVVM) con **4 microservicios** Spring Boot (Java 17 + JPA)
y consumo de una **API externa** (Open-Meteo) vía Retrofit.

## 1. Integrantes
- _Nombre Apellido 1_ — _rol técnico_
- _Nombre Apellido 2_ — _rol técnico_

## 2. Arquitectura
```
 App Android (Compose + MVVM)
   UI (screens) → ViewModel → Repository → Retrofit
                                              │
        ┌──────────────┬──────────────┬───────┴──────┬───────────────┐
        ▼              ▼              ▼              ▼               ▼
  ms-granjas:8081  ms-temperaturas:8082  ms-flushing:8083  ms-usuarios:8084   API externa
  Granja>Galpón>   Lecturas, estado,     CRUD flushing     Login, roles        Open-Meteo
  Línea            alertas                                                      (clima)
```
Cada microservicio tiene su propio proyecto Maven, su propio puerto y **su propia base de datos** (H2 en archivo).
Se comunican con la app por HTTP/JSON; entre ellos solo se referencian por `lineaId` (sin compartir tablas).

Paquetes de cada microservicio: `controller` (REST) · `service` (lógica) · `repository` (JPA) · `model` (entidades) ·
`dto` · `mapper` · `config` (CORS, datos de prueba) · `exception` (errores JSON `{"error": "..."}`).

Paquetes de la app (`cl.ariztia.bebederos`): `data/model` · `data/remote` (Retrofit) · `data/repository` ·
`viewmodel` · `ui/screens` · `ui/components` · `ui/navigation` · `ui/theme` · `util`.

## 3. Funcionalidades
- Login con rol **Operario** o **Supervisor** (menús distintos).
- Navegación Granja → Galpón → Línea → Detalle con temperatura y estado (Normal / Advertencia / Crítico).
- Registrar temperatura (el servidor calcula el estado y genera alertas).
- Alertas activas + **notificación** del sistema para alertas críticas.
- Registrar **flushing** (formulario con menús en cascada y validaciones), historial, **editar** y **eliminar**.
- Clima exterior de la granja desde **API externa**.

## 4. Endpoints
| Servicio | Método y ruta | Descripción |
|---|---|---|
| ms-usuarios 8084 | `POST /api/usuarios/login` | Iniciar sesión |
| | `GET/POST /api/usuarios`, `GET/PUT/DELETE /api/usuarios/{id}` | CRUD usuarios |
| ms-granjas 8081 | `GET/POST /api/granjas`, `GET/PUT/DELETE /api/granjas/{id}` | CRUD granjas |
| | `GET /api/granjas/{id}/galpones` | Galpones de una granja |
| | `POST /api/galpones`, `GET/PUT/DELETE /api/galpones/{id}`, `GET /api/galpones/{id}/lineas` | Galpones |
| | `POST /api/lineas`, `GET/PUT/DELETE /api/lineas/{id}` | Líneas |
| ms-temperaturas 8082 | `POST /api/temperaturas` | Registrar lectura (calcula estado + alerta) |
| | `GET /api/temperaturas/linea/{id}` · `/linea/{id}/ultima` · `/ultimas?lineaIds=1&lineaIds=2` | Consultas |
| | `PUT/DELETE /api/temperaturas/{id}` | Corregir / eliminar |
| | `GET /api/alertas?activas=true` · `/api/alertas/linea/{id}` · `PUT /api/alertas/{id}/atender` | Alertas |
| ms-flushing 8083 | `GET/POST /api/flushing`, `GET/PUT/DELETE /api/flushing/{id}` | CRUD flushing |
| **API externa** | `GET https://api.open-meteo.com/v1/forecast?latitude=..&longitude=..&current=temperature_2m,relative_humidity_2m` | Clima actual |

Umbrales (configurables en `ms-temperaturas/.../application.properties`): ≤25 °C Normal · >25 Advertencia · >30 Crítico.
Usuarios de prueba: `operario@ariztia.cl` / `supervisor@ariztia.cl`, clave `1234`.

## 5. Pasos para ejecutar
**Backend** (JDK 17 + Maven, o abrir cada carpeta en IntelliJ/Eclipse/VS Code y ejecutar la clase `*Application`):
```bash
cd microservicios/ms-granjas      && mvn spring-boot:run     # 8081
cd microservicios/ms-temperaturas && mvn spring-boot:run     # 8082  (levantar DESPUÉS de ms-granjas no es obligatorio)
cd microservicios/ms-flushing     && mvn spring-boot:run     # 8083
cd microservicios/ms-usuarios     && mvn spring-boot:run     # 8084
```
Probar en el navegador: http://localhost:8081/api/granjas · consola BD: http://localhost:8081/h2-console
(JDBC `jdbc:h2:file:./data/ms_granjas`, usuario `sa`, sin clave).

**App**: abrir la carpeta `android-app` en Android Studio → Sync → ejecutar en emulador.
- Emulador: la app usa `10.0.2.2` (ya configurado en `data/remote/ApiConfig.kt`).
- Celular físico: cambiar `HOST` por la IP del PC (misma WiFi) y permitir los puertos 8081–8084 en el firewall.

## 6. Pruebas unitarias
`Run > Run Tests in 'test'` en Android Studio o `./gradlew testDebugUnitTest`. Usan JUnit4 + MockK + coroutines-test
sobre ViewModels, reglas de estado y manejo de errores. Para medir cobertura: clic derecho en la carpeta `test` →
*Run with Coverage* (el enunciado pide ≥80 % de la lógica: verifícalo y agrega pruebas si falta).
Backend: `mvn test` en `ms-temperaturas`.

## 7. APK firmado (release)
```bash
cd android-app/app
keytool -genkey -v -keystore ariztia-release.jks -alias ariztia -keyalg RSA -keysize 2048 -validity 10000
cd .. && cp keystore.properties.example keystore.properties   # completar contraseñas
./gradlew assembleRelease
```
Resultado: `app/build/outputs/apk/release/app-release.apk`. Android Studio también: *Build > Generate Signed Bundle / APK*.
Subir al repositorio el `.jks`, `keystore.properties` y el APK (lo exige el enunciado) y agregar aquí la captura.

## 8. Entrega y colaboración
Dos repos públicos (app y microservicios) con commits de **ambos** integrantes, tablero Trello con tareas repartidas,
y no tocar el repositorio después de la fecha límite.
