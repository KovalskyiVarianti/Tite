package com.example.tite.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.tite.R
import com.example.tite.data.network.NotificationData
import com.example.tite.data.network.RetrofitNotificationApi.Companion.CHAT
import com.example.tite.data.network.RetrofitNotificationApi.Companion.ID
import com.example.tite.data.network.RetrofitNotificationApi.Companion.NAME
import com.example.tite.data.network.RetrofitNotificationApi.Companion.PHOTO
import com.example.tite.data.network.RetrofitNotificationApi.Companion.TEXT
import com.example.tite.domain.repository.PersonRepository
import com.example.tite.presentation.MainActivity
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

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Timber.d("New token: $p0")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("From: ${remoteMessage.from}")
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("Message data payload: ${remoteMessage.data}")
            createNotificationChannel()
            remoteMessage.showNotification(remoteMessage.getNotificationData())
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

    private fun RemoteMessage.showNotification(data: NotificationData) {
        Glide.with(this@FirebaseMessageService)
            .asBitmap()
            .load(data.photo)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val builder = NotificationCompat.Builder(this@FirebaseMessageService, from!!)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(resource)
                        .setContentTitle(data.name)
                        .setContentText(data.text)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(getPendingIntent(data.id, data.chat))
                        .setChannelId(CHANNEL_NAME)
                    with(NotificationManagerCompat.from(this@FirebaseMessageService)) {
                        notify(Random().nextInt(), builder.build())
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}

            })
    }

    private fun getPendingIntent(id: String, chatId: String): PendingIntent? {
        return PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java)
                .putExtra(ID, id).putExtra(CHAT, chatId).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                },
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    private fun RemoteMessage.getNotificationData() = NotificationData(
        data[ID].orEmpty(),
        data[NAME].orEmpty(),
        data[PHOTO].orEmpty(),
        data[TEXT].orEmpty(),
        data[CHAT].orEmpty()
    )
}