package io.legado.app.help.config

import android.content.Context
import androidx.core.content.edit
import splitties.init.appCtx

object LocalConfig {
    private const val versionCodeKey = "appVersionCode"

    private val localConfig =
        appCtx.getSharedPreferences("local", Context.MODE_PRIVATE)

    val readHelpVersionIsLast: Boolean
        get() = isLastVersion(1, "readHelpVersion", "firstRead")

    val backupHelpVersionIsLast: Boolean
        get() = isLastVersion(1, "backupHelpVersion", "firstBackup")

    val readMenuHelpVersionIsLast: Boolean
        get() = isLastVersion(1, "readMenuHelpVersion", "firstReadMenu")

    val bookSourcesHelpVersionIsLast: Boolean
        get() = isLastVersion(1, "bookSourceHelpVersion", "firstOpenBookSources")

    val ruleHelpVersionIsLast: Boolean
        get() = isLastVersion(1, "ruleHelpVersion")

    val needUpHttpTTS: Boolean
        get() = !isLastVersion(5, "httpTtsVersion")

    val needUpTxtTocRule: Boolean
        get() = !isLastVersion(1, "txtTocRuleVersion")

    val needUpRssSources: Boolean
        get() = !isLastVersion(4, "rssSourceVersion")

    var versionCode
        get() = localConfig.getLong(versionCodeKey, 0)
        set(value) {
            localConfig.edit {
                putLong(versionCodeKey, value)
            }
        }

    val isFirstOpenApp: Boolean
        get() {
            val value = localConfig.getBoolean("firstOpen", true)
            if (value) {
                localConfig.edit { putBoolean("firstOpen", false) }
            }
            return value
        }

    @Suppress("SameParameterValue")
    private fun isLastVersion(
        lastVersion: Int,
        versionKey: String,
        firstOpenKey: String? = null
    ): Boolean {
        var version = localConfig.getInt(versionKey, 0)
        if (version == 0 && firstOpenKey != null) {
            if (!localConfig.getBoolean(firstOpenKey, true)) {
                version = 1
            }
        }
        if (version < lastVersion) {
            localConfig.edit { putInt(versionKey, lastVersion) }
            return false
        }
        return true
    }

}