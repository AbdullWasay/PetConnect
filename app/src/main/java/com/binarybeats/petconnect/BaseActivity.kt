

import android.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.akexorcist.screenshotdetection.ScreenshotDetectionDelegate

private const val CHANNEL_ID = "screenshot_channel"
private const val NOTIFICATION_ID = 123

open class BaseActivity : AppCompatActivity(), ScreenshotDetectionDelegate.ScreenshotDetectionListener {

    private lateinit var screenshotDetectionDelegate: ScreenshotDetectionDelegate

    override fun onResume() {
        super.onResume()
        screenshotDetectionDelegate = ScreenshotDetectionDelegate(this, this)
        screenshotDetectionDelegate.startScreenshotDetection()
    }

    override fun onPause() {
        super.onPause()
        screenshotDetectionDelegate.stopScreenshotDetection()
    }

    override fun onScreenCaptured(path: String) {
        sendNotification(this)
    }

    override fun onScreenCapturedWithDeniedPermission() {
        // Handle denied permission if needed
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Screenshot Detection"
            val descriptionText = "Notification for Screenshot Detection"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = context.getSystemService(
                NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_menu_camera)
            .setContentTitle("Screenshot Detected")
            .setContentText("A screenshot has been captured.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(context: Context) {
        createNotificationChannel(context)
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, buildNotification(context).build())
        }
    }
}

