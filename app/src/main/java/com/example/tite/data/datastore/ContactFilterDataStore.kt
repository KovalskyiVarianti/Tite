//package com.example.tite.data.datastore
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.datastore.preferences.preferencesDataStore
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//class ContactFilterDataStore(private val context: Context) {
//    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = contactFilterDataStore)
//
//
//    suspend fun saveRelation(relation: String) = withContext(Dispatchers.IO) {
//        context.dataStore.edit { mutablePreferences ->
//            mutablePreferences[RELATION] = relation
//        }
//    }
//
//    suspend fun getRelation() =
//        context.dataStore.data.map {
//            it[RELATION] ?: ""
//        }
//
//
//    private companion object {
//        const val contactFilterDataStore: String = "CONTACT_FILTER_DATA_STORE"
//        val RELATION = stringPreferencesKey("KEY_RELATION")
//    }
//}