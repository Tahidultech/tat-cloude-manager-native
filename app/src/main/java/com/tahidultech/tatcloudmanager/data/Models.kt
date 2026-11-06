package com.tahidultech.tatcloudmanager.data
data class CloudFile(val id: String = System.currentTimeMillis().toString(), val title: String = "", val category: String = "Software Project", val cloud: String = "Google Drive", val cloudAcc: String = "", val folderPath: String = "", val directLink: String = "", val tags: String = "", val note: String = "", val updatedAt: String = "")
data class VaultAccount(val id: String = System.currentTimeMillis().toString(), val provider: String = "Google", val name: String = "", val mail: String = "", val password: String = "", val note: String = "")
