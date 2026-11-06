package cl.ariztia.bebederos.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import cl.ariztia.bebederos.R
import cl.ariztia.bebederos.data.model.Alerta
import cl.ariztia.bebederos.data.model.EstadoLinea

/**
 * [NOTIFICACIONES] Muestra una notificación del sistema por cada alerta CRÍTICA activa
 * (requisito del caso: "Notificaciones"). Recuerda cuáles ya avisó para no repetirlas.
 */
object AlertNotifier {
    private const val CANAL_ID = "alertas_temperatura"
    private val yaNotificadas = mutableSetOf<Long>()

    fun notificarCriticas(context: Context, alertas: List<Alerta>) {
        // En Android 13+ hace falta el permiso concedido por el usuario
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return

        crearCanal(context)
        alertas
            .filter { !it.atendida && it.estado == EstadoLinea.CRITICO && yaNotificadas.add(it.id) }
            .forEach { alerta ->
                val notificacion = NotificationCompat.Builder(context, CANAL_ID)
                    .setSmallIcon(R.drawable.ic_notificacion)
                    .setContentTitle("Alerta crítica: ${alerta.lineaNombre ?: "Línea ${alerta.lineaId}"}")
                    .setContentText(alerta.mensaje)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(alerta.mensaje))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build()
                NotificationManagerCompat.from(context).notify(alerta.id.toInt(), notificacion)
            }
    }

    /** Desde Android 8 las notificaciones requieren un "canal" */
    private fun crearCanal(context: Context) {
        val canal = NotificationChannel(CANAL_ID, "Alertas de temperatura", NotificationManager.IMPORTANCE_HIGH)
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }
}
