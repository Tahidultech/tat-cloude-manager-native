package com.tahidultech.tatcloudmanager.data
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class PrefsManager(context: Context) {
    private val prefs = context.getSharedPreferences("tat_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    fun getMasterPwd(): String? = prefs.getString("master_pwd", null)
    fun setMasterPwd(v: String) { prefs.edit().putString("master_pwd", v).apply() }
    fun getPasskey(): String? = prefs.getString("passkey", null)
    fun setPasskey(v: String) { prefs.edit().putString("passkey", v).apply() }
    fun getFiles(): MutableList<CloudFile> {
        val json = prefs.getString("files", "[]") ?: "[]"
        val type = object : TypeToken<MutableList<CloudFile>>() {}.type
        return try { gson.fromJson(json, type) ?: mutableListOf() } catch (e: Exception) { mutableListOf() }
    }
    fun setFiles(list: List<CloudFile>) { prefs.edit().putString("files", gson.toJson(list)).apply() }
    fun getAccounts(): MutableList<VaultAccount> {
        val json = prefs.getString("accounts", "[]") ?: "[]"
        val type = object : TypeToken<MutableList<VaultAccount>>() {}.type
        return try { gson.fromJson(json, type) ?: mutableListOf() } catch (e: Exception) { mutableListOf() }
    }
    fun setAccounts(list: List<VaultAccount>) { prefs.edit().putString("accounts", gson.toJson(list)).apply() }
    fun getViewMode(): String = prefs.getString("view_mode", "grid") ?: "grid"
    fun setViewMode(mode: String) { prefs.edit().putString("view_mode", mode).apply() }
}
