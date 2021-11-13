package com.example.tite.data.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetrofitNotificationApi {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun sendNotification(@Body notification: Notification)

    companion object {

        const val BASE_URL = "https://fcm.googleapis.com"
        //DDDANGER
        private const val SERVER_KEY =
            "AAAAbxYOMQ4:APA91bETudjsYvoPp4l4XGqUru4-7jCQTfsesBKVlYw9f4kYOntUcsVH6qlgpzCoQL-xSBEFgaoKaSVht_Gimy_G77CpWg4BzESVQEP2YWVW33kQocwkINLhy-YXYGS8XQn16SDBJKzB"
        private const val CONTENT_TYPE = "application/json"
    }
}

data class NotificationData(val title: String, val body: String)
data class Notification(val data: NotificationData, val to: String)