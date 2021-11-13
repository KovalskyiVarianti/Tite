package com.example.tite.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.tite.R
import com.example.tite.domain.repository.PersonRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*

const val CHANNEL_NAME = "TITE TEST CHANNEL"

class FirebaseMessageService : FirebaseMessagingService() {

    private val personRepository: PersonRepository by inject()
    private val personInfo = personRepository.personInfo

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Timber.d("New token: $p0")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("From: ${remoteMessage.from}")
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("Message data payload: ${remoteMessage.data}")
        }
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]
        title?.let {
            personRepository.addPersonInfoListener(it)
            createNotificationChannel()
            coroutineScope.launch {
                if (body != null) {
                    remoteMessage.createNotification(title, body)
                }
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_NAME, CHANNEL_NAME, importance)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private suspend fun RemoteMessage.createNotification(title: String, body: String) {
        personInfo.collect { personEntity ->
            val personIcon = getFuturePersonIcon(personEntity.photo)
            val builder = NotificationCompat.Builder(this@FirebaseMessageService, from!!)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(personIcon.get())
                .setContentTitle(personEntity.name)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            with(NotificationManagerCompat.from(this@FirebaseMessageService)) {
                notify(Random().nextInt(), builder.build())
            }
            personRepository.removePersonInfoListener(title)
        }
    }

    private fun getFuturePersonIcon(photoUrl: String) =
        Glide.with(this@FirebaseMessageService)
            .asBitmap()
            .load(photoUrl)
            .submit()
}