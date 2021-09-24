package com.example.tite.data.firebase.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber

class FirebaseMessageDatabase(firebaseRTDB: FirebaseDatabase) {
    private val messageDB = firebaseRTDB.getReference(MESSAGE_ROOT)
    private val chatDB = firebaseRTDB.getReference(CHAT_ROOT)

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private val _messageDBEntityList =
        MutableSharedFlow<List<MessageDBEntity>>(1, 0, BufferOverflow.DROP_OLDEST)
    val messageDBEntityList = _messageDBEntityList.asSharedFlow()

    private val messageListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            coroutineScope.launch {
                val messageList = mutableListOf<MessageDBEntity>()
                for (messageChild in snapshot.children) {
                    messageList.add(
                        messageChild.getValue(MessageDBEntity::class.java) ?: continue
                    )
                }
                _messageDBEntityList.emit(messageList)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Timber.d("New data ${error.message}")
        }
    }

    suspend fun sendMessage(chatId: String, messageDBEntity: MessageDBEntity) =
        withContext(Dispatchers.IO) {
            val messagePush = messageDB.child(chatId).push()
            messagePush.setValue(messageDBEntity.copy(id = messagePush.key))
            chatDB.child(messageDBEntity.sender?.uid.orEmpty()).child(chatId).child("message")
                .setValue(messageDBEntity.text)
            chatDB.child(messageDBEntity.receiverUID.orEmpty()).child(chatId).child("message")
                .setValue(messageDBEntity.text)
        }

    fun addMessageListener(chatId: String) {
        messageDB.child(chatId).addValueEventListener(messageListener)
    }

    fun removeMessageListener(chatId: String) {
        messageDB.child(chatId).removeEventListener(messageListener)
    }

    private companion object {
        const val CHAT_ROOT = "chats"
        const val MESSAGE_ROOT = "messages"
    }
}