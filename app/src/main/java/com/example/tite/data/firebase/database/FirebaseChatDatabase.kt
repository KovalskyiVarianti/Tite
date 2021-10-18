package com.example.tite.data.firebase.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber

class FirebaseChatDatabase(firebaseRTDB: FirebaseDatabase) {

    private val chatDB = firebaseRTDB.getReference(CHAT_ROOT)

    private val _chatDBEntityList =
        MutableSharedFlow<List<ChatDBEntity>>(1, 0, BufferOverflow.DROP_OLDEST)
    val chatDBEntityList = _chatDBEntityList.asSharedFlow()

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private val chatListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            coroutineScope.launch {
                val chatList = mutableListOf<ChatDBEntity>()
                for (chatChild in snapshot.children) {
                    val chat = chatChild.getValue(ChatDBEntity::class.java) ?: continue
                    chatList.add(chat)
                }
                _chatDBEntityList.emit(chatList)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Timber.d("New data ${error.message}")
        }
    }

    fun addChatListener(uid: String) {
        chatDB.child(uid).addValueEventListener(chatListener)
    }

    fun removeChatListener(uid: String){
        chatDB.child(uid).removeEventListener(chatListener)
    }

    suspend fun createChat(selfPerson: PersonDBEntity, person: PersonDBEntity) =
        withContext(Dispatchers.IO) {
            val chatSelfPush = chatDB.child(selfPerson.uid.orEmpty()).push()
            chatSelfPush.key?.let { key ->
                chatSelfPush.setValue(ChatDBEntity(key, listOf(selfPerson.uid.orEmpty(), person.uid.orEmpty())))
                chatDB.child(person.uid.orEmpty()).child(key)
                    .setValue(ChatDBEntity(key, listOf(selfPerson.uid.orEmpty(), person.uid.orEmpty())))
            }
        }

    private companion object {
        const val CHAT_ROOT = "chats"
    }
}