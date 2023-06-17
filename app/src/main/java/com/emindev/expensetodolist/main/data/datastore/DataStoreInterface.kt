package com.emindev.expensetodolist.main.data.datastore

interface DataStoreInterface {
   suspend fun save(key: String, value: Int)
   suspend fun save(key: String, value: Long)
   suspend fun save(key: String, value: String)
   suspend fun save(key: String, value: Float)
   suspend fun save(key: String, value: Boolean)
   suspend fun readInt(key: String):Int?
   suspend fun readString(key: String):String?
   suspend fun readLong(key: String):Long?
   suspend fun readFloat(key: String):Float?
   suspend fun readBoolean(key: String):Boolean?

}