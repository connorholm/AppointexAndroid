package com.example.insuppli.repository.local.sharedpreference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager private constructor() {

    companion object {

        @SuppressLint("StaticFieldLeak")

        private var manager: SharedPreferenceManager? = null

        private lateinit var applicationContext: Context

        private lateinit var sharedPreferences: SharedPreferences

        private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

        @Synchronized
        fun init(applicationContext: Context) {
            Companion.applicationContext = applicationContext
            manager = SharedPreferenceManager()
        }

        @Synchronized
        fun getInstance(): SharedPreferenceManager {
            if (manager == null) {
                throw IllegalStateException("SharedPreferenceManager not initialized. Call init() before calling getInstance()")
            }
            setupSharedPreference(applicationContext.packageName)
            return manager as SharedPreferenceManager
        }

        @Synchronized
        fun getInstance(preference: String): SharedPreferenceManager {
            if (manager == null) {
                throw IllegalStateException("SharedPreferenceManager not initialized. Call init() before calling getInstance()")
            }
            setupSharedPreference(preference)
            return manager as SharedPreferenceManager
        }

        @SuppressLint("CommitPrefEdits")
        private fun setupSharedPreference(preference: String) {
            sharedPreferences = applicationContext.getSharedPreferences(preference, Context.MODE_PRIVATE)
            sharedPreferencesEditor = sharedPreferences.edit()
        }
    }

    fun saveString(key: String, value: String) {
        putString(key, value)
        save()
    }

    fun updateString(key: String, value: String) {
        putString(key, value)
        commit()
    }

    @SuppressLint("CommitPrefEdits")
    fun putString(key: String, value: String) {
        sharedPreferencesEditor.putString(key, value)
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun getString(key: String,defaultValue : String): String? {
        return sharedPreferences.getString(key, defaultValue)
    }
    fun saveStringList(keyList: List<String>, valueList: List<String>) {
        putStringList(keyList, valueList)
        save()
    }

    @SuppressLint("CommitPrefEdits")
    fun putStringList(keyList: List<String>, valueList: List<String>) {
        for (i in keyList.indices) {
            sharedPreferencesEditor.putString(keyList[i], valueList[i])
        }
    }

    fun saveStringSet(key: String, value: Set<String>) {
        putStringSet(key, value)
        save()
    }

    @SuppressLint("CommitPrefEdits")
    fun putStringSet(key: String, value: Set<String>) {
        sharedPreferencesEditor.putStringSet(key, value)
    }

    fun getStringSet(key: String): Set<String>? {
        return sharedPreferences.getStringSet(key, null)
    }

    fun saveInt(key: String, value: Int) {
        putInt(key, value)
        save()
    }

    @SuppressLint("CommitPrefEdits")
    fun putInt(key: String, value: Int) {
        sharedPreferencesEditor.putInt(key, value)
    }

    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, -1)
    }

    fun saveIntList(keyList: List<String>, valueList: List<Int>) {
        putIntList(keyList, valueList)
        save()
    }

    @SuppressLint("CommitPrefEdits")
    fun putIntList(keyList: List<String>, valueList: List<Int>) {
        for (i in 0 until keyList.size) {
            sharedPreferencesEditor.putInt(keyList[i], valueList[i])
        }
    }

    fun saveLong(key: String, value: Long) {
        putLong(key, value)
        save()
    }

    @SuppressLint("CommitPrefEdits")
    fun putLong(key: String, value: Long) {
        sharedPreferencesEditor.putLong(key, value)
    }

    fun getLong(key: String): Long {
        return sharedPreferences.getLong(key, -1L)
    }

    fun saveLongList(keyList: List<String>, valueList: List<Long>) {
        putLongList(keyList, valueList)
        save()
    }

    @SuppressLint("CommitPrefEdits")
    fun putLongList(keyList: List<String>, valueList: List<Long>) {
        for (i in keyList.indices) {
            sharedPreferencesEditor.putLong(keyList[i], valueList[i])
        }
    }

    fun saveFloat(key: String, value: Float) {
        putFloat(key, value)
        save()
    }

    @SuppressLint("CommitPrefEdits")
    fun putFloat(key: String, value: Float) {
        sharedPreferencesEditor.putFloat(key, value)
    }

    fun getFloat(key: String): Float {
        return sharedPreferences.getFloat(key, -1f)
    }

    fun saveFloatList(keyList: List<String>, valueList: List<Float>) {
        putFloatList(keyList, valueList)
        save()
    }

    @SuppressLint("CommitPrefEdits")
    fun putFloatList(keyList: List<String>, valueList: List<Float>) {
        for (i in keyList.indices) {
            sharedPreferencesEditor.putFloat(keyList[i], valueList[i])
        }
    }

    fun saveBoolean(key: String, value: Boolean) {
        putBoolean(key, value)
        save()
    }

    @SuppressLint("CommitPrefEdits")
    fun putBoolean(key: String, value: Boolean) {
        sharedPreferencesEditor.putBoolean(key, value)
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun saveBooleanList(keyList: List<String>, valueList: List<Boolean>) {
        putBooleanList(keyList, valueList)
        save()
    }

    @SuppressLint("CommitPrefEdits")
    fun putBooleanList(keyList: List<String>, valueList: List<Boolean>) {
        for (i in keyList.indices) {
            sharedPreferencesEditor.putBoolean(keyList[i], valueList[i])
        }
    }

    fun remove(key: String) {
        sharedPreferencesEditor.remove(key)
        save()
    }

    fun clear() {
        sharedPreferencesEditor.clear()
        save()
    }

    fun save() {
        sharedPreferencesEditor.apply()
    }

    fun getData(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun commit() {
        sharedPreferencesEditor.commit()
    }

}